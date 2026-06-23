package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import alabaster.hearthandharvest.common.tag.HHCompatibilityTags;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class HHBlockTags extends BlockTagsProvider {
    public HHBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HearthAndHarvest.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerMinecraftTags();
        this.registerCommonTags();
        this.registerCompatTags();
        this.registerFDTags();
        this.registerBlockMineables();
    }

    protected void registerModTags() {

        tag(HHModTags.CROW_EDIBLE_CROPS).add(
                Blocks.WHEAT,
                Blocks.PUMPKIN_STEM,
                Blocks.MELON_STEM,
                Blocks.SWEET_BERRY_BUSH,
                Blocks.CAVE_VINES_PLANT,
                ModBlocks.CABBAGE_CROP.get(),
                ModBlocks.TOMATO_CROP.get(),
                ModBlocks.BUDDING_TOMATO_CROP.get(),
                HHModBlocks.BLUEBERRY_BUSH.get(),
                HHModBlocks.RASPBERRY_BUSH.get(),
                HHModBlocks.GRAPE_TRELLIS.get(),
                HHModBlocks.PEANUT_CROP.get(),
                HHModBlocks.CORN_STALK.get()
        );

        tag(HHModTags.REPELS_CROWS).add(
                Blocks.CARVED_PUMPKIN,
                Blocks.JACK_O_LANTERN,
                HHModBlocks.SCARECROW.get()
        );

        tag(HHModTags.SALT_BLOCKS).add(
                HHModBlocks.SALT_BLOCK.get(),
                HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(),
                HHModBlocks.WELL_LICKED_SALT_BLOCK.get(),
                HHModBlocks.HEAVILY_LICKED_SALT_BLOCK.get()
        );

        tag(HHModTags.RIGHT_CLICK_HARVESTABLE)
                .add(ModBlocks.TOMATO_CROP.get())
                .add(ModBlocks.TOMATO_CROP_ON_ROPE.get());
    }

    protected void registerMinecraftTags() {
        tag(BlockTags.CROPS).add(
                HHModBlocks.GRAPE_TRELLIS.get(),
                HHModBlocks.COTTON_CROP.get(),
                HHModBlocks.PEANUT_CROP.get(),
                HHModBlocks.CORN_STALK.get()
        );

        tag(ModTags.Blocks.STRAW_BLOCKS).add(
                HHModBlocks.STRAW_RUG.get()
        );

        tag(ModTags.Blocks.CAMPFIRE_SIGNAL_SMOKE).add(
                HHModBlocks.STICK_BRUSH.get()
        );

        tag(BlockTags.MAINTAINS_FARMLAND).add(
                HHModBlocks.COTTON_CROP.get(),
                HHModBlocks.PEANUT_CROP.get(),
                HHModBlocks.CORN_STALK.get(),
                HHModBlocks.TRELLIS.get(),
                HHModBlocks.GRAPE_TRELLIS.get()
        );

        tag(BlockTags.CLIMBABLE).add(
                HHModBlocks.TRELLIS.get(),
                HHModBlocks.GRAPE_TRELLIS.get()
        );

        tag(BlockTags.FLOWERS).add(
                HHModBlocks.YELLOW_MUM.get(),
                HHModBlocks.ORANGE_MUM.get(),
                HHModBlocks.RED_MUM.get(),
                HHModBlocks.BLUE_MUM.get(),
                HHModBlocks.LIGHT_BLUE_MUM.get(),
                HHModBlocks.PURPLE_MUM.get(),
                HHModBlocks.PINK_MUM.get(),
                HHModBlocks.WHITE_MUM.get()
        );

        tag(BlockTags.SMALL_FLOWERS).add(
                HHModBlocks.WILD_COTTON.get(),
                HHModBlocks.WILD_PEANUTS.get(),
                HHModBlocks.WILD_GREEN_GRAPES.get(),
                HHModBlocks.WILD_RED_GRAPES.get(),
                HHModBlocks.YELLOW_MUM.get(),
                HHModBlocks.ORANGE_MUM.get(),
                HHModBlocks.RED_MUM.get(),
                HHModBlocks.BLUE_MUM.get(),
                HHModBlocks.LIGHT_BLUE_MUM.get(),
                HHModBlocks.PURPLE_MUM.get(),
                HHModBlocks.PINK_MUM.get(),
                HHModBlocks.WHITE_MUM.get()
        );

        tag(BlockTags.FLOWER_POTS).add(
                HHModBlocks.POTTED_YELLOW_MUM.get(),
                HHModBlocks.POTTED_ORANGE_MUM.get(),
                HHModBlocks.POTTED_RED_MUM.get(),
                HHModBlocks.POTTED_BLUE_MUM.get(),
                HHModBlocks.POTTED_LIGHT_BLUE_MUM.get(),
                HHModBlocks.POTTED_PURPLE_MUM.get(),
                HHModBlocks.POTTED_PINK_MUM.get(),
                HHModBlocks.POTTED_WHITE_MUM.get()
        );

        tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(
                HHModBlocks.STOMPING_BASIN.get()
        );

        tag(BlockTags.SLABS).add(
                HHModBlocks.SALT_SLAB.get(),
                HHModBlocks.POLISHED_SALT_SLAB.get(),
                HHModBlocks.MANURE_BRICK_SLAB.get(),
                HHModBlocks.POLISHED_MANURE_SLAB.get()
        );

        tag(BlockTags.STAIRS).add(
                HHModBlocks.SALT_STAIRS.get(),
                HHModBlocks.POLISHED_SALT_STAIRS.get(),
                HHModBlocks.MANURE_BRICK_STAIRS.get(),
                HHModBlocks.POLISHED_MANURE_STAIRS.get()
        );

        tag(BlockTags.WALLS).add(
                HHModBlocks.SALT_WALL.get(),
                HHModBlocks.POLISHED_SALT_WALL.get(),
                HHModBlocks.MANURE_BRICK_WALL.get(),
                HHModBlocks.POLISHED_MANURE_WALL.get()
        );

        tag(BlockTags.BAMBOO_PLANTABLE_ON).add(
                HHModBlocks.MULCH.get()
        );

    }

    protected void registerCommonTags() {
        tag(HHCommonTags.STORAGE_BLOCKS_APPLE)
                .add(HHModBlocks.APPLE_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GOLDEN_APPLE)
                .add(HHModBlocks.GOLDEN_APPLE_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GOLDEN_CARROT)
                .add(HHModBlocks.GOLDEN_CARROT_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GLISTERING_MELON)
                .add(HHModBlocks.GLISTERING_MELON_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_POISONOUS_POTATO)
                .add(HHModBlocks.POISONOUS_POTATO_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_ROTTEN_TOMATO)
                .add(HHModBlocks.ROTTEN_TOMATO_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GLOW_BERRY)
                .add(HHModBlocks.GLOW_BERRY_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_SWEET_BERRY)
                .add(HHModBlocks.SWEET_BERRY_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_SUGAR)
                .add(HHModBlocks.SUGAR_BAG.get());
        tag(HHCommonTags.STORAGE_BLOCKS_COCOA_BEAN)
                .add(HHModBlocks.COCOA_BEAN_BAG.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GUNPOWDER)
                .add(HHModBlocks.GUNPOWDER_BAG.get());
        tag(HHCommonTags.STORAGE_BLOCKS_FLOUR)
                .add(HHModBlocks.FLOUR_BAG.get());
        tag(HHCommonTags.STORAGE_BLOCKS_MANURE)
                .add(HHModBlocks.MANURE_BAG.get());
        tag(HHCommonTags.STORAGE_BLOCKS_STRING)
                .add(HHModBlocks.SPOOL.get());
        tag(HHCommonTags.STORAGE_BLOCKS_BROWN_MUSHROOM)
                .add(HHModBlocks.BROWN_MUSHROOM_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_RED_MUSHROOM)
                .add(HHModBlocks.RED_MUSHROOM_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_WARPED_FUNGUS)
                .add(HHModBlocks.WARPED_FUNGUS_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_CRIMSON_FUNGUS)
                .add(HHModBlocks.CRIMSON_FUNGUS_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_BLUEBERRY)
                .add(HHModBlocks.BLUEBERRY_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_RASPBERRY)
                .add(HHModBlocks.RASPBERRY_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GRAPE)
                .add(HHModBlocks.GREEN_GRAPE_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_GRAPE)
                .add(HHModBlocks.RED_GRAPE_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_CHERRY)
                .add(HHModBlocks.CHERRY_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_PEANUT)
                .add(HHModBlocks.PEANUT_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_COTTON)
                .add(HHModBlocks.COTTON_BALE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_CORN)
                .add(HHModBlocks.CORN_CRATE.get());
        tag(HHCommonTags.STORAGE_BLOCKS_CHARCOAL)
                .add(HHModBlocks.CHARCOAL_BLOCK.get());
        tag(HHCommonTags.STORAGE_BLOCKS_STICK)
                .add(HHModBlocks.STICK_BRUSH.get());
        tag(HHCommonTags.STORAGE_BLOCKS_SUGAR_CANE)
                .add(HHModBlocks.SUGAR_CANE_BUNDLE.get());

        tag(Tags.Blocks.STORAGE_BLOCKS).add(
                HHModBlocks.RASPBERRY_CRATE.get(),
                HHModBlocks.BLUEBERRY_CRATE.get(),
                HHModBlocks.RED_GRAPE_CRATE.get(),
                HHModBlocks.GREEN_GRAPE_CRATE.get(),
                HHModBlocks.CHERRY_CRATE.get(),
                HHModBlocks.PEANUT_CRATE.get(),
                HHModBlocks.CORN_CRATE.get(),
                HHModBlocks.APPLE_CRATE.get(),
                HHModBlocks.GOLDEN_APPLE_CRATE.get(),
                HHModBlocks.GOLDEN_CARROT_CRATE.get(),
                HHModBlocks.GLISTERING_MELON_CRATE.get(),
                HHModBlocks.POISONOUS_POTATO_CRATE.get(),
                HHModBlocks.ROTTEN_TOMATO_CRATE.get(),
                HHModBlocks.GLOW_BERRY_CRATE.get(),
                HHModBlocks.SWEET_BERRY_CRATE.get(),
                HHModBlocks.BROWN_MUSHROOM_CRATE.get(),
                HHModBlocks.RED_MUSHROOM_CRATE.get(),
                HHModBlocks.WARPED_FUNGUS_CRATE.get(),
                HHModBlocks.CRIMSON_FUNGUS_CRATE.get(),
                HHModBlocks.GUNPOWDER_BAG.get(),
                HHModBlocks.SALT_BAG.get(),
                HHModBlocks.SUGAR_BAG.get(),
                HHModBlocks.COCOA_BEAN_BAG.get(),
                HHModBlocks.FLOUR_BAG.get(),
                HHModBlocks.MANURE_BAG.get(),
                HHModBlocks.COTTON_BALE.get(),
                HHModBlocks.SPOOL.get(),
                HHModBlocks.ROPE_COIL.get(),
                HHModBlocks.CORN_HUSK_BUNDLE.get(),
                HHModBlocks.CORN_KERNEL_BAG.get(),
                HHModBlocks.CHARCOAL_BLOCK.get(),
                HHModBlocks.STICK_BRUSH.get()
        );
    }

    protected void registerFDTags() {
        tag(ModTags.Blocks.WILD_CROPS).add(
                HHModBlocks.WILD_RED_GRAPES.get(),
                HHModBlocks.WILD_GREEN_GRAPES.get(),
                HHModBlocks.WILD_COTTON.get(),
                HHModBlocks.WILD_PEANUTS.get()
        );

        tag(ModTags.Blocks.COMPOST_ACTIVATORS).add(
                HHModBlocks.MULCH.get()
        );
    }

    protected void registerCompatTags() {
        tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS_BLOCK).add(
                HHModBlocks.BLUEBERRY_BUSH.get(),
                HHModBlocks.RASPBERRY_BUSH.get(),
                HHModBlocks.GRAPE_TRELLIS.get(),
                HHModBlocks.PEANUT_CROP.get()
        );

        tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS_BLOCK).add(
                HHModBlocks.BLUEBERRY_BUSH.get(),
                HHModBlocks.RASPBERRY_BUSH.get(),
                HHModBlocks.GRAPE_TRELLIS.get(),
                HHModBlocks.COTTON_CROP.get(),
                HHModBlocks.PEANUT_CROP.get(),
                HHModBlocks.CORN_STALK.get()
        );

        tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS_BLOCK).add(
                HHModBlocks.GRAPE_TRELLIS.get(),
                HHModBlocks.COTTON_CROP.get(),
                HHModBlocks.CORN_STALK.get()
        );

        tag(HHCompatibilityTags.SERENE_SEASONS_UNBREAKABLE_INFERTILE_CROPS).add(
                HHModBlocks.BLUEBERRY_BUSH.get(),
                HHModBlocks.RASPBERRY_BUSH.get(),
                HHModBlocks.CORN_STALK.get()
        );
    }

    protected void registerBlockMineables() {
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                        HHModBlocks.TREE_TAPPER.get(),
                        HHModBlocks.CASK.get(),
                        HHModBlocks.STOMPING_BASIN.get(),
                        HHModBlocks.CRATE.get(),
                        HHModBlocks.SPRINKLER.get(),
                        HHModBlocks.TRELLIS.get(),
                        HHModBlocks.GRAPE_TRELLIS.get(),
                        HHModBlocks.OAK_HALF_CABINET.get(),
                        HHModBlocks.BIRCH_HALF_CABINET.get(),
                        HHModBlocks.SPRUCE_HALF_CABINET.get(),
                        HHModBlocks.JUNGLE_HALF_CABINET.get(),
                        HHModBlocks.ACACIA_HALF_CABINET.get(),
                        HHModBlocks.DARK_OAK_HALF_CABINET.get(),
                        HHModBlocks.MANGROVE_HALF_CABINET.get(),
                        HHModBlocks.CHERRY_HALF_CABINET.get(),
                        HHModBlocks.BAMBOO_HALF_CABINET.get(),
                        HHModBlocks.CRIMSON_HALF_CABINET.get(),
                        HHModBlocks.WARPED_HALF_CABINET.get(),
                        HHModBlocks.BIRCH_BOTTLE_RACK.get(),
                        HHModBlocks.SPRUCE_BOTTLE_RACK.get(),
                        HHModBlocks.JUNGLE_BOTTLE_RACK.get(),
                        HHModBlocks.ACACIA_BOTTLE_RACK.get(),
                        HHModBlocks.DARK_OAK_BOTTLE_RACK.get(),
                        HHModBlocks.MANGROVE_BOTTLE_RACK.get(),
                        HHModBlocks.CHERRY_BOTTLE_RACK.get(),
                        HHModBlocks.BAMBOO_BOTTLE_RACK.get(),
                        HHModBlocks.CRIMSON_BOTTLE_RACK.get(),
                        HHModBlocks.WARPED_BOTTLE_RACK.get(),
                        HHModBlocks.RASPBERRY_CRATE.get(),
                        HHModBlocks.BLUEBERRY_CRATE.get(),
                        HHModBlocks.RED_GRAPE_CRATE.get(),
                        HHModBlocks.GREEN_GRAPE_CRATE.get(),
                        HHModBlocks.CHERRY_CRATE.get(),
                        HHModBlocks.PEANUT_CRATE.get(),
                        HHModBlocks.CORN_CRATE.get(),
                        HHModBlocks.APPLE_CRATE.get(),
                        HHModBlocks.GOLDEN_APPLE_CRATE.get(),
                        HHModBlocks.GOLDEN_CARROT_CRATE.get(),
                        HHModBlocks.GLISTERING_MELON_CRATE.get(),
                        HHModBlocks.POISONOUS_POTATO_CRATE.get(),
                        HHModBlocks.ROTTEN_TOMATO_CRATE.get(),
                        HHModBlocks.GLOW_BERRY_CRATE.get(),
                        HHModBlocks.SWEET_BERRY_CRATE.get(),
                        HHModBlocks.BROWN_MUSHROOM_CRATE.get(),
                        HHModBlocks.RED_MUSHROOM_CRATE.get(),
                        HHModBlocks.WARPED_FUNGUS_CRATE.get(),
                        HHModBlocks.CRIMSON_FUNGUS_CRATE.get(),
                        HHModBlocks.CORN_STALK.get(),
                        HHModBlocks.STICK_BRUSH.get())
                .addOptional(ResourceLocation.fromNamespaceAndPath("crabbersdelight", "palm_half_cabinet"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("crabbersdelight", "palm_bottle_rack")
                );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                HHModBlocks.JUG.get(),
                HHModBlocks.SAP_CAULDRON.get(),
                HHModBlocks.COUNTER.get(),
                HHModBlocks.DRAWER.get(),
                HHModBlocks.BASIN.get(),
                HHModBlocks.TROUGH.get(),
                HHModBlocks.CHARCOAL_BLOCK.get(),
                HHModBlocks.SALT_BLOCK.get(),
                HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(),
                HHModBlocks.WELL_LICKED_SALT_BLOCK.get(),
                HHModBlocks.HEAVILY_LICKED_SALT_BLOCK.get(),
                HHModBlocks.POLISHED_SALT_BLOCK.get(),
                HHModBlocks.SALT_STAIRS.get(),
                HHModBlocks.POLISHED_SALT_STAIRS.get(),
                HHModBlocks.SALT_SLAB.get(),
                HHModBlocks.POLISHED_SALT_SLAB.get(),
                HHModBlocks.SALT_WALL.get(),
                HHModBlocks.POLISHED_SALT_WALL.get(),
                HHModBlocks.SALT_DRIP.get(),
                HHModBlocks.MANURE_BRICKS_BLOCK.get(),
                HHModBlocks.POLISHED_MANURE.get(),
                HHModBlocks.MANURE_BRICK_STAIRS.get(),
                HHModBlocks.POLISHED_MANURE_STAIRS.get(),
                HHModBlocks.MANURE_BRICK_SLAB.get(),
                HHModBlocks.POLISHED_MANURE_SLAB.get(),
                HHModBlocks.MANURE_BRICK_WALL.get(),
                HHModBlocks.POLISHED_MANURE_WALL.get()
        );

        tag(BlockTags.MINEABLE_WITH_HOE).add(
                HHModBlocks.NEST.get(),
                HHModBlocks.CORN_HUSK_BUNDLE.get(),
                HHModBlocks.COTTON_BALE.get(),
                HHModBlocks.SUGAR_CANE_BUNDLE.get()
        );

        tag(ModTags.Blocks.MINEABLE_WITH_KNIFE).add(
                HHModBlocks.HAY_RUG.get(),
                HHModBlocks.STRAW_RUG.get(),
                HHModBlocks.SALT_BAG.get(),
                HHModBlocks.SUGAR_BAG.get(),
                HHModBlocks.COCOA_BEAN_BAG.get(),
                HHModBlocks.CORN_KERNEL_BAG.get(),
                HHModBlocks.FLOUR_BAG.get(),
                HHModBlocks.GUNPOWDER_BAG.get(),
                HHModBlocks.MANURE_BAG.get()
        );

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                HHModBlocks.MULCH.get(),
                HHModBlocks.MANURE_BLOCK.get()
        );

        tag(HHModTags.MINEABLE_WITH_PITCHFORK)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.LEAVES)
                .add(Blocks.HAY_BLOCK,
                ModBlocks.STRAW_BALE.get(),
                HHModBlocks.HAY_RUG.get(),
                HHModBlocks.STRAW_RUG.get(),
                HHModBlocks.MULCH.get(),
                HHModBlocks.MANURE_BLOCK.get(),
                HHModBlocks.STICK_BRUSH.get(),
                HHModBlocks.COTTON_BALE.get(),
                HHModBlocks.SUGAR_CANE_BUNDLE.get()
        );
    }
}