package alabaster.hearthandharvest.common;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class HHFoodValues {

    public static final int BRIEF_DURATION = 600;    // 30 seconds
    public static final int SHORT_DURATION = 1200;    // 1 minute
    public static final int MEDIUM_DURATION = 3600;    // 3 minutes
    public static final int LONG_DURATION = 6000;    // 5 minutes

    public static final FoodProperties BLUEBERRIES = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.3f).build();
    public static final FoodProperties CHERRY = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.5f).build();
    public static final FoodProperties RASPBERRY = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4f).build();
    public static final FoodProperties GRAPES = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties PEANUT = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).build();

    public static final FoodProperties MEAD = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 2400, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1), 1.0F)
            .build();
    public static final FoodProperties BLUEBERRY_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 2400, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 2400, 1), 1.0F)
            .build();
    public static final FoodProperties CHERRY_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 900, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 900, 1), 1.0F)
            .build();
    public static final FoodProperties GREEN_GRAPE_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 1800, 0), 1.0F)
            .build();
    public static final FoodProperties RASPBERRY_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, BRIEF_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties RED_GRAPE_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, SHORT_DURATION, 0), 1.0F)
            .build();
    public static final FoodProperties SWEET_BERRY_WINE = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, SHORT_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, SHORT_DURATION, 0), 1.0F)
            .build();

    public static final FoodProperties GOAT_MILK_BOTTLE = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).build();
    public static final FoodProperties BLUEBERRY_JUICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties CHERRY_JUICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties RASPBERRY_JUICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties GRAPE_JUICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();

    public static final FoodProperties RASPBERRY_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties CHERRY_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties BLUEBERRY_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties GRAPE_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties APPLE_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties SWEET_BERRY_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties GLOW_BERRY_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties MELON_JAM = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();

    public static final FoodProperties PICKLED_BEETROOTS = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_CABBAGE = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_CARROTS = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_ONIONS = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties PICKLED_POTATOES = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();

    public static final FoodProperties CARAMEL = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.3f).build();
    public static final FoodProperties CARAMEL_APPLE = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.3f).build();
    public static final FoodProperties CHOCOLATE_BAR = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties COTTON_CANDY = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).build();
    public static final FoodProperties BLUEBERRY_MUFFIN = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER_COOKIE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).fast().build();
    public static final FoodProperties TRAIL_MIX = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MEDIUM_DURATION, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, SHORT_DURATION, 0), 1.0F)
            .fast().build();
    public static final FoodProperties ROASTED_PEANUTS = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.5f).fast().build();

    public static final FoodProperties MARSHMALLOW_STICK = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).alwaysEat().fast().build();
    public static final FoodProperties ROASTED_MARSHMALLOW_STICK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), SHORT_DURATION, 0), 1.0F).alwaysEat().fast().build();
    public static final FoodProperties CHARRED_MARSHMALLOW_STICK = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.1f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0F).alwaysEat().fast().build();
    public static final FoodProperties SMORE = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 200, 0), 1.0F).alwaysEat().fast().build();

    public static final FoodProperties RASPBERRY_PIE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties BLUEBERRY_PIE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties GRAPE_PIE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER_PIE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties CHICKEN_POT_PIE_SLICE = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties CARROT_CAKE_SLICE = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.3f).build();

    public static final FoodProperties PEANUT_BUTTER = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.3f).build();
    public static final FoodProperties CHEDDAR_CHEESE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties GOAT_CHEESE_SLICE = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties RAW_SAUSAGE = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).build();
    public static final FoodProperties COOKED_SAUSAGE = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.3f).build();
    public static final FoodProperties SKEWERED_SAUSAGE = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.3f).build();
    public static final FoodProperties JERKY = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.3f).build();
    public static final FoodProperties RAISINS = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3f).fast().build();
    public static final FoodProperties SUNFLOWER_SEEDS = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.3f).fast().build();

    public static final FoodProperties ONION_SOUP = (new FoodProperties.Builder())
            .nutrition(10).saturationMod(0.3f).effect(() -> new MobEffectInstance(HHModEffects.PUNGENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
    public static final FoodProperties MACARONI_AND_CHEESE = (new FoodProperties.Builder())
            .nutrition(11).saturationMod(0.3f).build();
    public static final FoodProperties MASHED_POTATOES = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0.3f).build();
    public static final FoodProperties PEANUT_BUTTER_AND_JELLY_SANDWICH = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0.3f).build();
    public static final FoodProperties WAFFLE = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.3f).build();
    public static final FoodProperties BISCUITS_AND_GRAVY = (new FoodProperties.Builder())
            .nutrition(10).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 1), 1.0F).build();
}