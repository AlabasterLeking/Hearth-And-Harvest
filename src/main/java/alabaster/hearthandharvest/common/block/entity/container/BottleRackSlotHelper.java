package alabaster.hearthandharvest.common.block.entity.container;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BottleRackSlotHelper {

    public static int getSlotFromHit(BlockState state, BlockHitResult hit) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Vec3 local = hit.getLocation().subtract(hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());

        local = switch (facing) {
            case NORTH -> local;
            case SOUTH -> new Vec3(1 - local.x, local.y, 1 - local.z);
            case EAST -> new Vec3(local.z, local.y, 1 - local.x);
            case WEST -> new Vec3(1 - local.z, local.y, local.x);
            default -> local;
        };

        int col = getSlotCoord(local.x);
        int row = getSlotCoord(1 - local.y); // y is top-down

        if (col < 0 || row < 0) return -1;

        // Flip left/right columns for all horizontal facings
        if (facing == Direction.NORTH || facing == Direction.SOUTH || facing == Direction.EAST || facing == Direction.WEST) {
            if (col == 0) col = 2;
            else if (col == 2) col = 0;
        }

        return row * 3 + col;
    }

    private static int getSlotCoord(double coord) {
        int pixel = (int) (coord * 16);
        if (pixel < 1 || pixel > 14) return -1;
        int index = (pixel - 1) / 5;
        int offset = (pixel - 1) % 5;
        return offset < 4 ? index : -1;
    }
}

