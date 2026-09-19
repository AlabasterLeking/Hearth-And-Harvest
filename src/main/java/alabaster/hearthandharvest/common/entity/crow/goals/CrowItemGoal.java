package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public abstract class CrowItemGoal extends Goal {
    protected final CrowEntity crow;
    protected final double speed;

    private final int scanInterval;
    private final double scanRadius;
    private final int repathInterval;

    @Nullable
    protected ItemEntity targetItem;
    private int scanCooldown;
    private int repathTimer;

    protected CrowItemGoal(CrowEntity crow, double speed, int scanInterval, double scanRadius, int repathInterval, EnumSet<Flag> flags) {
        this.crow = crow;
        this.speed = speed;
        this.scanInterval = scanInterval;
        this.scanRadius = scanRadius;
        this.repathInterval = repathInterval;
        this.setFlags(flags);
    }

    protected abstract boolean isValidTarget(@Nullable ItemEntity item);

    protected boolean isWorthApproaching(ItemEntity item) {
        return true;
    }

    protected boolean scanReady() {
        if (--scanCooldown > 0) return false;
        scanCooldown = scanInterval;
        return true;
    }

    protected void setScanCooldown(int ticks) {
        scanCooldown = ticks;
    }

    @Nullable
    protected ItemEntity findNearestTarget() {
        return crow.level().getEntitiesOfClass(ItemEntity.class, crow.getBoundingBox().inflate(scanRadius), this::isValidTarget)
                .stream()
                .sorted(Comparator.comparingDouble(crow::distanceToSqr))
                .filter(this::isWorthApproaching)
                .findFirst()
                .orElse(null);
    }

    protected boolean approach(Entity target, double arriveDistanceSqr) {
        if (crow.distanceToSqr(target) <= arriveDistanceSqr) {
            crow.getNavigation().stop();
            repathTimer = 0;
            return true;
        }
        repath(target, speed);
        return false;
    }

    protected void repath(Entity target, double moveSpeed) {
        if (--repathTimer > 0 && !crow.getNavigation().isDone()) return;
        repathTimer = repathInterval;
        crow.getNavigation().moveTo(target, moveSpeed);
    }

    protected void repath(double x, double y, double z, double moveSpeed, int interval) {
        if (--repathTimer > 0 && !crow.getNavigation().isDone()) return;
        repathTimer = interval;
        crow.getNavigation().moveTo(x, y, z, moveSpeed);
    }

    protected void resetRepath() {
        repathTimer = 0;
    }

    protected void lookAtTarget(double yOffset) {
        if (targetItem != null) {
            crow.getLookControl().setLookAt(targetItem.getX(), targetItem.getY() + yOffset, targetItem.getZ());
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        targetItem = null;
        crow.getNavigation().stop();
    }
}