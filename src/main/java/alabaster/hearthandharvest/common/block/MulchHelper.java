package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public final class MulchHelper {
    private MulchHelper() {
    }

    public static boolean isNearMulch(LevelReader level, BlockPos pos) {
        for (BlockPos check : BlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 0, 2))) {
            if (level.getBlockState(check).is(HHModBlocks.MULCH.get())) return true;
        }
        return false;
    }
}
