package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.TreeTapperBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModParticleTypes;
import alabaster.hearthandharvest.common.tag.HHModTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class TreeTapperBlock extends BaseEntityBlock {

        public static final MapCodec<TreeTapperBlock> CODEC = simpleCodec(TreeTapperBlock::new);

        @Override
        protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

        public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

        public TreeTapperBlock(Properties properties) {
                super(properties);
                this.registerDefaultState(this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(WATERLOGGED, false));
        }

        @Override
        public RenderShape getRenderShape(BlockState state) {
                return RenderShape.MODEL;
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new TreeTapperBlockEntity(pos, state);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                return switch (state.getValue(FACING)) {
                        case EAST  -> Block.box(6, 0, 3, 16, 16, 13);
                        case SOUTH -> Block.box(3, 0, 6, 13, 16, 16);
                        case WEST  -> Block.box(0, 0, 3, 10, 16, 13);
                        default    -> Block.box(3, 0, 0, 13, 16, 10);
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
        public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
                Direction dir = state.getValue(FACING);
                return level.getBlockState(pos.relative(dir)).isFaceSturdy(level, pos.relative(dir), dir);
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
                BlockState state = this.defaultBlockState();
                LevelReader levelReader = context.getLevel();
                BlockPos blockPos = context.getClickedPos();
                FluidState fluidState = context.getLevel().getFluidState(blockPos);
                for (Direction direction : context.getNearestLookingDirections()) {
                        if (direction.getAxis().isHorizontal()) {
                                state = state.setValue(FACING, direction);
                                if (state.canSurvive(levelReader, blockPos))
                                        return state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                        }
                }
                return null;
        }

        @Override
        public boolean hasAnalogOutputSignal(BlockState state) {
                return true;
        }

        @Override
        public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
                if (level.getBlockEntity(pos) instanceof TreeTapperBlockEntity tapper)
                        return HHFluidHandling.comparatorOutput(tapper.tank);
                return 0;
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                builder.add(FACING, WATERLOGGED);
        }

        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
                if (level.isClientSide()) return null;
                return createTickerHelper(type, HHModBlockEntities.TREE_TAPPER.get(), TreeTapperBlockEntity::serverTick);
        }

        @Override
        public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
                if (level.isClientSide()) return ItemInteractionResult.sidedSuccess(true);
                if (!(level.getBlockEntity(pos) instanceof TreeTapperBlockEntity))
                        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                if (!FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection()))
                        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                return ItemInteractionResult.sidedSuccess(false);
        }

        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                if (!(level.getBlockEntity(pos) instanceof TreeTapperBlockEntity tapper)) return;
                if (tapper.isFull()) return;
                Direction dir = state.getValue(FACING);
                if (!level.getBlockState(pos.relative(dir)).is(HHModTags.TAPPABLE)) return;
                if (random.nextFloat() >= 0.1f) return;
                double x = pos.getX() + 0.5, y = pos.getY() + 0.75, z = pos.getZ() + 0.5;
                double offset = 0.1;
                switch (dir) {
                        case NORTH -> z -= offset;
                        case SOUTH -> z += offset;
                        case WEST  -> x -= offset;
                        case EAST  -> x += offset;
                }
                level.addParticle(HHModParticleTypes.DRIPPING_SAP.get(), x, y, z, 0, 0, 0);
        }

}