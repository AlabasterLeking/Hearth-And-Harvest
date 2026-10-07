package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.common.registry.HHDataMaps;
import alabaster.hearthandharvest.common.registry.HHModFluids;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.data.DataMapProvider;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.world.item.Item;
import alabaster.hearthandharvest.common.data.VintageStyle;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class DataMaps extends DataMapProvider
{
    protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                // 30% chance
                .add(HHModItems.CORN_HUSK.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.HAY_RUG.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.COTTON_SEEDS.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.SUNFLOWER_SEEDS.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.BLUEBERRIES.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.RASPBERRY.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.CORN.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(HHModItems.CORN_KERNELS.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)

                // 50% chance
                .add(HHModItems.CORN_HUSK_BUNDLE.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
                .add(HHModItems.COTTON_BALE.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
                .add(HHModItems.STICK_BRUSH.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
                .add(HHModItems.SUGAR_CANE_BUNDLE.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)

                // 65% chance
                .add(HHModItems.YELLOW_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.ORANGE_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.RED_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.BLUE_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.LIGHT_BLUE_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.PURPLE_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.PINK_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WHITE_MUM.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WILD_COTTON.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WILD_PEANUTS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WILD_GREEN_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WILD_RED_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.WILD_HOPS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.GREEN_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.RED_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.HOPS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.COTTON.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.PEANUT.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.CHERRY.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)

                // 85% chance
                .add(HHModItems.BLUEBERRY_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.RASPBERRY_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.GRAPE_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.PEANUT_BUTTER_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.CHICKEN_POT_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.CARROT_CAKE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.CHERRY_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
                .add(HHModItems.CHOCOLATE_CAKE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)

                // 100% chance
                .add(HHModItems.BLUEBERRY_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.RASPBERRY_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.GRAPE_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.PEANUT_BUTTER_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CHICKEN_POT_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CARROT_CAKE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CHERRY_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CHOCOLATE_CAKE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
        ;

        builder(HHDataMaps.FLUID_BOTTLE)
                .add(HHModFluids.GOAT_MILK.source().get().builtInRegistryHolder(), HHModItems.GOAT_MILK_BOTTLE.get().asItem(), false)
                .add(NeoForgeMod.MILK.value().builtInRegistryHolder(), ModItems.MILK_BOTTLE.get().asItem(), false)
                .add(HHModFluids.COOKING_OIL.source().get().builtInRegistryHolder(), HHModItems.COOKING_OIL.get().asItem(), false)
                .add(HHModFluids.SYRUP.source().get().builtInRegistryHolder(), HHModItems.SYRUP_BOTTLE.get().asItem(), false)
                .add(HHModFluids.APPLE_CIDER.source().get().builtInRegistryHolder(), ModItems.APPLE_CIDER.get().asItem(), false)
                .add(HHModFluids.HARD_CIDER.source().get().builtInRegistryHolder(), HHModItems.HARD_CIDER.get().asItem(), false)
                .add(HHModFluids.ROOT_BEER.source().get().builtInRegistryHolder(), HHModItems.ROOT_BEER.get().asItem(), false)
                .add(HHModFluids.BEER.source().get().builtInRegistryHolder(), HHModItems.BEER.get().asItem(), false)
                .add(HHModFluids.MEAD.source().get().builtInRegistryHolder(), HHModItems.MEAD.get().asItem(), false)
                .add(HHModFluids.MOONSHINE.source().get().builtInRegistryHolder(), HHModItems.MOONSHINE.get().asItem(), false)
                .add(HHModFluids.BLUEBERRY_JUICE.source().get().builtInRegistryHolder(), HHModItems.BLUEBERRY_JUICE.get().asItem(), false)
                .add(HHModFluids.CHERRY_JUICE.source().get().builtInRegistryHolder(), HHModItems.CHERRY_JUICE.get().asItem(), false)
                .add(HHModFluids.GREEN_GRAPE_JUICE.source().get().builtInRegistryHolder(), HHModItems.GREEN_GRAPE_JUICE.get().asItem(), false)
                .add(HHModFluids.RASPBERRY_JUICE.source().get().builtInRegistryHolder(), HHModItems.RASPBERRY_JUICE.get().asItem(), false)
                .add(HHModFluids.RED_GRAPE_JUICE.source().get().builtInRegistryHolder(), HHModItems.RED_GRAPE_JUICE.get().asItem(), false)
                .add(HHModFluids.SWEET_BERRY_JUICE.source().get().builtInRegistryHolder(), HHModItems.SWEET_BERRY_JUICE.get().asItem(), false)
                .add(HHModFluids.MELON_JUICE.source().get().builtInRegistryHolder(), ModItems.MELON_JUICE.get().asItem(), false)
                .add(HHModFluids.GLOW_BERRY_JUICE.source().get().builtInRegistryHolder(), HHModItems.GLOW_BERRY_JUICE.get().asItem(), false)
                .add(HHModFluids.BLUEBERRY_WINE.source().get().builtInRegistryHolder(), HHModItems.BLUEBERRY_WINE.get().asItem(), false)
                .add(HHModFluids.CHERRY_WINE.source().get().builtInRegistryHolder(), HHModItems.CHERRY_WINE.get().asItem(), false)
                .add(HHModFluids.GREEN_GRAPE_WINE.source().get().builtInRegistryHolder(), HHModItems.GREEN_GRAPE_WINE.get().asItem(), false)
                .add(HHModFluids.RASPBERRY_WINE.source().get().builtInRegistryHolder(), HHModItems.RASPBERRY_WINE.get().asItem(), false)
                .add(HHModFluids.RED_GRAPE_WINE.source().get().builtInRegistryHolder(), HHModItems.RED_GRAPE_WINE.get().asItem(), false)
                .add(HHModFluids.SWEET_BERRY_WINE.source().get().builtInRegistryHolder(), HHModItems.SWEET_BERRY_WINE.get().asItem(), false)
                .add(HHModFluids.GLOW_BERRY_WINE.source().get().builtInRegistryHolder(), HHModItems.GLOW_BERRY_WINE.get().asItem(), false)
                .add(HHModFluids.MELON_WINE.source().get().builtInRegistryHolder(), HHModItems.MELON_WINE.get().asItem(), false)
        ;

        VintageStyle bottle = new VintageStyle(true, false, "bottle", true);
        VintageStyle jar = new VintageStyle(true, true, "jar", true);
        VintageStyle cheese = new VintageStyle(true, false, VintageStyle.NO_OVERLAY, false);

        var vintage = builder(HHDataMaps.VINTAGE_STYLE);
        for (Supplier<Item> item : List.of(
                HHModItems.MEAD, HHModItems.HARD_CIDER, HHModItems.ROOT_BEER, HHModItems.BEER, HHModItems.MOONSHINE,
                HHModItems.BLUEBERRY_WINE, HHModItems.CHERRY_WINE, HHModItems.RASPBERRY_WINE,
                HHModItems.RED_GRAPE_WINE, HHModItems.GREEN_GRAPE_WINE, HHModItems.SWEET_BERRY_WINE,
                HHModItems.GLOW_BERRY_WINE, HHModItems.MELON_WINE)) {
            vintage.add(item.get().asItem().builtInRegistryHolder(), bottle, false);
        }
        for (Supplier<Item> item : List.of(
                HHModItems.PICKLED_BEETROOTS, HHModItems.PICKLED_CABBAGE, HHModItems.PICKLED_CARROTS,
                HHModItems.PICKLED_ONIONS, HHModItems.PICKLED_POTATOES)) {
            vintage.add(item.get().asItem().builtInRegistryHolder(), jar, false);
        }
        for (Supplier<Item> item : List.of(HHModItems.CHEESE_WHEEL, HHModItems.GOAT_CHEESE_WHEEL)) {
            vintage.add(item.get().asItem().builtInRegistryHolder(), cheese, false);
        }
    }
}