package alabaster.hearthandharvest.common.entity.goal;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

public class PungentEffectGoal extends Goal {
    private static final int SCAN_INTERVAL = 20;

    private final Mob mob;
    private LivingEntity pungentSource;
    private final double farSpeed;
    private final double nearSpeed;
    private final double baseRadius;

    private int scanCooldown;

    public PungentEffectGoal(Mob mob, double farSpeed, double nearSpeed, double baseRadius) {
        this.mob = mob;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.baseRadius = baseRadius;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    // Computes the effective radius using the amplifier of the Pungent effect.
    private double getEffectiveRadius() {
        if (pungentSource != null && pungentSource.hasEffect(HHModEffects.PUNGENT)) {
            int amplifier = pungentSource.getEffect(HHModEffects.PUNGENT).getAmplifier();
            return baseRadius * (1.0 + 0.5 * amplifier);
        }
        return baseRadius;
    }

    @Override
    public boolean canUse() {
        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;

        // Search for any living entity (other than the mob itself) within the base radius that has the Pungent effect.
        List<LivingEntity> candidates = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                mob.getBoundingBox().inflate(baseRadius),
                entity -> entity != mob && entity.hasEffect(HHModEffects.PUNGENT)
        );
        if (!candidates.isEmpty()) {
            // Pick the nearest one.
            pungentSource = candidates.stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
            return pungentSource != null;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        // Use the effective radius calculated from the amplifier to determine if the source is still in range.
        double effectiveRadius = getEffectiveRadius();
        return pungentSource != null && mob.distanceToSqr(pungentSource) < effectiveRadius * effectiveRadius;
    }

    @Override
    public void start() {
        fleeFromSource();
    }

    @Override
    public void tick() {
        if (pungentSource != null) {
            fleeFromSource();
        }
    }

    private void fleeFromSource() {
        double effectiveRadius = getEffectiveRadius();
        // Calculate a flee vector – moving in the opposite direction from the pungent source.
        Vec3 direction = new Vec3(
                mob.getX() - pungentSource.getX(),
                0,
                mob.getZ() - pungentSource.getZ()
        ).normalize();
        // Determine the flee target location using the effective radius.
        double targetX = mob.getX() + direction.x * effectiveRadius;
        double targetZ = mob.getZ() + direction.z * effectiveRadius;
        PathNavigation navigation = mob.getNavigation();
        // Adjust speed depending on proximity.
        navigation.moveTo(targetX, mob.getY(), targetZ, mob.distanceToSqr(pungentSource) < 16 ? nearSpeed : farSpeed);
    }

    @Override
    public void stop() {
        pungentSource = null;
    }
}
