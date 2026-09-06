package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.BasinBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BasinBlock extends BaseEntityBlock {

    public static final MapCodec<BasinBlock> CODEC = simpleCodec(BasinBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape INSIDE = box(2, 1, 2, 14, 16, 14);
    protected static final VoxelShape SHAPE;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public BasinBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return INSIDE;
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
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BasinBlockEntity(pos, state);
    }


    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BasinBlockEntity basin) {
            return HHFluidHandling.comparatorOutput(basin.tank);
        }
        return 0;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return ItemInteractionResult.sidedSuccess(true);
        if (!(level.getBlockEntity(pos) instanceof BasinBlockEntity basin))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        return HHFluidHandling.useOnTank(level, pos, player, hand, basin.tank, null);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide && !level.hasNeighborSignal(pos)) {
            level.scheduleTick(pos, this, 60);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean isPowered = level.hasNeighborSignal(pos);
            boolean isScheduled = level.getBlockTicks().hasScheduledTick(pos, this);
            if (!isPowered && !isScheduled) {
                if (level.getBlockEntity(pos) instanceof BasinBlockEntity basin && !basin.isFull()) {
                    level.scheduleTick(pos, this, 60);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.hasNeighborSignal(pos)) return;
        if (!(level.getBlockEntity(pos) instanceof BasinBlockEntity basin)) return;
        if (basin.isFull()) return;

        basin.tank.fill(new FluidStack(Fluids.WATER, BasinBlockEntity.BOTTLE_AMOUNT), IFluidHandler.FluidAction.EXECUTE);
        level.playSound(null, pos, SoundEvents.FISH_SWIM, SoundSource.BLOCKS, 1f, 1f);

        if (!basin.isFull()) {
            level.scheduleTick(pos, this, 60);
        }
    }

    static {
        SHAPE = Shapes.join(Shapes.block(), INSIDE, BooleanOp.ONLY_FIRST);
    }
}