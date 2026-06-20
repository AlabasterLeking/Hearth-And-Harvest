package alabaster.hearthandharvest.common;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class HHFoodValues {

    // Duration Constants
    public static final int BRIEF_DURATION = 600;    // 30 seconds
    public static final int SHORT_DURATION = 1200;   // 1 minute
    public static final int MEDIUM_DURATION = 3600;  // 3 minutes
    public static final int LONG_DURATION = 6000;    // 5 minutes

    // Raw Ingredients
    public static final FoodProperties BLUEBERRIES = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.3f).build();
    public static final FoodProperties RASPBERRY = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.4f).build();
    public static final FoodProperties CHERRY = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.5f).build();
    public static final FoodProperties GRAPES = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties PEANUT = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3f).build();
    public static final FoodProperties CORN = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.5f).build();
    public static final FoodProperties RAISINS = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3f).fast().build();
    public static final FoodProperties SUNFLOWER_SEEDS = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.3f).fast().build();

    // Drinks
    public static final FoodProperties CHOCOLATE_MILK_BOTTLE = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties GOAT_MILK_BOTTLE = new FoodProperties.Builder()
            .build();

    // Juices
    public static final FoodProperties BLUEBERRY_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties RASPBERRY_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.PRICKLY, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties CHERRY_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties RED_GRAPE_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties GREEN_GRAPE_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties SWEET_BERRY_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.TEMPTING, SHORT_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties GLOW_BERRY_JUICE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, BRIEF_DURATION, 0), 1.0F)
            .build();

    // Alcoholic Beverages

    // --- Light Drinks ---
    public static final FoodProperties ROOT_BEER = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, SHORT_DURATION, 0), 0.5F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, BRIEF_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, BRIEF_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties HARD_CIDER = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, SHORT_DURATION, 0), 0.5F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BRIEF_DURATION, 0), 1.0F)
            .build();

    // --- Medium Drinks (Wines & Mead) ---
    public static final FoodProperties MEAD = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, BRIEF_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties BLUEBERRY_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, BRIEF_DURATION, 1), 0.75F)
            .build();

    public static final FoodProperties CHERRY_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, BRIEF_DURATION, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, BRIEF_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties GREEN_GRAPE_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, BRIEF_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties RASPBERRY_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(HHModEffects.PRICKLY, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, SHORT_DURATION, 0), 0.5F)
            .build();

    public static final FoodProperties RED_GRAPE_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BRIEF_DURATION, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, BRIEF_DURATION, 0), 0.5F)
            .build();

    public static final FoodProperties SWEET_BERRY_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 0, 0), 1.0F)
            .effect(() -> new MobEffectInstance(HHModEffects.TEMPTING, MEDIUM_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties GLOW_BERRY_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(HHModEffects.CLARITY, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, SHORT_DURATION, 0), 0.5F)
            .build();

    public static final FoodProperties MELON_WINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 0), 0.75F)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 0, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, SHORT_DURATION, 0), 1.0F)
            .build();

    // --- Strong Drinks ---
    public static final FoodProperties MOONSHINE = new FoodProperties.Builder()
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(HHModEffects.DRUNK, MEDIUM_DURATION, 1), 1.0F) // Starts at Drunk II
            .effect(() -> new MobEffectInstance(MobEffects.SATURATION, MEDIUM_DURATION, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, SHORT_DURATION, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SHORT_DURATION, 1), 1.0F) // Penalty baked in
            .build();

    // Jams & Spreads
    public static final FoodProperties RASPBERRY_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties BLUEBERRY_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties GRAPE_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties APPLE_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties SWEET_BERRY_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties GLOW_BERRY_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties MELON_JAM = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();

    // Pickled Foods
    public static final FoodProperties PICKLED_BEETROOTS = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f)
            .effect(() -> new MobEffectInstance(HHModEffects.PUNGENT, MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_CABBAGE = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f)
            .effect(() -> new MobEffectInstance(HHModEffects.PUNGENT, MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_CARROTS = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f)
            .effect(() -> new MobEffectInstance(HHModEffects.PUNGENT, MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_ONIONS = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f)
            .effect(() -> new MobEffectInstance(HHModEffects.PUNGENT, MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_POTATOES = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f)
            .effect(() -> new MobEffectInstance(HHModEffects.PUNGENT, MEDIUM_DURATION, 0), 1.0F).build();

    // Sweets & Snacks
    public static final FoodProperties CARAMEL = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.3f).build();
    public static final FoodProperties CARAMEL_APPLE = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties CHOCOLATE_BAR = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties COTTON_CANDY = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3f).build();
    public static final FoodProperties BLUEBERRY_MUFFIN = new FoodProperties.Builder()
            .nutrition(6).saturationModifier(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER_COOKIE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).fast().build();
    public static final FoodProperties TRAIL_MIX = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, SHORT_DURATION, 0), 1.0F)
            .fast().build();
    public static final FoodProperties ROASTED_PEANUTS = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.5f).fast().build();
    public static final FoodProperties POPCORN = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).fast().build();
    public static final FoodProperties CANDY_CORN = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.6f).fast().build();
    public static final FoodProperties MARSHMALLOW_STICK = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3f).alwaysEdible().fast().usingConvertsTo(Items.STICK).build();
    public static final FoodProperties ROASTED_MARSHMALLOW_STICK = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, SHORT_DURATION, 0), 1.0F)
            .alwaysEdible().fast().usingConvertsTo(Items.STICK).build();
    public static final FoodProperties CHARRED_MARSHMALLOW_STICK = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.1f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0F)
            .alwaysEdible().fast().usingConvertsTo(Items.STICK).build();
    public static final FoodProperties SMORE = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 200, 0), 1.0F)
            .alwaysEdible().fast().build();
    public static final FoodProperties SUGAR_CUBES = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0), 1.0F)
            .alwaysEdible().fast().build();

    // Baked Goods
    public static final FoodProperties RASPBERRY_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties BLUEBERRY_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties GRAPE_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties CHICKEN_POT_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.3f).build();
    public static final FoodProperties CARROT_CAKE_SLICE = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties WAFFLE = new FoodProperties.Builder()
            .nutrition(6).saturationModifier(0.3f).build();
    public static final FoodProperties CIDER_DONUT = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 200, 0), 1.0F)
            .build();
    public static final FoodProperties CORN_BREAD = new FoodProperties.Builder()
            .nutrition(6).saturationModifier(0.5f).build();

    // Dairy, Meat, and Savory Dishes
    public static final FoodProperties CHEDDAR_CHEESE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties GOAT_CHEESE_SLICE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties RAW_SAUSAGE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties COOKED_SAUSAGE = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.5f).build();
    public static final FoodProperties RAW_SKEWERED_SAUSAGE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties SKEWERED_SAUSAGE = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.5f).build();
    public static final FoodProperties UNCOOKED_CORN_ON_THE_COB = new FoodProperties.Builder()
            .nutrition(6).saturationModifier(0.6f).build();
    public static final FoodProperties COOKED_CORN_ON_THE_COB = new FoodProperties.Builder()
            .nutrition(8).saturationModifier(0.8f).build();
    public static final FoodProperties TORTILLA = new FoodProperties.Builder()
            .nutrition(5).saturationModifier(0.6f).build();
    public static final FoodProperties TACO = new FoodProperties.Builder()
            .nutrition(11).saturationModifier(0.8f).build();
    public static final FoodProperties JERKY = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties MACARONI_AND_CHEESE = new FoodProperties.Builder()
            .nutrition(11).saturationModifier(0.3f).build();
    public static final FoodProperties MASHED_POTATOES = new FoodProperties.Builder()
            .nutrition(8).saturationModifier(0.4f).build();
    public static final FoodProperties PEANUT_BUTTER_AND_JELLY_SANDWICH = new FoodProperties.Builder()
            .nutrition(8).saturationModifier(0.3f).build();
    public static final FoodProperties BISCUITS_AND_GRAVY = new FoodProperties.Builder()
            .nutrition(10).saturationModifier(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, MEDIUM_DURATION, 1), 1.0F).build();
    public static final FoodProperties GLAZED_CARROTS = new FoodProperties.Builder()
            .nutrition(10).saturationModifier(0.5f).build();
    public static final FoodProperties CORN_STEW = new FoodProperties.Builder()
            .nutrition(8).saturationModifier(0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, MEDIUM_DURATION, 1), 1.0F).build();
    public static final FoodProperties ELOTE = new FoodProperties.Builder()
            .nutrition(10).saturationModifier(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, MEDIUM_DURATION, 1), 1.0F).build();
    public static final FoodProperties TAMALE = new FoodProperties.Builder()
            .nutrition(7).saturationModifier(0.6f).build();
}
