package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.CaskBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CaskBlock extends BaseEntityBlock {
    public static final MapCodec<CaskBlock> CODEC = simpleCodec(CaskBlock::new);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");

    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D);

    public CaskBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(SEALED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CaskBlockEntity cookingPotEntity) {
                if (!cookingPotEntity.isSealed()) {
                    Containers.dropContents(level, pos, cookingPotEntity.getDroppableInventory());
                }
                cookingPotEntity.getUsedRecipesAndPopExperience(level, Vec3.atCenterOf(pos));
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSecondaryUseActive() && stack.isEmpty()) {
            if (!pLevel.isClientSide()) {
                boolean sealed = toggleSealed(pLevel, pos);
                player.displayClientMessage(Component.translatable(sealed ? "hearthandharvest.cask.sealed" : "hearthandharvest.cask.unsealed"), true);
            }
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
        }

        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pos);
            if(entity instanceof CaskBlockEntity caskBlockEntity) {
                (player).openMenu(new SimpleMenuProvider(caskBlockEntity, Component.literal("Cask")), pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    public static boolean toggleSealed(Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof CaskBlockEntity cask)) return false;

        boolean sealed = !cask.isSealed();
        cask.setSealed(sealed);
        level.playSound(null, pos, sealed ? SoundEvents.HONEYCOMB_WAX_ON : SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
        return sealed;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CaskBlockEntity cask ? cask.getComparatorOutput() : 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof CaskBlockEntity cask) || !cask.isAging()) return;

        if (random.nextInt(8) == 0) {
            level.addParticle(ParticleTypes.BUBBLE_POP,
                    pos.getX() + 0.3D + random.nextDouble() * 0.4D,
                    pos.getY() + 0.7D + random.nextDouble() * 0.2D,
                    pos.getZ() + 0.3D + random.nextDouble() * 0.4D,
                    0.0D, 0.01D, 0.0D);
        }
        if (random.nextInt(160) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.3F, 0.7F + random.nextFloat() * 0.3F, false);
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof CaskBlockEntity cask && cask.isSealed()) {
            ItemStack stack = new ItemStack(this);
            cask.saveToItem(stack, params.getLevel().registryAccess());
            return List.of(stack);
        }
        return super.getDrops(state, params);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(SEALED, false);
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
        builder.add(FACING, OPEN, SEALED);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CaskBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, HHModBlockEntities.CASK.get(), CaskBlockEntity::cookingTick);
    }
}