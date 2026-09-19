package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class ChickenGlideEvents {
    private static final double GLIDE_FALL_SPEED = 0.12D;
    private static final double GLIDE_FORWARD_BOOST = 0.012D;
    private static final double GLIDE_MAX_HORIZONTAL = 0.36D;
    private static final double ADVANCEMENT_DESCENT = 10.0D;
    private static final int FEATHER_INTERVAL = 4;
    private static final int CLUCK_MIN_INTERVAL = 40;
    private static final int CLUCK_CHANCE = 60;
    private static final String GLIDE_START_TAG = "hearthandharvest:glide_start_y";

    @SubscribeEvent
    public static void onInteractChicken(PlayerInteractEvent.EntityInteract event) {
        if (!Config.CHICKEN_GLIDING.get()) return;
        if (!(event.getTarget() instanceof Chicken chicken)) return;
        Player player = event.getEntity();

        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (player.isShiftKeyDown() || !player.getMainHandItem().isEmpty()) return;
        if (chicken.isBaby() || chicken.isPassenger() || chicken.isVehicle() || chicken.isLeashed()) return;
        if (!player.getPassengers().isEmpty() || player.isPassenger() || player.isSpectator()) return;

        if (!player.level().isClientSide && chicken.startRiding(player, true)) {
            syncPassengersToSelf(player);
            chicken.getNavigation().stop();
            chicken.playSound(SoundEvents.CHICKEN_AMBIENT, 1.0F, 1.2F);
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Chicken chicken = getHeldChicken(player);
        if (chicken == null) {
            clearGlideStart(player);
            return;
        }

        faceWithPlayer(chicken, player);

        if (!player.level().isClientSide) {
            if (!Config.CHICKEN_GLIDING.get() || player.isShiftKeyDown() || player.isPassenger() || player.isSpectator()) {
                putDown(chicken, player);
                return;
            }
        }

        if (!canGlide(player)) {
            clearGlideStart(player);
            return;
        }
        player.resetFallDistance();

        if (player instanceof ServerPlayer serverPlayer) {
            trackDescent(serverPlayer);
            glideEffects(serverPlayer, chicken);
        }

        if (player.level().isClientSide && player.isLocalPlayer()) {
            applyGlide(player);
        }
    }

    @SubscribeEvent
    public static void onChickenHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Chicken chicken
                && chicken.getVehicle() instanceof Player
                && event.getSource().is(DamageTypes.IN_WALL)) {
            event.setCanceled(true);
        }
    }

    private static void glideEffects(ServerPlayer player, Chicken chicken) {
        if (player.tickCount % FEATHER_INTERVAL == 0) {
            FeatherParticles.trail(chicken, 1);
        }
        if (player.tickCount % CLUCK_MIN_INTERVAL == 0 && player.getRandom().nextInt(CLUCK_CHANCE) < CLUCK_MIN_INTERVAL) {
            chicken.playSound(SoundEvents.CHICKEN_AMBIENT, 0.8F, 1.1F + player.getRandom().nextFloat() * 0.3F);
        }
    }

    private static void trackDescent(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(GLIDE_START_TAG)) {
            data.putDouble(GLIDE_START_TAG, player.getY());
            return;
        }
        if (data.getDouble(GLIDE_START_TAG) - player.getY() >= ADVANCEMENT_DESCENT) {
            HHModTriggers.CHICKEN_GLIDE.get().trigger(player);
        }
    }

    private static void clearGlideStart(Player player) {
        if (!player.level().isClientSide && player.getPersistentData().contains(GLIDE_START_TAG)) {
            player.getPersistentData().remove(GLIDE_START_TAG);
        }
    }

    public static boolean isHoldingChicken(Player player) {
        return getHeldChicken(player) != null;
    }

    @Nullable
    private static Chicken getHeldChicken(Player player) {
        for (var passenger : player.getPassengers()) {
            if (passenger instanceof Chicken chicken) return chicken;
        }
        return null;
    }

    private static boolean canGlide(Player player) {
        return !player.onGround()
                && !player.isInWater()
                && !player.isInLava()
                && !player.onClimbable()
                && !player.isFallFlying()
                && !player.getAbilities().flying;
    }

    private static void applyGlide(Player player) {
        Vec3 motion = player.getDeltaMovement();
        double y = motion.y < -GLIDE_FALL_SPEED ? -GLIDE_FALL_SPEED : motion.y;
        double x = motion.x;
        double z = motion.z;

        if (player.zza > 0.0F) {
            float yawRad = player.getYRot() * Mth.DEG_TO_RAD;
            x += -Mth.sin(yawRad) * GLIDE_FORWARD_BOOST;
            z += Mth.cos(yawRad) * GLIDE_FORWARD_BOOST;
        }

        double horizontal = Math.sqrt(x * x + z * z);
        if (horizontal > GLIDE_MAX_HORIZONTAL) {
            double scale = GLIDE_MAX_HORIZONTAL / horizontal;
            x *= scale;
            z *= scale;
        }

        player.setDeltaMovement(x, y, z);
    }

    private static void faceWithPlayer(Chicken chicken, Player player) {
        chicken.setYRot(player.getYRot());
        chicken.yRotO = player.yRotO;
        chicken.yBodyRot = player.yBodyRot;
        chicken.yBodyRotO = player.yBodyRotO;
        chicken.setYHeadRot(player.getYHeadRot());
    }

    private static void syncPassengersToSelf(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetPassengersPacket(serverPlayer));
        }
    }

    private static void putDown(Chicken chicken, Player player) {
        chicken.stopRiding();
        syncPassengersToSelf(player);
        Vec3 look = Vec3.directionFromRotation(0.0F, player.getYRot());
        chicken.moveTo(player.getX() + look.x * 0.8D, player.getY() + 0.5D, player.getZ() + look.z * 0.8D, player.getYRot(), 0.0F);
        chicken.setDeltaMovement(player.getDeltaMovement());
    }
}