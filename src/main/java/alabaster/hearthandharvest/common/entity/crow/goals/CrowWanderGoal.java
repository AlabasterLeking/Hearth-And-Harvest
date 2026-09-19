package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CrowWanderGoal extends WaterAvoidingRandomFlyingGoal {
    private static final int PERCH_SAMPLES = 16;
    private static final int PERCH_HORIZONTAL_RANGE = 12;
    private static final int PERCH_VERTICAL_RANGE = 8;
    private static final int PERCHED_INTERVAL = 400;
    private static final int GROUND_INTERVAL = 120;

    public CrowWanderGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof TamableAnimal tamable && tamable.isOrderedToSit()) return false;
        this.setInterval(isPerched() ? PERCHED_INTERVAL : GROUND_INTERVAL);
        return super.canUse();
    }

    private boolean isPerched() {
        if (!this.mob.onGround()) return false;
        return isPerchBlock(this.mob.level().getBlockState(this.mob.blockPosition().below()));
    }

    private static boolean isPerchBlock(BlockState state) {
        return state.getBlock() instanceof LeavesBlock
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.FENCES)
                || state.is(BlockTags.WALLS);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.mob.isInWater()) {
            Vec3 land = LandRandomPos.getPos(this.mob, 15, 15);
            if (land != null) return land;
        }

        if (this.mob.onGround() && !isPerched() && this.mob.getRandom().nextFloat() < 0.4F) {
            Vec3 stroll = LandRandomPos.getPos(this.mob, 4, 2);
            if (stroll != null) return stroll;
        }

        Vec3 perch = findPerch();
        return perch != null ? perch : super.getPosition();
    }

    @Nullable
    private Vec3 findPerch() {
        Level level = this.mob.level();
        RandomSource random = this.mob.getRandom();
        BlockPos origin = this.mob.blockPosition();
        BlockPos best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < PERCH_SAMPLES; i++) {
            BlockPos column = origin.offset(
                    random.nextInt(PERCH_HORIZONTAL_RANGE * 2 + 1) - PERCH_HORIZONTAL_RANGE,
                    0,
                    random.nextInt(PERCH_HORIZONTAL_RANGE * 2 + 1) - PERCH_HORIZONTAL_RANGE);
            if (!level.hasChunkAt(column)) continue;

            BlockPos top = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, column);
            int rise = top.getY() - origin.getY();
            if (Math.abs(rise) > PERCH_VERTICAL_RANGE) continue;
            if (top.closerThan(origin, 3.0D)) continue;
            if (!level.getFluidState(top.below()).isEmpty()) continue;

            BlockState below = level.getBlockState(top.below());
            if (below.is(HHModTags.REPELS_CROWS)) continue;

            double score = rise + (isPerchBlock(below) ? 6.0D : 0.0D) + random.nextDouble() * 4.0D;
            if (score > bestScore) {
                bestScore = score;
                best = top;
            }
        }

        return best != null ? Vec3.atBottomCenterOf(best) : null;
    }
}