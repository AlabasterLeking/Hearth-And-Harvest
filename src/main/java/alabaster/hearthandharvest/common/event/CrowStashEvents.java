package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CrowStashEvents {

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getStack().isEmpty()) return;
        if (event.getOriginalEntity().getOwner() instanceof CrowEntity) {
            HHSimpleTrigger.trigger(HHModTriggers.CROW_STASH_FOUND, event.getEntity());
        }
    }
}