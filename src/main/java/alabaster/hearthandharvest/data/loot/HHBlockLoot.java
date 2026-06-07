package alabaster.hearthandharvest.data.loot;

import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class HHBlockLoot extends BlockLootSubProvider {
    private final Set<Block> generatedLootTables = new HashSet<>();

    public HHBlockLoot(HolderLookup.Provider holder) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), holder);
    }

    @Override
    protected void generate() {
        dropSelf(HHModBlocks.TREE_TAPPER.get());
        dropSelf(HHModBlocks.CASK.get());
        dropSelf(HHModBlocks.STOMPING_BASIN.get());
        dropSelf(HHModBlocks.JUG.get());
        dropOther(HHModBlocks.SAP_CAULDRON.get(), Items.CAULDRON);
        dropSelf(HHModBlocks.COUNTER.get());
        dropNamedContainer(HHModBlocks.DRAWER.get());
        dropSelf(HHModBlocks.BASIN.get());
        dropSelf(HHModBlocks.NEST.get());
        dropSelf(HHModBlocks.HAY_RUG.get());
        dropSelf(HHModBlocks.STRAW_RUG.get());
        dropSelf(HHModBlocks.SCARECROW.get());
        dropSelf(HHModBlocks.TRELLIS.get());
        dropNamedContainer(HHModBlocks.OAK_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.SPRUCE_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.BIRCH_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.JUNGLE_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.ACACIA_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.DARK_OAK_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.MANGROVE_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.BAMBOO_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.CHERRY_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.CRIMSON_HALF_CABINET.get());
        dropNamedContainer(HHModBlocks.WARPED_HALF_CABINET.get());
        dropSelf(HHModBlocks.OAK_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.SPRUCE_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.BIRCH_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.JUNGLE_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.ACACIA_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.DARK_OAK_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.MANGROVE_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.BAMBOO_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.CHERRY_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.CRIMSON_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.WARPED_BOTTLE_RACK.get());
        dropSelf(HHModBlocks.CRATE.get());
        dropSelf(HHModBlocks.RASPBERRY_CRATE.get());
        dropSelf(HHModBlocks.BLUEBERRY_CRATE.get());
        dropSelf(HHModBlocks.RED_GRAPE_CRATE.get());
        dropSelf(HHModBlocks.GREEN_GRAPE_CRATE.get());
        dropSelf(HHModBlocks.CHERRY_CRATE.get());
        dropSelf(HHModBlocks.PEANUT_CRATE.get());
        dropSelf(HHModBlocks.CORN_CRATE.get());
        dropSelf(HHModBlocks.APPLE_CRATE.get());
        dropSelf(HHModBlocks.GOLDEN_APPLE_CRATE.get());
        dropSelf(HHModBlocks.GOLDEN_CARROT_CRATE.get());
        dropSelf(HHModBlocks.POISONOUS_POTATO_CRATE.get());
        dropSelf(HHModBlocks.ROTTEN_TOMATO_CRATE.get());
        dropSelf(HHModBlocks.GLOW_BERRY_CRATE.get());
        dropSelf(HHModBlocks.SWEET_BERRY_CRATE.get());
        dropSelf(HHModBlocks.SALT_BAG.get());
        dropSelf(HHModBlocks.SUGAR_BAG.get());
        dropSelf(HHModBlocks.COCOA_BEAN_BAG.get());
        dropSelf(HHModBlocks.GUNPOWDER_BAG.get());
        dropSelf(HHModBlocks.CORN_KERNEL_BAG.get());
        dropSelf(HHModBlocks.CORN_HUSK_BUNDLE.get());
        dropSelf(HHModBlocks.COTTON_BALE.get());
        dropSelf(HHModBlocks.SPOOL.get());
        dropSelf(HHModBlocks.ROPE_COIL.get());
        dropSelf(HHModBlocks.CHARCOAL_BLOCK.get());
        dropSelf(HHModBlocks.STICK_BRUSH.get());
        dropSelf(HHModBlocks.YELLOW_MUM.get());
        dropSelf(HHModBlocks.ORANGE_MUM.get());
        dropSelf(HHModBlocks.RED_MUM.get());
        dropSelf(HHModBlocks.BLUE_MUM.get());
        dropSelf(HHModBlocks.LIGHT_BLUE_MUM.get());
        dropSelf(HHModBlocks.PURPLE_MUM.get());
        dropSelf(HHModBlocks.PINK_MUM.get());
        dropSelf(HHModBlocks.WHITE_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_YELLOW_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_ORANGE_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_RED_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_BLUE_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_LIGHT_BLUE_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_PURPLE_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_PINK_MUM.get());
        dropPottedContents(HHModBlocks.POTTED_WHITE_MUM.get());
        dropSelf(HHModBlocks.POLISHED_SALT_BLOCK.get());
        dropSelf(HHModBlocks.SALT_STAIRS.get());
        dropSelf(HHModBlocks.POLISHED_SALT_STAIRS.get());
        dropSelf(HHModBlocks.SALT_WALL.get());
        dropSelf(HHModBlocks.POLISHED_SALT_WALL.get());
        dropSelf(HHModBlocks.SALT_DRIP.get());
        dropSelf(HHModBlocks.SALT_LAMP.get());
        dropSelf(HHModBlocks.MANURE_BLOCK.get());
        dropSelf(HHModBlocks.MANURE_BRICKS_BLOCK.get());
        dropSelf(HHModBlocks.POLISHED_MANURE.get());
        dropSelf(HHModBlocks.MANURE_BRICK_STAIRS.get());
        dropSelf(HHModBlocks.POLISHED_MANURE_STAIRS.get());
        dropSelf(HHModBlocks.MANURE_BRICK_WALL.get());
        dropSelf(HHModBlocks.POLISHED_MANURE_WALL.get());
        dropSelf(HHModBlocks.MULCH.get());
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    protected void dropNamedContainer(Block block) {
        add(block, this::createNameableBlockEntityTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}
