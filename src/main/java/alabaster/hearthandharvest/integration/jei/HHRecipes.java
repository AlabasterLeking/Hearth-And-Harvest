package alabaster.hearthandharvest.integration.jei;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.crafting.StompingBasinRecipe;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.List;
import java.util.Optional;

public class HHRecipes
{
    private final RecipeManager recipeManager;

    public HHRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level != null) {
            this.recipeManager = level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft world must not be null.");
        }
    }

    public List<RecipeHolder<CaskRecipe>> getCaskRecipes() {
        return recipeManager.getAllRecipesFor(HHModRecipeTypes.AGING.get());
    }

    public List<RecipeHolder<StompingBasinRecipe>> getStompingRecipes() {
        return recipeManager.getAllRecipesFor(HHModRecipeTypes.STOMPING.get());
    }

    public List<RecipeHolder<KegRecipe>> getKegRecipes() {
        return recipeManager.getAllRecipesFor(HHModRecipeTypes.FERMENTING.get());
    }
}