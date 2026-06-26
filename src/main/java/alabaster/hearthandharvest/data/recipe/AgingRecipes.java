package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import alabaster.hearthandharvest.data.builder.CaskRecipeBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class AgingRecipes {
    public static final int FAST_AGING = 300;      // 1.25 minutes
    public static final int NORMAL_AGING = 600;    // 2.5 minutes
    public static final int SLOW_AGING = 1200;     // 5 minutes
    public static final int VERY_SLOW = 2400;      // 10 minutes

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static final Ingredient WATER_BOTTLE = new Ingredient(DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER), Items.POTION).getCustomIngredient());


    public static void register(RecipeOutput output) {
        ageCheese(output);
        ageDrinks(output);
        agePickles(output);
        ageMisc(output);
    }

    private static void ageCheese(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.CHEDDAR_CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.GOAT_CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);
    }

    private static void ageDrinks(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.HARD_CIDER.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(ModItems.APPLE_CIDER.get())
                .unlockedByAnyIngredient(ModItems.APPLE_CIDER.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);


        CaskRecipeBuilder.caskRecipe(HHModItems.BLUEBERRY_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.BLUEBERRY_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.BLUEBERRY_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.CHERRY_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.CHERRY_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.CHERRY_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.RASPBERRY_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.RASPBERRY_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.RASPBERRY_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.RED_GRAPE_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.RED_GRAPE_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.RED_GRAPE_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.GREEN_GRAPE_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.GREEN_GRAPE_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.GREEN_GRAPE_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.SWEET_BERRY_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.SWEET_BERRY_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.SWEET_BERRY_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.GLOW_BERRY_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(HHModItems.GLOW_BERRY_JUICE.get())
                .unlockedByAnyIngredient(HHModItems.GLOW_BERRY_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.MELON_WINE.get(), 1, VERY_SLOW, MEDIUM_EXP)
                .addIngredient(ModItems.MELON_JUICE.get())
                .unlockedByAnyIngredient(ModItems.MELON_JUICE.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.MEAD.get(), 1, SLOW_AGING, SMALL_EXP)
                .addIngredient(WATER_BOTTLE)
                .addIngredient(Items.HONEY_BLOCK)
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(Items.HONEY_BLOCK)
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.ROOT_BEER.get(), 1, SLOW_AGING, SMALL_EXP)
                .addIngredient(WATER_BOTTLE)
                .addIngredient(Items.HANGING_ROOTS)
                .addIngredient(Items.HANGING_ROOTS)
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(Items.HANGING_ROOTS)
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.MOONSHINE.get(), 1, SLOW_AGING, SMALL_EXP)
                .addIngredient(WATER_BOTTLE)
                .addIngredient(Items.SUGAR)
                .addIngredient(HHModItems.CORN_MEAL.get())
                .addIngredient(HHModItems.CORN_MEAL.get())
                .unlockedByAnyIngredient(HHModItems.CORN_MEAL.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(output);
    }

    private static void agePickles(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_CARROTS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.CARROT)
                .addIngredient(Items.CARROT)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.CARROT)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_POTATOES.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.POTATO)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_ONIONS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(ModItems.ONION.get())
                .addIngredient(ModItems.ONION.get())
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(ModItems.ONION.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_CABBAGE.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(ModItems.CABBAGE.get())
                .addIngredient(ModItems.CABBAGE.get())
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(ModItems.CABBAGE.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_BEETROOTS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.BEETROOT)
                .addIngredient(Items.BEETROOT)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.BEETROOT)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);
    }

    private static void ageMisc(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.JERKY.get(), 3, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .unlockedByAnyIngredient(Items.ROTTEN_FLESH)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"jerky_from_rotten_flesh");

        CaskRecipeBuilder.caskRecipe(HHModItems.JERKY.get(), 3, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .unlockedByAnyIngredient()
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"jerky_from_raw_meat");

        CaskRecipeBuilder.caskRecipe(HHModItems.FERTILIZER_BAG.get(), 1, VERY_SLOW, LARGE_EXP)
                .addIngredient(Items.BONE_MEAL)
                .addIngredient(HHModItems.MANURE.get())
                .addIngredient(ModItems.TREE_BARK.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"fertilizer_bag_from_bark");

        CaskRecipeBuilder.caskRecipe(HHModItems.FERTILIZER_BAG.get(), 1, VERY_SLOW, LARGE_EXP)
                .addIngredient(Items.BONE_MEAL)
                .addIngredient(HHModItems.MANURE.get())
                .addIngredient(HHModItems.CORN_HUSK.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"fertilizer_bag_from_corn_husk");

        CaskRecipeBuilder.caskRecipe(HHModItems.FERTILIZER_BAG.get(), 1, VERY_SLOW, LARGE_EXP)
                .addIngredient(Items.BONE_MEAL)
                .addIngredient(HHModItems.MANURE.get())
                .addIngredient(ModItems.STRAW.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"fertilizer_bag_from_straw");
    }
}
