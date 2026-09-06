package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.StompingBasinBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StompingBasinBlock extends BaseEntityBlock {

    public static final MapCodec<StompingBasinBlock> CODEC = simpleCodec(StompingBasinBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<MultiblockPart> MULTIBLOCK_PART = EnumProperty.create("multiblock_part", MultiblockPart.class);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    private static final VoxelShape FLOOR      = box(0,  0,  0,  16, 1,  16);
    private static final VoxelShape WEST_WALL  = box(0,  1,  0,  2,  12, 16);
    private static final VoxelShape EAST_WALL  = box(14, 1,  0,  16, 12, 16);
    private static final VoxelShape NORTH_WALL = box(2,  1,  0,  14, 12, 2);
    private static final VoxelShape SOUTH_WALL = box(2,  1,  14, 14, 12, 16);
    public static final VoxelShape SHAPE = Shapes.or(FLOOR, WEST_WALL, EAST_WALL, NORTH_WALL, SOUTH_WALL);

    private static final VoxelShape SHAPE_NW = Shapes.or(FLOOR, WEST_WALL,  box(2,  1,  0,  16, 12, 2));
    private static final VoxelShape SHAPE_NE = Shapes.or(FLOOR, EAST_WALL,  box(0,  1,  0,  14, 12, 2));
    private static final VoxelShape SHAPE_SW = Shapes.or(FLOOR, WEST_WALL,  box(2,  1,  14, 16, 12, 16));
    private static final VoxelShape SHAPE_SE = Shapes.or(FLOOR, EAST_WALL,  box(0,  1,  14, 14, 12, 16));

    private static final float MIN_STOMP_FALL = 0.3f;
    private static final float RIM_Y = 12f / 16f;
    private static final Set<UUID> AIRBORNE_IN_BASIN = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Double> AIRBORNE_PEAK_Y = new ConcurrentHashMap<>();

    public StompingBasinBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MULTIBLOCK_PART, MultiblockPart.NONE));
    }

    public static void clearAirborneTracking() {
        AIRBORNE_IN_BASIN.clear();
        AIRBORNE_PEAK_Y.clear();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MULTIBLOCK_PART);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        MultiblockPart part = state.getValue(MULTIBLOCK_PART);
        if (part == MultiblockPart.NONE) return SHAPE;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof StompingBasinBlockEntity basin)) return SHAPE;

        BlockPos controllerPos = (part == MultiblockPart.CONTROLLER)
                ? pos
                : basin.getControllerPos();
        if (controllerPos == null) return SHAPE;

        int dx = pos.getX() - controllerPos.getX();
        int dz = pos.getZ() - controllerPos.getZ();

        if (dx == 0 && dz == 0) return SHAPE_NW;
        if (dx == 1 && dz == 0) return SHAPE_NE;
        if (dx == 0 && dz == 1) return SHAPE_SW;
        if (dx == 1 && dz == 1) return SHAPE_SE;
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return switch (state.getValue(MULTIBLOCK_PART)) {
            case CONTROLLER, MEMBER -> RenderShape.ENTITYBLOCK_ANIMATED;
            case NONE -> RenderShape.MODEL;
        };
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity living)) return;

        double feetY = living.getY() - pos.getY();
        if (feetY >= RIM_Y) return;

        UUID id = living.getUUID();

        if (!living.onGround()) {
            AIRBORNE_IN_BASIN.add(id);
            AIRBORNE_PEAK_Y.merge(id, living.getY(), Math::max);
            return;
        }

        if (AIRBORNE_IN_BASIN.remove(id)) {
            Double peakY = AIRBORNE_PEAK_Y.remove(id);
            double fallHeight = (peakY != null ? peakY : living.getY()) - living.getY();
            if (fallHeight >= MIN_STOMP_FALL) {
                if (level.getBlockEntity(pos) instanceof StompingBasinBlockEntity basin) {
                    basin.tryProcess(living);
                }
            }
        } else {
            AIRBORNE_PEAK_Y.remove(id);
        }
    }


    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof StompingBasinBlockEntity basin) {
            return HHFluidHandling.comparatorOutput(basin.getFluidHandlerForCapability());
        }
        return 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof StompingBasinBlockEntity basin))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        ItemInteractionResult fluidResult = HHFluidHandling.useOnTank(
                level, pos, player, hand, basin.getFluidTank(), null);
        if (fluidResult != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) return fluidResult;

        ItemStack inHand = player.getItemInHand(hand);
        if (!inHand.isEmpty()) {
            ItemStack remainder = basin.insertItem(inHand.copy());
            if (remainder.getCount() < inHand.getCount()) {
                player.setItemInHand(hand, remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                        0.4f, 0.8f + level.random.nextFloat() * 0.4f);
                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!(level.getBlockEntity(pos) instanceof StompingBasinBlockEntity basin))
            return InteractionResult.PASS;

        if (player.isShiftKeyDown()) basin.extractAll(player);
        else basin.extractOne(player);
        return InteractionResult.CONSUME;
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
            for (int dz = 0; dz >= -1; dz--) {
                BlockPos nwCorner = pos.offset(dx, 0, dz);
                if (canFormAt(level, nwCorner)) {
                    formMultiblock(level, nwCorner);
                    return;
                }
            }
        }
    }

    private boolean canFormAt(Level level, BlockPos nwCorner) {
        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                BlockPos p = nwCorner.offset(dx, 0, dz);
                BlockState bs = level.getBlockState(p);
                if (!bs.is(this)) return false;
                if (bs.getValue(MULTIBLOCK_PART) != MultiblockPart.NONE) return false;
            }
        }
        return true;
    }

    private void formMultiblock(Level level, BlockPos nwPos) {
        BlockPos nePos = nwPos.east();
        BlockPos swPos = nwPos.south();
        BlockPos sePos = nwPos.east().south();

        StompingBasinBlockEntity controllerBE = getBE(level, nwPos);
        StompingBasinBlockEntity neBE = getBE(level, nePos);
        StompingBasinBlockEntity swBE = getBE(level, swPos);
        StompingBasinBlockEntity seBE = getBE(level, sePos);
        if (controllerBE == null || neBE == null || swBE == null || seBE == null) return;

        level.setBlock(nwPos, level.getBlockState(nwPos).setValue(MULTIBLOCK_PART, MultiblockPart.CONTROLLER), 3);
        level.setBlock(nePos, level.getBlockState(nePos).setValue(MULTIBLOCK_PART, MultiblockPart.MEMBER), 3);
        level.setBlock(swPos, level.getBlockState(swPos).setValue(MULTIBLOCK_PART, MultiblockPart.MEMBER), 3);
        level.setBlock(sePos, level.getBlockState(sePos).setValue(MULTIBLOCK_PART, MultiblockPart.MEMBER), 3);

        controllerBE.formAsController(neBE, swBE, seBE);
        neBE.formAsMember(nwPos);
        swBE.formAsMember(nwPos);
        seBE.formAsMember(nwPos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            MultiblockPart part = state.getValue(MULTIBLOCK_PART);
            if (!level.isClientSide && part != MultiblockPart.NONE) {
                dissolveMultiblock(level, pos, part);
            }
            if (part != MultiblockPart.MEMBER) {
                if (level.getBlockEntity(pos) instanceof StompingBasinBlockEntity basin) {
                    basin.dropContents();
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void dissolveMultiblock(Level level, BlockPos brokenPos, MultiblockPart brokenPart) {
        BlockPos controllerPos;
        if (brokenPart == MultiblockPart.CONTROLLER) {
            controllerPos = brokenPos;
        } else {
            StompingBasinBlockEntity memberBE = getBE(level, brokenPos);
            if (memberBE == null) return;
            controllerPos = memberBE.getControllerPos();
            if (controllerPos == null) return;
        }

        BlockPos nePos = controllerPos.east();
        BlockPos swPos = controllerPos.south();
        BlockPos sePos = controllerPos.east().south();
        List<BlockPos> quadrants = List.of(controllerPos, nePos, swPos, sePos);

        List<StompingBasinBlockEntity> survivors = new ArrayList<>();
        for (BlockPos p : quadrants) {
            if (p.equals(brokenPos)) continue;
            StompingBasinBlockEntity be = getBE(level, p);
            if (be != null) survivors.add(be);
        }

        StompingBasinBlockEntity controllerBE = getBE(level, controllerPos);
        if (controllerBE != null) controllerBE.dissolve(survivors);

        for (BlockPos p : quadrants) {
            if (p.equals(brokenPos)) continue;
            BlockState bs = level.getBlockState(p);
            if (bs.is(this) && bs.getValue(MULTIBLOCK_PART) != MultiblockPart.NONE) {
                level.setBlock(p, bs.setValue(MULTIBLOCK_PART, MultiblockPart.NONE), 3);
                StompingBasinBlockEntity be = getBE(level, p);
                if (be != null) be.dissolveAsMember();
            }
        }
    }

    @Nullable
    private StompingBasinBlockEntity getBE(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof StompingBasinBlockEntity sbe ? sbe : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StompingBasinBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, HHModBlockEntities.STOMPING_BASIN.get(),
                StompingBasinBlockEntity::serverTick);
    }
}