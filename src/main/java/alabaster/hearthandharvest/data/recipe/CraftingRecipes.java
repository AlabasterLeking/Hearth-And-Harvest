package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import alabaster.hearthandharvest.common.tag.HHModTags;
import alabaster.hearthandharvest.data.builder.ShapelessRemainderRecipeBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CraftingRecipes
{
    public static final Ingredient WATER_BOTTLE = new Ingredient(DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER), Items.POTION).getCustomIngredient());

    public static void register(RecipeOutput output) {
        recipesBlocks(output);
        recipesTools(output);
        recipesMaterials(output);
        recipesFoodstuffs(output);
    }

    private static void recipesBlocks(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.TREE_TAPPER.get(), 1)
                .pattern(" I ")
                .pattern("SBS")
                .pattern(" S ")
                .define('B', Items.BUCKET)
                .define('I', Items.IRON_INGOT)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.CASK.get(), 1)
                .pattern("SSS")
                .pattern("C C")
                .pattern("SSS")
                .define('C', Items.COPPER_INGOT)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.STOMPING_BASIN.get(), 1)
                .pattern("C C")
                .pattern("PSP")
                .define('C', Items.COPPER_INGOT)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.TRELLIS.get(), 4)
                .pattern(" S ")
                .pattern("SSS")
                .pattern(" S ")
                .define('S', Items.STICK)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.BAMBOO_TRELLIS.get(), 4)
                .pattern(" S ")
                .pattern("SSS")
                .pattern(" S ")
                .define('S', Items.BAMBOO)
                .unlockedBy("has_bamboo", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAMBOO))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.JAR.get(), 4)
                .pattern(" C ")
                .pattern("G G")
                .pattern("GGG")
                .define('G', Items.GLASS_PANE)
                .define('C', ModItems.CANVAS.get())
                .unlockedBy("has_glass_pane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLASS_PANE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModItems.JUG.get(), 1)
                .pattern(" I ")
                .pattern("IBI")
                .pattern("IBI")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BUCKET)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.JUG.get(), 1)
                .requires(HHModItems.JUG.get())
                .unlockedBy("has_jug", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.JUG.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "jug_cleaning"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.COUNTER.get())
                .pattern("ii")
                .pattern("BB")
                .pattern("BB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.DRAWER.get())
                .pattern("iii")
                .pattern("BCB")
                .pattern("BBB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('C', ModTags.Items.CABINETS_WOODEN)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BASIN.get())
                .pattern("iii")
                .pattern("BCB")
                .pattern("BBB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('C', Items.CAULDRON)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.NEST.get())
                .pattern("SSS")
                .pattern("sss")
                .define('S', ModItems.STRAW.get())
                .define('s', Items.STICK)
                .unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.SCARECROW.get())
                .pattern("cwc")
                .pattern("sHs")
                .pattern(" s ")
                .define('c', ModItems.CANVAS.get())
                .define('s', Items.STICK)
                .define('H', Items.HAY_BLOCK)
                .define('w', Items.WHEAT)
                .unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .unlockedBy("has_hay_block", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HAY_BLOCK))
                .unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.HAY_RUG.get(), 4)
                .requires(Items.HAY_BLOCK)
                .requires(Items.HAY_BLOCK)
                .unlockedBy("has_hay_block", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HAY_BLOCK))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.STRAW_RUG.get(), 4)
                .requires(ModItems.STRAW_BALE.get())
                .requires(ModItems.STRAW_BALE.get())
                .unlockedBy("has_straw_bale", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_BALE.get()))
                .save(output);

        // Cabinets
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.OAK_CABINET.get(), 1)
                .requires(HHModItems.OAK_HALF_CABINET.get())
                .requires(HHModItems.OAK_HALF_CABINET.get())
                .unlockedBy("has_oak_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.OAK_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "oak_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.SPRUCE_CABINET.get(), 1)
                .requires(HHModItems.SPRUCE_HALF_CABINET.get())
                .requires(HHModItems.SPRUCE_HALF_CABINET.get())
                .unlockedBy("has_spruce_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SPRUCE_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "spruce_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.BIRCH_CABINET.get(), 1)
                .requires(HHModItems.BIRCH_HALF_CABINET.get())
                .requires(HHModItems.BIRCH_HALF_CABINET.get())
                .unlockedBy("has_birch_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BIRCH_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "birch_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.JUNGLE_CABINET.get(), 1)
                .requires(HHModItems.JUNGLE_HALF_CABINET.get())
                .requires(HHModItems.JUNGLE_HALF_CABINET.get())
                .unlockedBy("has_jungle_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.JUNGLE_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "jungle_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.ACACIA_CABINET.get(), 1)
                .requires(HHModItems.ACACIA_HALF_CABINET.get())
                .requires(HHModItems.ACACIA_HALF_CABINET.get())
                .unlockedBy("has_acacia_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ACACIA_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "acacia_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.DARK_OAK_CABINET.get(), 1)
                .requires(HHModItems.DARK_OAK_HALF_CABINET.get())
                .requires(HHModItems.DARK_OAK_HALF_CABINET.get())
                .unlockedBy("has_dark_oak_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.DARK_OAK_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "dark_oak_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.MANGROVE_CABINET.get(), 1)
                .requires(HHModItems.MANGROVE_HALF_CABINET.get())
                .requires(HHModItems.MANGROVE_HALF_CABINET.get())
                .unlockedBy("has_mangrove_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANGROVE_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "mangrove_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.CHERRY_CABINET.get(), 1)
                .requires(HHModItems.CHERRY_HALF_CABINET.get())
                .requires(HHModItems.CHERRY_HALF_CABINET.get())
                .unlockedBy("has_cherry_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "cherry_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.BAMBOO_CABINET.get(), 1)
                .requires(HHModItems.BAMBOO_HALF_CABINET.get())
                .requires(HHModItems.BAMBOO_HALF_CABINET.get())
                .unlockedBy("has_bamboo_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BAMBOO_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "bamboo_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.CRIMSON_CABINET.get(), 1)
                .requires(HHModItems.CRIMSON_HALF_CABINET.get())
                .requires(HHModItems.CRIMSON_HALF_CABINET.get())
                .unlockedBy("has_crimson_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CRIMSON_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "crimson_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.WARPED_CABINET.get(), 1)
                .requires(HHModItems.WARPED_HALF_CABINET.get())
                .requires(HHModItems.WARPED_HALF_CABINET.get())
                .unlockedBy("has_warped_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.WARPED_HALF_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "warped_cabinet_from_halves"));
        
        // Half-Cabinets
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.OAK_HALF_CABINET.get(), 2)
                .requires(ModItems.OAK_CABINET.get())
                .requires(ModItems.OAK_CABINET.get())
                .unlockedBy("has_oak_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.OAK_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "oak_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.SPRUCE_HALF_CABINET.get(), 2)
                .requires(ModItems.SPRUCE_CABINET.get())
                .requires(ModItems.SPRUCE_CABINET.get())
                .unlockedBy("has_spruce_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SPRUCE_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "spruce_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.BIRCH_HALF_CABINET.get(), 2)
                .requires(ModItems.BIRCH_CABINET.get())
                .requires(ModItems.BIRCH_CABINET.get())
                .unlockedBy("has_birch_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BIRCH_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "birch_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.JUNGLE_HALF_CABINET.get(), 2)
                .requires(ModItems.JUNGLE_CABINET.get())
                .requires(ModItems.JUNGLE_CABINET.get())
                .unlockedBy("has_jungle_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.JUNGLE_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "jungle_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.ACACIA_HALF_CABINET.get(), 2)
                .requires(ModItems.ACACIA_CABINET.get())
                .requires(ModItems.ACACIA_CABINET.get())
                .unlockedBy("has_acacia_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ACACIA_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "acacia_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.DARK_OAK_HALF_CABINET.get(), 2)
                .requires(ModItems.DARK_OAK_CABINET.get())
                .requires(ModItems.DARK_OAK_CABINET.get())
                .unlockedBy("has_dark_oak_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DARK_OAK_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "dark_oak_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.MANGROVE_HALF_CABINET.get(), 2)
                .requires(ModItems.MANGROVE_CABINET.get())
                .requires(ModItems.MANGROVE_CABINET.get())
                .unlockedBy("has_mangrove_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MANGROVE_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "mangrove_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.CHERRY_HALF_CABINET.get(), 2)
                .requires(ModItems.CHERRY_CABINET.get())
                .requires(ModItems.CHERRY_CABINET.get())
                .unlockedBy("has_cherry_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CHERRY_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "cherry_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.BAMBOO_HALF_CABINET.get(), 2)
                .requires(ModItems.BAMBOO_CABINET.get())
                .requires(ModItems.BAMBOO_CABINET.get())
                .unlockedBy("has_bamboo_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BAMBOO_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "bamboo_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.CRIMSON_HALF_CABINET.get(), 2)
                .requires(ModItems.CRIMSON_CABINET.get())
                .requires(ModItems.CRIMSON_CABINET.get())
                .unlockedBy("has_crimson_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRIMSON_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "crimson_cabinet_into_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, HHModItems.WARPED_HALF_CABINET.get(), 2)
                .requires(ModItems.WARPED_CABINET.get())
                .requires(ModItems.WARPED_CABINET.get())
                .unlockedBy("has_warped_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WARPED_CABINET.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "warped_cabinet_into_halves"));

        // Bottle Racks
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.OAK_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.OAK_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BIRCH_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.BIRCH_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.SPRUCE_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.SPRUCE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.JUNGLE_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.JUNGLE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.ACACIA_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.ACACIA_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.DARK_OAK_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.DARK_OAK_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.MANGROVE_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.MANGROVE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.CHERRY_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.CHERRY_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BAMBOO_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.BAMBOO_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.CRIMSON_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.CRIMSON_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.WARPED_BOTTLE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.WARPED_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.CRATE.get(), 2)
                .pattern("sss")
                .pattern("www")
                .define('s', Items.STICK)
                .define('w', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HHModItems.UNIVERSAL_FEED.get(), 8)
                .pattern("   ")
                .pattern("HCF")
                .pattern("ABS")
                .define('H', Items.HAY_BLOCK)
                .define('C', Items.CARROT)
                .define('A', Items.APPLE)
                .define('B', Items.BONE)
                .define('F', Items.SALMON)
                .define('S', Items.SWEET_BERRIES)
                .unlockedBy("has_hay_bale", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HAY_BLOCK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.WATERING_CAN.get(), 1)
                .pattern("C  ")
                .pattern("CBC")
                .pattern("CC ")
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BUCKET)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.ARROW, 4)
                .pattern("F")
                .pattern("S")
                .pattern("C")
                .define('F', Items.FLINT)
                .define('S', Items.STICK)
                .define('C', HHModItems.CROW_FEATHER.get())
                .unlockedBy("has_crow_feather", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CROW_FEATHER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "arrows_from_crow_feather"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Items.WRITABLE_BOOK, 1)
                .requires(Items.INK_SAC)
                .requires(Items.BOOK)
                .requires(HHModItems.CROW_FEATHER.get())
                .unlockedBy("has_crow_feather", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CROW_FEATHER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "book_and_quill_with_crow_feather"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.BRUSH, 1)
                .pattern("F")
                .pattern("C")
                .pattern("S")
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .define('F', HHModItems.CROW_FEATHER.get())
                .unlockedBy("has_crow_feather", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CROW_FEATHER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "brush_from_crow_feather"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER, 3)
                .requires(HHModItems.CORN_HUSK.get())
                .requires(HHModItems.CORN_HUSK.get())
                .requires(HHModItems.CORN_HUSK.get())
                .unlockedBy("has_corn_husk", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_HUSK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "paper_from_corn_husks"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CANVAS.get(), 2)
                .requires(HHModItems.CORN_HUSK.get())
                .requires(HHModItems.CORN_HUSK.get())
                .requires(HHModItems.CORN_HUSK.get())
                .requires(HHModItems.CORN_HUSK.get())
                .unlockedBy("has_corn_husk", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_HUSK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "canvas_from_corn_husks"));

        // Storage Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.BLUEBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.FRUITS_BLUEBERRY)
                .unlockedBy("has_blueberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CHERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.FRUITS_CHERRY)
                .unlockedBy("has_cherry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RASPBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.FRUITS_RASPBERRY)
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RED_GRAPE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.RED_GRAPES.get())
                .unlockedBy("has_red_grapes", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GREEN_GRAPE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.GREEN_GRAPES.get())
                .unlockedBy("has_green_grapes", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GREEN_GRAPES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.PEANUT_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.CROPS_PEANUT)
                .unlockedBy("has_peanut", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CORN_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.VEGETABLES_CORN)
                .unlockedBy("has_corn", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.APPLE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.APPLE)
                .unlockedBy("has_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GOLDEN_APPLE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_APPLE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GOLDEN_CARROT_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GOLDEN_CARROT)
                .unlockedBy("has_golden_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_CARROT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GLISTERING_MELON_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GLISTERING_MELON_SLICE)
                .unlockedBy("has_glistering_melon_slice", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLISTERING_MELON_SLICE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POISONOUS_POTATO_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.POISONOUS_POTATO)
                .unlockedBy("has_poisonous_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POISONOUS_POTATO))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.ROTTEN_TOMATO_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.ROTTEN_TOMATO.get())
                .unlockedBy("has_rotten_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ROTTEN_TOMATO.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GLOW_BERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GLOW_BERRIES)
                .unlockedBy("has_glow_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLOW_BERRIES))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SWEET_BERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.SWEET_BERRIES)
                .unlockedBy("has_sweet_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SWEET_BERRIES))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.COTTON_BALE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.CROPS_COTTON)
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SPOOL.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STRING)
                .unlockedBy("has_string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.ROPE_COIL.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.ROPE.get())
                .unlockedBy("has_rope", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ROPE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CORN_KERNEL_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.CORN_KERNELS.get())
                .unlockedBy("has_corn_kernels", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_KERNELS.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CORN_HUSK_BUNDLE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.CORN_HUSK.get())
                .unlockedBy("has_corn_husk", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_HUSK.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHCommonTags.DUSTS_SALT)
                .unlockedBy("has_salt", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SUGAR_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.SUGAR)
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.COCOA_BEAN_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.COCOA_BEANS)
                .unlockedBy("has_cocoa_bean", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COCOA_BEANS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GUNPOWDER_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GUNPOWDER)
                .unlockedBy("has_gunpowder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GUNPOWDER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.FLOUR_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.FLOUR.get())
                .unlockedBy("has_flour", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.MANURE.get())
                .unlockedBy("has_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CHARCOAL_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.CHARCOAL)
                .unlockedBy("has_charcoal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHARCOAL))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.STICK_BRUSH.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STICK)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SUGAR_CANE_BUNDLE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.SUGAR_CANE)
                .unlockedBy("has_sugar_cane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR_CANE))
                .save(output);

        // Half Crates
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.BROWN_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.BROWN_MUSHROOM)
                .unlockedBy("has_brown_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BROWN_MUSHROOM))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RED_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.RED_MUSHROOM)
                .unlockedBy("has_red_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RED_MUSHROOM))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CRIMSON_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.CRIMSON_FUNGUS)
                .unlockedBy("has_crimson_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_FUNGUS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.WARPED_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.WARPED_FUNGUS)
                .unlockedBy("has_warped_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WARPED_FUNGUS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_BLOCK.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.SALT.get())
                .unlockedBy("has_salt", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "salt_block_from_salt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_BLOCK.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.SALT_DRIP.get())
                .unlockedBy("has_salt_drip", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_DRIP.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "salt_block_from_salt_drips"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', HHModItems.SALT_BLOCK.get())
                .unlockedBy("has_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BLOCK.get()))
                .group("stairs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_SLAB.get(), 6)
                .pattern("###")
                .define('#', HHModItems.SALT_BLOCK.get())
                .unlockedBy("has_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BLOCK.get()))
                .group("slabs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.SALT_BLOCK.get())
                .unlockedBy("has_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BLOCK.get()))
                .group("walls")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_SALT_BLOCK.get(), 4)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.SALT_BLOCK.get())
                .unlockedBy("has_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BLOCK.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_SALT_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', HHModItems.POLISHED_SALT_BLOCK.get())
                .unlockedBy("has_polished_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_SALT_BLOCK.get()))
                .group("stairs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_SALT_SLAB.get(), 6)
                .pattern("###")
                .define('#', HHModItems.POLISHED_SALT_BLOCK.get())
                .unlockedBy("has_polished_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_SALT_BLOCK.get()))
                .group("slabs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_SALT_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.POLISHED_SALT_BLOCK.get())
                .unlockedBy("has_polished_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_SALT_BLOCK.get()))
                .group("walls")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_LAMP.get(), 1)
                .pattern("S")
                .pattern("G")
                .pattern("B")
                .define('S', HHModItems.POLISHED_SALT_BLOCK.get())
                .define('G', Items.GLOWSTONE)
                .define('B', ItemTags.BUTTONS)
                .unlockedBy("has_polished_salt_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_SALT_BLOCK.get()))
                .group("walls")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BLOCK.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.MANURE.get())
                .unlockedBy("has_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BRICK_BLOCK.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.MANURE_BRICK.get())
                .unlockedBy("has_manure_brick", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BRICK_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', HHModItems.MANURE_BRICK_BLOCK.get())
                .unlockedBy("has_manure_brick_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE_BRICK_BLOCK.get()))
                .group("stairs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BRICK_SLAB.get(), 6)
                .pattern("###")
                .define('#', HHModItems.MANURE_BRICK_BLOCK.get())
                .unlockedBy("has_manure_brick_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE_BRICK_BLOCK.get()))
                .group("slabs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MANURE_BRICK_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.MANURE_BRICK_BLOCK.get())
                .unlockedBy("has_manure_brick_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE_BRICK_BLOCK.get()))
                .group("walls")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_MANURE_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', HHModItems.POLISHED_MANURE.get())
                .unlockedBy("has_polished_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_MANURE.get()))
                .group("stairs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_MANURE_SLAB.get(), 6)
                .pattern("###")
                .define('#', HHModItems.POLISHED_MANURE.get())
                .unlockedBy("has_polished_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_MANURE.get()))
                .group("slabs")
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POLISHED_MANURE_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.POLISHED_MANURE.get())
                .unlockedBy("has_polished_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POLISHED_MANURE.get()))
                .group("walls")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.MULCH.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.TREE_BARK.get())
                .unlockedBy("has_tree_bark", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_BARK.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ORGANIC_COMPOST.get(), 1)
                .requires(Items.DIRT)
                .requires(ModItems.STRAW.get())
                .requires(ModItems.STRAW.get())
                .requires(HHModItems.MANURE.get())
                .requires(HHModItems.MANURE.get())
                .requires(HHModItems.MANURE.get())
                .requires(HHModItems.MANURE.get())
                .requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL)
                .unlockedBy("has_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE.get()))
                .unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
                .group("fd_organic_compost")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "organic_compost_from_manure"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.TROUGH.get(), 1)
                .pattern("n n")
                .pattern("iii")
                .define('i', Items.IRON_INGOT)
                .define('n', Items.IRON_NUGGET)
                .unlockedBy("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(output);
    }

    private static void recipesTools(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.FLINT_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy("has_flint", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FLINT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.IRON_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.GOLDEN_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.DIAMOND_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.SEED_POUCH.get(), 1)
                .pattern(" S ")
                .pattern("CsC")
                .pattern(" C ")
                .define('C', ModItems.CANVAS.get())
                .define('S', Items.STRING)
                .define('s', Tags.Items.SEEDS)
                .unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
                .save(output);
    }

    private static void recipesMaterials(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RASPBERRY.get(), 9)
                .requires(HHModItems.RASPBERRY_CRATE.get())
                .unlockedBy("has_raspberry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BLUEBERRIES.get(), 9)
                .requires(HHModItems.BLUEBERRY_CRATE.get())
                .unlockedBy("has_blueberry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRY_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CHERRY.get(), 9)
                .requires(HHModItems.CHERRY_CRATE.get())
                .unlockedBy("has_cherry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RED_GRAPES.get(), 9)
                .requires(HHModItems.RED_GRAPE_CRATE.get())
                .unlockedBy("has_red_grape_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPE_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.GREEN_GRAPES.get(), 9)
                .requires(HHModItems.GREEN_GRAPE_CRATE.get())
                .unlockedBy("has_green_grape_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GREEN_GRAPE_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.PEANUT.get(), 9)
                .requires(HHModItems.PEANUT_CRATE.get())
                .unlockedBy("has_peanut_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CORN.get(), 9)
                .requires(HHModItems.CORN_CRATE.get())
                .unlockedBy("has_corn_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.APPLE, 9)
                .requires(HHModItems.APPLE_CRATE.get())
                .unlockedBy("has_apple_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.APPLE_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "apple_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GOLDEN_APPLE, 9)
                .requires(HHModItems.GOLDEN_APPLE_CRATE.get())
                .unlockedBy("has_golden_apple_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GOLDEN_APPLE_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "golden_apple_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GOLDEN_CARROT, 9)
                .requires(HHModItems.GOLDEN_CARROT_CRATE.get())
                .unlockedBy("has_golden_carrot_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GOLDEN_CARROT_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "golden_carrot_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GLISTERING_MELON_SLICE, 9)
                .requires(HHModItems.GLISTERING_MELON_CRATE.get())
                .unlockedBy("has_glistering_melon_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GLISTERING_MELON_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "glistering_melon_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.POISONOUS_POTATO, 9)
                .requires(HHModItems.POISONOUS_POTATO_CRATE.get())
                .unlockedBy("has_poisonous_potato_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POISONOUS_POTATO_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "poisonous_potato_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ROTTEN_TOMATO.get(), 9)
                .requires(HHModItems.ROTTEN_TOMATO_CRATE.get())
                .unlockedBy("has_rotten_tomato_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROTTEN_TOMATO_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "rotten_tomato_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GLOW_BERRIES, 9)
                .requires(HHModItems.GLOW_BERRY_CRATE.get())
                .unlockedBy("has_glow_berry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GLOW_BERRY_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "glow_berry_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SWEET_BERRIES, 9)
                .requires(HHModItems.SWEET_BERRY_CRATE.get())
                .unlockedBy("has_sweet_berry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SWEET_BERRY_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sweet_berry_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HHModItems.COTTON.get(), 9)
                .requires(HHModItems.COTTON_BALE.get())
                .unlockedBy("has_cotton_bale", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON_BALE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 9)
                .requires(HHModItems.SPOOL.get())
                .unlockedBy("has_spool", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SPOOL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "string_from_spool"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ROPE.get(), 9)
                .requires(HHModItems.ROPE_COIL.get())
                .unlockedBy("has_rope_coil", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROPE_COIL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "rope_from_coil"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CORN_KERNELS.get(), 9)
                .requires(HHModItems.CORN_KERNEL_BAG.get())
                .unlockedBy("has_corn_kernel_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_KERNEL_BAG.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.FLOUR.get(), 9)
                .requires(HHModItems.FLOUR_BAG.get())
                .unlockedBy("has_flour_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR_BAG.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HHModItems.CORN_HUSK.get(), 9)
                .requires(HHModItems.CORN_HUSK_BUNDLE.get())
                .unlockedBy("has_corn_husk_bundle", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN_HUSK_BUNDLE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SALT.get(), 9)
                .requires(HHModItems.SALT_BAG.get())
                .unlockedBy("has_salt_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BAG.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SUGAR, 9)
                .requires(HHModItems.SUGAR_BAG.get())
                .unlockedBy("has_sugar_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SUGAR_BAG.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sugar_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.COCOA_BEANS, 9)
                .requires(HHModItems.COCOA_BEAN_BAG.get())
                .unlockedBy("has_cocoa_bean_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COCOA_BEAN_BAG.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "cocoa_bean_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 9)
                .requires(HHModItems.GUNPOWDER_BAG.get())
                .unlockedBy("has_gunpowder_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GUNPOWDER_BAG.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "gunpowder_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HHModItems.MANURE.get(), 9)
                .requires(HHModItems.MANURE_BAG.get())
                .unlockedBy("has_manure_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE_BAG.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "manure_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL, 9)
                .requires(HHModItems.CHARCOAL_BLOCK.get())
                .unlockedBy("has_charcoal_block", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHARCOAL_BLOCK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "charcoal_from_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STICK, 9)
                .requires(HHModItems.STICK_BRUSH.get())
                .unlockedBy("has_stick_brush", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.STICK_BRUSH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "stick_from_brush"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SUGAR_CANE, 9)
                .requires(HHModItems.SUGAR_CANE_BUNDLE.get())
                .unlockedBy("has_sugar_cane_bundle", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SUGAR_CANE_BUNDLE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sugar_cane_from_bundle"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.BROWN_MUSHROOM, 9)
                .requires(HHModItems.BROWN_MUSHROOM_CRATE.get())
                .unlockedBy("has_brown_mushroom_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BROWN_MUSHROOM_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "brown_mushroom_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RED_MUSHROOM, 9)
                .requires(HHModItems.RED_MUSHROOM_CRATE.get())
                .unlockedBy("has_red_mushroom_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_MUSHROOM_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "red_mushroom_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.CRIMSON_FUNGUS, 9)
                .requires(HHModItems.CRIMSON_FUNGUS_CRATE.get())
                .unlockedBy("has_crimson_fungus_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CRIMSON_FUNGUS_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "crimson_fungus_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.WARPED_FUNGUS, 9)
                .requires(HHModItems.WARPED_FUNGUS_CRATE.get())
                .unlockedBy("has_warped_fungus_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.WARPED_FUNGUS_CRATE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "warped_fungus_from_crate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 2)
                .requires(HHCommonTags.CROPS_COTTON)
                .requires(HHCommonTags.CROPS_COTTON)
                .requires(HHCommonTags.CROPS_COTTON)
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "string_from_cotton"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL, 1)
                .requires(HHModItems.CHARRED_MARSHMALLOW_STICK.get())
                .unlockedBy("has_charred_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHARRED_MARSHMALLOW_STICK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "charcoal_from_charred_marshmallow"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HHModItems.WET_MANURE_BRICK.get(), 2)
                .requires(ModItems.STRAW.get())
                .requires(HHModItems.MANURE.get())
                .unlockedBy("has_manure", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANURE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "wet_manure_bricks_from_straw"));
    }

    private static void recipesFoodstuffs(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.MARSHMALLOW_STICK.get(), 1)
                .pattern(" #")
                .pattern("S ")
                .define('#', Items.SUGAR)
                .define('S', Items.STICK)
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SMORE.get(), 1)
                .requires(HHModItems.ROASTED_MARSHMALLOW_STICK.get())
                .requires(HHModItems.CHOCOLATE_BAR.get())
                .requires(Items.COOKIE)
                .requires(Items.COOKIE)
                .unlockedBy("has_roasted_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROASTED_MARSHMALLOW_STICK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "smore"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.COTTON_CANDY.get(), 1)
                .pattern(" W ")
                .pattern("#C#")
                .pattern(" S ")
                .define('#', Items.SUGAR)
                .define('S', Items.STICK)
                .define('W', Items.WIND_CHARGE)
                .define('C', HHCommonTags.CROPS_COTTON)
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_COOKIE.get(), 8)
                .requires(Items.WHEAT)
                .requires(Items.WHEAT)
                .requires(HHModItems.PEANUT_BUTTER.get())
                .unlockedBy("has_peanut_butter", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_MUFFIN.get(), 1)
                .pattern(" B ")
                .pattern("BSB")
                .pattern(" W ")
                .define('W', HHModItems.BATTER.get())
                .define('S', Items.SUGAR)
                .define('B', HHCommonTags.FRUITS_BLUEBERRY)
                .unlockedBy("has_blueberries", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.RASPBERRY_SCONE.get(), 1)
                .pattern(" B ")
                .pattern("RSR")
                .pattern(" W ")
                .define('W', HHModItems.BATTER.get())
                .define('S', Items.SUGAR)
                .define('R', HHCommonTags.FRUITS_RASPBERRY)
                .define('B', HHModItems.BUTTER.get())
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.RASPBERRY_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHCommonTags.FRUITS_RASPBERRY)
                .define('P', ModItems.PIE_CRUST.get())
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHCommonTags.FRUITS_BLUEBERRY)
                .define('P', ModItems.PIE_CRUST.get())
                .unlockedBy("has_blueberries", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.GRAPE_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHCommonTags.FRUITS_GRAPE)
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_grapes", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPES.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHCommonTags.CROPS_PEANUT)
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_peanut_butters", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROASTED_PEANUTS.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.RASPBERRY_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.RASPBERRY_PIE_SLICE.get())
                .unlockedBy("has_raspberry_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY_PIE_SLICE.get()))
                .group("raspberry_pie")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "raspberry_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.BLUEBERRY_PIE_SLICE.get())
                .unlockedBy("has_blueberry_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRY_PIE_SLICE.get()))
                .group("blueberry_pie")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "blueberry_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.GRAPE_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.GRAPE_PIE_SLICE.get())
                .unlockedBy("has_grape_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GRAPE_PIE_SLICE.get()))
                .group("grape_pie")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "grape_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.PEANUT_BUTTER_PIE_SLICE.get())
                .unlockedBy("has_peanut_butter_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER_PIE_SLICE.get()))
                .group("peanut_butter_pie")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "peanut_butter_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CHICKEN_POT_PIE.get(), 1)
                .pattern(" C ")
                .pattern("TVO")
                .pattern("MPS")
                .define('T', Items.POTATO)
                .define('V', Items.CARROT)
                .define('S', HHCommonTags.DUSTS_SALT)
                .define('M', Tags.Items.DRINKS_MILK)
                .define('C', ModItems.COOKED_CHICKEN_CUTS.get())
                .define('O', ModItems.ONION.get())
                .define('P', ModItems.PIE_CRUST.get())
                .unlockedBy("has_chicken_cuts", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COOKED_CHICKEN_CUTS.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CHICKEN_POT_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.CHICKEN_POT_PIE_SLICE.get())
                .unlockedBy("has_grape_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GRAPE_PIE_SLICE.get()))
                .group("grape_pie")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "chicken_pot_pie_from_slices"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CARROT_CAKE.get())
                .pattern("CMC")
                .pattern("SES")
                .pattern("WWW")
                .define('C', Items.CARROT)
                .define('S', Items.SUGAR)
                .define('W', Items.WHEAT)
                .define('E', Items.EGG)
                .define('M', Tags.Items.DRINKS_MILK)
                .unlockedBy("has_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CARROT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CARROT_CAKE.get())
                .pattern("CMC")
                .pattern("SES")
                .pattern(" B ")
                .define('C', Items.CARROT)
                .define('S', Items.SUGAR)
                .define('B', HHModItems.BATTER.get())
                .define('E', Items.EGG)
                .define('M', Tags.Items.DRINKS_MILK)
                .unlockedBy("has_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CARROT))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "carrot_cake_from_batter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.CAKE)
                .pattern("CMC")
                .pattern("SES")
                .pattern(" B ")
                .define('C', Items.CARROT)
                .define('S', Items.SUGAR)
                .define('B', HHModItems.BATTER.get())
                .define('E', Items.EGG)
                .define('M', Tags.Items.DRINKS_MILK)
                .unlockedBy("has_milk", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MILK_BUCKET))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "cake_from_batter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.TRAIL_MIX.get())
                .requires(HHCommonTags.CROPS_PEANUT)
                .requires(HHModItems.RAISINS.get())
                .requires(Items.COCOA_BEANS)
                .requires(Tags.Items.SEEDS)
                .unlockedBy("has_raisins", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RAISINS.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "trail_mix"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CARROT_CAKE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .requires(HHModItems.CARROT_CAKE_SLICE.get())
                .unlockedBy("has_carrot_cake_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CARROT_CAKE_SLICE.get()))
                .group("carrot_cake")
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "carrot_cake_from_slices"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CHOCOLATE_MILK_BOTTLE.get(),1)
                .requires(Tags.Items.DRINKS_MILK)
                .requires(Items.COCOA_BEANS)
                .requires(Items.SUGAR)
                .unlockedBy("has_cocoa_beans", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COCOA_BEANS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SAUSAGE.get(),3)
                .requires(HHModTags.CLEAVERS)
                .requires(Items.BEEF)
                .unlockedBy("has_beef", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BEEF))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sausage_from_beef"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SAUSAGE.get(),2)
                .requires(HHModTags.CLEAVERS)
                .requires(Items.PORKCHOP)
                .unlockedBy("has_porkchop", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PORKCHOP))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sausage_from_porkchop"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SAUSAGE.get(),2)
                .requires(HHModTags.CLEAVERS)
                .requires(Items.MUTTON)
                .unlockedBy("has_mutton", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MUTTON))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sausage_from_mutton"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SAUSAGE.get(),1)
                .requires(HHModTags.CLEAVERS)
                .requires(Items.CHICKEN)
                .unlockedBy("has_chicken", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHICKEN))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sausage_from_chicken"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SAUSAGE.get(),1)
                .requires(HHModTags.CLEAVERS)
                .requires(Items.RABBIT)
                .unlockedBy("has_rabbit", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RABBIT))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sausage_from_rabbit"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RAW_SKEWERED_SAUSAGE.get(), 1)
                .requires(HHModItems.RAW_SAUSAGE.get())
                .requires(Items.STICK)
                .unlockedBy("has_raw_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RAW_SAUSAGE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "raw_skewered_sausage"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SKEWERED_SAUSAGE.get(), 1)
                .requires(HHModItems.COOKED_SAUSAGE.get())
                .requires(Items.STICK)
                .unlockedBy("has_cooked_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COOKED_SAUSAGE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "skewered_sausage"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.UNCOOKED_CORN_ON_THE_COB.get(), 1)
                .requires(HHCommonTags.VEGETABLES_CORN)
                .requires(HHModItems.BUTTER.get())
                .requires(Items.STICK)
                .unlockedBy("has_corn", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CORN.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "uncooked_corn_on_the_cob"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_AND_JELLY_SANDWICH.get(), 1)
                .requires(HHModTags.JAMS)
                .requires(HHModItems.PEANUT_BUTTER.get())
                .requires(Items.BREAD)
                .unlockedBy("has_peanut_butter", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "peanut_butter_and_jelly_sandwich"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BATTER.get(), 1)
                .requires(Tags.Items.DRINKS_MILK)
                .requires(HHCommonTags.FLOURS)
                .requires(Items.BOWL)
                .unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "batter"));

        ShapelessRemainderRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.TORTILLA.get(), 2)
                .requires(Ingredient.of(HHCommonTags.DUSTS_SALT))
                .requires(Ingredient.of(HHCommonTags.FLOURS))
                .requires(WATER_BOTTLE, Items.GLASS_BOTTLE)
                .unlockedBy("has_salt", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "tortilla"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SUGAR, 4)
                .requires(HHModItems.SYRUP_BOTTLE.get())
                .unlockedBy("has_syrup", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SYRUP_BOTTLE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "sugar_from_syrup"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.PIE_CRUST.get(), 2)
                .pattern("fbf")
                .define('f', HHModItems.FLOUR.get())
                .define('b', HHModItems.BUTTER.get())
                .unlockedBy("has_flour", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "pie_crust_from_flour"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BUTTER.get(), 2)
                .requires(HHCommonTags.DUSTS_SALT)
                .requires(Tags.Items.DRINKS_MILK)
                .unlockedBy("has_milk", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MILK_BUCKET))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "butter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WHEAT_DOUGH.get(), 2)
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.SALT.get())
                .requires(Tags.Items.EGGS)
                .group("fd_dough")
                .unlockedBy("has_flour", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "flour_dough_from_egg"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WHEAT_DOUGH.get(), 2)
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.SALT.get())
                .requires(WATER_BOTTLE)
                .group("fd_dough")
                .unlockedBy("has_flour", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "flour_dough_from_water_bottle"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WHEAT_DOUGH.get(), 2)
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.FLOUR.get())
                .requires(HHModItems.SALT.get())
                .requires(Items.WATER_BUCKET)
                .group("fd_dough")
                .unlockedBy("has_flour", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.FLOUR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "flour_dough_from_water_bucket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.MAPLE_COOKIE.get(), 8)
                .requires(Items.WHEAT)
                .requires(Items.WHEAT)
                .requires(HHModItems.SYRUP_BOTTLE.get())
                .unlockedBy("has_syrup_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SYRUP_BOTTLE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.TACO.get(), 1)
                .requires(HHModItems.TORTILLA.get())
                .requires(ModItems.BEEF_PATTY.get())
                .requires(HHModTags.CHEESE_SLICES)
                .requires(ModItems.TOMATO.get())
                .requires(ModItems.CABBAGE_LEAF.get())
                .unlockedBy("has_tortilla", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.TORTILLA.get()))
                .save(output);
    }
}