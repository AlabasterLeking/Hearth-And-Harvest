package alabaster.hearthandharvest.common.worldgen;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.ArrayList;
import java.util.List;

public class HHPlacedFeatures {

    public static final List<ResourceKey<PlacedFeature>> ALL_2COLOR_PLACED_KEYS = new ArrayList<>();

    public static final ResourceKey<PlacedFeature> SALT_CAVE = registerKey("salt_cave");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(SALT_CAVE, new PlacedFeature(
                configuredFeatures.getOrThrow(HHConfiguredFeatures.SALT_CAVE),
                List.of(
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-40),
                                VerticalAnchor.absolute(30)),
                        BiomeFilter.biome()
                )
        ));

        // Mum combinations
        for (var configKey : HHConfiguredFeatures.ALL_2COLOR_KEYS) {
            String name = configKey.location().getPath() + "_placed";
            ResourceKey<PlacedFeature> placedKey = registerKey(name);
            ALL_2COLOR_PLACED_KEYS.add(placedKey);

            Holder<ConfiguredFeature<?, ?>> featureHolder = configuredFeatures.getOrThrow(configKey);

            context.register(placedKey, new PlacedFeature(featureHolder, List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                    BiomeFilter.biome()
            )));
        }
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, name));
    }
}