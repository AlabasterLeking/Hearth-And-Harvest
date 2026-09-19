package alabaster.hearthandharvest.common.entity.crow.goals;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.block.CornStalkBlock;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CrowEatCropsGoal extends Goal {
    private final CrowEntity crow;
    private final double speedModifier;
    private BlockPos targetCropPos;
    private int peckTime;

    private static final int SCAN_RADIUS = 8;
    private static final int PECK_DURATION = 40; // ticks (2 seconds)

    public CrowEatCropsGoal(CrowEntity crow, double speedModifier) {
        this.crow = crow;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (crow.isTame() || crow.isBaby()) return false;
        if (!Config.CROW_EAT_CROPS.get() || !EventHooks.canEntityGrief(crow.level(), crow)) return false;
        if (crow.getRandom().nextInt(50) != 0) return false; // run occasionally

        BlockPos pos = findNearbyEdibleCrop();
        if (pos != null) {
            this.targetCropPos = pos;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetCropPos == null) return false;
        if (!Config.CROW_EAT_CROPS.get() || !EventHooks.canEntityGrief(crow.level(), crow)) return false;

        BlockState state = crow.level().getBlockState(targetCropPos);

        if (state.hasProperty(CornStalkBlock.CROW_PROOF) &&
                state.getValue(CornStalkBlock.CROW_PROOF)) {
            return false;
        }

        return state.is(HHModTags.CROW_EDIBLE_CROPS) && targetCropPos.closerToCenterThan(crow.position(), SCAN_RADIUS + 2);
    }

    @Override
    public void start() {
        peckTime = 0;
        if (targetCropPos != null) {
            Vec3 center = Vec3.atCenterOf(targetCropPos);
            crow.getNavigation().moveTo(center.x, center.y + 1.0D, center.z, speedModifier);
        }
    }

    @Override
    public void tick() {
        if (targetCropPos == null) return;

        double dist = crow.distanceToSqr(Vec3.atCenterOf(targetCropPos));
        if (dist < 2.0D) {
            crow.getLookControl().setLookAt(
                    targetCropPos.getX() + 0.5D,
                    targetCropPos.getY() + 0.5D,
                    targetCropPos.getZ() + 0.5D
            );
            peckTime++;

            // Play breaking particle
            if (peckTime == 20 && !crow.isSilent()) {
                BlockState current = crow.level().getBlockState(targetCropPos);
                crow.level().levelEvent(2001, targetCropPos, Block.getId(current));
            }

            // After full peck duration, reset crop growth
            if (peckTime == 20 && crow.level() instanceof ServerLevel serverLevel) {
                BlockState state = crow.level().getBlockState(targetCropPos);
                if (state.is(HHModTags.CROW_EDIBLE_CROPS)) {
                    Property<?> ageProp = null;
                    for (Property<?> prop : state.getProperties()) {
                        if (prop.getName().equalsIgnoreCase("age") && prop instanceof IntegerProperty) {
                            ageProp = prop;
                            break;
                        }
                    }
                    if (ageProp instanceof IntegerProperty intProp) {
                        int age = state.getValue(intProp);
                        if (age > 0 && crow.level() instanceof ServerLevel) {
                            BlockState reset = state.setValue(intProp, 0);
                            serverLevel.levelEvent(2001, targetCropPos, Block.getId(reset));
                            serverLevel.setBlock(targetCropPos, reset, 3);
                        }
                    }
                }
            }

            // Finish goal a bit after peck animation
            if (peckTime > PECK_DURATION) {
                targetCropPos = null;
            }

        } else if (crow.getNavigation().isDone() && targetCropPos != null) {
            Vec3 pos = Vec3.atCenterOf(targetCropPos);
            crow.getNavigation().moveTo(pos.x, pos.y + 1.0D, pos.z, speedModifier);
        }
    }

    @Override
    public void stop() {
        targetCropPos = null;
        peckTime = 0;
    }

    @Nullable
    private BlockPos findNearbyEdibleCrop() {
        BlockPos mobPos = crow.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int dx = -SCAN_RADIUS; dx <= SCAN_RADIUS; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -SCAN_RADIUS; dz <= SCAN_RADIUS; dz++) {
                    mutable.set(mobPos.getX() + dx, mobPos.getY() + dy, mobPos.getZ() + dz);
                    BlockState state = crow.level().getBlockState(mutable);

                    // Skip if crop is marked crow-proof
                    if (state.hasProperty(CornStalkBlock.CROW_PROOF) && state.getValue(CornStalkBlock.CROW_PROOF)) {
                        continue;
                    }

                    // Must match edible crop tag
                    if (!state.is(HHModTags.CROW_EDIBLE_CROPS)) continue;

                    // Find an integer property named "age" or "AGE"
                    Property<?> ageProperty = null;
                    for (Property<?> prop : state.getProperties()) {
                        if (prop.getName().equalsIgnoreCase("age")) {
                            ageProperty = prop;
                            break;
                        }
                    }

                    // If it has an age property and is not fully regressed
                    if (ageProperty instanceof IntegerProperty intProp) {
                        int age = state.getValue(intProp);
                        if (age > 0) {
                            return mutable.immutable();
                        }
                    }
                }
            }
        }
        return null;
    }
}