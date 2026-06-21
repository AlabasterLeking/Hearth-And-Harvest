package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.JugBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidType;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModFluids;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class JugBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<JugBlock> CODEC = simpleCodec(JugBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final int BOTTLE_VOLUME = 250;

    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);

    public JugBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidState fluid = level.getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof JugBlockEntity jug) {
            int amount = jug.getFluidAmount();
            int signal = amount / 1000;
            return Math.min(signal, 8);
        }
        return 0;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof JugBlockEntity tile) {
            ItemStack itemstack = saveTileToItem(tile);
            return Collections.singletonList(itemstack);
        }
        return super.getDrops(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof JugBlockEntity tile) {
            return saveTileToItem(tile);
        }
        return super.getCloneItemStack(level, pos, state);
    }

    public static ItemStack saveTileToItem(BlockEntity tile) {
        Block block = tile.getBlockState().getBlock();
        ItemStack stack = new ItemStack(block.asItem());

        if (tile instanceof JugBlockEntity jug && !jug.getFluidTank().isEmpty()) {
            CompoundTag customTag = new CompoundTag();
            CompoundTag tankTag = new CompoundTag();
            jug.getFluidTank().writeToNBT(tile.getLevel().registryAccess(), tankTag);
            customTag.put("FluidTank", tankTag);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(customTag));
        }

        return stack;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof JugBlockEntity jugBlockEntity)) return ItemInteractionResult.SUCCESS;

        if (player.isShiftKeyDown() && stack.isEmpty() && hand == InteractionHand.MAIN_HAND) {
            ItemStack jugItem = saveTileToItem(jugBlockEntity);
            level.removeBlock(pos, false);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f,
                    ((level.random.nextFloat() - level.random.nextFloat()) * 0.7f + 1f) * 2f);
            if (!player.addItem(jugItem)) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, jugItem));
            }
            return ItemInteractionResult.SUCCESS;
        }

        FluidStack tankFluid = jugBlockEntity.getFluidTank().getFluid();

        // Fill a glass bottle from the jug
        if (stack.is(Items.GLASS_BOTTLE) && !tankFluid.isEmpty() && tankFluid.getAmount() >= BOTTLE_VOLUME) {
            Item bottleResult = HHModFluids.FLUIDS.getEntries().stream()
                    .map(h -> h.get())
                    .filter(f -> f instanceof HHFluidType hhf && hhf.isSource(f.defaultFluidState()))
                    .filter(f -> f.isSame(tankFluid.getFluid()))
                    .map(f -> ((HHFluidType) f).getBottle())
                    .filter(item -> item != Items.AIR && item != null)
                    .findFirst()
                    .orElse(null);

            if (bottleResult != null) {
                jugBlockEntity.getFluidTank().drain(BOTTLE_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                stack.shrink(1);
                ItemStack result = new ItemStack(bottleResult);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, result);
                } else if (!player.addItem(result)) {
                    level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), result));
                }
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockEntity.setChanged();
                return ItemInteractionResult.CONSUME;
            }
        }

        // Fill a held container (buckets, HH bottles via capability, mod containers) from the jug
        FluidActionResult fillResult = FluidUtil.tryFillContainer(
                stack, jugBlockEntity.getFluidTank(), Integer.MAX_VALUE, player, true);
        if (fillResult.isSuccess()) {
            ItemStack filled = fillResult.getResult();
            stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, filled);
            } else if (!player.addItem(filled)) {
                player.drop(filled, false);
            }
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            blockEntity.setChanged();
            return ItemInteractionResult.CONSUME;
        }

        // Empty a held container (buckets, HH bottles via capability, mod containers) into the jug
        FluidActionResult emptyResult = FluidUtil.tryEmptyContainer(
                stack, jugBlockEntity.getFluidTank(), Integer.MAX_VALUE, player, true);
        if (emptyResult.isSuccess()) {
            ItemStack emptied = emptyResult.getResult();
            stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, emptied);
            } else if (!player.addItem(emptied)) {
                player.drop(emptied, false);
            }
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            blockEntity.setChanged();
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return HHModBlockEntities.JUG.get().create(pos, state);
    }
}