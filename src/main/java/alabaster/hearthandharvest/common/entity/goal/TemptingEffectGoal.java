package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.server.level.ServerPlayer;
import java.util.Comparator;
import java.util.EnumSet;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class TemptingEffectGoal extends Goal {
    private static final int SCAN_INTERVAL = 20;
    private static final int REPATH_INTERVAL = 10;
    private static final int CROWD_SIZE = 10;

    private final Mob mob;
    private final double approachSpeed;
    private final double closeSpeed;
    private final double baseRadius;
    private LivingEntity attractSource;
    private int scanCooldown;
    private int repathCooldown;

    public TemptingEffectGoal(Mob mob, double approachSpeed, double closeSpeed, double baseRadius) {
        this.mob = mob;
        this.approachSpeed = approachSpeed;
        this.closeSpeed = closeSpeed;
        this.baseRadius = baseRadius;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    private double getEffectiveRadius() {
        if (attractSource != null) {
            MobEffectInstance effect = attractSource.getEffect(HHModEffects.TEMPTING);
            if (effect != null) return baseRadius * (1.0 + 0.5 * effect.getAmplifier());
        }
        return baseRadius;
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof Animal)) return false;
        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;
        if (RestingMobs.isResting(mob)) return false;

        attractSource = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                mob.getBoundingBox().inflate(baseRadius),
                entity -> entity != mob && entity.isAlive() && entity.hasEffect(HHModEffects.TEMPTING)
        ).stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        return attractSource != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (attractSource == null || !attractSource.isAlive() || !attractSource.hasEffect(HHModEffects.TEMPTING)) return false;
        if (RestingMobs.isResting(mob)) return false;
        double radius = getEffectiveRadius();
        return mob.distanceToSqr(attractSource) < radius * radius;
    }

    @Override
    public void start() {
        repathCooldown = 0;
        checkCrowd();
        moveTowardSource();
    }

    private void checkCrowd() {
        if (!(attractSource instanceof ServerPlayer player)) return;
        double radius = getEffectiveRadius();
        int followers = player.level().getEntitiesOfClass(Animal.class, player.getBoundingBox().inflate(radius),
                animal -> animal.distanceToSqr(player) < radius * radius && !RestingMobs.isResting(animal)).size();
        if (followers >= CROWD_SIZE) HHModTriggers.TEMPTING_CROWD.get().trigger(player);
    }

    @Override
    public void tick() {
        if (attractSource == null) return;
        mob.getLookControl().setLookAt(attractSource, mob.getMaxHeadYRot() + 20, mob.getMaxHeadXRot());
        moveTowardSource();
    }

    private void moveTowardSource() {
        if (--repathCooldown > 0 && !mob.getNavigation().isDone()) return;
        repathCooldown = REPATH_INTERVAL;

        double speed = mob.distanceToSqr(attractSource) < 16 ? closeSpeed : approachSpeed;

        if (attractSource instanceof Player) {
            if (mob.distanceToSqr(attractSource) < 2.25) {
                mob.getNavigation().stop();
                return;
            }
            double dx = mob.getX() - attractSource.getX();
            double dz = mob.getZ() - attractSource.getZ();
            int offsetX = Math.abs(dx) < 1.0E-6 && Math.abs(dz) < 1.0E-6 ? 1 : (int) Math.signum(dx);
            int offsetZ = (int) Math.signum(dz);
            mob.getNavigation().moveTo(attractSource.getX() + offsetX, attractSource.getY(), attractSource.getZ() + offsetZ, speed);
            return;
        }

        mob.getNavigation().moveTo(attractSource, speed);
    }

    @Override
    public void stop() {
        attractSource = null;
        if (RestingMobs.isResting(mob)) mob.getNavigation().stop();
    }
}