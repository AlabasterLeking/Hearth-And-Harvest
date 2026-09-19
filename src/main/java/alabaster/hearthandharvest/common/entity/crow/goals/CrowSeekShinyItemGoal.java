package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.block.entity.NestBlockEntity;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CrowSeekShinyItemGoal extends CrowItemGoal {
    private static final int SCAN_INTERVAL = 10;
    private static final double SCAN_RADIUS = 10.0D;
    private static final int REPATH_INTERVAL = 5;
    private static final double PICKUP_RANGE_SQR = 2.25D;
    private static final double MAX_CHASE_DISTANCE_SQR = 16.0D * 16.0D;
    private static final int NEST_SEARCH_RADIUS = 16;
    private static final int NEST_ARRIVAL_TICKS = 20;
    private static final int CARRY_TICKS = 100;

    private BlockPos nestTarget;
    private boolean nestSearched;
    private Vec3 escapeTarget;
    private int carryTimer;
    private int nestArrivalTimer;
    private boolean completed;

    public CrowSeekShinyItemGoal(CrowEntity crow, double speed) {
        super(crow, speed, SCAN_INTERVAL, SCAN_RADIUS, REPATH_INTERVAL, EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isHoldingShiny() {
        ItemStack held = crow.getMainHandItem();
        return !held.isEmpty() && held.is(HHModTags.CROW_SHINY_ITEMS);
    }

    private boolean isUnavailable() {
        return crow.isTame() || crow.isBusy();
    }

    @Override
    public boolean canUse() {
        if (isUnavailable()) return false;
        if (isHoldingShiny()) return true;
        if (!Config.CROW_STEAL_SHINY_ITEMS.get()) return false;
        if (!scanReady()) return false;

        targetItem = findNearestTarget();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (isUnavailable()) return false;
        if (isHoldingShiny()) return true;
        return isValidTarget(targetItem) && crow.distanceToSqr(targetItem) < MAX_CHASE_DISTANCE_SQR;
    }

    @Override
    public void start() {
        crow.setSnatching(true);
        nestTarget = null;
        nestSearched = false;
        escapeTarget = null;
        carryTimer = CARRY_TICKS;
        nestArrivalTimer = -1;
        completed = false;
        resetRepath();
    }

    @Override
    public void stop() {
        super.stop();
        crow.setSnatching(false);
        nestTarget = null;
        nestSearched = false;
        escapeTarget = null;
        nestArrivalTimer = -1;
        setScanCooldown(completed ? 100 + crow.getRandom().nextInt(80) : 20);
        completed = false;
    }

    @Override
    public void tick() {
        if (crow.getMainHandItem().isEmpty()) {
            chaseTarget();
        } else if (isHoldingShiny()) {
            carryLoot();
        }
    }

    private void chaseTarget() {
        if (!isValidTarget(targetItem)) return;

        lookAtTarget(0.3D);
        if (!approach(targetItem, PICKUP_RANGE_SQR)) return;

        Player thrower = targetItem.getOwner() instanceof Player player ? player : null;
        ItemStack remaining = targetItem.getItem().copy();
        ItemStack single = remaining.split(1);
        crow.take(targetItem, 1);
        if (remaining.isEmpty()) {
            targetItem.discard();
        } else {
            targetItem.setItem(remaining);
        }

        crow.holdItem(single);
        crow.playSound(SoundEvents.ITEM_PICKUP, 0.4F, 1.4F);

        if (thrower != null) {
            HHSimpleTrigger.trigger(HHModTriggers.CROW_STOLE_ITEM.get(), thrower);
            crow.tryTameFromPickup(thrower);
        }

        targetItem = null;
        carryTimer = CARRY_TICKS;
    }

    private void carryLoot() {
        if (!nestSearched) {
            nestSearched = true;
            nestTarget = findNearestNest();
        }

        if (nestTarget != null && !crow.level().getBlockState(nestTarget).is(HHModTags.NESTS)) {
            nestSearched = false;
            nestTarget = null;
            return;
        }

        if (nestTarget != null) {
            flyToNest();
        } else {
            escapeAndDrop();
        }
    }

    private void flyToNest() {
        double x = nestTarget.getX() + 0.5;
        double y = nestTarget.getY() + 1.1;
        double z = nestTarget.getZ() + 0.5;

        if (crow.distanceToSqr(x, y, z) >= 1.5D) {
            nestArrivalTimer = -1;
            repath(x, y, z, speed * 1.2, REPATH_INTERVAL);
            return;
        }

        crow.getNavigation().stop();
        if (nestArrivalTimer < 0) {
            nestArrivalTimer = NEST_ARRIVAL_TICKS;
            return;
        }
        if (--nestArrivalTimer > 0) return;

        depositIntoNest(nestTarget);
    }

    private void escapeAndDrop() {
        if (escapeTarget == null) {
            escapeTarget = findEscapeTarget();
        }

        boolean arrived = crow.distanceToSqr(escapeTarget) < 4.0D;
        if (!arrived && --carryTimer > 0) {
            repath(escapeTarget.x, escapeTarget.y, escapeTarget.z, speed * 1.2, REPATH_INTERVAL * 2);
            return;
        }

        ItemStack held = crow.getMainHandItem().copy();
        crow.holdItem(ItemStack.EMPTY);
        ItemEntity dropped = crow.spawnAtLocation(held);
        if (dropped != null) dropped.setThrower(crow);
        completed = true;
    }

    private Vec3 findEscapeTarget() {
        Player nearest = crow.level().getNearestPlayer(crow, 16.0D);
        Vec3 pos = nearest != null
                ? LandRandomPos.getPosAway(crow, 16, 7, nearest.position())
                : LandRandomPos.getPos(crow, 12, 7);
        return pos != null ? pos : crow.position();
    }

    @Override
    protected boolean isWorthApproaching(ItemEntity item) {
        return !isNearNest(item.blockPosition());
    }

    @Override
    protected boolean isValidTarget(@Nullable ItemEntity item) {
        return item != null
                && item.isAlive()
                && !item.getItem().isEmpty()
                && item.getItem().is(HHModTags.CROW_SHINY_ITEMS)
                && !(item.getOwner() instanceof CrowEntity);
    }

    @Nullable
    private BlockPos findNearestNest() {
        return BlockPos.findClosestMatch(crow.blockPosition(), NEST_SEARCH_RADIUS, 3,
                pos -> crow.level().getBlockState(pos).is(HHModTags.NESTS)).orElse(null);
    }

    private boolean isNearNest(BlockPos pos) {
        for (BlockPos check : BlockPos.betweenClosed(pos.offset(-1, -2, -1), pos.offset(1, 2, 1))) {
            if (crow.level().getBlockState(check).is(HHModTags.NESTS)) return true;
        }
        return false;
    }

    private void depositIntoNest(BlockPos pos) {
        ItemStack stack = crow.getMainHandItem().copy();
        crow.holdItem(ItemStack.EMPTY);
        completed = true;
        nestTarget = null;
        nestArrivalTimer = -1;

        if (!(crow.level() instanceof ServerLevel server)) return;
        if (server.getBlockEntity(pos) instanceof NestBlockEntity nest) {
            stack = nest.insert(stack);
            nest.markCrowStash();
            if (stack.isEmpty()) return;
        }

        double angle = crow.getRandom().nextDouble() * Math.PI * 2.0;
        ItemEntity itemEntity = new ItemEntity(server,
                pos.getX() + 0.5 + Math.cos(angle) * 0.9,
                pos.getY() + 0.25,
                pos.getZ() + 0.5 + Math.sin(angle) * 0.9,
                stack);
        itemEntity.setDefaultPickUpDelay();
        itemEntity.setDeltaMovement(0, 0, 0);
        itemEntity.setThrower(crow);
        server.addFreshEntity(itemEntity);
    }
}