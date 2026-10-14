package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.common.event.FeatherParticles;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class ChickenPlucking {

    private static final int PLUCK_COOLDOWN_TICKS = 600;

    @SubscribeEvent
    public static void onChickenTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Chicken chicken)) return;
        if (chicken.level().isClientSide) return;
        int cooldown = HHModAttachments.PLUCK_COOLDOWN.get(chicken);
        if (cooldown > 0) HHModAttachments.PLUCK_COOLDOWN.set(chicken, cooldown - 1);
    }

    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Chicken chicken)) return;
        if (Config.DISABLE_CHICKEN_PLUCKING.get()) return;
        Player player = event.getEntity();
        Level world = player.level();

        if (!player.isShiftKeyDown()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        if (!world.isClientSide) {
            if (HHModAttachments.PLUCK_COOLDOWN.get(chicken) > 0) {
                event.setCancellationResult(InteractionResult.FAIL);
                event.setCanceled(true);
                return;
            }
            HHModAttachments.PLUCK_COOLDOWN.set(chicken, PLUCK_COOLDOWN_TICKS);
            chicken.spawnAtLocation(Items.FEATHER);
            FeatherParticles.burst(chicken, 8);
            HHSimpleTrigger.trigger(HHModTriggers.PLUCKED_CHICKEN, player);

            ItemStack heldItem = player.getMainHandItem();

            if (heldItem.is(Items.SHEARS)) {
                heldItem.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                chicken.hurt(chicken.damageSources().playerAttack(player), 0.0F);
            } else {
                if (world.random.nextDouble() < 0.25) {
                    chicken.hurt(chicken.damageSources().playerAttack(player), 1.0F);
                }
                else {
                    chicken.hurt(chicken.damageSources().playerAttack(player), 0.0F);
                }
            }
            if (chicken.getNavigation() != null) {
                chicken.getNavigation().moveTo(player.getX() + (world.random.nextDouble() - 0.5) * 6.0,
                        player.getY(),
                        player.getZ() + (world.random.nextDouble() - 0.5) * 6.0,
                        1.25);
            }
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}