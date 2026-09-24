package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

public class CookingRecipes
{
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static final Ingredient WATER_BOTTLE = new Ingredient(DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER), Items.POTION).getCustomIngredient());

    public static void register(RecipeOutput output) {
        jarFoods(output);
        cookMeals(output);
    }

    private static void jarFoods(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.RASPBERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHCommonTags.FRUITS_RASPBERRY)
                .addIngredient(HHCommonTags.FRUITS_RASPBERRY)
                .addIngredient(HHCommonTags.FRUITS_RASPBERRY)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_raspberry", HHModItems.RASPBERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.BLUEBERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHCommonTags.FRUITS_BLUEBERRY)
                .addIngredient(HHCommonTags.FRUITS_BLUEBERRY)
                .addIngredient(HHCommonTags.FRUITS_BLUEBERRY)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_blueberries", HHModItems.BLUEBERRIES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GRAPE_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHCommonTags.FRUITS_GRAPE)
                .addIngredient(HHCommonTags.FRUITS_GRAPE)
                .addIngredient(HHCommonTags.FRUITS_GRAPE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_grapes", HHModItems.RED_GRAPES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.APPLE_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.APPLE)
                .addIngredient(Items.APPLE)
                .addIngredient(Items.APPLE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_apple", Items.APPLE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SWEET_BERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_sweet_berries", Items.SWEET_BERRIES)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GLOW_BERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_glow_berries", Items.GLOW_BERRIES)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MELON_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_melon_slice", Items.MELON_SLICE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CHERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_cherry", HHModItems.CHERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.PEANUT_BUTTER.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_peanut", HHModItems.PEANUT.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
    }

    private static void cookMeals(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CARAMEL.get(), 1, FAST_COOKING, MEDIUM_EXP)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .unlockedByItems("has_sugar", Items.SUGAR)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CHOCOLATE_BAR.get(), 1, FAST_COOKING, MEDIUM_EXP)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.COCOA_BEANS)
                .addIngredient(Items.COCOA_BEANS)
                .addIngredient(ModItems.MILK_BOTTLE.get())
                .unlockedByItems("has_cocoa_beans", Items.COCOA_BEANS)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CARAMEL_APPLE.get(), 1, FAST_COOKING, LARGE_EXP, Items.STICK)
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(Items.APPLE)
                .unlockedByItems("has_caramel", HHModItems.CARAMEL.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SALT.get(), 8, FAST_COOKING, SMALL_EXP)
                .addIngredient(Items.WATER_BUCKET)
                .unlockedByItems("has_water_bucket", Items.WATER_BUCKET)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(output, "salt_from_bucket");
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SALT.get(), 2, FAST_COOKING, SMALL_EXP)
                .addIngredient(WATER_BOTTLE)
                .unlockedBy("has_water_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POTION))
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(output, "salt_from_bottle");

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(), 1, SLOW_COOKING, LARGE_EXP)
                .addIngredient(ModItems.MILK_BOTTLE.get())
                .addIngredient(ModItems.MILK_BOTTLE.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .unlockedByItems("has_milk_bottle", ModItems.MILK_BOTTLE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get(), 1, SLOW_COOKING, LARGE_EXP)
                .addIngredient(HHModItems.GOAT_MILK_BOTTLE.get())
                .addIngredient(HHModItems.GOAT_MILK_BOTTLE.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .unlockedByItems("has_goat_milk_bottle", HHModItems.GOAT_MILK_BOTTLE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MASHED_POTATOES.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(HHModItems.BUTTER.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .unlockedByItems("has_potato", Items.POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MACARONI_AND_CHEESE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(ModItems.RAW_PASTA.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .addIngredient(HHModItems.BUTTER.get())
                .addIngredient(HHModTags.CHEESE_SLICES)
                .unlockedByItems("has_pasta", ModItems.RAW_PASTA.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.PANCAKE.get(), 2, FAST_COOKING, SMALL_EXP)
                .addIngredient(HHModItems.BATTER.get())
                .addIngredient(HHModItems.BUTTER.get())
                .unlockedByItems("has_batter", HHModItems.BATTER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.WAFFLE.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(HHModItems.BATTER.get())
                .addIngredient(HHModItems.SYRUP_BOTTLE.get())
                .unlockedByItems("has_batter", HHModItems.BATTER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.BISCUITS_AND_GRAVY.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(HHModItems.COOKED_SAUSAGE.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .addIngredient(HHCommonTags.FLOURS)
                .unlockedByItems("has_cooked_sausage", HHModItems.COOKED_SAUSAGE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CIDER_DONUT.get(), 2, SLOW_COOKING, MEDIUM_EXP)
                .addIngredient(ModItems.APPLE_CIDER.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.EGG)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_apple_cider", ModItems.APPLE_CIDER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CANDY_CORN.get(), 4, FAST_COOKING, SMALL_EXP)
                .addIngredient(HHModItems.CORN_KERNELS.get())
                .addIngredient(Items.ORANGE_DYE)
                .addIngredient(HHModItems.SYRUP_BOTTLE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_corn_kernels", HHModItems.CORN_KERNELS.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CORN_BREAD.get(), 1, SLOW_COOKING, MEDIUM_EXP)
                .addIngredient(HHModItems.CORN_MEAL.get())
                .addIngredient(HHModItems.CORN_MEAL.get())
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.EGG)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .unlockedByItems("has_corn_meal", HHModItems.CORN_MEAL.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CORN_STEW.get(), 1, SLOW_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(HHCommonTags.VEGETABLES_CORN)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.POTATO)
                .addIngredient(Tags.Items.DRINKS_MILK)
                .unlockedByItems("has_corn", HHModItems.CORN.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.TAMALE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, HHModItems.CORN_HUSK.get())
                .addIngredient(Tags.Items.FOODS_COOKED_MEAT)
                .addIngredient(Tags.Items.FOODS_VEGETABLE)
                .addIngredient(HHModTags.CHEESE_SLICES)
                .unlockedByItems("has_corn_husk", HHModItems.CORN_HUSK.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.ELOTE.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(HHModItems.COOKED_CORN_ON_THE_COB.get())
                .addIngredient(Items.BLAZE_POWDER)
                .addIngredient(HHModTags.CHEESE_SLICES)
                .unlockedByItems("has_corn_on_the_cob", HHModItems.COOKED_CORN_ON_THE_COB.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GLAZED_CARROTS.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.CARROT)
                .addIngredient(Items.CARROT)
                .addIngredient(HHModItems.SYRUP_BOTTLE.get())
                .unlockedByItems("has_carrot", Items.CARROT)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output);
    }
}