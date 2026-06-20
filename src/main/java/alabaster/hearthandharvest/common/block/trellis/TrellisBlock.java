package alabaster.hearthandharvest.common.block.trellis;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TrellisBlock extends Block implements BonemealableBlock {

    public static final BooleanProperty MIDDLE_EW = BooleanProperty.create("middle_ew");
    public static final BooleanProperty MIDDLE_NS = BooleanProperty.create("middle_ns");
    public static final BooleanProperty SIDE_NORTH = BooleanProperty.create("side_north");
    public static final BooleanProperty SIDE_SOUTH = BooleanProperty.create("side_south");
    public static final BooleanProperty SIDE_EAST = BooleanProperty.create("side_east");
    public static final BooleanProperty SIDE_WEST = BooleanProperty.create("side_west");
    public static final BooleanProperty HAS_FLAT = BooleanProperty.create("has_flat");
    public static final BooleanProperty HAS_TOP = BooleanProperty.create("has_top");
    public static final BooleanProperty GROWTH_BLOCKED = BooleanProperty.create("growth_blocked");
    public static final EnumProperty<TrellisMaterial> MATERIAL = EnumProperty.create("material", TrellisMaterial.class);
    public static final EnumProperty<TrellisPlant> PLANT = EnumProperty.create("plant", TrellisPlant.class,
            TrellisPlant.NONE, TrellisPlant.VINE, TrellisPlant.ROSE);

    private static final VoxelShape MIDDLE_EW_SHAPE = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape MIDDLE_NS_SHAPE = Block.box(7, 0, 0, 9, 16, 16);
    private static final VoxelShape FLAT_SHAPE = Block.box( 0, 0, 0, 16, 2, 16);
    private static final VoxelShape TOP_SHAPE = Block.box( 0,14, 0, 16, 16, 16);
    private static final VoxelShape SIDE_N_SHAPE = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape SIDE_S_SHAPE = Block.box(0, 0, 0, 16, 16,  2);
    private static final VoxelShape SIDE_E_SHAPE = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape SIDE_W_SHAPE = Block.box(14, 0, 0, 16, 16, 16);

    private static final VoxelShape[] SHAPE_CACHE;
    static { SHAPE_CACHE = new VoxelShape[256];
        for (int i = 0; i < 256; i++) {
            VoxelShape s = null;
            if ((i & 1) != 0) s = s == null ? MIDDLE_EW_SHAPE : Shapes.or(s, MIDDLE_EW_SHAPE);
            if ((i & 2) != 0) s = s == null ? MIDDLE_NS_SHAPE : Shapes.or(s, MIDDLE_NS_SHAPE);
            if ((i & 4) != 0) s = s == null ? SIDE_N_SHAPE : Shapes.or(s, SIDE_N_SHAPE);
            if ((i & 8) != 0) s = s == null ? SIDE_S_SHAPE : Shapes.or(s, SIDE_S_SHAPE);
            if ((i & 16) != 0) s = s == null ? SIDE_E_SHAPE : Shapes.or(s, SIDE_E_SHAPE);
            if ((i & 32) != 0) s = s == null ? SIDE_W_SHAPE : Shapes.or(s, SIDE_W_SHAPE);
            if ((i & 64) != 0) s = s == null ? FLAT_SHAPE : Shapes.or(s, FLAT_SHAPE);
            if ((i & 128) != 0) s = s == null ? TOP_SHAPE : Shapes.or(s, TOP_SHAPE);
            SHAPE_CACHE[i] = s == null ? Shapes.block() : s;
        }
    }

    @Nullable
    private final Supplier<Block> grapeVariant;

    public TrellisBlock(Properties props, @Nullable Supplier<Block> grapeVariant) {
        super(props);
        this.grapeVariant = grapeVariant;
        registerDefaultState(defaultBlockState()
                .setValue(MIDDLE_EW, false)
                .setValue(MIDDLE_NS, false)
                .setValue(SIDE_NORTH, false)
                .setValue(SIDE_SOUTH, false)
                .setValue(SIDE_EAST, false)
                .setValue(SIDE_WEST, false)
                .setValue(HAS_FLAT, false)
                .setValue(HAS_TOP, false)
                .setValue(GROWTH_BLOCKED, false)
                .setValue(MATERIAL, TrellisMaterial.STICK)
                .setValue(getPlantProperty(), TrellisPlant.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MIDDLE_EW, MIDDLE_NS, SIDE_NORTH, SIDE_SOUTH, SIDE_EAST, SIDE_WEST,
                HAS_FLAT, HAS_TOP, GROWTH_BLOCKED, MATERIAL);
        addPlantAndAgeProperties(builder);
    }

    protected void addPlantAndAgeProperties(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLANT);
    }

    protected EnumProperty<TrellisPlant> getPlantProperty() {
        return PLANT;
    }

    public TrellisPlant getPlant(BlockState state) {
        return state.getValue(PLANT);
    }

    protected BlockState applyPlantToState(BlockState state, TrellisPlant plant) {
        return state.setValue(PLANT, plant);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        int idx = (state.getValue(MIDDLE_EW) ? 1 : 0)
                | (state.getValue(MIDDLE_NS) ? 2 : 0)
                | (state.getValue(SIDE_NORTH) ? 4 : 0)
                | (state.getValue(SIDE_SOUTH) ? 8 : 0)
                | (state.getValue(SIDE_EAST) ? 16 : 0)
                | (state.getValue(SIDE_WEST) ? 32 : 0)
                | (state.getValue(HAS_FLAT) ? 64 : 0)
                | (state.getValue(HAS_TOP) ? 128 : 0);
        return SHAPE_CACHE[idx];
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        TrellisPlant plant = state.getValue(PLANT);
        return plant == TrellisPlant.VINE || plant == TrellisPlant.ROSE;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        TrellisPlant plant = state.getValue(PLANT);
        if ((plant == TrellisPlant.VINE || plant == TrellisPlant.ROSE) && random.nextInt(5) == 0) {
            trySpread(state, level, pos);
        }
    }

    protected void trySpread(BlockState state, ServerLevel level, BlockPos pos) {
        TrellisPlant plant = state.getValue(PLANT);
        List<BlockPos> candidates = new ArrayList<>();

        BlockPos above = pos.above();
        if (isEmptyTrellis(level, above)) { candidates.add(above); candidates.add(above); }

        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos side = pos.relative(dir);
            if (isEmptyTrellis(level, side)) candidates.add(side);
        }

        BlockPos below = pos.below();
        if (isEmptyTrellis(level, below) && candidates.isEmpty()) candidates.add(below);

        if (candidates.isEmpty()) return;
        BlockPos target = candidates.get(level.random.nextInt(candidates.size()));
        level.setBlock(target, level.getBlockState(target).setValue(PLANT, plant), Block.UPDATE_ALL);
    }

    protected boolean isEmptyTrellis(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof TrellisBlock
                && state.hasProperty(PLANT)
                && state.getValue(PLANT) == TrellisPlant.NONE
                && !state.getValue(GROWTH_BLOCKED);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        TrellisPlant currentPlant = getPlant(state);

        if (stack.is(Items.SHEARS) && currentPlant == TrellisPlant.NONE) {
            if (!level.isClientSide()) {
                boolean nowBlocked = !state.getValue(GROWTH_BLOCKED);
                level.setBlock(pos, state.setValue(GROWTH_BLOCKED, nowBlocked), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1f, 1f);
                ((ServerLevel) level).sendParticles(ParticleTypes.CRIT,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        5, 0.3, 0.3, 0.3, 0.0);
                player.displayClientMessage(Component.translatable(nowBlocked
                        ? "block.hearthandharvest.trellis.growth_blocked"
                        : "block.hearthandharvest.trellis.growth_allowed"), true);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.is(Items.SHEARS) && !currentPlant.isGrape() && currentPlant != TrellisPlant.NONE) {
            if (!level.isClientSide()) {
                ItemStack drop = switch (currentPlant) {
                    case VINE -> new ItemStack(Items.VINE);
                    case ROSE -> new ItemStack(Items.ROSE_BUSH);
                    default -> ItemStack.EMPTY;
                };
                if (!drop.isEmpty()) popResource(level, pos, drop);
                level.setBlock(pos, state.setValue(PLANT, TrellisPlant.NONE), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1f, 1f);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.canPerformAction(ItemAbilities.AXE_STRIP)
                && state.getValue(MATERIAL) == TrellisMaterial.BAMBOO) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(MATERIAL, TrellisMaterial.STRIPPED_BAMBOO), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1f, 1f);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.is(Items.VINE) && currentPlant == TrellisPlant.NONE && state.hasProperty(PLANT)) {
            return applyPlant(stack, state, level, pos, player, TrellisPlant.VINE);
        }
        if (stack.is(Items.ROSE_BUSH) && currentPlant == TrellisPlant.NONE && state.hasProperty(PLANT)) {
            return applyPlant(stack, state, level, pos, player, TrellisPlant.ROSE);
        }

        if ((stack.is(HHModItems.RED_GRAPES.get()) || stack.is(HHModItems.GREEN_GRAPES.get()))
                && currentPlant == TrellisPlant.NONE && grapeVariant != null) {
            TrellisPlant plant = stack.is(HHModItems.RED_GRAPES.get())
                    ? TrellisPlant.RED_GRAPE : TrellisPlant.GREEN_GRAPE;
            if (!level.isClientSide()) {
                TrellisBlock targetBlock = (TrellisBlock) grapeVariant.get();
                BlockState converted = targetBlock.applyPlantToState(copyStructure(state, targetBlock), plant);
                level.setBlock(pos, converted, Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1f, 1f);
                if (!player.isCreative()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected ItemInteractionResult applyPlant(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, TrellisPlant plant) {
        if (!level.isClientSide()) {
            level.setBlock(pos, applyPlantToState(state, plant), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1f, 1f);
            if (!player.isCreative()) stack.shrink(1);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    public static BlockState copyStructure(BlockState from, Block target) {
        return target.defaultBlockState()
                .setValue(MATERIAL, from.getValue(MATERIAL))
                .setValue(MIDDLE_EW, from.getValue(MIDDLE_EW))
                .setValue(MIDDLE_NS, from.getValue(MIDDLE_NS))
                .setValue(SIDE_NORTH, from.getValue(SIDE_NORTH))
                .setValue(SIDE_SOUTH, from.getValue(SIDE_SOUTH))
                .setValue(SIDE_EAST, from.getValue(SIDE_EAST))
                .setValue(SIDE_WEST, from.getValue(SIDE_WEST))
                .setValue(HAS_FLAT, from.getValue(HAS_FLAT))
                .setValue(HAS_TOP, from.getValue(HAS_TOP))
                .setValue(GROWTH_BLOCKED, from.getValue(GROWTH_BLOCKED));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return switch (state.getValue(MATERIAL)) {
            case STICK -> new ItemStack(HHModItems.TRELLIS.get());
            case BAMBOO -> new ItemStack(HHModItems.BAMBOO_TRELLIS.get());
            case STRIPPED_BAMBOO -> new ItemStack(HHModItems.STRIPPED_BAMBOO_TRELLIS.get());
        };
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        Item item = switch (state.getValue(MATERIAL)) {
            case STICK -> HHModItems.TRELLIS.get();
            case BAMBOO -> HHModItems.BAMBOO_TRELLIS.get();
            case STRIPPED_BAMBOO -> HHModItems.STRIPPED_BAMBOO_TRELLIS.get();
        };
        int count = (state.getValue(MIDDLE_EW)  ? 1 : 0) + (state.getValue(MIDDLE_NS) ? 1 : 0)
                + (state.getValue(SIDE_NORTH) ? 1 : 0) + (state.getValue(SIDE_SOUTH) ? 1 : 0)
                + (state.getValue(SIDE_EAST)  ? 1 : 0) + (state.getValue(SIDE_WEST)  ? 1 : 0)
                + (state.getValue(HAS_FLAT)   ? 1 : 0) + (state.getValue(HAS_TOP)    ? 1 : 0);
        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < count; i++) drops.add(new ItemStack(item));
        return drops;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        if (state.getValue(SIDE_NORTH) || state.getValue(SIDE_SOUTH)
                || state.getValue(SIDE_EAST) || state.getValue(SIDE_WEST)
                || state.getValue(MIDDLE_EW) || state.getValue(MIDDLE_NS)) {
            return true;
        }
        // Back-face case: entity is in the adjacent block pressed against the outer face of a panel
        BlockState n = level.getBlockState(pos.north());
        if (n.getBlock() instanceof TrellisBlock && n.getValue(SIDE_SOUTH)) return true;
        BlockState s = level.getBlockState(pos.south());
        if (s.getBlock() instanceof TrellisBlock && s.getValue(SIDE_NORTH)) return true;
        BlockState e = level.getBlockState(pos.east());
        if (e.getBlock() instanceof TrellisBlock && e.getValue(SIDE_WEST)) return true;
        BlockState w = level.getBlockState(pos.west());
        if (w.getBlock() instanceof TrellisBlock && w.getValue(SIDE_EAST)) return true;
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {

    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        if (getPlant(state) != TrellisPlant.NONE) {
            if (level.isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("block.hearthandharvest.trellis.remove_plant_first"), true);
            }
            return InteractionResult.FAIL;
        }

        BooleanProperty toRemove = componentFromHit(state, hit);
        if (toRemove == null || !state.getValue(toRemove)) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            BlockState newState = state.setValue(toRemove, false);
            boolean anyLeft = newState.getValue(MIDDLE_EW)  || newState.getValue(MIDDLE_NS)
                    || newState.getValue(SIDE_NORTH) || newState.getValue(SIDE_SOUTH)
                    || newState.getValue(SIDE_EAST) || newState.getValue(SIDE_WEST)
                    || newState.getValue(HAS_FLAT) || newState.getValue(HAS_TOP);

            if (anyLeft) {
                level.setBlock(pos, newState, Block.UPDATE_ALL);
            } else {
                level.removeBlock(pos, false);
            }

            Item item = switch (state.getValue(MATERIAL)) {
                case STICK -> HHModItems.TRELLIS.get();
                case BAMBOO -> HHModItems.BAMBOO_TRELLIS.get();
                case STRIPPED_BAMBOO -> HHModItems.STRIPPED_BAMBOO_TRELLIS.get();
            };
            ItemStack returned = new ItemStack(item);
            if (!player.getInventory().add(returned)) popResource(level, pos, returned);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 0.8f, 1.2f);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    private BooleanProperty componentFromHit(BlockState state, BlockHitResult hit) {
        Direction face = hit.getDirection();
        BlockPos pos = hit.getBlockPos();
        double lx = hit.getLocation().x - pos.getX();
        double ly = hit.getLocation().y - pos.getY();
        double lz = hit.getLocation().z - pos.getZ();

        return switch (face) {
            case NORTH, SOUTH -> {
                if (ly > 0.8 && state.getValue(HAS_TOP))  yield HAS_TOP;
                if (ly < 0.2 && state.getValue(HAS_FLAT)) yield HAS_FLAT;
                if (face == Direction.NORTH && state.getValue(SIDE_NORTH)) yield SIDE_NORTH;
                if (face == Direction.SOUTH && state.getValue(SIDE_SOUTH)) yield SIDE_SOUTH;
                yield state.getValue(MIDDLE_EW) ? MIDDLE_EW : null;
            }
            case EAST, WEST -> {
                if (ly > 0.8 && state.getValue(HAS_TOP))  yield HAS_TOP;
                if (ly < 0.2 && state.getValue(HAS_FLAT)) yield HAS_FLAT;
                if (face == Direction.EAST && state.getValue(SIDE_EAST)) yield SIDE_EAST;
                if (face == Direction.WEST && state.getValue(SIDE_WEST)) yield SIDE_WEST;
                yield state.getValue(MIDDLE_NS) ? MIDDLE_NS : null;
            }
            case UP -> {
                if (state.getValue(HAS_TOP)) yield HAS_TOP;
                if (lx < 0.3 || lx > 0.7) yield state.getValue(MIDDLE_NS) ? MIDDLE_NS : null;
                if (lz < 0.3 || lz > 0.7) yield state.getValue(MIDDLE_EW) ? MIDDLE_EW : null;
                yield state.getValue(HAS_FLAT) ? HAS_FLAT : null;
            }
            case DOWN -> {
                if (state.getValue(HAS_FLAT)) yield HAS_FLAT;
                yield state.getValue(HAS_TOP) ? HAS_TOP : null;
            }
        };
    }
}