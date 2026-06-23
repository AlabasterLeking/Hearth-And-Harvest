package alabaster.hearthandharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MulchBlock extends Block {
    public MulchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        Block aboveBlock = aboveState.getBlock();

        if (aboveBlock instanceof SaplingBlock) {
            aboveState.randomTick(level, above, random);
        } else if (aboveBlock instanceof BambooSaplingBlock || aboveBlock instanceof BambooStalkBlock) {
            BlockPos top = above;
            for (int i = 0; i < 16; i++) {
                if (!(level.getBlockState(top.above()).getBlock() instanceof BambooStalkBlock)) break;
                top = top.above();
            }
            level.getBlockState(top).randomTick(level, top, random);
        }
    }
}