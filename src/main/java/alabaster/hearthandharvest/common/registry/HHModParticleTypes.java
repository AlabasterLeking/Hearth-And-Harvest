package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModParticleTypes {
        public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, HearthAndHarvest.MODID);

        public static final Supplier<SimpleParticleType> DRIPPING_SAP = PARTICLE_TYPES.register("dripping_sap",
                () -> new SimpleParticleType(true));

        public static final Supplier<SimpleParticleType> FLIES = PARTICLE_TYPES.register("flies",
                () -> new SimpleParticleType(true));

        public static final Supplier<SimpleParticleType> FEATHER = PARTICLE_TYPES.register("feather",
                () -> new SimpleParticleType(false));
}