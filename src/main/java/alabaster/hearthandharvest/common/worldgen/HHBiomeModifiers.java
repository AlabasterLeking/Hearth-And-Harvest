package alabaster.hearthandharvest.common.worldgen;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class HHBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_MUMS = registerKey("add_mums");
    public static final ResourceKey<BiomeModifier> ADD_SALT_CAVES = registerKey("add_salt_caves");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Mums in surface biomes
        HolderSet<Biome> mumBiomes = HolderSet.direct(
                biomes.getOrThrow(Biomes.PLAINS),
                biomes.getOrThrow(Biomes.FOREST),
                biomes.getOrThrow(Biomes.FLOWER_FOREST),
                biomes.getOrThrow(Biomes.MEADOW)
        );

        HolderSet<PlacedFeature> mumPlacedFeatures = HolderSet.direct(
                HHPlacedFeatures.ALL_2COLOR_PLACED_KEYS.stream()
                        .map(placedFeatures::getOrThrow)
                        .toList()
        );

        context.register(ADD_MUMS, new BiomeModifiers.AddFeaturesBiomeModifier(
                mumBiomes,
                mumPlacedFeatures,
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_SALT_CAVES, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HHModTags.HAS_SALT_CAVES),
                HolderSet.direct(placedFeatures.getOrThrow(HHPlacedFeatures.SALT_CAVE)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, name));
    }
}