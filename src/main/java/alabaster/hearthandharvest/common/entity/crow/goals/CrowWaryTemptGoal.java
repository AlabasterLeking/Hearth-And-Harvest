package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;

import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CrowWaryTemptGoal extends Goal {
    private static final double TEMPT_RANGE = 10.0D;
    private static final double GIVE_UP_RANGE = 14.0D;
    private static final int REPATH_INTERVAL = 10;

    private final CrowEntity crow;
    private final double speed;
    private final TargetingConditions temptConditions;
    private Player player;
    private int repathTimer;
    private int cooldown;

    public CrowWaryTemptGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        this.temptConditions = TargetingConditions.forNonCombat()
                .range(TEMPT_RANGE)
                .ignoreLineOfSight()
                .selector(entity -> entity instanceof Player target && !target.isSpectator() && CrowEntity.isHoldingTemptItem(target));
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isUnavailable() {
        return crow.isTame() || crow.isBusy() || crow.isAlarmed() || !crow.getMainHandItem().isEmpty();
    }

    @Override
    public boolean canUse() {
        if (!Config.CROW_TEMPTING.get()) return false;
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (isUnavailable()) return false;
        player = crow.level().getNearestPlayer(temptConditions, crow);
        return player != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !isUnavailable()
                && player != null
                && player.isAlive()
                && !player.isSpectator()
                && CrowEntity.isHoldingTemptItem(player)
                && crow.distanceToSqr(player) < GIVE_UP_RANGE * GIVE_UP_RANGE;
    }

    @Override
    public void start() {
        repathTimer = 0;
        crow.setBeingTempted(true);
    }

    @Override
    public void stop() {
        player = null;
        cooldown = 40;
        crow.setBeingTempted(false);
        crow.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        crow.getLookControl().setLookAt(player, crow.getMaxHeadYRot() + 20.0F, crow.getMaxHeadXRot());

        if (player.isSprinting()) {
            crow.addTemptTrust(-5);
        } else {
            crow.addTemptTrust(player.isShiftKeyDown() ? 2 : 1);
            if (crow.isFullyTrusting()) HHSimpleTrigger.trigger(HHModTriggers.CROW_FULL_TRUST.get(), player);
        }

        double comfort = crow.getComfortDistance();
        double distance = crow.distanceTo(player);

        if (distance > comfort - 0.75D && distance < comfort + 1.0D) {
            crow.getNavigation().stop();
            return;
        }

        if (--repathTimer > 0 && !crow.getNavigation().isDone()) return;
        repathTimer = REPATH_INTERVAL;

        Vec3 away = new Vec3(crow.getX() - player.getX(), 0.0D, crow.getZ() - player.getZ());
        if (away.lengthSqr() < 1.0E-4D) {
            away = new Vec3(crow.getRandom().nextDouble() - 0.5D, 0.0D, crow.getRandom().nextDouble() - 0.5D);
        }
        double standOff = distance > comfort ? comfort : comfort + 1.0D;
        Vec3 target = player.position().add(away.normalize().scale(standOff));
        crow.getNavigation().moveTo(target.x, player.getY(), target.z, speed);
    }
}