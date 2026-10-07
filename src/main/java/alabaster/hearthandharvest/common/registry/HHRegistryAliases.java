package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

public class HHRegistryAliases {
    private static final Map<String, String> RENAMES = Map.of(
            "elote", "street_corn",
            "cheddar_cheese_wheel", "cheese_wheel",
            "unripe_cheddar_cheese_wheel", "unripe_cheese_wheel",
            "cheddar_cheese_slice", "cheese_slice",
            "grape_trellis", "crop_trellis"
    );

    public static void register() {
        addAliases(HHModItems.ITEMS);
        addAliases(HHModBlocks.BLOCKS);
    }

    private static void addAliases(DeferredRegister<?> registry) {
        RENAMES.forEach((from, to) -> registry.addAlias(
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, from),
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, to)));
    }
}