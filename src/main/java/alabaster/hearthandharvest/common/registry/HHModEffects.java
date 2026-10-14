package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HHModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HearthAndHarvest.MODID);

    public static final RegistryObject<MobEffect> PUNGENT = EFFECTS.register("pungent", PungentEffect::new);
    public static final RegistryObject<MobEffect> TEMPTING = EFFECTS.register("tempting", TemptingEffect::new);
    public static final RegistryObject<MobEffect> DRUNK = EFFECTS.register("drunk", DrunkEffect::new);
    public static final RegistryObject<MobEffect> PRICKLY = EFFECTS.register("prickly", PricklyEffect::new);
    public static final RegistryObject<MobEffect> CLARITY = EFFECTS.register("clarity", ClarityEffect::new);
    public static final RegistryObject<MobEffect> PINNED = EFFECTS.register("pinned", PinnedEffect::new);
    public static final RegistryObject<MobEffect> HORSE_BOOST = EFFECTS.register("horse_boost", HorseBoostEffect::new);
}
