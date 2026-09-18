package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class HHModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, HearthAndHarvest.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CROW_STOLE_ITEM = TRIGGERS.register("crow_stole_item", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CROW_DELIVERED_ITEM = TRIGGERS.register("crow_delivered_item", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> STOMPED_RECIPE = TRIGGERS.register("stomped_recipe", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> ATE_SALTED_FOOD = TRIGGERS.register("ate_salted_food", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CLEAVER_KILL = TRIGGERS.register("cleaver_kill", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> PITCHFORK_KILL = TRIGGERS.register("pitchfork_kill", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> PLUCKED_CHICKEN = TRIGGERS.register("plucked_chicken", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> MILKED_GOAT = TRIGGERS.register("milked_goat", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> FOUND_CORN_MAZE = TRIGGERS.register("found_corn_maze", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> BOTTLED_SYRUP = TRIGGERS.register("bottled_syrup", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> LICKED_SALT_AWAY = TRIGGERS.register("licked_salt_away", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> FED_SUGAR_CUBES = TRIGGERS.register("fed_sugar_cubes", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> HORSESHOE_RINGER = TRIGGERS.register("horseshoe_ringer", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> BIG_PIG_LITTER = TRIGGERS.register("big_pig_litter", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> BIG_RABBIT_LITTER = TRIGGERS.register("big_rabbit_litter", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> BIG_STOMP = TRIGGERS.register("big_stomp", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CASK_AGED = TRIGGERS.register("cask_aged", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> SPRINKLER_EXTINGUISHED = TRIGGERS.register("sprinkler_extinguished", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CROW_STASH_FOUND = TRIGGERS.register("crow_stash_found", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CROW_FULL_TRUST = TRIGGERS.register("crow_full_trust", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CROW_FLOCK_ALARM = TRIGGERS.register("crow_flock_alarm", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> MANURE_HIT = TRIGGERS.register("manure_hit", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> PLAYER_POOPED = TRIGGERS.register("player_pooped", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> PUNGENT_SCARED = TRIGGERS.register("pungent_scared", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> TEMPTING_CROWD = TRIGGERS.register("tempting_crowd", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> FARMERS_HAT_WORN_OUT = TRIGGERS.register("farmers_hat_worn_out", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> FERTILIZER_GREW_CROP = TRIGGERS.register("fertilizer_grew_crop", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> HOE_AREA_WORK = TRIGGERS.register("hoe_area_work", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> FILLED_BOTTLE_RACK = TRIGGERS.register("filled_bottle_rack", HHSimpleTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> CHICKEN_GLIDE = TRIGGERS.register("chicken_glide", HHSimpleTrigger::new);

    public static Criterion<HHSimpleTrigger.TriggerInstance> criterion(DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> trigger) {
        return trigger.get().createCriterion(new HHSimpleTrigger.TriggerInstance(Optional.empty()));
    }
}