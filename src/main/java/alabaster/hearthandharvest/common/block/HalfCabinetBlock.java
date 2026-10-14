package alabaster.hearthandharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.CabinetBlock;

public class HalfCabinetBlock extends CabinetBlock {
    public HalfCabinetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (direction == Direction.NORTH) {
            return Block.box(0.0D, 0.0D, 8.0, 16.0D, 16.0D, 16.0D);//
        }
        if (direction == Direction.EAST) {
            return Block.box(0.0D, 0.0D, 0.0, 8.0D, 16.0D, 16.0D);
        }
        if (direction == Direction.SOUTH) {
            return Block.box(0.0D, 0.0D, 0.0, 16.0D, 16.0D, 8.0D);//
        }
        if (direction == Direction.WEST) {
            return Block.box(8.0D, 0.0D, 0.0, 16.0D, 16.0D, 16.0D);//
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
