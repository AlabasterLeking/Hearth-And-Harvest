package alabaster.hearthandharvest.data;

import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import alabaster.hearthandharvest.data.recipe.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class Recipes extends RecipeProvider
{
    public Recipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        RecipeOutput output = new RecipeOutput(consumer);
        CraftingRecipes.register(output);
        SmeltingRecipes.register(output);
        StonecuttingRecipes.register(output);
        CookingRecipes.register(output);
        CuttingRecipes.register(output);
        AgingRecipes.register(output);
        KegRecipes.register(output);
        StompingRecipes.register(output);
    }
}