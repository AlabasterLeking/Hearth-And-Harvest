package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.entity.ManureProjectile;
import alabaster.hearthandharvest.common.entity.cleaver.ThrownCleaver;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchfork;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HearthAndHarvest.MODID);

    public static final Supplier<EntityType<CrowEntity>> CROW =
            ENTITY_TYPES.register("crow",
                    () -> EntityType.Builder.of(CrowEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.5f)
                            .build("crow"));

    public static final Supplier<EntityType<ManureProjectile>> MANURE_PROJECTILE =
            ENTITY_TYPES.register("manure_projectile", () ->
                    EntityType.Builder.<ManureProjectile>of(ManureProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("manure_projectile")
            );

    public static final Supplier<EntityType<ThrownPitchfork>> THROWN_PITCHFORK =
            ENTITY_TYPES.register("thrown_pitchfork", () ->
                    EntityType.Builder.<ThrownPitchfork>of(ThrownPitchfork::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(1)
                            .build("thrown_pitchfork")
            );

    public static final Supplier<EntityType<ThrownCleaver>> THROWN_CLEAVER =
            ENTITY_TYPES.register("thrown_cleaver", () ->
                    EntityType.Builder.<ThrownCleaver>of(ThrownCleaver::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(1)
                            .build("thrown_cleaver")
            );
}