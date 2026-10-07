package alabaster.hearthandharvest.integration;

import alabaster.hearthandharvest.common.registry.HHModItems;
import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ThirstWasTakenCompat {

    @SubscribeEvent
    public static void compat(RegisterThirstValueEvent event) {
        event.addDrink(HHModItems.SWEET_BERRY_WINE.get(), 10, 14);
        event.addDrink(HHModItems.BLUEBERRY_WINE.get(), 10, 14);
        event.addDrink(HHModItems.RASPBERRY_WINE.get(), 10, 14);
        event.addDrink(HHModItems.RED_GRAPE_WINE.get(), 10, 14);
        event.addDrink(HHModItems.GREEN_GRAPE_WINE.get(), 10, 14);
        event.addDrink(HHModItems.CHERRY_WINE.get(), 10, 14);
        event.addDrink(HHModItems.GLOW_BERRY_WINE.get(), 10, 14);
        event.addDrink(HHModItems.MELON_WINE.get(), 10, 14);
        event.addDrink(HHModItems.MEAD.get(), 12, 18);
        event.addDrink(HHModItems.ROOT_BEER.get(), 12, 18);
        event.addDrink(HHModItems.BEER.get(), 12, 18);
        event.addDrink(HHModItems.HARD_CIDER.get(), 12, 18);
        event.addDrink(HHModItems.MOONSHINE.get(), 12, 18);
        event.addDrink(HHModItems.BLUEBERRY_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.RASPBERRY_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.CHERRY_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.RED_GRAPE_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.GLOW_BERRY_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.GREEN_GRAPE_JUICE.get(), 8, 12);
        event.addDrink(HHModItems.GOAT_MILK_BOTTLE.get(), 6, 8);
    }
}