package alabaster.hearthandharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;

public class PizzaBlock extends PieBlock {
    private static final VoxelShape NORTH_WEST = Block.box(1.0D, 0.0D, 1.0D, 8.0D, 2.0D, 8.0D);
    private static final VoxelShape NORTH_EAST = Block.box(8.0D, 0.0D, 1.0D, 15.0D, 2.0D, 8.0D);
    private static final VoxelShape SOUTH_WEST = Block.box(1.0D, 0.0D, 8.0D, 8.0D, 2.0D, 15.0D);
    private static final VoxelShape SOUTH_EAST = Block.box(8.0D, 0.0D, 8.0D, 15.0D, 2.0D, 15.0D);

    private static final VoxelShape[][] SHAPES = buildShapes();

    public PizzaBlock(Properties properties, Supplier<Item> slice) {
        super(properties, slice);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int bites = state.getValue(BITES);
        Direction facing = state.getValue(FACING);
        return SHAPES[Math.min(bites, SHAPES.length - 1)][facing.get2DDataValue()];
    }

    private static VoxelShape[][] buildShapes() {
        VoxelShape[] base = {
                Shapes.or(NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST),
                Shapes.or(NORTH_EAST, SOUTH_WEST, SOUTH_EAST),
                Shapes.or(SOUTH_WEST, SOUTH_EAST),
                SOUTH_EAST
        };

        VoxelShape[][] shapes = new VoxelShape[base.length][4];
        for (int bites = 0; bites < base.length; bites++) {
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                shapes[bites][facing.get2DDataValue()] = rotate(base[bites], (int) facing.toYRot() % 360);
            }
        }
        return shapes;
    }

    private static VoxelShape rotate(VoxelShape shape, int degrees) {
        VoxelShape rotated = shape;
        for (int turns = (degrees / 90) % 4; turns > 0; turns--) {
            VoxelShape previous = rotated;
            VoxelShape[] result = {Shapes.empty()};
            previous.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    result[0] = Shapes.or(result[0], Shapes.box(1.0D - maxZ, minY, minX, 1.0D - minZ, maxY, maxX)));
            rotated = result[0];
        }
        return rotated;
    }
}