package alabaster.hearthandharvest.common.block;

import net.minecraft.world.InteractionHand;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltLampBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 14, 12);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SaltLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(POWERED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, POWERED);
    }

    protected InteractionResult hhUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        boolean nowLit = !state.getValue(LIT);
        level.setBlockAndUpdate(pos, state.setValue(LIT, nowLit));
        level.playSound(null, pos, HHModSounds.SALT_LAMP_SWITCH.get(), SoundSource.BLOCKS, 0.3f, nowLit ? 1.0f : 0.8f);
        return InteractionResult.CONSUME;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;
        boolean nowPowered = level.hasNeighborSignal(pos);
        if (nowPowered == state.getValue(POWERED)) return;

        BlockState newState = state.setValue(POWERED, nowPowered);
        if (nowPowered) newState = newState.cycle(LIT);
        boolean nowLit = newState.getValue(LIT);
        level.setBlockAndUpdate(pos, newState);
        level.playSound(null, pos, HHModSounds.SALT_LAMP_SWITCH.get(), SoundSource.BLOCKS, 0.3f, nowLit ? 1.0f : 0.8f);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return hand == InteractionHand.MAIN_HAND ? hhUseWithoutItem(state, level, pos, player, hit) : InteractionResult.PASS;
    }
}
