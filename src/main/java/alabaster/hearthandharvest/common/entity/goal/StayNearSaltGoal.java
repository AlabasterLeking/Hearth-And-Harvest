package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.block.SaltBlock;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class StayNearSaltGoal extends Goal {

    private static final int SCAN_INTERVAL = 5;

    private final PathfinderMob mob;
    private @Nullable BlockPos saltPos;
    private int scanCooldown = SCAN_INTERVAL;
    private long nextLickTime = -1;
    private boolean licking;

    public StayNearSaltGoal(PathfinderMob mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;

        long now = mob.level().getGameTime();
        if (nextLickTime < 0) {
            int interval = Config.SALT_LICK_INTERVAL.get();
            nextLickTime = now + interval / 2 + mob.getRandom().nextInt(interval / 2);
        }

        if (saltPos != null) {
            BlockState s = mob.level().getBlockState(saltPos);
            if (!(s.getBlock() instanceof SaltBlock) || s.getValue(SaltBlock.WAXED)) saltPos = null;
        }
        if (saltPos == null) saltPos = findNearbySaltBlock();
        if (saltPos == null) return false;

        if (now >= nextLickTime) {
            licking = true;
            return true;
        }

        licking = false;
        int stayRange = Config.SALT_ANIMAL_RADIUS.get();
        return mob.blockPosition().distSqr(saltPos) > (double) (stayRange * stayRange);
    }

    @Override
    public boolean canContinueToUse() {
        if (saltPos == null) return false;
        BlockState s = mob.level().getBlockState(saltPos);
        if (!(s.getBlock() instanceof SaltBlock) || s.getValue(SaltBlock.WAXED)) {
            saltPos = null;
            return false;
        }
        int stayRange = Config.SALT_ANIMAL_RADIUS.get();
        double threshold = licking ? 4.0 : (double) (stayRange * stayRange);
        return mob.blockPosition().distSqr(saltPos) > threshold;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(saltPos.getX() + 0.5, saltPos.getY(), saltPos.getZ() + 0.5, 1.0);
    }

    @Override
    public void stop() {
        long now = mob.level().getGameTime();
        if (licking && saltPos != null && mob.blockPosition().distSqr(saltPos) <= 4.0) {
            BlockState state = mob.level().getBlockState(saltPos);
            if (state.getBlock() instanceof SaltBlock && !state.getValue(SaltBlock.WAXED)) {
                mob.level().playSound(null, saltPos, HHModSounds.LICK.get(), SoundSource.NEUTRAL,
                        0.8f, 0.6f + mob.getRandom().nextFloat() * 0.6f);
                SaltBlock.degradeBlock(mob.level(), saltPos, state);
            }
            nextLickTime = now + Config.SALT_LICK_INTERVAL.get();
        } else if (licking) {
            nextLickTime = now + Config.SALT_LICK_INTERVAL.get() / 4;
        }
        licking = false;
        mob.getNavigation().stop();
    }

    @Nullable
    private BlockPos findNearbySaltBlock() {
        BlockPos origin = mob.blockPosition();
        BlockPos nearest = null;
        double bestDist = Double.MAX_VALUE;
        int attractRange = Config.SALT_ANIMAL_RADIUS.get() + 2;
        for (BlockPos candidate : BlockPos.betweenClosed(
                origin.offset(-attractRange, -3, -attractRange),
                origin.offset(attractRange, 3, attractRange))) {
            BlockState s = mob.level().getBlockState(candidate);
            if (s.getBlock() instanceof SaltBlock && !s.getValue(SaltBlock.WAXED)) {
                double d = origin.distSqr(candidate);
                if (d < bestDist) {
                    bestDist = d;
                    nearest = candidate.immutable();
                }
            }
        }
        return nearest;
    }
}