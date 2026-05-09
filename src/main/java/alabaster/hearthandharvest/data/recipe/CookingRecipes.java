package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.function.Consumer;

public class CookingRecipes
{
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static final Ingredient WATER_BOTTLE = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));

    public static void register(Consumer<FinishedRecipe> consumer) {
        jarFoods(consumer);
        cookMeals(consumer);
    }

    private static void jarFoods(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.RASPBERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.RASPBERRY.get())
                .addIngredient(HHModItems.RASPBERRY.get())
                .addIngredient(HHModItems.RASPBERRY.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_raspberry", HHModItems.RASPBERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.BLUEBERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.BLUEBERRIES.get())
                .addIngredient(HHModItems.BLUEBERRIES.get())
                .addIngredient(HHModItems.BLUEBERRIES.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_blueberries", HHModItems.BLUEBERRIES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CHERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_cherries", HHModItems.CHERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GRAPE_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.RED_GRAPES.get())
                .addIngredient(HHModItems.RED_GRAPES.get())
                .addIngredient(HHModItems.RED_GRAPES.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_red_grapes", HHModItems.RED_GRAPES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.APPLE_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.APPLE)
                .addIngredient(Items.APPLE)
                .addIngredient(Items.APPLE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_apple", Items.APPLE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SWEET_BERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_sweet_berries", Items.SWEET_BERRIES)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GLOW_BERRY_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.GLOW_BERRIES)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_glow_berries", Items.GLOW_BERRIES)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MELON_JAM.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.MELON_SLICE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_melon_slice", Items.MELON_SLICE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.PEANUT_BUTTER.get(), 1, SLOW_COOKING, MEDIUM_EXP, HHModItems.JAR.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHModItems.PEANUT.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_peanut", HHModItems.PEANUT.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
    }

    private static void cookMeals(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CARAMEL.get(), 1, FAST_COOKING, MEDIUM_EXP)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(HHModItems.SALT.get())
                .unlockedByItems("has_sugar", Items.SUGAR)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CHOCOLATE_BAR.get(), 1, FAST_COOKING, MEDIUM_EXP)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.COCOA_BEANS)
                .addIngredient(Items.COCOA_BEANS)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get())
                .unlockedByItems("has_cocoa_beans", Items.COCOA_BEANS)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CARAMEL_APPLE.get(), 1, FAST_COOKING, LARGE_EXP, Items.STICK)
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(HHModItems.CARAMEL.get())
                .addIngredient(Items.APPLE)
                .unlockedByItems("has_caramel", HHModItems.CARAMEL.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.BLUEBERRY_JUICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(HHModItems.BLUEBERRIES.get())
                .addIngredient(HHModItems.BLUEBERRIES.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_blueberries", HHModItems.BLUEBERRIES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.CHERRY_JUICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(HHModItems.CHERRY.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_cherries", HHModItems.CHERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.RASPBERRY_JUICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(HHModItems.RASPBERRY.get())
                .addIngredient(HHModItems.RASPBERRY.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_raspberry", HHModItems.RASPBERRY.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.RED_GRAPE_JUICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(HHModItems.RED_GRAPES.get())
                .addIngredient(HHModItems.RED_GRAPES.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_red_grapes", HHModItems.RED_GRAPES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.GREEN_GRAPE_JUICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(HHModItems.GREEN_GRAPES.get())
                .addIngredient(HHModItems.GREEN_GRAPES.get())
                .addIngredient(Items.SUGAR)
                .unlockedByItems("has_green_grapes", HHModItems.GREEN_GRAPES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SALT.get(), 8, FAST_COOKING, SMALL_EXP)
                .addIngredient(Items.WATER_BUCKET)
                .unlockedByItems("has_water_bucket", Items.WATER_BUCKET)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer, "salt_from_bucket");
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.SALT.get(), 2, FAST_COOKING, SMALL_EXP)
                .addIngredient(WATER_BOTTLE)
                .unlockedBy("has_water_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POTION))
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .save(consumer, "salt_from_bottle");

        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(), 1, SLOW_COOKING, LARGE_EXP)
                .addIngredient(ModItems.MILK_BOTTLE.get())
                .addIngredient(ModItems.MILK_BOTTLE.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(HHModItems.SALT.get())
                .unlockedByItems("has_milk_bottle", ModItems.MILK_BOTTLE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get(), 1, SLOW_COOKING, LARGE_EXP)
                .addIngredient(HHModItems.GOAT_MILK_BOTTLE.get())
                .addIngredient(HHModItems.GOAT_MILK_BOTTLE.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(HHModItems.SALT.get())
                .unlockedByItems("has_goat_milk_bottle", HHModItems.GOAT_MILK_BOTTLE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MASHED_POTATOES.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(CommonTags.Items.MILK_BOTTLE)
                .unlockedByItems("has_potato", Items.POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.MACARONI_AND_CHEESE.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(ModItems.RAW_PASTA.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(CommonTags.Items.MILK_BOTTLE)
                .addIngredient(HHModTags.CHEESE_SLICES)
                .unlockedByItems("has_pasta", vectorwing.farmersdelight.common.registry.ModItems.RAW_PASTA.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.ONION_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(CommonTags.Items.MILK_BOTTLE)
                .addIngredient(HHModTags.CHEESE_SLICES)
                .unlockedByItems("has_onion", vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.WAFFLE.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(HHModItems.BATTER.get())
                .addIngredient(HHModItems.SYRUP_BOTTLE.get())
                .unlockedByItems("has_batter", HHModItems.BATTER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(HHModItems.BISCUITS_AND_GRAVY.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(HHModItems.COOKED_SAUSAGE.get())
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(CommonTags.Items.MILK_BOTTLE)
                .addIngredient(Items.BREAD)
                .unlockedByItems("has_cooked_sausage", HHModItems.COOKED_SAUSAGE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(consumer);
    }
}