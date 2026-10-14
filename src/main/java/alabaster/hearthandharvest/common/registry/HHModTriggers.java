package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class HHModTriggers {
    public static final HHSimpleTrigger CROW_STOLE_ITEM = register("crow_stole_item");
    public static final HHSimpleTrigger CROW_DELIVERED_ITEM = register("crow_delivered_item");
    public static final HHSimpleTrigger STOMPED_RECIPE = register("stomped_recipe");
    public static final HHSimpleTrigger ATE_SALTED_FOOD = register("ate_salted_food");
    public static final HHSimpleTrigger CLEAVER_KILL = register("cleaver_kill");
    public static final HHSimpleTrigger PITCHFORK_KILL = register("pitchfork_kill");
    public static final HHSimpleTrigger PLUCKED_CHICKEN = register("plucked_chicken");
    public static final HHSimpleTrigger MILKED_GOAT = register("milked_goat");
    public static final HHSimpleTrigger FOUND_CORN_MAZE = register("found_corn_maze");
    public static final HHSimpleTrigger BOTTLED_SYRUP = register("bottled_syrup");
    public static final HHSimpleTrigger SYRUPED_STACK = register("syruped_stack");
    public static final HHSimpleTrigger LICKED_SALT_AWAY = register("licked_salt_away");
    public static final HHSimpleTrigger FED_SUGAR_CUBES = register("fed_sugar_cubes");
    public static final HHSimpleTrigger HORSESHOE_RINGER = register("horseshoe_ringer");
    public static final HHSimpleTrigger BIG_PIG_LITTER = register("big_pig_litter");
    public static final HHSimpleTrigger BIG_RABBIT_LITTER = register("big_rabbit_litter");
    public static final HHSimpleTrigger BIG_STOMP = register("big_stomp");
    public static final HHSimpleTrigger CASK_AGED = register("cask_aged");
    public static final HHSimpleTrigger SPRINKLER_EXTINGUISHED = register("sprinkler_extinguished");
    public static final HHSimpleTrigger CROW_STASH_FOUND = register("crow_stash_found");
    public static final HHSimpleTrigger CROW_FULL_TRUST = register("crow_full_trust");
    public static final HHSimpleTrigger CROW_FLOCK_ALARM = register("crow_flock_alarm");
    public static final HHSimpleTrigger MANURE_HIT = register("manure_hit");
    public static final HHSimpleTrigger PLAYER_POOPED = register("player_pooped");
    public static final HHSimpleTrigger PUNGENT_SCARED = register("pungent_scared");
    public static final HHSimpleTrigger TEMPTING_CROWD = register("tempting_crowd");
    public static final HHSimpleTrigger FARMERS_HAT_WORN_OUT = register("farmers_hat_worn_out");
    public static final HHSimpleTrigger FERTILIZER_GREW_CROP = register("fertilizer_grew_crop");
    public static final HHSimpleTrigger HOE_AREA_WORK = register("hoe_area_work");
    public static final HHSimpleTrigger FILLED_BOTTLE_RACK = register("filled_bottle_rack");
    public static final HHSimpleTrigger CHICKEN_GLIDE = register("chicken_glide");
    public static final HHSimpleTrigger CROW_PAIR = register("crow_pair");

    private static HHSimpleTrigger register(String name) {
        return CriteriaTriggers.register(new HHSimpleTrigger(new ResourceLocation(HearthAndHarvest.MODID, name)));
    }

    public static void init() {
    }
}
