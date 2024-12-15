package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.common.registry.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;

import java.util.stream.Stream;

public class CraftingRecipes
{
    public static final Ingredient WATER_BOTTLE = new Ingredient(DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER), Items.POTION).getCustomIngredient());

    public static void register(RecipeOutput output) {
        recipesBlocks(output);
        recipesTools(output);
        recipesMaterials(output);
        recipesFoodstuffs(output);
        recipesFoodBlocks(output);
        recipesCraftedMeals(output);
    }

    private static void recipesBlocks(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RASPBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.RASPBERRY.get())
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RASPBERRY.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.BLUEBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.BLUEBERRIES.get())
                .unlockedBy("has_blueberry", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BLUEBERRIES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.GRAPE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.GRAPES.get())
                .unlockedBy("has_grape", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRAPES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.PEANUT_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.PEANUT.get())
                .unlockedBy("has_peanut", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PEANUT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.EGG_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.EGG)
                .unlockedBy("has_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EGG))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.MILK_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get())
                .unlockedBy("has_milk_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.GOAT_MILK_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.GOAT_MILK_BOTTLE.get())
                .unlockedBy("has_goat_milk_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOAT_MILK_BOTTLE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.MEAD_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.MEAD.get())
                .unlockedBy("has_mead", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MEAD.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.WINE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.WINE.get())
                .unlockedBy("has_wine", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WINE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.HONEY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.HONEY_BOTTLE)
                .unlockedBy("has_honey_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HONEY_BOTTLE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.WATER_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', WATER_BOTTLE)
                .unlockedBy("has_water_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POTION))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.BROWN_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.BROWN_MUSHROOM)
                .unlockedBy("has_brown_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BROWN_MUSHROOM))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RED_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.RED_MUSHROOM)
                .unlockedBy("has_red_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RED_MUSHROOM))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.CRIMSON_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.CRIMSON_FUNGUS)
                .unlockedBy("has_crimson_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_FUNGUS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.WARPED_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.WARPED_FUNGUS)
                .unlockedBy("has_warped_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WARPED_FUNGUS))
                .save(output);
    }

    private static void recipesTools(RecipeOutput output) {

    }

    private static void recipesMaterials(RecipeOutput output) {

    }

    private static void recipesFoodstuffs(RecipeOutput output) {

    }

    private static void recipesFoodBlocks(RecipeOutput output) {

    }

    private static void recipesCraftedMeals(RecipeOutput output) {

    }
}