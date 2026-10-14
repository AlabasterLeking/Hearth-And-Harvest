package alabaster.hearthandharvest.common.entity.crow.goals;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class CrowFleeEntityGoal extends Goal {
    private static final double SNATCHING_FLEE_RADIUS = 2.5D;

    private final CrowEntity crow;
    private final double speedModifier;

    private double fleeDist = -1;

    private LivingEntity threat;
    private Vec3 fleeTarget;
    private int scanCooldown = 0;

    public CrowFleeEntityGoal(CrowEntity crow, double speedModifier) {
        this.crow = crow;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    private void initDistances() {
        if (fleeDist < 0) {
            double r = Config.CROW_SCARE_RADIUS.get();
            fleeDist = r;
        }
    }

    @Override
    public boolean canUse() {
        if (crow.isTame() || crow.isBusy()) return false;
        if (--scanCooldown > 0 && !crow.consumeFreshAlarm()) return false;
        scanCooldown = 10 + crow.getRandom().nextInt(10);

        initDistances();
        LivingEntity nearestThreat = getNearestThreat();
        if (nearestThreat == null)
            return false;

        Vec3 away = getFleePos(nearestThreat);
        if (away == null)
            return false;

        this.threat = nearestThreat;
        this.fleeTarget = away;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (threat == null || crow.isTame())
            return false;

        initDistances();
        double radius = crow.getThreatRadius(threat, fleeDist);
        if (radius <= 0.0D)
            return false;

        double stop = threat == crow.getAlarmSource() ? radius * 1.25D : radius * 1.6D;
        return crow.distanceToSqr(threat) < stop * stop;
    }

    @Override
    public void start() {
        if (fleeTarget != null) {
            crow.getNavigation().moveTo(fleeTarget.x, fleeTarget.y, fleeTarget.z, speedModifier);
        }
    }

    @Override
    public void stop() {
        this.threat = null;
        this.fleeTarget = null;
    }

    @Override
    public void tick() {
        if (threat != null && crow.getNavigation().isDone()) {
            Vec3 newTarget = getFleePos(threat);
            if (newTarget != null) {
                crow.getNavigation().moveTo(newTarget.x, newTarget.y, newTarget.z, speedModifier);
            }
        }
    }

    @Nullable
    private LivingEntity getNearestThreat() {
        initDistances();
        double baseRadius = crow.isSnatching() ? Math.min(fleeDist, SNATCHING_FLEE_RADIUS) : fleeDist;
        double scanRadius = crow.isAlarmed() ? Math.max(baseRadius, CrowEntity.ALARM_RANGE) : baseRadius;

        List<LivingEntity> candidates = crow.level().getEntitiesOfClass(
                LivingEntity.class,
                crow.getBoundingBox().inflate(scanRadius),
                entity -> {
                    double radius = crow.getThreatRadius(entity, baseRadius);
                    return radius > 0.0D && crow.distanceToSqr(entity) <= radius * radius;
                }
        );

        if (candidates.isEmpty())
            return null;

        LivingEntity alarmSource = crow.getAlarmSource();
        if (alarmSource != null && candidates.contains(alarmSource))
            return alarmSource;

        return crow.level().getNearestEntity(
                candidates,
                TargetingConditions.forNonCombat(),
                crow,
                crow.getX(),
                crow.getY(),
                crow.getZ()
        );
    }

    @Nullable
    private Vec3 getFleePos(LivingEntity threat) {
        Vec3 diff = new Vec3(
                crow.getX() - threat.getX(),
                0.0,
                crow.getZ() - threat.getZ()
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