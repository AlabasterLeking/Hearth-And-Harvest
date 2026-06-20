package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
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
                .add(HHModItems.GREEN_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(HHModItems.RED_GRAPES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
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

                // 100% chance
                .add(HHModItems.BLUEBERRY_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.RASPBERRY_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.GRAPE_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.PEANUT_BUTTER_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CHICKEN_POT_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(HHModItems.CARROT_CAKE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)

    ;}
}