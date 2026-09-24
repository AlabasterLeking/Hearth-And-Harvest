package alabaster.hearthandharvest.client.recipebook;

import alabaster.hearthandharvest.common.crafting.FermentingRecipe;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

public class RecipeCategories
{
    public static RecipeBookCategories AGING_SEARCH = RecipeBookCategories.valueOf("HEARTHANDHARVEST_AGING_SEARCH");
    public static RecipeBookCategories AGING_MEALS = RecipeBookCategories.valueOf("HEARTHANDHARVEST_AGING_MEALS");
    public static RecipeBookCategories AGING_DRINKS = RecipeBookCategories.valueOf("HEARTHANDHARVEST_AGING_DRINKS");
    public static RecipeBookCategories AGING_MISC = RecipeBookCategories.valueOf("HEARTHANDHARVEST_AGING_MISC");

    public static RecipeBookCategories FERMENTING_SEARCH = RecipeBookCategories.valueOf("HEARTHANDHARVEST_FERMENTING_SEARCH");
    public static RecipeBookCategories FERMENTING_MEALS = RecipeBookCategories.valueOf("HEARTHANDHARVEST_FERMENTING_MEALS");
    public static RecipeBookCategories FERMENTING_DRINKS = RecipeBookCategories.valueOf("HEARTHANDHARVEST_FERMENTING_DRINKS");
    public static RecipeBookCategories FERMENTING_MISC = RecipeBookCategories.valueOf("HEARTHANDHARVEST_FERMENTING_MISC");

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(RecipeBookType.valueOf("HEARTHANDHARVEST_FERMENTING"), ImmutableList.of(FERMENTING_SEARCH, FERMENTING_MEALS, FERMENTING_DRINKS, FERMENTING_MISC));
        event.registerAggregateCategory(FERMENTING_SEARCH, ImmutableList.of(FERMENTING_MEALS, FERMENTING_DRINKS, FERMENTING_MISC));
        event.registerRecipeCategoryFinder(HHModRecipeTypes.FERMENTING.get(), recipe ->
        {
            if (recipe.value() instanceof FermentingRecipe fermentingRecipe) {
                CaskRecipeBookTab tab = fermentingRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case MEALS -> FERMENTING_MEALS;
                        case DRINKS -> FERMENTING_DRINKS;
                        case MISC -> FERMENTING_MISC;
                    };
                }
            }
            return FERMENTING_MISC;
        });

        event.registerBookCategories(RecipeBookType.valueOf("HEARTHANDHARVEST_AGING"), ImmutableList.of(AGING_SEARCH, AGING_MEALS, AGING_DRINKS, AGING_MISC));
        event.registerAggregateCategory(AGING_SEARCH, ImmutableList.of(AGING_MEALS, AGING_DRINKS, AGING_MISC));
        event.registerRecipeCategoryFinder(HHModRecipeTypes.AGING.get(), recipe ->
        {
            if (recipe.value() instanceof CaskRecipe caskRecipe) {
                CaskRecipeBookTab tab = caskRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case MEALS -> AGING_MEALS;
                        case DRINKS -> AGING_DRINKS;
                        case MISC -> AGING_MISC;
                    };
                }
            }
            return AGING_MISC;
        });
    }
}