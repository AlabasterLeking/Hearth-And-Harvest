package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class StonecuttingRecipes {

    public static void register(RecipeOutput output) {
        // Salt Block
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_BLOCK.get(), 1, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.SALT_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.SALT_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.SALT_WALL.get(), 1, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_WALL.get(), 1, output);

        // Polished Salt
        stonecutting(HHModBlocks.POLISHED_SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.POLISHED_SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.POLISHED_SALT_BLOCK.get(), HHModBlocks.POLISHED_SALT_WALL.get(), 1, output);
        
        // Manure Bricks
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.POLISHED_MANURE.get(), 1, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.MANURE_BRICK_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.MANURE_BRICK_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.MANURE_BRICK_WALL.get(), 1, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.POLISHED_MANURE_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.POLISHED_MANURE_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.MANURE_BRICKS_BLOCK.get(), HHModBlocks.POLISHED_MANURE_WALL.get(), 1, output);

        // Polished Salt
        stonecutting(HHModBlocks.POLISHED_MANURE.get(), HHModBlocks.POLISHED_MANURE_STAIRS.get(), 1, output);
        stonecutting(HHModBlocks.POLISHED_MANURE.get(), HHModBlocks.POLISHED_MANURE_SLAB.get(), 2, output);
        stonecutting(HHModBlocks.POLISHED_MANURE.get(), HHModBlocks.POLISHED_MANURE_WALL.get(), 1, output);
    }

    private static void stonecutting(ItemLike ingredient, ItemLike result, int count, RecipeOutput output) {
        String ingredientName = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath();
        String resultName = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), RecipeCategory.BUILDING_BLOCKS, result, count)
                .unlockedBy("has_" + ingredientName, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(output, new ResourceLocation(HearthAndHarvest.MODID,
                        resultName + "_from_" + ingredientName + "_stonecutting"));
    }
}