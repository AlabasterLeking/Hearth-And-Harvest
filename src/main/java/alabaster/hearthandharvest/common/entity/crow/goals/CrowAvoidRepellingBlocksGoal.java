package alabaster.hearthandharvest.common.entity.crow.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CrowAvoidRepellingBlocksGoal extends Goal {
    private final CrowEntity crow;
    private final double speedModifier;

    private BlockPos repelSource;
    private Vec3 fleeTarget;
    private int scanCooldown = 0;

    private double fleeDist = -1;
    private double stopDist = -1;
    private int scanRadius = -1;

    private void initDistances() {
        if (fleeDist < 0) {
            double r = Config.CROW_SCARE_RADIUS.get();
            fleeDist = r;
            stopDist = r * 1.6;
            scanRadius = (int) (r * 1.3);
        }
    }

    public CrowAvoidRepellingBlocksGoal(CrowEntity crow, double speedModifier) {
        this.crow = crow;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (crow.isTame() || crow.isBusy()) return false;
        if (--scanCooldown > 0) return false;
        scanCooldown = 10 + crow.getRandom().nextInt(10);

        BlockPos found = findNearestRepellingBlock();
        if (found == null) return false;

        this.repelSource = found;
        Vec3 away = getFleePos(found);
        if (away == null) return false;

        this.fleeTarget = away;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (repelSource == null) return false;
        initDistances();
        double dist = crow.distanceToSqr(Vec3.atCenterOf(repelSource));
        return dist < (stopDist * stopDist);
    }

    @Override
    public void start() {
        if (fleeTarget != null) {
            crow.getNavigation().moveTo(fleeTarget.x, fleeTarget.y, fleeTarget.z, speedModifier);
        }
    }

    @Override
    public void stop() {
        repelSource = null;
        fleeTarget = null;
    }

    @Override
    public void tick() {
        if (repelSource != null && crow.getNavigation().isDone()) {
            Vec3 newTarget = getFleePos(repelSource);
            if (newTarget != null) {
                crow.getNavigation().moveTo(newTarget.x, newTarget.y, newTarget.z, speedModifier);
            }
        }
    }

    @Nullable
    private BlockPos findNearestRepellingBlock() {
        initDistances();
        BlockPos crowPos = crow.blockPosition();
        double closestDist = Double.MAX_VALUE;
        BlockPos closest = null;

        for (BlockPos pos : BlockPos.betweenClosed(
                crowPos.offset(-scanRadius, -scanRadius, -scanRadius),
                crowPos.offset(scanRadius, scanRadius, scanRadius))) {

            BlockState state = crow.level().getBlockState(pos);
            if (state.is(HHModTags.REPELS_CROWS)) {
                double dist = crow.distanceToSqr(Vec3.atCenterOf(pos));
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = pos.immutable();
                }
            }
        }

        if (closest == null) return null;
        if (Math.sqrt(closestDist) > fleeDist) return null;
        return closest;
    }

    @Nullable
    private Vec3 getFleePos(BlockPos blockPos) {
        Vec3 diff = new Vec3(
                crow.getX() - blockPos.getX(),
                0.0,
                crow.getZ() - blockPos.getZ()
        );

        if (diff.lengthSqr() < 1.0E-8) return null;

        Vec3 awayDir = diff.normalize().scale(8.0D);

        Vec3 target = new Vec3(
                crow.getX() + awayDir.x,
                crow.getY() + 3 + crow.getRandom().nextInt(3),
                crow.getZ() + awayDir.z
        );

        Vec3 pos = LandRandomPos.getPosTowards(crow, 12, 8, target);
        return pos != null ? pos : target;
    }
}