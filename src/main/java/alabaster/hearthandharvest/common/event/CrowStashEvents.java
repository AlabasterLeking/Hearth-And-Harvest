package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CrowStashEvents {

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        if (event.getPlayer().level().isClientSide) return;
        if (event.getOriginalStack().isEmpty()) return;
        if (event.getItemEntity().getOwner() instanceof CrowEntity) {
            HHSimpleTrigger.trigger(HHModTriggers.CROW_STASH_FOUND.get(), event.getPlayer());
        }
    }
}