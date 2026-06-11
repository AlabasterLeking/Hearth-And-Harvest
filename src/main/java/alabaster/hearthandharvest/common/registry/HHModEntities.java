package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.entity.ManureProjectile;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import net.neoforged.neoforge.registries.DeferredHolder;
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
}

