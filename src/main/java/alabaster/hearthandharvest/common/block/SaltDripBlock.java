package alabaster.hearthandharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class SaltDripBlock extends Block implements Fallable {

    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<SaltDripThickness> THICKNESS =
            EnumProperty.create("thickness", SaltDripThickness.class);

    private static final VoxelShape SMALL_DOWN = Block.box(5, 8, 5, 11, 16, 11);
    private static final VoxelShape MEDIUM_DOWN = Shapes.or(
            Block.box(4, 4, 4, 12, 16, 12),
            Block.box(5, 0, 5, 11, 4, 11));
    private static final VoxelShape LARGE_DOWN = Shapes.or(
            Block.box(3, 4, 3, 13, 16, 13),
            Block.box(4, 0, 4, 12, 4, 12));
    private static final VoxelShape POINT_DOWN = Block.box(6, 8, 6, 10, 16, 10);
    private static final VoxelShape MERGE_SHAPE = Block.box(6, 3, 6, 10, 13, 10);

    private static final VoxelShape SMALL_UP = Block.box(5, 0, 5, 11, 8, 11);
    private static final VoxelShape MEDIUM_UP = Shapes.or(
            Block.box(4, 0, 4, 12, 12, 12),
            Block.box(5, 12, 5, 11, 16, 11));
    private static final VoxelShape LARGE_UP = Shapes.or(
            Block.box(3, 0, 3, 13, 12, 13),
            Block.box(4, 12, 4, 12, 16, 12));
    private static final VoxelShape POINT_UP = Block.box(6, 0, 6, 10, 8, 10);

    private static final Map<SaltDripThickness, VoxelShape> DOWN_SHAPES = Map.of(
            SaltDripThickness.SMALL, SMALL_DOWN,
            SaltDripThickness.MEDIUM, MEDIUM_DOWN,
            SaltDripThickness.LARGE, LARGE_DOWN,
            SaltDripThickness.POINT, POINT_DOWN,
            SaltDripThickness.POINT_MERGE, MERGE_SHAPE
    );

    private static final Map<SaltDripThickness, VoxelShape> UP_SHAPES = Map.of(
            SaltDripThickness.SMALL, SMALL_UP,
            SaltDripThickness.MEDIUM, MEDIUM_UP,
            SaltDripThickness.LARGE, LARGE_UP,
            SaltDripThickness.POINT, POINT_UP,
            SaltDripThickness.POINT_MERGE, MERGE_SHAPE
    );

    public SaltDripBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TIP_DIRECTION, Direction.DOWN)
                .setValue(THICKNESS, SaltDripThickness.SMALL));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction tipDir = context.getClickedFace() == Direction.UP
                ? Direction.UP
                : Direction.DOWN;
        return this.defaultBlockState()
                .setValue(TIP_DIRECTION, tipDir)
                .setValue(THICKNESS, SaltDripThickness.SMALL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Map<SaltDripThickness, VoxelShape> shapes =
                state.getValue(TIP_DIRECTION) == Direction.DOWN ? DOWN_SHAPES : UP_SHAPES;
        return shapes.get(state.getValue(THICKNESS));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isValidSupport(state, level, pos);
    }

    private static boolean isValidSupport(BlockState state, LevelReader level, BlockPos pos) {
        SaltDripThickness thickness = state.getValue(THICKNESS);
        Direction tipDir = state.getValue(TIP_DIRECTION);

        return switch (thickness) {
            case SMALL, MEDIUM, LARGE -> {
                BlockPos supportPos = pos.relative(tipDir.getOpposite());
                yield level.getBlockState(supportPos).isFaceSturdy(level, supportPos, tipDir);
            }
            case POINT -> {
                BlockPos supportPos = pos.relative(tipDir.getOpposite());
                BlockState support = level.getBlockState(supportPos);
                yield isSaltDrip(support)
                        && support.getValue(TIP_DIRECTION) == tipDir
                        && support.getValue(THICKNESS) == SaltDripThickness.LARGE;
            }
            case POINT_MERGE -> isLargeFacing(level.getBlockState(pos.above()), Direction.DOWN)
                    && isLargeFacing(level.getBlockState(pos.below()), Direction.UP);
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!isValidSupport(state, level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        if (state.getValue(THICKNESS) == SaltDripThickness.POINT
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            if (isLargeFacing(level.getBlockState(pos.above()), Direction.DOWN)
                    && isLargeFacing(level.getBlockState(pos.below()), Direction.UP)) {
                return state.setValue(THICKNESS, SaltDripThickness.POINT_MERGE);
            }
        }

        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isValidSupport(state, level, pos)) return;

        if (state.getValue(TIP_DIRECTION) == Direction.DOWN) {
            FallingBlockEntity.fall(level, pos, state);
        } else {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        Direction tipDir = state.getValue(TIP_DIRECTION);
        SaltDripThickness thickness = state.getValue(THICKNESS);

        boolean isSharpTip = tipDir == Direction.UP
                && (thickness == SaltDripThickness.POINT || thickness == SaltDripThickness.POINT_MERGE);

        if (isSharpTip) {
            entity.causeFallDamage(fallDistance, 2.0f, entity.damageSources().stalagmite());
            if (!level.isClientSide()) {
                level.destroyBlock(pos, true);
            }
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public static boolean isSaltDrip(BlockState state) {
        return state.getBlock() instanceof SaltDripBlock;
    }

    private static boolean isLargeFacing(BlockState state, Direction tipDir) {
        return isSaltDrip(state)
                && state.getValue(TIP_DIRECTION) == tipDir
                && state.getValue(THICKNESS) == SaltDripThickness.LARGE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        SaltDripThickness thickness = state.getValue(THICKNESS);
        Direction tipDir = state.getValue(TIP_DIRECTION);

        if (tipDir == Direction.DOWN && isDrippingTip(thickness) && random.nextFloat() < 0.04f) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.15;
            double y = pos.getY() + 0.06;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.15;
            level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, x, y, z, 0, 0, 0);
        }

        if (random.nextFloat() < 0.09f) {
            double x = pos.getX() + 0.2 + random.nextDouble() * 0.6;
            double y = pos.getY() + 0.2 + random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.2 + random.nextDouble() * 0.6;
            level.addParticle(ParticleTypes.WHITE_ASH, x, y, z, 0, random.nextDouble() * 0.02, 0);
        }
    }

    private static boolean isDrippingTip(SaltDripThickness t) {
        return t == SaltDripThickness.LARGE
                || t == SaltDripThickness.POINT
                || t == SaltDripThickness.POINT_MERGE;
    }

    public enum SaltDripThickness implements StringRepresentable {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large"),
        POINT("point"),
        POINT_MERGE("point_merge");

        private final String name;

        SaltDripThickness(String name) { this.name = name; }

        @Override
        public String getSerializedName() { return name; }
    }
}