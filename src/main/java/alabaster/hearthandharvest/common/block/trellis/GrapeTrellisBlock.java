package alabaster.hearthandharvest.common.block.trellis;

import alabaster.hearthandharvest.common.block.IHarvestable;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Supplier;

public class GrapeTrellisBlock extends TrellisBlock implements IHarvestable {

    public static final EnumProperty<TrellisPlant> PLANT = EnumProperty.create("plant", TrellisPlant.class,
            TrellisPlant.NONE, TrellisPlant.RED_GRAPE, TrellisPlant.GREEN_GRAPE);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    private static final int MAX_GRAPE_HEIGHT = 5;

    private final Supplier<Block> regularVariant;

    public GrapeTrellisBlock(Properties props, Supplier<Block> regularVariant) {
        super(props, null);
        this.regularVariant = regularVariant;
        registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected void addPlantAndAgeProperties(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLANT, AGE);
    }

    @Override
    protected EnumProperty<TrellisPlant> getPlantProperty() {
        return PLANT;
    }

    @Override
    public TrellisPlant getPlant(BlockState state) {
        return state.getValue(PLANT);
    }

    @Override
    protected BlockState applyPlantToState(BlockState state, TrellisPlant plant) {
        return state.setValue(PLANT, plant).setValue(AGE, 0);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(PLANT).isGrape();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PLANT).isGrape()) {
            tickGrape(state, level, pos, random);
        }
    }

    private void tickGrape(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos base = findGrapeColumnBase(level, pos, state.getValue(PLANT));
        if (!level.getBlockState(base.below()).is(Tags.Blocks.VILLAGER_FARMLANDS)) return;

        int heightFromBase = pos.getY() - base.getY();
        int age = state.getValue(AGE);

        if (age < 4) {
            if (random.nextInt(5) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
            }
        } else if (random.nextInt(7) == 0) {
            tryGrapeSpread(state, level, pos, heightFromBase);
        }
    }

    private BlockPos findGrapeColumnBase(ServerLevel level, BlockPos pos, TrellisPlant plant) {
        BlockPos current = pos;
        while (true) {
            BlockPos below = current.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.getBlock() instanceof GrapeTrellisBlock && belowState.getValue(PLANT) == plant) {
                current = below;
            } else {
                break;
            }
        }
        return current;
    }

    private void tryGrapeSpread(BlockState state, ServerLevel level, BlockPos pos, int heightFromBase) {
        TrellisPlant plant = state.getValue(PLANT);

        if (heightFromBase < MAX_GRAPE_HEIGHT) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.getBlock() instanceof TrellisBlock tb && tb.getPlant(aboveState) == TrellisPlant.NONE
                    && !aboveState.getValue(TrellisBlock.GROWTH_BLOCKED)) {
                level.setBlock(above, copyStructure(aboveState, this).setValue(PLANT, plant).setValue(AGE, 0), Block.UPDATE_ALL);
                return;
            }
        }

        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction dir = dirs[level.random.nextInt(4)];
        BlockPos side = pos.relative(dir);
        BlockState sideState = level.getBlockState(side);
        if (sideState.getBlock() instanceof TrellisBlock tb && tb.getPlant(sideState) == TrellisPlant.NONE
                && !sideState.getValue(TrellisBlock.GROWTH_BLOCKED)) {
            level.setBlock(side, copyStructure(sideState, this).setValue(PLANT, plant).setValue(AGE, 0), Block.UPDATE_ALL);
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        TrellisPlant currentPlant = state.getValue(PLANT);

        if (stack.is(Items.SHEARS) && currentPlant.isGrape()) {
            if (!level.isClientSide()) {
                Item drop = currentPlant == TrellisPlant.RED_GRAPE
                        ? HHModItems.RED_GRAPES.get() : HHModItems.GREEN_GRAPES.get();
                popResource(level, pos, new ItemStack(drop));
                level.setBlock(pos, copyStructure(state, regularVariant.get()), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1f, 1f);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if ((stack.is(HHModItems.RED_GRAPES.get()) || stack.is(HHModItems.GREEN_GRAPES.get()))
                && currentPlant == TrellisPlant.NONE) {
            TrellisPlant plant = stack.is(HHModItems.RED_GRAPES.get())
                    ? TrellisPlant.RED_GRAPE : TrellisPlant.GREEN_GRAPE;
            return applyPlant(stack, state, level, pos, player, plant);
        }

        if (isHarvestReady(state)) {
            if (!level.isClientSide()) {
                harvestBlock(state, level, pos, player, ItemStack.EMPTY);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public boolean isHarvestReady(BlockState state) {
        return state.getValue(PLANT).isGrape() && state.getValue(AGE) == 4;
    }

    @Override
    public void harvestBlock(BlockState state, Level level, BlockPos pos, Player player, ItemStack tool) {
        TrellisPlant plant = state.getValue(PLANT);
        Item drop = plant == TrellisPlant.RED_GRAPE ? HHModItems.RED_GRAPES.get() : HHModItems.GREEN_GRAPES.get();
        popResource(level, pos, new ItemStack(drop, 1 + level.random.nextInt(2)));
        level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1f, 0.8f + level.random.nextFloat() * 0.4f);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(PLANT).isGrape();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        TrellisPlant plant = state.getValue(PLANT);
        if (!plant.isGrape()) return;
        int age = state.getValue(AGE);
        if (age < 4) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        } else {
            BlockPos base = findGrapeColumnBase(level, pos, plant);
            tryGrapeSpread(state, level, pos, pos.getY() - base.getY());
        }
    }
}