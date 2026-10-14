package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.TroughBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TroughBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE;
    static {
        VoxelShape legNW = box(0, 0, 0, 4, 2, 4);
        VoxelShape legNE = box(12, 0, 0, 16, 2, 4);
        VoxelShape legSW = box(0, 0, 12, 4, 2, 16);
        VoxelShape legSE = box(12, 0, 12, 16, 2, 16);
        VoxelShape bottom = box(2, 0, 2, 14, 2, 14);
        VoxelShape wallN = box(0, 2, 0, 16, 8, 2);
        VoxelShape wallS = box(0, 2, 14, 16, 8, 16);
        VoxelShape wallW = box(0, 2, 2, 2, 8, 14);
        VoxelShape wallE = box(14, 2, 2, 16, 8, 14);
        SHAPE = Shapes.or(legNW, legNE, legSW, legSE, bottom, wallN, wallS, wallW, wallE);
    }


    public TroughBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof TroughBlockEntity trough) {
            return HHFluidHandling.comparatorOutput(trough.getFluidTank());
        }
        return 0;
    }

    protected InteractionResult hhUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof TroughBlockEntity trough))
            return null;

        InteractionResult fluidResult = HHFluidHandling.useOnTank(
                level, pos, player, hand, trough.getFluidTank(), null);
        if (fluidResult != null) return fluidResult;

        ItemStack inHand = player.getItemInHand(hand);
        if (!inHand.isEmpty() && trough.getFluidTank().isEmpty()) {
            ItemStack remainder = trough.insertItem(inHand.copy());
            if (remainder.getCount() < inHand.getCount()) {
                player.setItemInHand(hand, remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                        0.4f, 0.8f + level.random.nextFloat() * 0.4f);
                return InteractionResult.CONSUME;
            }
        }

        return null;
    }

    protected InteractionResult hhUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof TroughBlockEntity trough))
            return InteractionResult.PASS;

        if (player.isShiftKeyDown()) trough.extractAll(player);
        else trough.extractOne(player);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof TroughBlockEntity trough) {
                trough.dropContents();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TroughBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, HHModBlockEntities.TROUGH.get(), TroughBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult itemResult = hhUseItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (itemResult != null) return itemResult;
        return hand == InteractionHand.MAIN_HAND ? hhUseWithoutItem(state, level, pos, player, hit) : InteractionResult.PASS;
    }
}
