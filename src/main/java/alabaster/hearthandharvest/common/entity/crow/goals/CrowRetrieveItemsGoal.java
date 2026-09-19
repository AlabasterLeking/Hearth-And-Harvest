package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class CrowRetrieveItemsGoal extends Goal {
    private static final int SCAN_INTERVAL = 10;
    private static final int REPATH_INTERVAL = 5;
    private static final double SCAN_RADIUS = 8.0D;
    private static final double OWNER_PICKUP_RANGE_SQR = 3.0D * 3.0D;
    private static final double MAX_OWNER_DISTANCE_SQR = 32.0D * 32.0D;
    private static final double HANDOFF_RANGE_SQR = 2.0D * 2.0D;
    private static final double PERCH_RANGE_SQR = 1.2D * 1.2D;
    private static final int PERCH_PATIENCE = 100;
    private static final String DELIVERED_TAG = "hearthandharvest:crow_delivered";

    private final CrowEntity crow;
    private final double speed;
    private ItemEntity targetItem;
    private int scanCooldown;
    private int repathTimer;
    private int waitingTicks;

    public CrowRetrieveItemsGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Nullable
    private Player getOwnerPlayer() {
        if (!(crow.getOwner() instanceof Player owner)) return null;
        if (!owner.isAlive() || owner.isSpectator() || owner.level() != crow.level()) return null;
        if (crow.distanceToSqr(owner) > MAX_OWNER_DISTANCE_SQR) return null;
        return owner;
    }

    private boolean isAvailable() {
        return crow.isTame() && !crow.isOrderedToSit() && !crow.isPassenger();
    }

    @Override
    public boolean canUse() {
        if (!isAvailable()) return false;
        Player owner = getOwnerPlayer();
        if (owner == null) return false;
        if (!crow.getMainHandItem().isEmpty()) return true;
        if (!Config.CROW_FETCH_ITEMS.get()) return false;

        if (--scanCooldown > 0) return false;
        scanCooldown = SCAN_INTERVAL;

        targetItem = crow.level().getEntitiesOfClass(ItemEntity.class, crow.getBoundingBox().inflate(SCAN_RADIUS),
                        item -> isValidTarget(item, owner))
                .stream()
                .min(Comparator.comparingDouble(crow::distanceToSqr))
                .orElse(null);
        return targetItem != null;
    }

    private static boolean isValidTarget(@Nullable ItemEntity item, Player owner) {
        return item != null
                && item.isAlive()
                && !item.getItem().isEmpty()
                && !item.hasPickUpDelay()
                && !item.getPersistentData().getBoolean(DELIVERED_TAG)
                && !(item.getOwner() instanceof CrowEntity)
                && (Config.CROW_FETCH_THROWN_ITEMS.get() || item.getOwner() != owner)
                && item.distanceToSqr(owner) > OWNER_PICKUP_RANGE_SQR;
    }

    public static boolean isWorthFetching(ItemEntity item, Player owner) {
        return isValidTarget(item, owner) && !item.isInWater() && !item.isInLava() && owner.hasLineOfSight(item);
    }

    @Override
    public boolean canContinueToUse() {
        if (!isAvailable()) return false;
        Player owner = getOwnerPlayer();
        if (owner == null) return false;
        return !crow.getMainHandItem().isEmpty() || (targetItem != null && targetItem.isAlive() && !targetItem.getItem().isEmpty());
    }

    @Override
    public void start() {
        repathTimer = 0;
        waitingTicks = 0;
    }

    @Override
    public void stop() {
        targetItem = null;
        crow.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        Player owner = getOwnerPlayer();
        if (owner == null) return;

        if (crow.getMainHandItem().isEmpty()) {
            collectTarget();
        } else {
            deliverToOwner(owner);
        }
    }

    private void collectTarget() {
        if (targetItem == null || !targetItem.isAlive()) return;

        if (crow.distanceToSqr(targetItem) > 2.25D) {
            if (--repathTimer <= 0 || crow.getNavigation().isDone()) {
                repathTimer = REPATH_INTERVAL;
                crow.getNavigation().moveTo(targetItem, speed);
            }
            return;
        }

        ItemStack stack = targetItem.getItem().copy();
        crow.take(targetItem, stack.getCount());
        targetItem.discard();
        targetItem = null;
        crow.holdItem(stack);
        crow.playSound(SoundEvents.ITEM_PICKUP, 0.4F, 1.0F);
        repathTimer = 0;
    }

    private void deliverToOwner(Player owner) {
        boolean canPerch = owner instanceof ServerPlayer serverOwner && hasFreePerch(serverOwner);
        double distanceSqr = crow.distanceToSqr(owner);
        double arriveRangeSqr = canPerch ? PERCH_RANGE_SQR : HANDOFF_RANGE_SQR;

        if (distanceSqr > arriveRangeSqr) {
            if (distanceSqr <= HANDOFF_RANGE_SQR && ++waitingTicks > PERCH_PATIENCE) {
                handOff(owner, false);
                return;
            }
            if (--repathTimer <= 0 || crow.getNavigation().isDone()) {
                repathTimer = REPATH_INTERVAL;
                crow.getNavigation().moveTo(owner, speed);
            }
            return;
        }

        if (canPerch && !owner.onGround()) {
            if (++waitingTicks > PERCH_PATIENCE) handOff(owner, false);
            return;
        }

        handOff(owner, canPerch);
    }

    private void handOff(Player owner, boolean perch) {
        waitingTicks = 0;
        ItemStack delivery = crow.getMainHandItem().copy();
        crow.holdItem(ItemStack.EMPTY);
        crow.getNavigation().stop();

        if (perch && owner instanceof ServerPlayer serverOwner) {
            crow.setEntityOnShoulder(serverOwner);
        }

        owner.getInventory().add(delivery);
        if (!delivery.isEmpty()) {
            ItemEntity overflow = owner.drop(delivery, false, false);
            if (overflow != null) overflow.getPersistentData().putBoolean(DELIVERED_TAG, true);
        }

        owner.level().playSound(null, owner, SoundEvents.ITEM_PICKUP, owner.getSoundSource(), 0.3F, 1.4F);
        HHSimpleTrigger.trigger(HHModTriggers.CROW_DELIVERED_ITEM.get(), owner);
    }

    private static boolean hasFreePerch(ServerPlayer owner) {
        return !owner.getAbilities().flying
                && !owner.isInWater()
                && !owner.isInPowderSnow
                && !owner.isPassenger()
                && (owner.getShoulderEntityLeft().isEmpty() || owner.getShoulderEntityRight().isEmpty());
    }
}