package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class CrowEatDroppedFoodGoal extends Goal {
    private static final int SCAN_INTERVAL = 20;
    private static final double SCAN_RADIUS = 10.0D;
    private static final double GIVE_UP_DISTANCE_SQR = 20.0D * 20.0D;
    private static final int PECK_TICKS = 24;
    private static final int THREAT_CHECK_INTERVAL = 10;
    private static final int REPATH_INTERVAL = 5;

    private final CrowEntity crow;
    private final double speed;
    private ItemEntity targetItem;
    private int peckTimer;
    private int repathTimer;
    private int threatCheckTimer;
    private int cooldown;
    private boolean ate;

    public CrowEatDroppedFoodGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isUnavailable() {
        return crow.isTame() || crow.isOrderedToSit() || crow.isPassenger() || crow.isAlarmed() || !crow.getMainHandItem().isEmpty();
    }

    @Override
    public boolean canUse() {
        if (!Config.CROW_EAT_DROPPED_FOOD.get()) return false;
        if (--cooldown > 0) return false;
        cooldown = SCAN_INTERVAL;
        if (isUnavailable()) return false;

        targetItem = crow.level().getEntitiesOfClass(ItemEntity.class, crow.getBoundingBox().inflate(SCAN_RADIUS), this::isFood)
                .stream()
                .sorted(Comparator.comparingDouble(crow::distanceToSqr))
                .filter(item -> !isThreatened(item) && !isGuarded(item))
                .findFirst()
                .orElse(null);
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !isUnavailable() && isFood(targetItem) && crow.distanceToSqr(targetItem) < GIVE_UP_DISTANCE_SQR;
    }

    @Override
    public void start() {
        peckTimer = -1;
        repathTimer = 0;
        threatCheckTimer = THREAT_CHECK_INTERVAL;
        ate = false;
    }

    @Override
    public void stop() {
        targetItem = null;
        cooldown = ate ? 60 + crow.getRandom().nextInt(60) : SCAN_INTERVAL;
        ate = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!isFood(targetItem)) return;

        if (--threatCheckTimer <= 0) {
            threatCheckTimer = THREAT_CHECK_INTERVAL;
            if (isThreatened(targetItem)) {
                targetItem = null;
                return;
            }
        }

        crow.getLookControl().setLookAt(targetItem.getX(), targetItem.getY(), targetItem.getZ());

        if (crow.position().distanceToSqr(targetItem.position()) > 1.0D) {
            peckTimer = -1;
            if (--repathTimer <= 0 || crow.getNavigation().isDone()) {
                repathTimer = REPATH_INTERVAL;
                crow.getNavigation().moveTo(targetItem, speed);
            }
            return;
        }

        crow.getNavigation().stop();
        if (peckTimer < 0) peckTimer = PECK_TICKS;

        if (peckTimer % 8 == 0) {
            spawnCrumbs(targetItem.getItem(), 3);
        }

        if (--peckTimer <= 0) {
            eat();
        }
    }

    private void eat() {
        Player thrower = targetItem.getOwner() instanceof Player player ? player : null;
        ItemStack remaining = targetItem.getItem().copy();
        ItemStack eaten = remaining.split(1);
        if (remaining.isEmpty()) {
            targetItem.discard();
        } else {
            targetItem.setItem(remaining);
        }

        crow.heal(1.0F);
        spawnCrumbs(eaten, 8);
        if (!crow.isSilent()) {
            crow.playSound(HHModSounds.CROW_EAT.get(), 1.0F, CrowEntity.getPitch(crow.getRandom()));
        }
        crow.tryTameFromFood(thrower);

        ate = true;
        targetItem = null;
    }

    private void spawnCrumbs(ItemStack stack, int count) {
        if (!(crow.level() instanceof ServerLevel server)) return;
        server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
                targetItem != null ? targetItem.getX() : crow.getX(),
                (targetItem != null ? targetItem.getY() : crow.getY()) + 0.1D,
                targetItem != null ? targetItem.getZ() : crow.getZ(),
                count, 0.1D, 0.05D, 0.1D, 0.05D);
    }

    private boolean isGuarded(ItemEntity item) {
        int radius = Config.CROW_SCARE_RADIUS.get();
        if (radius <= 0) return false;
        BlockPos center = item.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            if (crow.level().getBlockState(pos).is(HHModTags.REPELS_CROWS)) return true;
        }
        return false;
    }

    private boolean isThreatened(ItemEntity item) {
        return crow.isSpotThreatened(item.position(), Config.CROW_SCARE_RADIUS.get());
    }

    private boolean isFood(@Nullable ItemEntity item) {
        return item != null && item.isAlive() && !item.getItem().isEmpty() && item.getItem().is(HHModTags.CROW_FOOD);
    }
}