package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class HHRegistryAliases {
    private static final Map<String, String> RENAMES = Map.ofEntries(
            Map.entry("elote", "street_corn"),
            Map.entry("cheddar_cheese_wheel", "cheese_wheel"),
            Map.entry("unripe_cheddar_cheese_wheel", "unripe_cheese_wheel"),
            Map.entry("cheddar_cheese_slice", "cheese_slice"),
            Map.entry("grape_trellis", "crop_trellis"),
            Map.entry("oak_wine_rack", "oak_bottle_rack"),
            Map.entry("spruce_wine_rack", "spruce_bottle_rack"),
            Map.entry("birch_wine_rack", "birch_bottle_rack"),
            Map.entry("jungle_wine_rack", "jungle_bottle_rack"),
            Map.entry("acacia_wine_rack", "acacia_bottle_rack"),
            Map.entry("dark_oak_wine_rack", "dark_oak_bottle_rack"),
            Map.entry("mangrove_wine_rack", "mangrove_bottle_rack"),
            Map.entry("cherry_wine_rack", "cherry_bottle_rack"),
            Map.entry("bamboo_wine_rack", "bamboo_bottle_rack"),
            Map.entry("crimson_wine_rack", "crimson_bottle_rack"),
            Map.entry("warped_wine_rack", "warped_bottle_rack")
    );

    private static final Map<String, String> BLOCK_ENTITY_RENAMES = Map.of(
            "wine_rack", "bottle_rack"
    );

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, Registries.ITEM, ForgeRegistries.ITEMS, RENAMES);
        remap(event, Registries.BLOCK, ForgeRegistries.BLOCKS, RENAMES);
        remap(event, Registries.BLOCK_ENTITY_TYPE, ForgeRegistries.BLOCK_ENTITY_TYPES, BLOCK_ENTITY_RENAMES);
    }

    private static <T> void remap(MissingMappingsEvent event, ResourceKey<? extends Registry<T>> key, IForgeRegistry<T> registry, Map<String, String> renames) {
        for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(key, HearthAndHarvest.MODID)) {
            String target = renames.get(mapping.getKey().getPath());
            if (target == null) continue;
            T value = registry.getValue(new ResourceLocation(HearthAndHarvest.MODID, target));
            if (value != null) mapping.remap(value);
        }
    }
}
