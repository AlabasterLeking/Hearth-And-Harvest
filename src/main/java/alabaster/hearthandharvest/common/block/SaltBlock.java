package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static alabaster.hearthandharvest.common.block.SaltDripBlock.*;

public class SaltBlock extends Block {

    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");

    private static final float GROWTH_CHANCE = 0.1f;

    private static @Nullable Map<Block, Block> nextByBlock;
    private static @Nullable Map<Block, Block> prevByBlock;

    public SaltBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WAXED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WAXED);
    }

    static Map<Block, Block> getNextByBlock() {
        if (nextByBlock == null) buildMaps();
        return nextByBlock;
    }

    static Map<Block, Block> getPrevByBlock() {
        if (prevByBlock == null) buildMaps();
        return prevByBlock;
    }

    private static void buildMaps() {
        nextByBlock = ImmutableMap.of(
                HHModBlocks.SALT_BLOCK.get(), HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(),
                HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(), HHModBlocks.WELL_LICKED_SALT_BLOCK.get(),
                HHModBlocks.WELL_LICKED_SALT_BLOCK.get(), HHModBlocks.HEAVILY_LICKED_SALT_BLOCK.get()
        );
        prevByBlock = ImmutableMap.of(
                HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(), HHModBlocks.SALT_BLOCK.get(),
                HHModBlocks.WELL_LICKED_SALT_BLOCK.get(), HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(),
                HHModBlocks.HEAVILY_LICKED_SALT_BLOCK.get(), HHModBlocks.WELL_LICKED_SALT_BLOCK.get()
        );
    }

    public static void registerDispenseBehavior() {
        DispenserBlock.registerBehavior(HHModItems.SALT.get(), new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                Direction facing = source.state().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.pos().relative(facing);
                BlockState targetState = source.level().getBlockState(targetPos);

                if (targetState.getBlock() instanceof SaltBlock && !targetState.getValue(WAXED)) {
                    Block prev = getPrevByBlock().get(targetState.getBlock());
                    if (prev != null) {
                        source.level().setBlockAndUpdate(targetPos,
                                prev.defaultBlockState().setValue(WAXED, false));
                        source.level().playSound(null, targetPos,
                                SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1.0f, 1.4f);
                        stack.shrink(1);
                    }
                    return stack;
                }

                return super.execute(source, stack);
            }
        });
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.HONEYCOMB) && !state.getValue(WAXED)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, state.setValue(WAXED, true));
                level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.getItem() instanceof AxeItem && state.getValue(WAXED)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, state.setValue(WAXED, false));
                level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.is(HHModItems.SALT.get().asItem())) {
            Block prev = getPrevByBlock().get(this);
            if (prev != null) {
                if (!level.isClientSide()) {
                    level.setBlockAndUpdate(pos, prev.defaultBlockState().setValue(WAXED, state.getValue(WAXED)));
                    level.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1.0f, 1.4f);
                    if (!player.isCreative()) stack.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(WAXED)) return InteractionResult.PASS;
        if (!player.getOffhandItem().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            level.playSound(null, pos, HHModSounds.LICK.get(), SoundSource.BLOCKS,
                    0.8f, 1.2f + level.random.nextFloat() * 0.3f);
            if (level.random.nextFloat() < Config.SALT_PLAYER_LICK_CHANCE.get().floatValue()) {
                degradeBlock(level, pos, state);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() <= GROWTH_CHANCE) {
            if (isWaterSource(level, pos.above())) tryGrow(level, pos, Direction.DOWN, random);
            if (isWaterSource(level, pos.below())) tryGrow(level, pos, Direction.UP, random);
        }
    }
    public static void degradeBlock(Level level, BlockPos pos, BlockState state) {
        Block next = getNextByBlock().get(state.getBlock());
        if (next != null) {
            level.setBlockAndUpdate(pos, next.defaultBlockState().setValue(WAXED, state.getValue(WAXED)));
        } else {
            level.removeBlock(pos, false);
            Block.popResource(level, pos, new ItemStack(HHModItems.SALT.get(), 1));
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, params));
        if (state.getValue(WAXED)) {
            drops.replaceAll(drop -> {
                if (drop.is(this.asItem())) {
                    ItemStack stamped = drop.copy();
                    stamped.set(DataComponents.BLOCK_STATE, new BlockItemStateProperties(Map.of("waxed", "true")));
                    return stamped;
                }
                return drop;
            });
        }
        return drops;
    }

    private static void tryGrow(ServerLevel level, BlockPos saltPos, Direction growDir, RandomSource random) {
        BlockPos growPos = saltPos.relative(growDir);
        BlockState growState = level.getBlockState(growPos);

        if (growState.isAir()) {
            placeDrip(level, growPos, growDir, SaltDripThickness.SMALL);
            return;
        }

        if (!isSaltDrip(growState)) return;
        if (growState.getValue(TIP_DIRECTION) != growDir) return;

        SaltDripThickness current = growState.getValue(THICKNESS);
        switch (current) {
            case SMALL -> level.setBlockAndUpdate(growPos, growState.setValue(THICKNESS, SaltDripThickness.MEDIUM));
            case MEDIUM -> level.setBlockAndUpdate(growPos, growState.setValue(THICKNESS, SaltDripThickness.LARGE));
            case LARGE -> tryGrowPoint(level, growPos, growDir);
            default -> {}
        }
    }

    private static void tryGrowPoint(ServerLevel level, BlockPos largePos, Direction growDir) {
        BlockPos pointPos = largePos.relative(growDir);
        if (!level.getBlockState(pointPos).isAir()) return;
        placeDrip(level, pointPos, growDir, SaltDripThickness.POINT);
        tryMerge(level, pointPos);
    }

    static void tryMerge(ServerLevel level, BlockPos pointPos) {
        boolean downLargeAbove = isLargeFacing(level.getBlockState(pointPos.above()), Direction.DOWN);
        boolean upLargeBelow = isLargeFacing(level.getBlockState(pointPos.below()), Direction.UP);
        if (downLargeAbove && upLargeBelow) {
            level.setBlockAndUpdate(pointPos,
                    level.getBlockState(pointPos).setValue(THICKNESS, SaltDripThickness.POINT_MERGE));
        }
    }

    private static void placeDrip(ServerLevel level, BlockPos pos, Direction tipDir, SaltDripThickness thickness) {
        level.setBlockAndUpdate(pos, HHModBlocks.SALT_DRIP.get().defaultBlockState()
                .setValue(TIP_DIRECTION, tipDir)
                .setValue(THICKNESS, thickness));
    }

    private static boolean isWaterSource(ServerLevel level, BlockPos pos) {
        var fluid = level.getFluidState(pos);
        return fluid.isSource() && fluid.getType() == Fluids.WATER;
    }

    private static boolean isLargeFacing(BlockState state, Direction tipDir) {
        return isSaltDrip(state)
                && state.getValue(TIP_DIRECTION) == tipDir
                && state.getValue(THICKNESS) == SaltDripThickness.LARGE;
    }
}