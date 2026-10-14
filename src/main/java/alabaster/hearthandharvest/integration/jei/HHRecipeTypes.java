package alabaster.hearthandharvest.integration.jei;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.crafting.StompingBasinRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class HHRecipeTypes
{
    public static final RecipeType<CaskRecipe> AGING = RecipeType.create(HearthAndHarvest.MODID, "aging", CaskRecipe.class);
    public static final RecipeType<StompingBasinRecipe> STOMPING = RecipeType.create(HearthAndHarvest.MODID, "stomping", StompingBasinRecipe.class);
    public static final RecipeType<KegRecipe> FERMENTING = RecipeType.create(HearthAndHarvest.MODID, "fermenting", KegRecipe.class);
}