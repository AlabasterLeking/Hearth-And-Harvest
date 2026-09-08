package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.JarBlockEntity;
import alabaster.hearthandharvest.common.item.JarBlockItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class JarBlock extends BaseEntityBlock {

    public static final MapCodec<JarBlock> CODEC = simpleCodec(JarBlock::new);
    public static final BooleanProperty SLOT_0 = BooleanProperty.create("slot_0");
    public static final BooleanProperty SLOT_1 = BooleanProperty.create("slot_1");
    public static final BooleanProperty SLOT_2 = BooleanProperty.create("slot_2");
    public static final BooleanProperty SLOT_3 = BooleanProperty.create("slot_3");

    public static final BooleanProperty[] SLOTS = { SLOT_0, SLOT_1, SLOT_2, SLOT_3 };

    private static final VoxelShape SHAPE_NW = box(1, 0,  1,  7, 10,  7);
    private static final VoxelShape SHAPE_NE = box(9, 0,  1, 15, 10,  7);
    private static final VoxelShape SHAPE_SW = box(1, 0,  9,  7, 10, 15);
    private static final VoxelShape SHAPE_SE = box(9, 0,  9, 15, 10, 15);

    private static final VoxelShape[] CORNER_SHAPES = { SHAPE_NW, SHAPE_NE, SHAPE_SW, SHAPE_SE };

    private static final VoxelShape[] COMBINED_SHAPES = new VoxelShape[16];
    static {
        for (int mask = 0; mask < 16; mask++) {
            VoxelShape shape = Shapes.empty();
            for (int i = 0; i < 4; i++) {
                if ((mask & (1 << i)) != 0) shape = Shapes.or(shape, CORNER_SHAPES[i]);
            }
            COMBINED_SHAPES[mask] = mask == 0 ? SHAPE_NW : shape;
        }
    }

    private static final VoxelShape FULL_FOOTPRINT = box(1, 0, 1, 15, 10, 15);

    public JarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(SLOT_0, false)
                .setValue(SLOT_1, false)
                .setValue(SLOT_2, false)
                .setValue(SLOT_3, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        int slot = quadrantFromHit(ctx.getClickLocation(), ctx.getClickedPos());
        return defaultBlockState().setValue(SLOTS[slot], true);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return tryRemoveJar(state, level, pos, player, hit)
                    ? InteractionResult.sidedSuccess(level.isClientSide)
                    : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    private boolean tryRemoveJar(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int slot = quadrantFromHit(hit.getLocation(), pos);
        if (!state.getValue(SLOTS[slot])) {
            slot = firstOccupiedSlot(state);
            if (slot == -1) return false;
        }

        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof JarBlockEntity be) {
                Item item = be.getSlot(slot);
                if (item != null) {
                    ItemStack drop = new ItemStack(item);
                    if (!player.getInventory().add(drop)) {
                        player.drop(drop, false);
                    }
                }
                be.setSlot(slot, null);
                BlockState newState = state.setValue(SLOTS[slot], false);

                if (slotMask(newState) == 0) {
                    level.removeBlock(pos, false);
                } else {
                    level.setBlock(pos, newState, 3);
                    level.sendBlockUpdated(pos, state, newState, 3);
                }
            }
        }
        return true;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return tryRemoveJar(state, level, pos, player, hit)
                    ? ItemInteractionResult.sidedSuccess(level.isClientSide)
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!(stack.getItem() instanceof JarBlockItem jarItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int targetSlot = quadrantFromHit(hit.getLocation(), pos);
        if (state.getValue(SLOTS[targetSlot])) {
            targetSlot = firstEmptySlot(state);
            if (targetSlot == -1) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }

        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof JarBlockEntity be) {
                be.setSlot(targetSlot, jarItem);
                BlockState newState = state.setValue(SLOTS[targetSlot], true);
                level.setBlock(pos, newState, 3);
                level.sendBlockUpdated(pos, state, newState, 3);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof JarBlockEntity be) {
                be.dropAllJars(level, pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return Collections.emptyList();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return FULL_FOOTPRINT;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return COMBINED_SHAPES[slotMask(state)];
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> state
                    .setValue(SLOT_1, state.getValue(SLOT_0))
                    .setValue(SLOT_3, state.getValue(SLOT_1))
                    .setValue(SLOT_2, state.getValue(SLOT_3))
                    .setValue(SLOT_0, state.getValue(SLOT_2));
            case CLOCKWISE_180 -> state
                    .setValue(SLOT_3, state.getValue(SLOT_0))
                    .setValue(SLOT_0, state.getValue(SLOT_3))
                    .setValue(SLOT_2, state.getValue(SLOT_1))
                    .setValue(SLOT_1, state.getValue(SLOT_2));
            case COUNTERCLOCKWISE_90 -> state
                    .setValue(SLOT_2, state.getValue(SLOT_0))
                    .setValue(SLOT_3, state.getValue(SLOT_2))
                    .setValue(SLOT_1, state.getValue(SLOT_3))
                    .setValue(SLOT_0, state.getValue(SLOT_1));
            default -> state;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state
                    .setValue(SLOT_2, state.getValue(SLOT_0))
                    .setValue(SLOT_0, state.getValue(SLOT_2))
                    .setValue(SLOT_3, state.getValue(SLOT_1))
                    .setValue(SLOT_1, state.getValue(SLOT_3));
            case FRONT_BACK -> state
                    .setValue(SLOT_1, state.getValue(SLOT_0))
                    .setValue(SLOT_0, state.getValue(SLOT_1))
                    .setValue(SLOT_3, state.getValue(SLOT_2))
                    .setValue(SLOT_2, state.getValue(SLOT_3));
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(SLOT_0, SLOT_1, SLOT_2, SLOT_3);
    }

    public static int quadrantFromHit(Vec3 hitLocation, BlockPos blockPos) {
        double localX = hitLocation.x - blockPos.getX();
        double localZ = hitLocation.z - blockPos.getZ();
        int east  = localX >= 0.5 ? 1 : 0;
        int south = localZ >= 0.5 ? 2 : 0;
        return south + east;
    }

    public static int firstEmptySlot(BlockState state) {
        for (int i = 0; i < SLOTS.length; i++) {
            if (!state.getValue(SLOTS[i])) return i;
        }
        return -1;
    }

    public static int firstOccupiedSlot(BlockState state) {
        for (int i = 0; i < SLOTS.length; i++) {
            if (state.getValue(SLOTS[i])) return i;
        }
        return -1;
    }

    public static int slotMask(BlockState state) {
        int mask = 0;
        for (int i = 0; i < SLOTS.length; i++) {
            if (state.getValue(SLOTS[i])) mask |= (1 << i);
        }
        return mask;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }
}