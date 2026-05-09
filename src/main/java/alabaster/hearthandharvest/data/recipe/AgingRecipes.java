package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.data.builder.CaskRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class AgingRecipes {
    public static final int FAST_AGING = 300;      // 1.25 minutes
    public static final int NORMAL_AGING = 600;    // 2.5 minutes
    public static final int SLOW_AGING = 1200;     // 5 minutes

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static void register(Consumer<FinishedRecipe> consumer) {
        ageCheese(consumer);
        ageDrinks(consumer);
        agePickles(consumer);
        ageMisc(consumer);
    }

    private static void ageCheese(Consumer<FinishedRecipe> consumer) {
        CaskRecipeBuilder.caskRecipe(HHModItems.CHEDDAR_CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.GOAT_CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);
    }

    private static void ageDrinks(Consumer<FinishedRecipe> consumer) {
        CaskRecipeBuilder.caskRecipe(HHModItems.BLUEBERRY_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(HHModItems.BLUEBERRY_JUICE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(HHModItems.BLUEBERRIES.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.CHERRY_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(HHModItems.CHERRY_JUICE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(HHModItems.CHERRY.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.RASPBERRY_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(HHModItems.RASPBERRY_JUICE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(HHModItems.RASPBERRY.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.RED_GRAPE_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(HHModItems.RED_GRAPE_JUICE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(HHModItems.RED_GRAPES.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.GREEN_GRAPE_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(HHModItems.GREEN_GRAPE_JUICE.get())
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(HHModItems.GREEN_GRAPES.get())
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.SWEET_BERRY_WINE.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SWEET_BERRIES)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.GLASS_BOTTLE)
                .unlockedByAnyIngredient(Items.SWEET_BERRIES)
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.MEAD.get(), 1, NORMAL_AGING, SMALL_EXP)
                .addIngredient(Items.HONEY_BOTTLE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(Items.HONEY_BOTTLE)
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .build(consumer);
    }

    private static void agePickles(Consumer<FinishedRecipe> consumer) {
        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_CARROTS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(Items.CARROT)
                .addIngredient(Items.CARROT)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.CARROT)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_POTATOES.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.POTATO)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_ONIONS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_CABBAGE.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE.get())
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);

        CaskRecipeBuilder.caskRecipe(HHModItems.PICKLED_BEETROOTS.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(Items.BEETROOT)
                .addIngredient(Items.BEETROOT)
                .addIngredient(HHModItems.JAR.get())
                .unlockedByAnyIngredient(Items.BEETROOT)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);
    }


    private static void ageMisc(Consumer<FinishedRecipe> consumer) {
        CaskRecipeBuilder.caskRecipe(HHModItems.JERKY.get(), 3, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.SALT.get())
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .unlockedByAnyIngredient(Items.ROTTEN_FLESH)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(consumer);
    }
}
