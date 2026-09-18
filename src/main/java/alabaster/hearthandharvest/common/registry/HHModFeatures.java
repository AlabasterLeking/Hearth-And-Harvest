package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.worldgen.NestFeature;
import alabaster.hearthandharvest.common.worldgen.SaltCaveFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, HearthAndHarvest.MODID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> SALT_CAVE =
            FEATURES.register("salt_cave", () -> new SaltCaveFeature(NoneFeatureConfiguration.CODEC));

    public static final Supplier<Feature<NoneFeatureConfiguration>> NEST =
            FEATURES.register("nest", () -> new NestFeature(NoneFeatureConfiguration.CODEC));
}