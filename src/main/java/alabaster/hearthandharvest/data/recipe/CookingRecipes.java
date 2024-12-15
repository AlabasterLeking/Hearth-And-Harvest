package alabaster.hearthandharvest.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;

import java.util.stream.Stream;

public class CookingRecipes
{
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static void register(RecipeOutput output) {
        cookMiscellaneous(output);
        cookMinecraftSoups(output);
        cookMeals(output);
    }

    private static void cookMiscellaneous(RecipeOutput output) {

    }

    private static void cookMinecraftSoups(RecipeOutput output) {

    }

    private static void cookMeals(RecipeOutput output) {

    }
}