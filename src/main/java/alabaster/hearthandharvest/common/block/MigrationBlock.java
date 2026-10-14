package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.trellis.CropTrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisPlant;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class MigrationBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    private final TrellisPlant targetPlant;

    public MigrationBlock(TrellisPlant targetPlant) {
        super(Block.Properties.of().randomTicks().noLootTable().noOcclusion().noCollission());
        this.targetPlant = targetPlant;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
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
        level.setBlock(pos, HHModBlocks.CROP_TRELLIS.get().defaultBlockState()
                .setValue(TrellisBlock.MIDDLE_EW, true)
                .setValue(TrellisBlock.MIDDLE_NS, true)
                .setValue(CropTrellisBlock.PLANT, targetPlant)
                .setValue(CropTrellisBlock.AGE, state.getValue(AGE)), Block.UPDATE_ALL);
    }
}
