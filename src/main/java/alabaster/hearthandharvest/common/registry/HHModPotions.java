package alabaster.hearthandharvest.common.registry;

import net.minecraft.core.registries.Registries;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class HHModPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, HearthAndHarvest.MODID);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    // Pungent Potion
    public static final RegistryObject<Potion> PUNGENT_POTION = POTIONS.register("pungent_potion", () -> new Potion(new MobEffectInstance(HHModEffects.PUNGENT.get(), 3600, 0)));
    // Long Pungent Potion
    public static final RegistryObject<Potion> LONG_PUNGENT_POTION = POTIONS.register("long_pungent_potion", () -> new Potion(new MobEffectInstance(HHModEffects.PUNGENT.get(), 9600, 0)));
    // Strong Pungent Potion
    public static final RegistryObject<Potion> STRONG_PUNGENT_POTION = POTIONS.register("strong_pungent_potion", () -> new Potion(new MobEffectInstance(HHModEffects.PUNGENT.get(), 1800, 1)));

    // Tempting Potion
    public static final RegistryObject<Potion> TEMPTING_POTION = POTIONS.register("tempting_potion", () -> new Potion(new MobEffectInstance(HHModEffects.TEMPTING.get(), 3600, 0)));
    // Long Tempting Potion
    public static final RegistryObject<Potion> LONG_TEMPTING_POTION = POTIONS.register("long_tempting_potion", () -> new Potion(new MobEffectInstance(HHModEffects.TEMPTING.get(), 9600, 0)));
    // Strong Tempting Potion
    public static final RegistryObject<Potion> STRONG_TEMPTING_POTION = POTIONS.register("strong_tempting_potion", () -> new Potion(new MobEffectInstance(HHModEffects.TEMPTING.get(), 1800, 1)));

    // Bad Omen Potion
    public static final RegistryObject<Potion> BAD_OMEN_POTION = POTIONS.register("bad_omen_potion", () -> new Potion(new MobEffectInstance(MobEffects.BAD_OMEN, 3600, 0)));
    // Long Bad Omen Potion
    public static final RegistryObject<Potion> LONG_BAD_OMEN_POTION = POTIONS.register("long_bad_omen_potion", () -> new Potion(new MobEffectInstance(MobEffects.BAD_OMEN, 9600, 0)));
    // Strong Bad Omen Potion
    public static final RegistryObject<Potion> STRONG_BAD_OMEN_POTION = POTIONS.register("strong_bad_omen_potion", () -> new Potion(new MobEffectInstance(MobEffects.BAD_OMEN, 1800, 1)));

}
