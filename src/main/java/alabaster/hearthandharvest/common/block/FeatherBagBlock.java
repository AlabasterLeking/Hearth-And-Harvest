package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.event.FeatherParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FeatherBagBlock extends Block {
    private static final float FALL_DAMAGE_MULTIPLIER = 0.2F;
    private static final float MIN_BURST_FALL = 0.5F;
    private static final int MIN_FEATHERS = 4;
    private static final int MAX_FEATHERS = 24;

    public FeatherBagBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance * FALL_DAMAGE_MULTIPLIER);
        if (fallDistance < MIN_BURST_FALL) return;

        int count = Mth.clamp(MIN_FEATHERS + (int) (fallDistance * 2.0F), MIN_FEATHERS, MAX_FEATHERS);
        FeatherParticles.burst(level, entity.getX(), pos.getY() + 1.0D, entity.getZ(), count, 0.35D);
    }
}