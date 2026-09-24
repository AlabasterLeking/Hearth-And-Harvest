package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.FermentingRecipe;
import alabaster.hearthandharvest.common.crafting.BottleCrateRecipe;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.crafting.StompingBasinRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, HearthAndHarvest.MODID);

    public static final Supplier<RecipeType<CaskRecipe>> AGING = RECIPE_TYPES.register("aging", () -> registerRecipeType("aging"));

    public static final Supplier<RecipeType<StompingBasinRecipe>> STOMPING = RECIPE_TYPES.register("stomping", () -> registerRecipeType("stomping"));
    public static final Supplier<RecipeType<FermentingRecipe>> FERMENTING = RECIPE_TYPES.register("fermenting", () -> registerRecipeType("fermenting"));
    public static final Supplier<RecipeType<BottleCrateRecipe>> BOTTLE_CRATE = RECIPE_TYPES.register("bottle_crate", () -> registerRecipeType("bottle_crate"));

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return HearthAndHarvest.MODID + ":" + identifier;
            }
        };
    }
}