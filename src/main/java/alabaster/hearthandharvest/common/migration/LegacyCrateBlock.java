package alabaster.hearthandharvest.common.migration;

import alabaster.hearthandharvest.common.block.CrateBlock;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.function.Supplier;

public class LegacyCrateBlock extends Block {
    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final Supplier<ItemStack> content;

    public LegacyCrateBlock(Supplier<ItemStack> content) {
        super(Block.Properties.of().randomTicks().noLootTable().noOcclusion().strength(2.0F, 3.0F));
        this.content = content;
        registerDefaultState(defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        convert(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        convert(state, level, pos);
    }

    private void convert(BlockState state, ServerLevel level, BlockPos pos) {
        SlabType type = state.getValue(TYPE);
        BlockState crate = HHModBlocks.CRATE.get().defaultBlockState()
                .setValue(CrateBlock.TYPE, type)
                .setValue(CrateBlock.FACING, Direction.NORTH);
        crate = Block.updateFromNeighbourShapes(crate, level, pos);
        level.setBlock(pos, crate, Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof CrateBlockEntity be) {
            int count = type == SlabType.DOUBLE ? CrateBlockEntity.TOTAL_SLOTS : CrateBlockEntity.SLOTS_PER_HALF;
            be.loadItemsFromTag(LegacyCrates.contentsTag(content, count));
            be.setChanged();
            level.sendBlockUpdated(pos, crate, crate, Block.UPDATE_ALL);
        }
    }
}
