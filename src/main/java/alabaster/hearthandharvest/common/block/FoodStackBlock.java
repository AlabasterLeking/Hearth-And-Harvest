package alabaster.hearthandharvest.common.block;

import net.minecraft.world.effect.MobEffectInstance;
import com.mojang.datafixers.util.Pair;
import alabaster.hearthandharvest.common.item.VintageHelper;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FoodStackBlock extends Block {
    public static final int MAX_STACK = 4;
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX_STACK);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty SYRUP = BooleanProperty.create("syrup");
    public static final int SYRUP_NUTRITION_BONUS = 2;

    private static final VoxelShape[] SHAPES = {
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D)
    };

    public FoodStackBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COUNT, 1).setValue(FACING, Direction.NORTH).setValue(SYRUP, false));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COUNT, FACING, SYRUP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(COUNT) - 1];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(COUNT, Math.min(MAX_STACK, existing.getValue(COUNT) + 1));
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(COUNT) < MAX_STACK) {
            return true;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.isSolid() && !(below.getBlock() instanceof FoodStackBlock);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    protected InteractionResult hhUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(this.asItem())) {
            return state.getValue(COUNT) < MAX_STACK ? InteractionResult.PASS : InteractionResult.CONSUME;
        }

        if (stack.is(HHModItems.SYRUP_BOTTLE.get())) {
            if (state.getValue(SYRUP)) {
                return InteractionResult.CONSUME;
            }
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(SYRUP, true), Block.UPDATE_ALL);
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                HHSimpleTrigger.trigger(HHModTriggers.SYRUPED_STACK, player);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return null;
    }

    protected InteractionResult hhUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack single = new ItemStack(this.asItem());

        if (player.isSecondaryUseActive()) {
            if (!level.isClientSide) {
                player.getInventory().placeItemBackInInventory(single);
                removeOne(state, level, pos);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.3F, 1.2F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        FoodProperties food = single.getFoodProperties(player);
        if (food == null || !player.canEat(food.canAlwaysEat())) {
            return InteractionResult.PASS;
        }

        if (state.getValue(SYRUP)) {
            food = withSyrup(food);
        }

        if (!level.isClientSide) {
            VintageHelper.applyFood(player, level, single, food);
            removeOne(state, level, pos);
            level.gameEvent(player, GameEvent.EAT, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static FoodProperties withSyrup(FoodProperties food) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(food.getNutrition() + SYRUP_NUTRITION_BONUS)
                .saturationMod(food.getSaturationModifier());
        if (food.isMeat()) builder.meat();
        if (food.canAlwaysEat()) builder.alwaysEat();
        if (food.isFastFood()) builder.fast();
        for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
            builder.effect(() -> new MobEffectInstance(effect.getFirst()), effect.getSecond());
        }
        return builder.build();
    }

    private static void removeOne(BlockState state, Level level, BlockPos pos) {
        int count = state.getValue(COUNT);
        if (count > 1) {
            level.setBlock(pos, state.setValue(COUNT, count - 1), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult itemResult = hhUseItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (itemResult != null) return itemResult;
        return hand == InteractionHand.MAIN_HAND ? hhUseWithoutItem(state, level, pos, player, hit) : InteractionResult.PASS;
    }
}
