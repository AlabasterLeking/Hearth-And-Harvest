package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.entity.crow.goals.CrowRetrieveItemsGoal;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CrowShoulderEvents {
    private static final int CHECK_INTERVAL = 20;
    private static final double SEARCH_RADIUS = 8.0D;
    private static final double SEARCH_HEIGHT = 4.0D;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % CHECK_INTERVAL != 0) return;
        if (!Config.CROW_FETCH_ITEMS.get() || !Config.CROW_LEAVE_SHOULDER_TO_FETCH.get()) return;
        if (!player.isAlive() || player.isSpectator() || player.isSleeping()) return;

        boolean crowOnLeft = isCrow(player.getShoulderEntityLeft());
        boolean crowOnRight = isCrow(player.getShoulderEntityRight());
        if (!crowOnLeft && !crowOnRight) return;
        if (!hasItemToFetch(player)) return;

        if (crowOnLeft) {
            release(player, player.getShoulderEntityLeft(), player::setShoulderEntityLeft, 0.4D);
        } else {
            release(player, player.getShoulderEntityRight(), player::setShoulderEntityRight, -0.4D);
        }
    }

    private static boolean isCrow(CompoundTag tag) {
        if (tag.isEmpty()) return false;
        return tag.getString("id").equals(BuiltInRegistries.ENTITY_TYPE.getKey(HHModEntities.CROW.get()).toString());
    }

    private static boolean hasItemToFetch(ServerPlayer player) {
        return !player.level().getEntitiesOfClass(ItemEntity.class,
                player.getBoundingBox().inflate(SEARCH_RADIUS, SEARCH_HEIGHT, SEARCH_RADIUS),
                item -> CrowRetrieveItemsGoal.isWorthFetching(item, player)).isEmpty();
    }

    private static void release(ServerPlayer player, CompoundTag tag, Consumer<CompoundTag> clearShoulder, double sideOffset) {
        ServerLevel level = player.serverLevel();
        EntityType.create(tag, level).ifPresent(entity -> {
            if (!(entity instanceof CrowEntity crow)) return;

            double yawRad = Math.toRadians(player.getYRot());
            double x = player.getX() + Math.cos(yawRad) * sideOffset;
            double z = player.getZ() + Math.sin(yawRad) * sideOffset;
            crow.setOwnerUUID(player.getUUID());
            crow.moveTo(x, player.getY() + 1.3D, z, player.getYRot(), 0.0F);
            if (level.addWithUUID(crow)) {
                clearShoulder.accept(new CompoundTag());
            }
        });
    }
}