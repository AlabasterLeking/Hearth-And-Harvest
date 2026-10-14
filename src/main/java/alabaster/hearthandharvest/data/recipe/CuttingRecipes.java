package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.common.tag.HHCommonTags;
import net.minecraft.nbt.NbtOps;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import alabaster.hearthandharvest.data.builder.HHFinishedRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.VintageIngredient;
import alabaster.hearthandharvest.common.item.VintageHelper;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.Optional;


public class CuttingRecipes {
    public static void register(RecipeOutput output) {
        // Knife
        cuttingFoods(output);
        cheeseCutting(output, HHModItems.CHEESE_WHEEL.get(), HHModItems.CHEESE_SLICE.get());
        cheeseCutting(output, HHModItems.GOAT_CHEESE_WHEEL.get(), HHModItems.GOAT_CHEESE_SLICE.get());
        cuttingFlowers(output);

        // Cleaver
        choppingAnimalItems(output);

        // Axe
        strippingBlocks(output);

        // Pickaxe
        miningBlocks(output);
    }

    private static void cuttingFoods(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.RASPBERRY_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.RASPBERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BLUEBERRY_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.BLUEBERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHERRY_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CHERRY_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.GRAPE_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.GRAPE_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PEANUT_BUTTER_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.PEANUT_BUTTER_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CARROT_CAKE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CARROT_CAKE_SLICE.get(), 7)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHOCOLATE_CAKE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CHOCOLATE_CAKE_SLICE.get(), 7)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHICKEN_POT_PIE.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CHICKEN_POT_PIE_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SUNFLOWER), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.SUNFLOWER_SEEDS.get(), 2)
                .addResultWithChance(HHModItems.SUNFLOWER_SEEDS.get(), 0.5F, 2)
                .setNamespace(HearthAndHarvest.MODID)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CORN.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CORN_KERNELS.get(), 2)
                .addResult(HHModItems.CORN_HUSK.get())
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CORN_KERNELS.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CORN_MEAL.get(), 1)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHEAT), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.FLOUR.get(), 2)
                .setNamespace(HearthAndHarvest.MODID)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PIZZA.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.MEAT_PIZZA.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.MEAT_PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.VEGGIE_PIZZA.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.VEGGIE_PIZZA_SLICE.get(), 4)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHEESE_PIZZA.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), HHModItems.CHEESE_PIZZA_SLICE.get(), 4)
                .save(output);
    }

    private static void cuttingFlowers(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.YELLOW_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.YELLOW_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.ORANGE_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.ORANGE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.RED_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.RED_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BLUE_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.BLUE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.LIGHT_BLUE_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.LIGHT_BLUE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PURPLE_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.PURPLE_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.PINK_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.PINK_DYE, 2)
                .save(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.WHITE_MUM.get()), Ingredient.of(HHCommonTags.TOOLS_KNIVES), Items.WHITE_DYE, 2)
                .save(output);
    }

    private static void choppingAnimalItems(RecipeOutput output) {

    }

    private static void strippingBlocks(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.BAMBOO_TRELLIS.get()), new ToolActionIngredient(ToolActions.AXE_DIG), HHModItems.STRIPPED_BAMBOO_TRELLIS.get())
                .save(output, new ResourceLocation(HearthAndHarvest.MODID, "stripped_bamboo_trellis"));
    }

    private static void miningBlocks(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.SALT_BLOCK.get()), new ToolActionIngredient(ToolActions.PICKAXE_DIG), HHModItems.SALT.get(), 4)
                .save(output, new ResourceLocation(HearthAndHarvest.MODID, "salt_from_salt_block"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.SALT_DRIP.get()), new ToolActionIngredient(ToolActions.PICKAXE_DIG), HHModItems.SALT.get(), 1)
                .addResultWithChance(HHModItems.SALT.get(), 0.5F, 1)
                .save(output, new ResourceLocation(HearthAndHarvest.MODID, "salt_from_salt_drip"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(HHModItems.CHARCOAL_BLOCK.get()), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.CHARCOAL, 9)
                .save(output, new ResourceLocation(HearthAndHarvest.MODID, "charcoal_from_charcoal_block"));
    }

    private static void cheeseCutting(RecipeOutput output, Item wheel, Item slice) {
        String name = BuiltInRegistries.ITEM.getKey(wheel).getPath();
        for (int vintage = 0; vintage <= VintageHelper.MAX_VINTAGE; vintage++) {
            ItemStack slices = VintageHelper.withVintage(new ItemStack(slice, 4), vintage);
            Ingredient input = new VintageIngredient(wheel, vintage);
            String path = vintage == 0 ? name : name + "_" + VintageHelper.STAGE_NAMES[vintage - 1];
            output.accept(HHFinishedRecipe.of(new ResourceLocation(HearthAndHarvest.MODID, "cutting/" + path), ModRecipeSerializers.CUTTING.get(), json -> {
                JsonArray ingredients = new JsonArray();
                ingredients.add(input.toJson());
                json.add("ingredients", ingredients);
                json.add("tool", Ingredient.of(HHCommonTags.TOOLS_KNIVES).toJson());
                JsonObject result = new JsonObject();
                result.addProperty("item", BuiltInRegistries.ITEM.getKey(slices.getItem()).toString());
                result.addProperty("count", slices.getCount());
                if (slices.hasTag()) result.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, slices.getTag()));
                JsonArray results = new JsonArray();
                results.add(result);
                json.add("result", results);
            }, java.util.Map.of()));
        }
    }
}