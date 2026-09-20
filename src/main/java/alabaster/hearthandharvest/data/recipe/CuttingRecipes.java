package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.crafting.ingredient.ItemAbilityIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;


public class CuttingRecipes {
    public static void register(RecipeOutput output) {
        // Knife
        cuttingFoods(output);
        cuttingFlowers(output);

        // Cleaver
        choppingAnimalItems(output);

        // Axe
        strippingBlocks(output);

        // Pickaxe
        miningBlocks(output);
    }

    private static void cuttingFoods(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.RASPBERRY_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.RASPBERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BLUEBERRY_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.BLUEBERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHERRY_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CHERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.GRAPE_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.GRAPE_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PEANUT_BUTTER_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.PEANUT_BUTTER_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.GOAT_CHEESE_WHEEL.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.GOAT_CHEESE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHEDDAR_CHEESE_WHEEL.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CHEDDAR_CHEESE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CARROT_CAKE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CARROT_CAKE_SLICE.get(), 7)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHICKEN_POT_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CHICKEN_POT_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SUNFLOWER), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.SUNFLOWER_SEEDS.get(), 2)
                .addResultWithChance(HHModItems.SUNFLOWER_SEEDS.get(), 0.5F, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CORN.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CORN_KERNELS.get(), 2)
                .addResult(HHModItems.CORN_HUSK.get())
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CORN_KERNELS.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CORN_MEAL.get(), 1)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHEAT), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.FLOUR.get(), 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PIZZA.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.MEAT_PIZZA.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.MEAT_PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.VEGGIE_PIZZA.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.VEGGIE_PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHEESE_PIZZA.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), HHModItems.CHEESE_PIZZA_SLICE.get(), 4)
                .save(output);
    }

    private static void cuttingFlowers(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.YELLOW_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.YELLOW_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.ORANGE_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.ORANGE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.RED_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.RED_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BLUE_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.BLUE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.LIGHT_BLUE_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.LIGHT_BLUE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PURPLE_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.PURPLE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PINK_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.PINK_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.WHITE_MUM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.WHITE_DYE, 2)
                .save(output);
    }

    private static void choppingAnimalItems(RecipeOutput output) {

    }

    private static void strippingBlocks(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BAMBOO_TRELLIS.get()), new ItemAbilityIngredient(ItemAbilities.AXE_DIG).toVanilla(), HHModItems.STRIPPED_BAMBOO_TRELLIS.get())
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "stripped_bamboo_trellis"));
    }

    private static void miningBlocks(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.SALT_BLOCK.get()), new ItemAbilityIngredient(ItemAbilities.PICKAXE_DIG).toVanilla(), HHModItems.SALT.get(), 4)
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "salt_from_salt_block"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.SALT_DRIP.get()), new ItemAbilityIngredient(ItemAbilities.PICKAXE_DIG).toVanilla(), HHModItems.SALT.get(), 1)
                .addResultWithChance(HHModItems.SALT.get(), 0.5F, 1)
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "salt_from_salt_drip"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHARCOAL_BLOCK.get()), new ItemAbilityIngredient(ItemAbilities.PICKAXE_DIG).toVanilla(), Items.CHARCOAL, 9)
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "charcoal_from_charcoal_block"));
    }
}