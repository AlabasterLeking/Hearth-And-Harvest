package alabaster.hearthandharvest.common.block;

import net.minecraft.world.InteractionResult;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.common.block.entity.BottleRackBlockEntity;
import alabaster.hearthandharvest.common.block.entity.container.BottleRackSlotHelper;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BottleRackBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BottleRackBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (direction == Direction.NORTH) {
            return Block.box(0.0D, 0.0D, 8.0, 16.0D, 16.0D, 16.0D);
        }
        if (direction == Direction.EAST) {
            return Block.box(0.0D, 0.0D, 0.0, 8.0D, 16.0D, 16.0D);
        }
        if (direction == Direction.SOUTH) {
            return Block.box(0.0D, 0.0D, 0.0, 16.0D, 16.0D, 8.0D);
        }
        if (direction == Direction.WEST) {
            return Block.box(8.0D, 0.0D, 0.0, 16.0D, 16.0D, 16.0D);
        }
        return super.getShape(state, level, pos, context);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected InteractionResult hhUseItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof BottleRackBlockEntity rack)) {
            return null;
        }

        int slot = BottleRackSlotHelper.getSlotFromHit(state, hit);
        if (slot < 0 || slot >= rack.getContainerSize()) {
            return null;
        }

        ItemStack current = rack.getItem(slot);

        if (current.isEmpty()) {
            if (!heldStack.isEmpty() && heldStack.is(HHModTags.BOTTLES)) {
                ItemStack placed = heldStack.copyWithCount(1);
                rack.setItem(slot, placed);
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
                level.sendBlockUpdated(pos, state, state, 3);
                if (!level.isClientSide && isFull(rack)) {
                    HHSimpleTrigger.trigger(HHModTriggers.FILLED_BOTTLE_RACK, player);
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            if (!level.isClientSide) {
                player.addItem(current.copy());
            }
            rack.setItem(slot, ItemStack.EMPTY);
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    private static boolean isFull(BottleRackBlockEntity rack) {
        for (int i = 0; i < rack.getContainerSize(); i++) {
            if (rack.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof BottleRackBlockEntity rack)) {
            return 0;
        }

        int filledSlots = 0;
        for (int i = 0; i < rack.getContainerSize(); i++) {
            if (!rack.getItem(i).isEmpty()) {
                filledSlots++;
            }
        }
        return Math.min(filledSlots, 15);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity instanceof BottleRackBlockEntity rack) {
            for (int i = 0; i < rack.getContainerSize(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), rack.getItem(i));
            }
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BottleRackBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult itemResult = hhUseItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (itemResult != null) return itemResult;
        return super.use(state, level, pos, player, hand, hit);
    }
}
