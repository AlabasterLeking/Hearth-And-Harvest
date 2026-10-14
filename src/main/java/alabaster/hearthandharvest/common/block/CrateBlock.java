package alabaster.hearthandharvest.common.block;

import net.minecraft.world.InteractionResult;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import alabaster.hearthandharvest.common.block.entity.container.CrateSlotHelper;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrateBlock extends Block implements EntityBlock {

    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty STACKED = BooleanProperty.create("stacked");

    private static final VoxelShape SHAPE_BOTTOM = Shapes.or(
            Block.box( 0, 0,  0, 16, 4, 16),
            Block.box( 0, 4,  0, 16, 8,  1),
            Block.box( 0, 4, 15, 16, 8, 16),
            Block.box( 0, 4,  0,  1, 8, 16),
            Block.box(15, 4,  0, 16, 8, 16)
    );
    private static final VoxelShape SHAPE_TOP = Shapes.or(
            Block.box( 0,  8,  0, 16, 12, 16),
            Block.box( 0, 12,  0, 16, 16,  1),
            Block.box( 0, 12, 15, 16, 16, 16),
            Block.box( 0, 12,  0,  1, 16, 16),
            Block.box(15, 12,  0, 16, 16, 16)
    );
    private static final VoxelShape SHAPE_DOUBLE = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP);

    public CrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(FACING, Direction.NORTH)
                .setValue(STACKED, false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING, STACKED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(TYPE) == SlabType.BOTTOM && direction == Direction.UP) {
            return state.setValue(STACKED, neighborState.is(this));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        ItemStack held    = context.getItemInHand();
        SlabType existing = state.getValue(TYPE);

        if (existing == SlabType.DOUBLE || !held.is(this.asItem())) return false;
        if (state.getValue(STACKED)) return false;
        if (existing == SlabType.BOTTOM && hasTallBottle(context.getLevel(), context.getClickedPos(), existing)) return false;

        Direction face = context.getClickedFace();

        if (face == Direction.UP)   return existing == SlabType.BOTTOM;
        if (face == Direction.DOWN) return existing == SlabType.TOP;

        double hitY = context.getClickLocation().y - context.getClickedPos().getY();
        return existing == SlabType.BOTTOM ? hitY > 0.5 : hitY <= 0.5;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState existing = context.getLevel().getBlockState(pos);
        Direction facing = context.getHorizontalDirection().getOpposite();

        if (existing.is(this) && existing.getValue(TYPE) != SlabType.DOUBLE) {
            return existing.setValue(TYPE, SlabType.DOUBLE).setValue(STACKED, false);
        }

        Direction face = context.getClickedFace();
        SlabType type;
        if (face == Direction.DOWN) type = SlabType.TOP;
        else if (face == Direction.UP) type = SlabType.BOTTOM;
        else {
            double hitY = context.getClickLocation().y - pos.getY();
            type = hitY > 0.5 ? SlabType.TOP : SlabType.BOTTOM;
        }

        boolean stacked = false;
        if (type == SlabType.BOTTOM) {
            BlockState above = context.getLevel().getBlockState(pos.above());
            stacked = above.is(this);
        }

        return this.defaultBlockState()
                .setValue(TYPE, type)
                .setValue(FACING, facing)
                .setValue(STACKED, stacked);
    }

    private boolean hasTallBottle(Level level, BlockPos pos, SlabType existingType) {
        if (!(level.getBlockEntity(pos) instanceof CrateBlockEntity be)) return false;
        if (existingType == SlabType.TOP) return false;
        for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
            if (be.getItem(i).is(HHModTags.TALL_BOTTLES)) return true;
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case BOTTOM -> SHAPE_BOTTOM;
            case TOP    -> SHAPE_TOP;
            case DOUBLE -> SHAPE_DOUBLE;
        };
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
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) == SlabType.DOUBLE;
    }

    protected InteractionResult hhUseItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (player.isShiftKeyDown() && heldStack.isEmpty() && hand == InteractionHand.MAIN_HAND) {
            if (level.isClientSide) return InteractionResult.SUCCESS;

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CrateBlockEntity crate) {
                SlabType type = state.getValue(TYPE);

                if (type != SlabType.DOUBLE) {
                    ItemStack drop = buildHalfDrop(crate, 0);
                    level.removeBlock(pos, false);
                    level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.8f, 0.8f);
                    if (!player.addItem(drop)) popResource(level, pos, drop);
                } else {
                    Vec3 eyes   = player.getEyePosition();
                    Vec3 look   = player.getLookAngle();
                    Vec3 target = eyes.add(look.scale(player.getBlockReach() + 1));
                    var hitResult = level.clip(new ClipContext(
                            eyes, target,
                            ClipContext.Block.OUTLINE,
                            ClipContext.Fluid.NONE,
                            player));

                    boolean topHalf      = hitResult.getLocation().y - pos.getY() >= 0.5;
                    int pickupOffset     = topHalf ? CrateBlockEntity.SLOTS_PER_HALF : 0;
                    int remainOffset     = topHalf ? 0 : CrateBlockEntity.SLOTS_PER_HALF;
                    SlabType remaining   = topHalf ? SlabType.BOTTOM : SlabType.TOP;

                    ItemStack drop = buildHalfDrop(crate, pickupOffset);

                    NonNullList<ItemStack> items = crate.getItems();
                    for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
                        items.set(i, crate.getItem(remainOffset + i).copy());
                    }
                    for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
                        items.set(CrateBlockEntity.SLOTS_PER_HALF + i, ItemStack.EMPTY);
                    }
                    crate.setChanged();
                    level.setBlock(pos, state.setValue(TYPE, remaining), 3);

                    level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.8f, 0.8f);
                    if (!player.addItem(drop)) popResource(level, pos, drop);
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (!(level.getBlockEntity(pos) instanceof CrateBlockEntity rack)) {
            return null;
        }

        int slot = CrateSlotHelper.getSlotFromHit(state, hit);
        if (slot < 0 || slot >= rack.getContainerSize()) {
            return null;
        }

        ItemStack current = rack.getItem(slot);

        if (current.isEmpty()) {
            if (!heldStack.isEmpty() && (heldStack.is(HHModTags.BOTTLES) || heldStack.is(HHModTags.CRATEABLE_ITEMS))) {
                ItemStack placed = heldStack.copyWithCount(1);
                rack.setItem(slot, placed);
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
                level.sendBlockUpdated(pos, state, state, 3);
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

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof CrateBlockEntity rack)) return 0;

        int filledSlots = 0;
        int maxSlot = state.getValue(TYPE) == SlabType.DOUBLE
                ? CrateBlockEntity.TOTAL_SLOTS
                : CrateBlockEntity.SLOTS_PER_HALF;

        for (int i = 0; i < maxSlot; i++) {
            if (!rack.getItem(i).isEmpty()) filledSlots++;
        }
        return Math.min(filledSlots, 15);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        playerWillDestroy(level, pos, state, player);
        return level.removeBlock(pos, false);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if (level.isClientSide) return;

        if (blockEntity instanceof CrateBlockEntity crate) {
            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                popResource(level, pos, buildHalfDrop(crate, 0));
                popResource(level, pos, buildHalfDrop(crate, CrateBlockEntity.SLOTS_PER_HALF));
            } else {
                popResource(level, pos, buildHalfDrop(crate, 0));
            }
        } else {
            popResource(level, pos, new ItemStack(this));
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        LootParams lootParams = params
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.BLOCK);

        BlockEntity be = lootParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (be instanceof CrateBlockEntity crate) {
            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                return List.of(
                        buildHalfDrop(crate, 0),
                        buildHalfDrop(crate, CrateBlockEntity.SLOTS_PER_HALF)
                );
            }
            return List.of(buildHalfDrop(crate, 0));
        }

        return List.of(new ItemStack(this));
    }

    private ItemStack buildHalfDrop(CrateBlockEntity crate, int slotOffset) {
        ItemStack drop = new ItemStack(this);

        NonNullList<ItemStack> halfItems =
                NonNullList.withSize(CrateBlockEntity.SLOTS_PER_HALF, ItemStack.EMPTY);
        boolean anyFilled = false;
        for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
            ItemStack s = crate.getItem(slotOffset + i);
            halfItems.set(i, s.copy());
            if (!s.isEmpty()) anyFilled = true;
        }

        if (anyFilled) {
            CompoundTag tag = new CompoundTag();
            ContainerHelper.saveAllItems(tag, halfItems);
            BlockItem.setBlockEntityData(drop, HHModBlockEntities.CRATE.get(), tag);
        }

        return drop;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult itemResult = hhUseItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (itemResult != null) return itemResult;
        return super.use(state, level, pos, player, hand, hit);
    }
}
