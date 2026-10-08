package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.util.Mth;
import java.util.EnumMap;
import java.util.Map;
import java.util.List;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import java.util.ArrayList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nullable;

public class KegBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<MultiblockPart> MULTIBLOCK_PART = EnumProperty.create("multiblock_part", MultiblockPart.class);
    public static final IntegerProperty CELL = IntegerProperty.create("cell", 0, 7);
    private static final VoxelShape BODY = Shapes.or(
            Block.box(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 16.0D),
            Block.box(0.0D, 0.0D, 2.0D, 16.0D, 1.0D, 6.0D),
            Block.box(0.0D, 0.0D, 10.0D, 16.0D, 1.0D, 14.0D),
            Block.box(0.0D, 1.0D, 2.0D, 1.0D, 5.0D, 6.0D),
            Block.box(0.0D, 1.0D, 10.0D, 1.0D, 5.0D, 14.0D),
            Block.box(15.0D, 1.0D, 2.0D, 16.0D, 5.0D, 6.0D),
            Block.box(15.0D, 1.0D, 10.0D, 16.0D, 5.0D, 14.0D));

    private static final VoxelShape NORTH_SHAPE = BODY;
    private static final VoxelShape EAST_SHAPE = rotate(BODY, 1);
    private static final VoxelShape SOUTH_SHAPE = rotate(BODY, 2);
    private static final VoxelShape WEST_SHAPE = rotate(BODY, 3);

    private static VoxelShape rotate(VoxelShape shape, int quarterTurns) {
        VoxelShape result = shape;
        for (int turn = 0; turn < quarterTurns; ++turn) {
            VoxelShape turned = Shapes.empty();
            for (AABB box : result.toAabbs()) {
                turned = Shapes.or(turned, Shapes.box(1.0D - box.maxZ, box.minY, box.minX, 1.0D - box.minZ, box.maxY, box.maxX));
            }
            result = turned;
        }
        return result;
    }

    private static final double[][] CUBE_BOXES = {
            {0.0D, 0.0D, 0.0D, 32.0D, 32.0D, 32.0D},
            {12.5D, 6.0D, -3.5D, 19.5D, 9.0D, 0.0D}
    };

    private static final Map<Direction, VoxelShape[]> CUBE_SHAPES = buildCubeShapes();

    private static Map<Direction, VoxelShape[]> buildCubeShapes() {
        Map<Direction, VoxelShape[]> shapes = new EnumMap<>(Direction.class);

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int turns = switch (facing) {
                case EAST -> 1;
                case SOUTH -> 2;
                case WEST -> 3;
                default -> 0;
            };

            VoxelShape[] cells = new VoxelShape[KegBlockEntity.MULTIBLOCK_SIZE];
            for (int index = 0; index < cells.length; index++) cells[index] = Shapes.empty();

            for (double[] box : CUBE_BOXES) {
                double[] rotated = rotateBox(box, turns);

                for (int dy = 0; dy <= 1; dy++) {
                    for (int dx = 0; dx <= 1; dx++) {
                        for (int dz = 0; dz <= 1; dz++) {
                            double minX = Math.max(rotated[0], dx * 16.0D) - dx * 16.0D;
                            double minY = Math.max(rotated[1], dy * 16.0D) - dy * 16.0D;
                            double minZ = Math.max(rotated[2], dz * 16.0D) - dz * 16.0D;
                            double maxX = Math.min(rotated[3], (dx + 1) * 16.0D) - dx * 16.0D;
                            double maxY = Math.min(rotated[4], (dy + 1) * 16.0D) - dy * 16.0D;
                            double maxZ = Math.min(rotated[5], (dz + 1) * 16.0D) - dz * 16.0D;
                            if (maxX <= minX || maxY <= minY || maxZ <= minZ) continue;

                            int index = cellIndex(dx, dy, dz);
                            cells[index] = Shapes.or(cells[index], Block.box(minX, minY, minZ, maxX, maxY, maxZ));
                        }
                    }
                }
            }

            shapes.put(facing, cells);
        }
        return shapes;
    }

    private static double[] rotateBox(double[] box, int turns) {
        double[] result = box.clone();
        for (int turn = 0; turn < turns; turn++) {
            double minX = 32.0D - result[5];
            double maxX = 32.0D - result[2];
            double minZ = result[0];
            double maxZ = result[3];
            result = new double[]{minX, result[1], minZ, maxX, result[4], maxZ};
        }
        return result;
    }

    private static int cellIndex(int dx, int dy, int dz) {
        return (dy * 4) + (dx * 2) + dz;
    }

    private static int modelCell(int dx, int dy, int dz, Direction facing) {
        return switch (facing) {
            case EAST -> cellIndex(dz, dy, 1 - dx);
            case SOUTH -> cellIndex(1 - dx, dy, 1 - dz);
            case WEST -> cellIndex(1 - dz, dy, dx);
            default -> cellIndex(dx, dy, dz);
        };
    }


    public static final MapCodec<KegBlock> CODEC = simpleCodec(KegBlock::new);

    public KegBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(MULTIBLOCK_PART, MultiblockPart.NONE)
                .setValue(CELL, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MULTIBLOCK_PART, CELL);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && state.getValue(MULTIBLOCK_PART) == MultiblockPart.NONE) {
            tryFormMultiblock(level, pos);
        }
    }

    private void tryFormMultiblock(Level level, BlockPos pos) {
        for (int dx = 0; dx >= -1; dx--) {
            for (int dy = 0; dy >= -1; dy--) {
                for (int dz = 0; dz >= -1; dz--) {
                    BlockPos corner = pos.offset(dx, dy, dz);
                    if (canFormAt(level, corner)) {
                        formMultiblock(level, corner);
                        return;
                    }
                }
            }
        }
    }

    private boolean canFormAt(Level level, BlockPos corner) {
        for (BlockPos p : cubePositions(corner)) {
            BlockState bs = level.getBlockState(p);
            if (!bs.is(this) || bs.getValue(MULTIBLOCK_PART) != MultiblockPart.NONE) return false;
            if (!(level.getBlockEntity(p) instanceof KegBlockEntity)) return false;
        }
        return true;
    }

    private static List<BlockPos> cubePositions(BlockPos corner) {
        List<BlockPos> positions = new ArrayList<>(KegBlockEntity.MULTIBLOCK_SIZE);
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    positions.add(corner.offset(dx, dy, dz));
                }
            }
        }
        return positions;
    }

    private void formMultiblock(Level level, BlockPos corner) {
        List<BlockPos> positions = cubePositions(corner);
        List<KegBlockEntity> members = new ArrayList<>();

        for (BlockPos p : positions) {
            if (!(level.getBlockEntity(p) instanceof KegBlockEntity keg)) return;
            if (!p.equals(corner)) members.add(keg);
        }

        if (!(level.getBlockEntity(corner) instanceof KegBlockEntity controller)) return;

        Direction facing = level.getBlockState(corner).getValue(FACING);
        for (BlockPos p : positions) {
            MultiblockPart part = p.equals(corner) ? MultiblockPart.CONTROLLER : MultiblockPart.MEMBER;
            int cell = modelCell(p.getX() - corner.getX(), p.getY() - corner.getY(), p.getZ() - corner.getZ(), facing);
            level.setBlock(p, level.getBlockState(p)
                    .setValue(MULTIBLOCK_PART, part)
                    .setValue(FACING, facing)
                    .setValue(CELL, cell), Block.UPDATE_ALL);
        }

        controller.formAsController(members);
        for (KegBlockEntity member : members) {
            member.formAsMember(corner);
        }
    }

    private void dissolveMultiblock(Level level, BlockPos brokenPos, MultiblockPart brokenPart) {
        BlockPos corner = brokenPart == MultiblockPart.CONTROLLER
                ? brokenPos
                : findController(level, brokenPos);
        if (corner == null) return;

        for (BlockPos p : cubePositions(corner)) {
            BlockState bs = level.getBlockState(p);
            if (!bs.is(this)) continue;

            level.setBlock(p, bs.setValue(MULTIBLOCK_PART, MultiblockPart.NONE).setValue(CELL, 0), Block.UPDATE_ALL);
            if (level.getBlockEntity(p) instanceof KegBlockEntity keg) keg.dissolve();
        }
    }

    @Nullable
    private BlockPos findController(Level level, BlockPos memberPos) {
        return level.getBlockEntity(memberPos) instanceof KegBlockEntity keg && keg.controller() != null
                ? keg.controller().getBlockPos()
                : null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(MULTIBLOCK_PART, MultiblockPart.NONE)
                .setValue(CELL, 0);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        MultiblockPart part = state.getValue(MULTIBLOCK_PART);
        if (part != MultiblockPart.NONE) {
            return cubeShape(state, level, pos, part);
        }

        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    private VoxelShape cubeShape(BlockState state, BlockGetter level, BlockPos pos, MultiblockPart part) {
        BlockPos corner = part == MultiblockPart.CONTROLLER ? pos : controllerPos(level, pos);
        if (corner == null) return Shapes.block();

        int dx = Mth.clamp(pos.getX() - corner.getX(), 0, 1);
        int dy = Mth.clamp(pos.getY() - corner.getY(), 0, 1);
        int dz = Mth.clamp(pos.getZ() - corner.getZ(), 0, 1);

        return CUBE_SHAPES.get(state.getValue(FACING))[cellIndex(dx, dy, dz)];
    }

    @Nullable
    private BlockPos controllerPos(BlockGetter level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof KegBlockEntity keg)) return null;
        KegBlockEntity controller = keg.controller();
        return controller == null ? null : controller.getBlockPos();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KegBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HHModBlockEntities.KEG.get(), KegBlockEntity::fermentingTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof KegBlockEntity keg) {
            KegBlockEntity controller = keg.controller();
            if (controller == null) return ItemInteractionResult.sidedSuccess(level.isClientSide);
            player.openMenu(controller, buf -> buf.writeBlockPos(controller.getBlockPos()));
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof KegBlockEntity keg) || !keg.isFermenting()) return;

        if (random.nextInt(180) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    HHModSounds.KEG_FERMENTING.get(), SoundSource.BLOCKS, 0.4F, 0.6F + random.nextFloat() * 0.2F, false);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            MultiblockPart part = state.getValue(MULTIBLOCK_PART);
            if (!level.isClientSide && part != MultiblockPart.NONE) {
                dissolveMultiblock(level, pos, part);
            }
            super.onRemove(state, level, pos, newState, moving);
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof KegBlockEntity keg) {
            ItemStack stack = new ItemStack(this);
            keg.saveToItem(stack, params.getLevel().registryAccess());
            return List.of(stack);
        }
        return super.getDrops(state, params);
    }
}