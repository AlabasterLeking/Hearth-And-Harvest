package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DeprecationTooltipHandler {

    private static Set<Item> deprecatedItems = null;

    private static Set<Item> getDeprecatedItems() {
        if (deprecatedItems == null) {
            deprecatedItems = Set.of(
            );
        }
        return deprecatedItems;
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!getDeprecatedItems().contains(event.getItemStack().getItem())) return;

        event.getToolTip().add(
                Component.translatable("tooltip.hearthandharvest.deprecated")
                        .withStyle(ChatFormatting.RED)
        );
        event.getToolTip().add(
                Component.translatable("tooltip.hearthandharvest.deprecated.hint")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}