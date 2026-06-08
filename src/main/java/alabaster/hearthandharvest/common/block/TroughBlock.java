package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.TroughBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class TroughBlock extends BaseEntityBlock {

    public static final MapCodec<TroughBlock> CODEC = simpleCodec(TroughBlock::new);

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

    private static final int BOTTLE_VOLUME = 250;

    public TroughBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof TroughBlockEntity trough))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack inHand = player.getItemInHand(hand);
        FluidStack tankFluid = trough.getFluidTank().getFluid();

        // Glass bottle: drain water from trough
        if (inHand.is(Items.GLASS_BOTTLE) && !tankFluid.isEmpty() && tankFluid.getAmount() >= BOTTLE_VOLUME) {
            ItemStack waterBottle = new ItemStack(Items.POTION);
            waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
            trough.getFluidTank().drain(BOTTLE_VOLUME, IFluidHandler.FluidAction.EXECUTE);
            inHand.shrink(1);
            if (inHand.isEmpty()) player.setItemInHand(hand, waterBottle);
            else if (!player.addItem(waterBottle)) player.drop(waterBottle, false);
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1f, 1f);
            return ItemInteractionResult.CONSUME;
        }

        // Water bottle: fill trough
        if (isWaterBottle(inHand)) {
            int accepted = trough.getFluidTank().fill(new FluidStack(Fluids.WATER, BOTTLE_VOLUME), IFluidHandler.FluidAction.SIMULATE);
            if (accepted == BOTTLE_VOLUME) {
                trough.getFluidTank().fill(new FluidStack(Fluids.WATER, BOTTLE_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                inHand.shrink(1);
                ItemStack glass = new ItemStack(Items.GLASS_BOTTLE);
                if (inHand.isEmpty()) player.setItemInHand(hand, glass);
                else if (!player.addItem(glass)) player.drop(glass, false);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1f, 1f);
                return ItemInteractionResult.CONSUME;
            }
        }

        // Fluid containers (buckets, etc.)
        FluidActionResult emptyResult = FluidUtil.tryEmptyContainer(inHand, trough.getFluidTank(), Integer.MAX_VALUE, player, true);
        if (emptyResult.isSuccess()) {
            ItemStack emptied = emptyResult.getResult();
            inHand.shrink(1);
            if (inHand.isEmpty()) player.setItemInHand(hand, emptied);
            else if (!player.addItem(emptied)) player.drop(emptied, false);
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1f, 1f);
            return ItemInteractionResult.CONSUME;
        }

        FluidActionResult fillResult = FluidUtil.tryFillContainer(inHand, trough.getFluidTank(), Integer.MAX_VALUE, player, true);
        if (fillResult.isSuccess()) {
            ItemStack filled = fillResult.getResult();
            inHand.shrink(1);
            if (inHand.isEmpty()) player.setItemInHand(hand, filled);
            else if (!player.addItem(filled)) player.drop(filled, false);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
            return ItemInteractionResult.CONSUME;
        }

        // Food insertion
        if (!inHand.isEmpty() && trough.getFluidTank().isEmpty()) {
            ItemStack remainder = trough.insertItem(inHand.copy());
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

    private static boolean isWaterBottle(ItemStack stack) {
        if (!stack.is(Items.POTION)) return false;
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.potion().isPresent() && contents.potion().get().is(Potions.WATER);
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
}