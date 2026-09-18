package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import java.util.Comparator;
import java.util.EnumSet;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class PungentEffectGoal extends Goal {
    private static final int SCAN_INTERVAL = 20;
    private static final int REPATH_INTERVAL = 10;

    private final Mob mob;
    private final double farSpeed;
    private final double nearSpeed;
    private final double baseRadius;
    private LivingEntity pungentSource;
    private int scanCooldown;
    private int repathCooldown;

    public PungentEffectGoal(Mob mob, double farSpeed, double nearSpeed, double baseRadius) {
        this.mob = mob;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.baseRadius = baseRadius;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    private double getEffectiveRadius() {
        if (pungentSource != null) {
            MobEffectInstance effect = pungentSource.getEffect(HHModEffects.PUNGENT);
            if (effect != null) return baseRadius * (1.0 + 0.5 * effect.getAmplifier());
        }
        return baseRadius;
    }

    @Override
    public boolean canUse() {
        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;
        if (RestingMobs.isResting(mob)) return false;

        pungentSource = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                mob.getBoundingBox().inflate(baseRadius),
                entity -> entity != mob && entity.isAlive() && entity.hasEffect(HHModEffects.PUNGENT)
        ).stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        return pungentSource != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (pungentSource == null || !pungentSource.isAlive() || !pungentSource.hasEffect(HHModEffects.PUNGENT)) return false;
        if (RestingMobs.isResting(mob)) return false;
        double radius = getEffectiveRadius();
        return mob.distanceToSqr(pungentSource) < radius * radius;
    }

    @Override
    public void start() {
        repathCooldown = 0;
        HHSimpleTrigger.trigger(HHModTriggers.PUNGENT_SCARED.get(), pungentSource);
        fleeFromSource();
    }

    @Override
    public void tick() {
        if (pungentSource != null) fleeFromSource();
    }

    private void fleeFromSource() {
        if (--repathCooldown > 0 && !mob.getNavigation().isDone()) return;
        repathCooldown = REPATH_INTERVAL;

        double dx = mob.getX() - pungentSource.getX();
        double dz = mob.getZ() - pungentSource.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 1.0E-4) {
            double angle = mob.getRandom().nextDouble() * Math.PI * 2.0;
            dx = Math.cos(angle);
            dz = Math.sin(angle);
            length = 1.0;
        }

        double radius = getEffectiveRadius();
        double targetX = mob.getX() + dx / length * radius;
        double targetZ = mob.getZ() + dz / length * radius;
        double speed = mob.distanceToSqr(pungentSource) < 16 ? nearSpeed : farSpeed;
        mob.getNavigation().moveTo(targetX, mob.getY(), targetZ, speed);
    }

    @Override
    public void stop() {
        pungentSource = null;
        if (RestingMobs.isResting(mob)) mob.getNavigation().stop();
    }
}