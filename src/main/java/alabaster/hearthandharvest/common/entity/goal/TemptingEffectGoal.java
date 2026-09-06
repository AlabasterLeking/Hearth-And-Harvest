package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class TemptingEffectGoal extends Goal {
    private static final int SCAN_INTERVAL = 20;

    private int scanCooldown;
    private final Mob mob; // This should be an animal
    private LivingEntity attractSource;
    private final double approachSpeed;
    private final double closeSpeed;
    private final double baseRadius;

    public TemptingEffectGoal(Mob mob, double approachSpeed, double closeSpeed, double baseRadius) {
        this.mob = mob;
        this.approachSpeed = approachSpeed;
        this.closeSpeed = closeSpeed;
        this.baseRadius = baseRadius;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    // Helper method to compute the effective radius based on the amplifier of the Tempting effect.
    private double getEffectiveRadius() {
        if (attractSource != null && attractSource.hasEffect(HHModEffects.TEMPTING)) {
            int amplifier = attractSource.getEffect(HHModEffects.TEMPTING).getAmplifier();
            return baseRadius * (1.0 + 0.5 * amplifier);
        }
        return baseRadius;
    }

    @Override
    public boolean canUse() {
        // This goal is intended only for animals.
        if (!(mob instanceof Animal)) {
            return false;
        }

        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;

        // Search for any living entity (other than the mob itself) within the base radius that has the Tempting effect.
        List<LivingEntity> candidates = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                mob.getBoundingBox().inflate(baseRadius),
                entity -> entity != mob && entity.hasEffect(HHModEffects.TEMPTING)
        );
        if (!candidates.isEmpty()) {
            // Pick the nearest one as the attract source.
            attractSource = candidates.stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
            return attractSource != null;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        double effectiveRadius = getEffectiveRadius();
        return attractSource != null && mob.distanceToSqr(attractSource) < effectiveRadius * effectiveRadius;
    }

    @Override
    public void start() {
        moveTowardSource();
    }

    @Override
    public void tick() {
        if (attractSource != null) {
            moveTowardSource();
        }
    }

    private void moveTowardSource() {
        double targetX, targetZ;
        // If the attract source is a player, choose a target within a 3x3 centered on the source
        // such that the mob ends up one block away from the source.
        if (attractSource instanceof Player) {
            // Calculate the relative difference between the mob and the source.
            double dx = mob.getX() - attractSource.getX();
            double dz = mob.getZ() - attractSource.getZ();
            int offsetX, offsetZ;
            // Use signum to get -1, 0, or 1. If both differences are nearly 0, fallback to (1, 0).
            if (Math.abs(dx) < 1e-6 && Math.abs(dz) < 1e-6) {
                offsetX = 1;
                offsetZ = 0;
            } else {
                offsetX = (int) Math.signum(dx);
                offsetZ = (int) Math.signum(dz);
            }
            targetX = attractSource.getX() + offsetX;
            targetZ = attractSource.getZ() + offsetZ;
        } else {
            // For non-player sources, use the effective radius behavior.
            double effectiveRadius = getEffectiveRadius();
            Vec3 direction = new Vec3(
                    attractSource.getX() - mob.getX(),
                    0,
                    attractSource.getZ() - mob.getZ()
            ).normalize();
            targetX = mob.getX() + direction.x * effectiveRadius;
            targetZ = mob.getZ() + direction.z * effectiveRadius;
        }

        PathNavigation navigation = mob.getNavigation();
        // Adjust speed depending on how close the mob is to the attract source.
        navigation.moveTo(targetX, mob.getY(), targetZ, mob.distanceToSqr(attractSource) < 16 ? closeSpeed : approachSpeed);
    }

    @Override
    public void stop() {
        attractSource = null;
    }
}

