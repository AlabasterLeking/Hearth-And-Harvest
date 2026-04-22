package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;

public class CuttingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {

        // Knife
        cuttingFoods(consumer);

        // Cleaver
        choppingAnimalItems(consumer);

        // Axe
        halfCabinets(consumer);
    }

    private static void cuttingFoods(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.RASPBERRY_PIE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.RASPBERRY_PIE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BLUEBERRY_PIE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.BLUEBERRY_PIE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.GRAPE_PIE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.GRAPE_PIE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PEANUT_BUTTER_PIE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.PEANUT_BUTTER_PIE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.GOAT_CHEESE_WHEEL.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.GOAT_CHEESE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHEDDAR_CHEESE_WHEEL.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.CHEDDAR_CHEESE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CARROT_CAKE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.CARROT_CAKE_SLICE.get(), 7)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHICKEN_POT_PIE.get()), Ingredient.of(ModTags.Items.KNIVES), HHModItems.CHICKEN_POT_PIE_SLICE.get(), 4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SUNFLOWER), Ingredient.of(ModTags.Items.KNIVES), HHModItems.SUNFLOWER_SEEDS.get(), 2)
                .addResultWithChance(HHModItems.SUNFLOWER_SEEDS.get(), 0.5F, 2)
                .save(consumer);
    }

    private static void choppingAnimalItems(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEEF), Ingredient.of(HHModTags.CLEAVERS), HHModItems.RAW_SAUSAGE.get(), 3)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID,"sausage_from_beef"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP), Ingredient.of(HHModTags.CLEAVERS), HHModItems.RAW_SAUSAGE.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID,"sausage_from_porkchop"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MUTTON), Ingredient.of(HHModTags.CLEAVERS), HHModItems.RAW_SAUSAGE.get(), 3)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sausage_from_mutton"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CHICKEN), Ingredient.of(HHModTags.CLEAVERS), HHModItems.RAW_SAUSAGE.get(), 1)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sausage_from_chicken"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.RABBIT), Ingredient.of(HHModTags.CLEAVERS), HHModItems.RAW_SAUSAGE.get(), 1)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sausage_from_rabbit"));
    }

    private static void halfCabinets(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.OAK_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.OAK_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "oak_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SPRUCE_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.SPRUCE_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "spruce_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BIRCH_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.BIRCH_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "birch_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.JUNGLE_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.JUNGLE_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "jungle_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.ACACIA_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.ACACIA_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "acacia_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.DARK_OAK_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.DARK_OAK_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "dark_oak_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.MANGROVE_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.MANGROVE_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "mangrove_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHERRY_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.CHERRY_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "cherry_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BAMBOO_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.BAMBOO_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "bamboo_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CRIMSON_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.CRIMSON_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "crimson_half_cabinet_from_full"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WARPED_CABINET.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.WARPED_HALF_CABINET.get(), 2)
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "warped_half_cabinet_from_full"));
    }
}