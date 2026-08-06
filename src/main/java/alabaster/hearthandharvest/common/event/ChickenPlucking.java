package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class ChickenPlucking {

    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Chicken chicken)) return;
        if (Config.DISABLE_CHICKEN_PLUCKING.get()) return;
        Player player = event.getEntity();
        Level world = player.level();

        if (!player.isShiftKeyDown()) return;

        if (!world.isClientSide) {
            chicken.spawnAtLocation(Items.FEATHER);

            ItemStack heldItem = player.getMainHandItem();

            if (heldItem.is(Items.SHEARS)) {
                heldItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
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