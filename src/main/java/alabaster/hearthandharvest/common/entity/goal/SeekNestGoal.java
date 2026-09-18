package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.common.block.entity.NestBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class SeekNestGoal extends Goal {
    private static final int SEARCH_RANGE = 12;
    private static final int VERTICAL_RANGE = 4;
    private static final int SEARCH_INTERVAL = 40;
    private static final int REPATH_INTERVAL = 20;
    private static final int START_SEEKING_AT = 600;
    private static final int MAX_HOLD_TICKS = 600;
    private static final int TRAVEL_LIMIT = 300;
    private static final int PATH_CANDIDATES = 3;
    private static final double OCCUPIED_DISTANCE = 1.2D;

    private final Chicken chicken;
    private final double speed;

    private BlockPos nestPos;
    private int searchCooldown;
    private int repathTimer;
    private int travelTicks;
    private int holdTicks;

    public SeekNestGoal(Chicken chicken, double speed) {
        this.chicken = chicken;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean isLayingSoon() {
        return !chicken.isBaby() && !chicken.isInLove() && chicken.eggTime <= START_SEEKING_AT;
    }

    @Override
    public boolean canUse() {
        if (!isLayingSoon()) return false;
        if (--searchCooldown > 0) return false;
        searchCooldown = SEARCH_INTERVAL;

        nestPos = findNest();
        return nestPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return nestPos != null
            && isLayingSoon()
            && travelTicks < TRAVEL_LIMIT
            && isUsableNest(nestPos);
    }

    @Override
    public void start() {
        claim(nestPos);
        repathTimer = 0;
        travelTicks = 0;
        holdTicks = 0;
        moveToNest();
    }

    @Override
    public void stop() {
        claim(null);
        nestPos = null;
        searchCooldown = SEARCH_INTERVAL;
        holdTicks = 0;
        chicken.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        chicken.getLookControl().setLookAt(nestPos.getX() + 0.5D, nestPos.getY() + 0.5D, nestPos.getZ() + 0.5D);

        if (isOnNest()) {
            travelTicks = 0;
            chicken.getNavigation().stop();
            return;
        }

        travelTicks++;
        if (--repathTimer <= 0 || chicken.getNavigation().isDone()) {
            repathTimer = REPATH_INTERVAL;
            moveToNest();
        }

        if (chicken.eggTime <= 1 && holdTicks < MAX_HOLD_TICKS) {
            chicken.eggTime = 2;
            holdTicks++;
        }
    }

    private boolean isOnNest() {
        return chicken.blockPosition().equals(nestPos);
    }

    private void moveToNest() {
        chicken.getNavigation().moveTo(nestPos.getX() + 0.5D, nestPos.getY(), nestPos.getZ() + 0.5D, speed);
    }

    private void claim(@Nullable BlockPos pos) {
        chicken.setData(HHModAttachments.CLAIMED_NEST.get(), pos == null ? 0L : pos.asLong());
    }

    @Nullable
    private BlockPos findNest() {
        Level level = chicken.level();
        BlockPos origin = chicken.blockPosition();
        List<BlockPos> candidates = new ArrayList<>();
        List<Chicken> others = nearbyChickens(SEARCH_RANGE * 2);

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SEARCH_RANGE, -VERTICAL_RANGE, -SEARCH_RANGE),
                origin.offset(SEARCH_RANGE, VERTICAL_RANGE, SEARCH_RANGE))) {
            if (!level.getBlockState(pos).is(HHModTags.NESTS)) continue;
            BlockPos nest = pos.immutable();
            if (!isUsableNest(nest) || isTakenByOther(nest, others)) continue;
            candidates.add(nest);
        }

        candidates.sort(Comparator.comparingDouble(pos -> chicken.distanceToSqr(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D)));
        for (int i = 0; i < Math.min(PATH_CANDIDATES, candidates.size()); i++) {
            BlockPos candidate = candidates.get(i);
            if (chicken.getNavigation().createPath(candidate, 1) != null) return candidate;
        }
        return null;
    }

    private boolean isUsableNest(BlockPos pos) {
        if (!chicken.level().getBlockState(pos).is(HHModTags.NESTS)) return false;
        return !(chicken.level().getBlockEntity(pos) instanceof NestBlockEntity nest) || nest.hasRoom();
    }

    private boolean isTakenByOther(BlockPos pos, List<Chicken> others) {
        long packed = pos.asLong();
        AABB box = new AABB(pos).inflate(OCCUPIED_DISTANCE);
        for (Chicken other : others) {
            if (other.getData(HHModAttachments.CLAIMED_NEST.get()) == packed) return true;
            if (!other.isBaby() && box.contains(other.position())) return true;
        }
        return false;
    }

    private List<Chicken> nearbyChickens(double range) {
        return chicken.level().getEntitiesOfClass(Chicken.class, chicken.getBoundingBox().inflate(range), other -> other != chicken);
    }
}