package alabaster.hearthandharvest.common.block;

import net.minecraft.world.effect.MobEffectInstance;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class SliceableCakeBlock extends Block {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
    protected static final VoxelShape[] SHAPE_BY_BITE;

    public final Supplier<Item> pieSlice;

    public SliceableCakeBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties);
        this.pieSlice = pieSlice;
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[(Integer)state.getValue(BITES)];
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack(this.pieSlice.get());
    }

    public int getMaxBites() {
        return 7;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    protected InteractionResult hhUseItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (heldStack.is(ModTags.Items.KNIVES)) {
            return cutSlice(level, pos, state, player);
        }
        return null;
    }

    protected InteractionResult hhUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (consumeBite(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return consumeBite(level, pos, state, player);
    }

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int bites = state.getValue(BITES);
        if (bites < getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, playerIn);

            if (sliceFood != null) {
                playerIn.getFoodData().eat(sliceFood.getNutrition(), sliceFood.getSaturationModifier());
                for (Pair<MobEffectInstance, Float> effect : sliceFood.getEffects()) {
                    if (!level.isClientSide && effect.getFirst() != null && level.random.nextFloat() < effect.getSecond()) {
                        playerIn.addEffect(new MobEffectInstance(effect.getFirst()));
                    }
                }
            }

            int bites = state.getValue(BITES);
            if (bites < getMaxBites() - 1) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                level.removeBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return getMaxBites() - blockState.getValue(BITES);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    static {
        SHAPE_BY_BITE = new VoxelShape[]{Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)3.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)5.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)7.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)9.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)11.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F), Block.box((double)13.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)8.0F, (double)15.0F)};
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult itemResult = hhUseItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (itemResult != null) return itemResult;
        return hand == InteractionHand.MAIN_HAND ? hhUseWithoutItem(state, level, pos, player, hit) : InteractionResult.PASS;
    }
}
