package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Consumer;

public class CraftingRecipes
{
    public static final Ingredient WATER_BOTTLE = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));

    public static void register(Consumer<FinishedRecipe> consumer) {
        recipesBlocks(consumer);
        recipesTools(consumer);
        recipesMaterials(consumer);
        recipesFoodstuffs(consumer);
        recipesFoodBlocks(consumer);
        recipesCraftedMeals(consumer);
    }

    private static void recipesBlocks(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HHModItems.TREE_TAPPER.get(), 1)
                .pattern(" I ")
                .pattern("SBS")
                .pattern(" S ")
                .define('B', Items.BUCKET)
                .define('I', Items.IRON_INGOT)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HHModItems.CASK.get(), 1)
                .pattern("SSS")
                .pattern("CCC")
                .pattern("SSS")
                .define('C', Items.COPPER_INGOT)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HHModItems.JAR.get(), 4)
                .pattern("G G")
                .pattern("G G")
                .pattern("GGG")
                .define('G', Items.GLASS_PANE)
                .unlockedBy("has_glass_pane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLASS_PANE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HHModItems.JUG.get(), 1)
                .pattern(" I ")
                .pattern("IBI")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BUCKET)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.COUNTER.get())
                .pattern("ii")
                .pattern("BB")
                .pattern("BB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.DRAWER.get())
                .pattern("iii")
                .pattern("BCB")
                .pattern("BBB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('C', ModTags.Items.CABINETS_WOODEN)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BASIN.get())
                .pattern("iii")
                .pattern("BCB")
                .pattern("BBB")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('C', Items.CAULDRON)
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.OAK_CABINET.get(), 1)
                .requires(HHModItems.OAK_HALF_CABINET.get())
                .requires(HHModItems.OAK_HALF_CABINET.get())
                .unlockedBy("has_oak_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.OAK_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "oak_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.SPRUCE_CABINET.get(), 1)
                .requires(HHModItems.SPRUCE_HALF_CABINET.get())
                .requires(HHModItems.SPRUCE_HALF_CABINET.get())
                .unlockedBy("has_spruce_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SPRUCE_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "spruce_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.BIRCH_CABINET.get(), 1)
                .requires(HHModItems.BIRCH_HALF_CABINET.get())
                .requires(HHModItems.BIRCH_HALF_CABINET.get())
                .unlockedBy("has_birch_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BIRCH_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "birch_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.JUNGLE_CABINET.get(), 1)
                .requires(HHModItems.JUNGLE_HALF_CABINET.get())
                .requires(HHModItems.JUNGLE_HALF_CABINET.get())
                .unlockedBy("has_jungle_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.JUNGLE_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "jungle_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.ACACIA_CABINET.get(), 1)
                .requires(HHModItems.ACACIA_HALF_CABINET.get())
                .requires(HHModItems.ACACIA_HALF_CABINET.get())
                .unlockedBy("has_acacia_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ACACIA_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "acacia_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.DARK_OAK_CABINET.get(), 1)
                .requires(HHModItems.DARK_OAK_HALF_CABINET.get())
                .requires(HHModItems.DARK_OAK_HALF_CABINET.get())
                .unlockedBy("has_dark_oak_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.DARK_OAK_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "dark_oak_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.MANGROVE_CABINET.get(), 1)
                .requires(HHModItems.MANGROVE_HALF_CABINET.get())
                .requires(HHModItems.MANGROVE_HALF_CABINET.get())
                .unlockedBy("has_mangrove_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MANGROVE_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "mangrove_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.CHERRY_CABINET.get(), 1)
                .requires(HHModItems.CHERRY_HALF_CABINET.get())
                .requires(HHModItems.CHERRY_HALF_CABINET.get())
                .unlockedBy("has_cherry_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "cherry_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.BAMBOO_CABINET.get(), 1)
                .requires(HHModItems.BAMBOO_HALF_CABINET.get())
                .requires(HHModItems.BAMBOO_HALF_CABINET.get())
                .unlockedBy("has_bamboo_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BAMBOO_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "bamboo_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.CRIMSON_CABINET.get(), 1)
                .requires(HHModItems.CRIMSON_HALF_CABINET.get())
                .requires(HHModItems.CRIMSON_HALF_CABINET.get())
                .unlockedBy("has_crimson_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CRIMSON_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "crimson_cabinet_from_halves"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.WARPED_CABINET.get(), 1)
                .requires(HHModItems.WARPED_HALF_CABINET.get())
                .requires(HHModItems.WARPED_HALF_CABINET.get())
                .unlockedBy("has_warped_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.WARPED_HALF_CABINET.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "warped_cabinet_from_halves"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.OAK_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.OAK_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BIRCH_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.BIRCH_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.SPRUCE_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.SPRUCE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.JUNGLE_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.JUNGLE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.ACACIA_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.ACACIA_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.DARK_OAK_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.DARK_OAK_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.MANGROVE_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.MANGROVE_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.CHERRY_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.CHERRY_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.BAMBOO_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.BAMBOO_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.CRIMSON_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.CRIMSON_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HHModBlocks.WARPED_WINE_RACK.get())
                .pattern("HSH")
                .pattern("SSS")
                .pattern("HSH")
                .define('S', Items.STICK)
                .define('H', Blocks.WARPED_SLAB)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

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
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.WATERING_CAN.get(), 1)
                .pattern("C  ")
                .pattern("CBC")
                .pattern("CC ")
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BUCKET)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.BLUEBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.BLUEBERRIES.get())
                .unlockedBy("has_blueberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CHERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.CHERRY.get())
                .unlockedBy("has_cherry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RASPBERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.RASPBERRY.get())
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RED_GRAPE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.RED_GRAPES.get())
                .unlockedBy("has_red_grape", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GREEN_GRAPE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.GREEN_GRAPES.get())
                .unlockedBy("has_green_grape", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GREEN_GRAPES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.PEANUT_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.PEANUT.get())
                .unlockedBy("has_peanut", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.APPLE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.APPLE)
                .unlockedBy("has_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GOLDEN_APPLE_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_APPLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GOLDEN_CARROT_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GOLDEN_CARROT)
                .unlockedBy("has_golden_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_CARROT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.POISONOUS_POTATO_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.POISONOUS_POTATO)
                .unlockedBy("has_poisonous_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POISONOUS_POTATO))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GLOW_BERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GLOW_BERRIES)
                .unlockedBy("has_glow_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLOW_BERRIES))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SWEET_BERRY_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.SWEET_BERRIES)
                .unlockedBy("has_sweet_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SWEET_BERRIES))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.COTTON_BALE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.COTTON.get())
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SPOOL.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STRING)
                .unlockedBy("has_string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.ROPE_COIL.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', vectorwing.farmersdelight.common.registry.ModItems.ROPE.get())
                .unlockedBy("has_rope", InventoryChangeTrigger.TriggerInstance.hasItems(vectorwing.farmersdelight.common.registry.ModItems.ROPE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SALT_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', HHModItems.SALT.get())
                .unlockedBy("has_salt", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.SUGAR_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.SUGAR)
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.COCOA_BEAN_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.COCOA_BEANS)
                .unlockedBy("has_cocoa_bean", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COCOA_BEANS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.GUNPOWDER_BAG.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.GUNPOWDER)
                .unlockedBy("has_gunpowder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.EGG_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.EGG)
                .unlockedBy("has_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EGG))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.TURTLE_EGG_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.TURTLE_EGG)
                .unlockedBy("has_turtle_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TURTLE_EGG))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.WATER_CRATE.get(), 1)
                .pattern("   ")
                .pattern(" # ")
                .pattern("   ")
                .define('#', WATER_BOTTLE)
                .unlockedBy("has_water_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.POTION))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.BROWN_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.BROWN_MUSHROOM)
                .unlockedBy("has_brown_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BROWN_MUSHROOM))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.RED_MUSHROOM_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.RED_MUSHROOM)
                .unlockedBy("has_red_mushroom", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RED_MUSHROOM))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.CRIMSON_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.CRIMSON_FUNGUS)
                .unlockedBy("has_crimson_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_FUNGUS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HHModItems.WARPED_FUNGUS_CRATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.WARPED_FUNGUS)
                .unlockedBy("has_warped_fungus", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WARPED_FUNGUS))
                .save(consumer);
    }

    private static void recipesTools(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.FLINT_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy("has_flint", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FLINT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.IRON_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.GOLDEN_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HHModItems.DIAMOND_CLEAVER.get(), 1)
                .pattern("  #")
                .pattern(" # ")
                .pattern("S  ")
                .define('#', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                .save(consumer);
    }

    private static void recipesMaterials(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RASPBERRY.get(), 9)
                .requires(HHModItems.RASPBERRY_CRATE.get())
                .unlockedBy("has_raspberry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BLUEBERRIES.get(), 9)
                .requires(HHModItems.BLUEBERRY_CRATE.get())
                .unlockedBy("has_blueberry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRY_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CHERRY.get(), 9)
                .requires(HHModItems.CHERRY_CRATE.get())
                .unlockedBy("has_cherry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RED_GRAPES.get(), 9)
                .requires(HHModItems.RED_GRAPE_CRATE.get())
                .unlockedBy("has_red_grape_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.GREEN_GRAPES.get(), 9)
                .requires(HHModItems.GREEN_GRAPE_CRATE.get())
                .unlockedBy("has_green_grape_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GREEN_GRAPE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SWEET_BERRY_WINE.get(), 9)
                .requires(HHModItems.SWEET_BERRY_WINE_CRATE.get())
                .unlockedBy("has_sweet_berry_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SWEET_BERRY_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.PEANUT.get(), 9)
                .requires(HHModItems.PEANUT_CRATE.get())
                .unlockedBy("has_peanut_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.APPLE, 9)
                .requires(HHModItems.APPLE_CRATE.get())
                .unlockedBy("has_apple_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.APPLE_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "apple_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLDEN_APPLE, 9)
                .requires(HHModItems.GOLDEN_APPLE_CRATE.get())
                .unlockedBy("has_golden_apple_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GOLDEN_APPLE_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "golden_apple_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLDEN_CARROT, 9)
                .requires(HHModItems.GOLDEN_CARROT_CRATE.get())
                .unlockedBy("has_golden_carrot_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GOLDEN_CARROT_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "golden_carrot_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.POISONOUS_POTATO, 9)
                .requires(HHModItems.POISONOUS_POTATO_CRATE.get())
                .unlockedBy("has_poisonous_potato_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.POISONOUS_POTATO_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "poisonous_potato_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GLOW_BERRIES, 9)
                .requires(HHModItems.GLOW_BERRY_CRATE.get())
                .unlockedBy("has_glow_berry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GLOW_BERRY_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "glow_berry_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SWEET_BERRIES, 9)
                .requires(HHModItems.SWEET_BERRY_CRATE.get())
                .unlockedBy("has_sweet_berry_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SWEET_BERRY_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sweet_berry_from_crate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HHModItems.COTTON.get(), 9)
                .requires(HHModItems.COTTON_BALE.get())
                .unlockedBy("has_cotton_bale", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON_BALE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 9)
                .requires(HHModItems.SPOOL.get())
                .unlockedBy("has_spool", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SPOOL.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "string_from_spool"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, vectorwing.farmersdelight.common.registry.ModItems.ROPE.get(), 9)
                .requires(HHModItems.ROPE_COIL.get())
                .unlockedBy("has_rope_coil", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROPE_COIL.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "rope_from_coil"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SALT.get(), 9)
                .requires(HHModItems.SALT_BAG.get())
                .unlockedBy("has_salt_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SALT_BAG.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SUGAR, 9)
                .requires(HHModItems.SUGAR_BAG.get())
                .unlockedBy("has_sugar_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SUGAR_BAG.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sugar_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.COCOA_BEANS, 9)
                .requires(HHModItems.COCOA_BEAN_BAG.get())
                .unlockedBy("has_cocoa_bean_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COCOA_BEAN_BAG.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "cocoa_bean_from_bag"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GUNPOWDER, 9)
                .requires(HHModItems.GUNPOWDER_BAG.get())
                .unlockedBy("has_gunpowder_bag", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GUNPOWDER_BAG.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "gunpowder_from_bag"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.EGG, 9)
                .requires(HHModItems.EGG_CRATE.get())
                .unlockedBy("has_egg_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.EGG_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "egg_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.TURTLE_EGG, 9)
                .requires(HHModItems.TURTLE_EGG_CRATE.get())
                .unlockedBy("has_turtle_egg_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.TURTLE_EGG_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "turtle_egg_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get(), 9)
                .requires(HHModItems.MILK_CRATE.get())
                .unlockedBy("has_milk_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MILK_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "milk_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.GOAT_MILK_BOTTLE.get(), 9)
                .requires(HHModItems.GOAT_MILK_CRATE.get())
                .unlockedBy("has_goat_milk_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GOAT_MILK_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BLUEBERRY_WINE.get(), 9)
                .requires(HHModItems.BLUEBERRY_WINE_CRATE.get())
                .unlockedBy("has_blueberry_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRY_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.CHERRY_WINE.get(), 9)
                .requires(HHModItems.CHERRY_WINE_CRATE.get())
                .unlockedBy("has_cherry_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHERRY_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RASPBERRY_WINE.get(), 9)
                .requires(HHModItems.RASPBERRY_WINE_CRATE.get())
                .unlockedBy("has_raspberry_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.RED_GRAPE_WINE.get(), 9)
                .requires(HHModItems.RED_GRAPE_WINE_CRATE.get())
                .unlockedBy("has_red_grape_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPE_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.GREEN_GRAPE_WINE.get(), 9)
                .requires(HHModItems.GREEN_GRAPE_WINE_CRATE.get())
                .unlockedBy("has_green_grape_wine_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GREEN_GRAPE_WINE_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.MEAD.get(), 9)
                .requires(HHModItems.MEAD_CRATE.get())
                .unlockedBy("has_mead_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.MEAD_CRATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.HONEY_BOTTLE, 9)
                .requires(HHModItems.HONEY_CRATE.get())
                .unlockedBy("has_honey_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.HONEY_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "honey_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SYRUP_BOTTLE.get(), 9)
                .requires(HHModItems.SYRUP_CRATE.get())
                .unlockedBy("has_syrup_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SYRUP_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "syrup_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.BROWN_MUSHROOM, 9)
                .requires(HHModItems.BROWN_MUSHROOM_CRATE.get())
                .unlockedBy("has_brown_mushroom_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BROWN_MUSHROOM_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "brown_mushroom_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RED_MUSHROOM, 9)
                .requires(HHModItems.RED_MUSHROOM_CRATE.get())
                .unlockedBy("has_red_mushroom_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_MUSHROOM_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "red_mushroom_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.CRIMSON_FUNGUS, 9)
                .requires(HHModItems.CRIMSON_FUNGUS_CRATE.get())
                .unlockedBy("has_crimson_fungus_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CRIMSON_FUNGUS_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "crimson_fungus_from_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.WARPED_FUNGUS, 9)
                .requires(HHModItems.WARPED_FUNGUS_CRATE.get())
                .unlockedBy("has_warped_fungus_crate", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.WARPED_FUNGUS_CRATE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "warped_fungus_from_crate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 2)
                .requires(HHModItems.COTTON.get())
                .requires(HHModItems.COTTON.get())
                .requires(HHModItems.COTTON.get())
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "string_from_cotton"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL, 1)
                .requires(HHModItems.CHARRED_MARSHMALLOW_STICK.get())
                .unlockedBy("has_charred_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.CHARRED_MARSHMALLOW_STICK.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "charcoal_from_charred_marshmallow"));
    }

    private static void recipesFoodstuffs(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.MARSHMALLOW_STICK.get(), 1)
                .pattern(" #")
                .pattern("S ")
                .define('#', Items.SUGAR)
                .define('S', Items.STICK)
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SMORE.get(), 1)
                .requires(HHModItems.ROASTED_MARSHMALLOW_STICK.get())
                .requires(HHModItems.CHOCOLATE_BAR.get())
                .requires(Items.COOKIE)
                .requires(Items.COOKIE)
                .unlockedBy("has_roasted_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROASTED_MARSHMALLOW_STICK.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "smore"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.COTTON_CANDY.get(), 1)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" S ")
                .define('#', Items.SUGAR)
                .define('S', Items.STICK)
                .define('C', HHModItems.COTTON.get())
                .unlockedBy("has_cotton", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COTTON.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_COOKIE.get(), 8)
                .pattern("   ")
                .pattern("WPW")
                .pattern("   ")
                .define('W', Items.WHEAT)
                .define('P', HHModItems.PEANUT_BUTTER.get())
                .unlockedBy("has_peanut_butter", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_MUFFIN.get(), 1)
                .pattern(" B ")
                .pattern("BSB")
                .pattern(" W ")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('B', HHModItems.BLUEBERRIES.get())
                .unlockedBy("has_blueberries", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.RASPBERRY_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHModItems.RASPBERRY.get())
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_raspberry", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHModItems.BLUEBERRIES.get())
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_blueberries", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRIES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.GRAPE_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHModItems.RED_GRAPES.get())
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_red_grapes", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RED_GRAPES.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_PIE.get(), 1)
                .pattern("WWW")
                .pattern("FFF")
                .pattern("SPS")
                .define('W', Items.WHEAT)
                .define('S', Items.SUGAR)
                .define('F', HHModItems.ROASTED_PEANUTS.get())
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_peanut_butters", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.ROASTED_PEANUTS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.RASPBERRY_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.RASPBERRY_PIE_SLICE.get())
                .unlockedBy("has_raspberry_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RASPBERRY_PIE_SLICE.get()))
                .group("raspberry_pie")
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "raspberry_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.BLUEBERRY_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.BLUEBERRY_PIE_SLICE.get())
                .unlockedBy("has_blueberry_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.BLUEBERRY_PIE_SLICE.get()))
                .group("blueberry_pie")
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "blueberry_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.GRAPE_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.GRAPE_PIE_SLICE.get())
                .unlockedBy("has_grape_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GRAPE_PIE_SLICE.get()))
                .group("grape_pie")
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "grape_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.PEANUT_BUTTER_PIE_SLICE.get())
                .unlockedBy("has_peanut_butter_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER_PIE_SLICE.get()))
                .group("peanut_butter_pie")
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "peanut_butter_pie_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CHICKEN_POT_PIE.get(), 1)
                .pattern(" C ")
                .pattern("TVO")
                .pattern("MPS")
                .define('T', Items.POTATO)
                .define('V', Items.CARROT)
                .define('S', HHModItems.SALT.get())
                .define('M', CommonTags.Items.MILK_BOTTLE)
                .define('C', vectorwing.farmersdelight.common.registry.ModItems.COOKED_CHICKEN_CUTS.get())
                .define('O', vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .define('P', vectorwing.farmersdelight.common.registry.ModItems.PIE_CRUST.get())
                .unlockedBy("has_chicken_cuts", InventoryChangeTrigger.TriggerInstance.hasItems(vectorwing.farmersdelight.common.registry.ModItems.COOKED_CHICKEN_CUTS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CHICKEN_POT_PIE.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', HHModItems.CHICKEN_POT_PIE_SLICE.get())
                .unlockedBy("has_grape_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.GRAPE_PIE_SLICE.get()))
                .group("grape_pie")
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "chicken_pot_pie_from_slices"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HHModItems.CARROT_CAKE.get())
                .pattern("CMC")
                .pattern("SES")
                .pattern("WWW")
                .define('C', Items.CARROT)
                .define('S', Items.SUGAR)
                .define('W', Items.SUGAR)
                .define('E', Items.EGG)
                .define('M', CommonTags.Items.MILK_BOTTLE)
                .unlockedBy("has_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CARROT))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.TRAIL_MIX.get())
                .requires(HHModItems.PEANUT.get())
                .requires(HHModItems.RAISINS.get())
                .requires(Items.COCOA_BEANS)
                .requires(CommonTags.Items.SEEDS)
                .unlockedBy("has_raisins", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.RAISINS.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "trail_mix"));

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
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "carrot_cake_from_slices"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.SKEWERED_SAUSAGE.get(), 1)
                .requires(HHModItems.COOKED_SAUSAGE.get())
                .requires(Items.STICK)
                .unlockedBy("has_cooked_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.COOKED_SAUSAGE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "skewered_sausage"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.PEANUT_BUTTER_AND_JELLY_SANDWICH.get(), 1)
                .requires(HHModTags.JAMS)
                .requires(HHModItems.PEANUT_BUTTER.get())
                .requires(Items.BREAD)
                .unlockedBy("has_peanut_butter", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.PEANUT_BUTTER.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "peanut_butter_and_jelly_sandwich"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HHModItems.BATTER.get(), 1)
                .requires(CommonTags.Items.MILK_BOTTLE)
                .requires(Items.WHEAT)
                .requires(Items.WHEAT)
                .requires(Items.BOWL)
                .unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "batter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SUGAR, 4)
                .requires(HHModItems.SYRUP_BOTTLE.get())
                .unlockedBy("has_syrup", InventoryChangeTrigger.TriggerInstance.hasItems(HHModItems.SYRUP_BOTTLE.get()))
                .save(consumer, new ResourceLocation(HearthAndHarvest.MODID, "sugar_from_syrup"));
    }

    private static void recipesFoodBlocks(Consumer<FinishedRecipe> consumer) {

    }

    private static void recipesCraftedMeals(Consumer<FinishedRecipe> consumer) {

    }
}