package alabaster.hearthandharvest.common.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraftforge.event.ForgeEventFactory;
import vectorwing.farmersdelight.common.tag.ModTags;

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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.is(ModTags.Items.KNIVES)) {
            return cutSlice(level, pos, state, player);
        }
        return InteractionResult.PASS;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
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
        ItemStack sliceItem = this.getPieSliceItem();
        ItemEntity sliceEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, sliceItem,
                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
        level.addFreshEntity(sliceEntity);

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            ItemStack sliceCopy = sliceStack.copy();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();

            playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack);
            // Fire an event for food-tracking mods like Spice of Life, but ignore the result.
            ForgeEventFactory.onItemUseFinish(playerIn, sliceCopy, 0, ItemStack.EMPTY);
            if (this.getPieSliceItem().getItem().isEdible() && sliceFood != null) {
                for (Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
                    if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                        playerIn.addEffect(new MobEffectInstance(pair.getFirst()));
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
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
        SHAPE_BY_BITE = new VoxelShape[]{
                Block.box(1.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(3.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(5.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(7.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(9.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(11.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(13.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F)
        };
    }
}
