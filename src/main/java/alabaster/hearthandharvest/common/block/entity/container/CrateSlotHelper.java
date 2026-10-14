package alabaster.hearthandharvest.common.block.entity.container;

import alabaster.hearthandharvest.common.block.CrateBlock;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CrateSlotHelper {

    private static final double BOTTOM_INNER_FLOOR = 4.0 / 16.0;
    private static final double TOP_INNER_FLOOR    = 12.0 / 16.0;

    public static int getSlotFromHit(BlockState state, BlockHitResult hit) {
        Vec3 local = hit.getLocation().subtract(
                hit.getBlockPos().getX(),
                hit.getBlockPos().getY(),
                hit.getBlockPos().getZ()
        );

        local = toModelSpace(local, state.getValue(CrateBlock.FACING));
        SlabType type = state.getValue(CrateBlock.TYPE);
        int baseSlot;

        switch (type) {
            case BOTTOM -> {
                if (local.y < BOTTOM_INNER_FLOOR) return -1;
                baseSlot = 0;
            }
            case TOP -> {
                if (local.y < TOP_INNER_FLOOR) return -1;
                baseSlot = 0;
            }
            case DOUBLE -> {
                if (local.y >= TOP_INNER_FLOOR) {
                    baseSlot = CrateBlockEntity.SLOTS_PER_HALF;
                } else if (local.y >= BOTTOM_INNER_FLOOR && local.y < 0.5) {
                    baseSlot = 0;
                } else {
                    return -1;
                }
            }
            default -> { return -1; }
        }

        int col = getSlotCoord(local.x);
        int row = getSlotCoord(local.z);
        if (col < 0 || row < 0) return -1;

        return baseSlot + row * 3 + col;
    }

    private static Vec3 toModelSpace(Vec3 local, Direction facing) {
        double x = local.x;
        double z = local.z;
        return switch (facing) {
            case WEST -> new Vec3(1.0 - z, local.y, x);
            case SOUTH -> new Vec3(1.0 - x, local.y, 1.0 - z);
            case EAST -> new Vec3(z, local.y, 1.0 - x);
            default -> local;
        };
    }

    private static int getSlotCoord(double coord) {
        int pixel  = (int) (coord * 16);
        if (pixel < 1 || pixel > 14) return -1;
        int index  = (pixel - 1) / 5;
        int offset = (pixel - 1) % 5;
        return offset < 4 ? index : -1;
    }
}