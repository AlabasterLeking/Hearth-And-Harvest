package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.registry.HHModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ManureBlock extends Block {
    public ManureBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) != 0) return;

        double bx = pos.getX();
        double by = pos.getY();
        double bz = pos.getZ();

        spawnFly(level, bx, by, bz, random);
    }

    private static void spawnFly(Level level, double bx, double by, double bz, RandomSource random) {
        double px, py, pz;
        if (random.nextFloat() < 0.65f) {
            px = bx + random.nextDouble();
            py = by + 0.9 + random.nextDouble() * 0.5;
            pz = bz + random.nextDouble();
        } else {
            double along = random.nextDouble();
            double up = 0.4 + random.nextDouble() * 0.6;
            switch (random.nextInt(4)) {
                case 0 -> { px = bx - 0.05; py = by + up; pz = bz + along; }
                case 1 -> { px = bx + 1.05; py = by + up; pz = bz + along; }
                case 2 -> { px = bx + along; py = by + up; pz = bz - 0.05; }
                default -> { px = bx + along; py = by + up; pz = bz + 1.05; }
            }
        }
        level.addParticle(HHModParticleTypes.FLIES.get(), px, py, pz, 0, 0, 0);
    }
}