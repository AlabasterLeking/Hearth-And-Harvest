package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class GoatMilking {

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Goat goat) || goat.isBaby()) return;

        ItemStack held = event.getEntity().getItemInHand(event.getHand());

        if (held.is(Items.BUCKET)) {
            milk(event, goat, HHModItems.GOAT_MILK_BUCKET.get());
            return;
        }

        if (held.is(Items.GLASS_BOTTLE) && !Config.DISABLE_BOTTLE_MILKING.get()) {
            milk(event, goat, HHModItems.GOAT_MILK_BOTTLE.get());
        }
    }

    private static void milk(PlayerInteractEvent.EntityInteract event, Goat goat, Item result) {
        Player player = event.getEntity();
        Level level = player.level();
        InteractionHand hand = event.getHand();

        if (!level.isClientSide) {
            goat.playSound(SoundEvents.GOAT_MILK, 1.0F, 1.0F);
            player.setItemInHand(hand, ItemUtils.createFilledResult(player.getItemInHand(hand), player, new ItemStack(result)));
            HHSimpleTrigger.trigger(HHModTriggers.MILKED_GOAT.get(), player);
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        event.setCanceled(true);
    }
}