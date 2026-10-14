package alabaster.hearthandharvest.client.recipebook;

import alabaster.hearthandharvest.common.HHRecipeBookTypes;
import alabaster.hearthandharvest.common.registry.HHModItems;
import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

public class RecipeCategories
{
    public static final Supplier<RecipeBookCategories> AGING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_AGING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> AGING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_AGING_MEALS", new ItemStack(HHModItems.CHEESE_WHEEL.get())));
    public static final Supplier<RecipeBookCategories> AGING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_AGING_DRINKS", new ItemStack(HHModItems.RED_GRAPE_WINE.get())));
    public static final Supplier<RecipeBookCategories> AGING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_AGING_MISC", new ItemStack(HHModItems.JERKY.get()), new ItemStack(HHModItems.PICKLED_CARROTS.get())));

    public static final Supplier<RecipeBookCategories> FERMENTING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_FERMENTING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> FERMENTING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_FERMENTING_MEALS", new ItemStack(HHModItems.PICKLED_CARROTS.get())));
    public static final Supplier<RecipeBookCategories> FERMENTING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_FERMENTING_DRINKS", new ItemStack(HHModItems.RED_GRAPE_WINE.get())));
    public static final Supplier<RecipeBookCategories> FERMENTING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("HEARTHANDHARVEST_FERMENTING_MISC", new ItemStack(HHModItems.CHEESE_WHEEL.get())));

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(HHRecipeBookTypes.FERMENTING, ImmutableList.of(FERMENTING_SEARCH.get(), FERMENTING_MEALS.get(), FERMENTING_DRINKS.get(), FERMENTING_MISC.get()));
        event.registerAggregateCategory(FERMENTING_SEARCH.get(), ImmutableList.of(FERMENTING_MEALS.get(), FERMENTING_DRINKS.get(), FERMENTING_MISC.get()));
        event.registerRecipeCategoryFinder(HHModRecipeTypes.FERMENTING.get(), recipe ->
        {
            if (recipe instanceof KegRecipe kegRecipe) {
                CaskRecipeBookTab tab = kegRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case MEALS -> FERMENTING_MEALS.get();
                        case DRINKS -> FERMENTING_DRINKS.get();
                        case MISC -> FERMENTING_MISC.get();
                    };
                }
            }
            return FERMENTING_MISC.get();
        });

        event.registerBookCategories(HHRecipeBookTypes.AGING, ImmutableList.of(AGING_SEARCH.get(), AGING_MEALS.get(), AGING_DRINKS.get(), AGING_MISC.get()));
        event.registerAggregateCategory(AGING_SEARCH.get(), ImmutableList.of(AGING_MEALS.get(), AGING_DRINKS.get(), AGING_MISC.get()));
        event.registerRecipeCategoryFinder(HHModRecipeTypes.AGING.get(), recipe ->
        {
            if (recipe instanceof CaskRecipe caskRecipe) {
                CaskRecipeBookTab tab = caskRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case MEALS -> AGING_MEALS.get();
                        case DRINKS -> AGING_DRINKS.get();
                        case MISC -> AGING_MISC.get();
                    };
                }
            }
            return AGING_MISC.get();
        });
    }
}