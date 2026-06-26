package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.block.entity.SprinklerBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class SprinklerBlock extends BaseEntityBlock {

    public static final MapCodec<SprinklerBlock> CODEC = simpleCodec(SprinklerBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 8, 14),
            Block.box(7, 8, 7, 9, 13, 9),
            Block.box(1, 9, 6, 15, 13, 10),
            Block.box(6, 9, 1, 10, 13, 15)
    );

    // Drip spawn at the nozzle tip opening (1px from block face, in open air).
    private static final double[][] NOZZLES = {
            { 0.5/16.0, 9.5/16.0, 8/16.0, -1, 0 }, // west
            { 15.5/16.0, 9.5/16.0, 8/16.0, 1, 0 }, // east
            { 8/16.0, 9.5/16.0, 0.5/16.0, 0, -1 }, // north
            { 8/16.0, 9.5/16.0, 15.5/16.0, 0, 1 }, // south
    };

    public SprinklerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof SprinklerBlockEntity be)) return 0;
        return (int) ((float) be.tank.getFluidAmount() / SprinklerBlockEntity.CAPACITY * 15);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof SprinklerBlockEntity be)) return;
        if (be.tank.isEmpty()) return;

        double bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
        for (double[] n : NOZZLES) {
            if (random.nextInt(4) == 0)
                level.addParticle(ParticleTypes.DRIPPING_WATER,
                        bx + n[0], by + n[1], bz + n[2], 0, 0, 0);
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return ItemInteractionResult.sidedSuccess(true);
        if (!(level.getBlockEntity(pos) instanceof SprinklerBlockEntity be))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection()))
            return ItemInteractionResult.sidedSuccess(false);

        if (stack.is(Items.BONE_MEAL) && be.addFertilizer(stack, player.getAbilities().instabuild) > 0)
            return ItemInteractionResult.sidedSuccess(false);

        ItemStack fertilizer = be.getFertilizer();
        Component fertComponent = fertilizer.isEmpty()
                ? Component.translatable("tooltip.hearthandharvest.sprinkler.fertilizer_none")
                : Component.translatable("tooltip.hearthandharvest.sprinkler.fertilizer_count", fertilizer.getCount());

        player.displayClientMessage(
                Component.translatable("tooltip.hearthandharvest.sprinkler.status",
                        be.tank.getFluidAmount(), SprinklerBlockEntity.CAPACITY, fertComponent),
                true
        );
        return ItemInteractionResult.sidedSuccess(false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, HHModBlockEntities.SPRINKLER.get(), SprinklerBlockEntity::serverTick);
    }
}