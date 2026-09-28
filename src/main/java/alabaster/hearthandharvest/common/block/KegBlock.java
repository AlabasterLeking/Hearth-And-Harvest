package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nullable;

public class KegBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape BODY = Shapes.or(
            Block.box(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 16.0D),
            Block.box(0.0D, 0.0D, 2.0D, 16.0D, 1.0D, 6.0D),
            Block.box(0.0D, 0.0D, 10.0D, 16.0D, 1.0D, 14.0D),
            Block.box(0.0D, 1.0D, 2.0D, 1.0D, 5.0D, 6.0D),
            Block.box(0.0D, 1.0D, 10.0D, 1.0D, 5.0D, 14.0D),
            Block.box(15.0D, 1.0D, 2.0D, 16.0D, 5.0D, 6.0D),
            Block.box(15.0D, 1.0D, 10.0D, 16.0D, 5.0D, 14.0D));

    private static final VoxelShape NORTH_SHAPE = BODY;
    private static final VoxelShape EAST_SHAPE = rotate(BODY, 1);
    private static final VoxelShape SOUTH_SHAPE = rotate(BODY, 2);
    private static final VoxelShape WEST_SHAPE = rotate(BODY, 3);

    private static VoxelShape rotate(VoxelShape shape, int quarterTurns) {
        VoxelShape result = shape;
        for (int turn = 0; turn < quarterTurns; ++turn) {
            VoxelShape turned = Shapes.empty();
            for (AABB box : result.toAabbs()) {
                turned = Shapes.or(turned, Shapes.box(1.0D - box.maxZ, box.minY, box.minX, 1.0D - box.minZ, box.maxY, box.maxX));
            }
            result = turned;
        }
        return result;
    }

    public static final MapCodec<KegBlock> CODEC = simpleCodec(KegBlock::new);

    public KegBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KegBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HHModBlockEntities.KEG.get(), KegBlockEntity::fermentingTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof KegBlockEntity keg) {
            player.openMenu(keg, buf -> buf.writeBlockPos(pos));
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof KegBlockEntity keg) || !keg.isFermenting()) return;

        if (random.nextInt(180) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    HHModSounds.KEG_FERMENTING.get(), SoundSource.BLOCKS, 0.4F, 0.6F + random.nextFloat() * 0.2F, false);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, moving);
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof KegBlockEntity keg) {
            ItemStack stack = new ItemStack(this);
            keg.saveToItem(stack, params.getLevel().registryAccess());
            return List.of(stack);
        }
        return super.getDrops(state, params);
    }
}