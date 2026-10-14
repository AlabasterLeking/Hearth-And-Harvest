package alabaster.hearthandharvest.common.entity.crow;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class CrowMoveControl extends MoveControl {
    private static final double CRUISE_SCALE = 0.36D;
    private static final double WALK_SCALE = 0.5D;
    private static final double WALK_RANGE_SQR = 4.0D * 4.0D;
    private static final double MIN_ARRIVE_SPEED = 0.05D;
    private static final double HORIZONTAL_ACCEL = 0.2D;
    private static final double VERTICAL_ACCEL = 0.22D;
    private static final double MAX_CLIMB = 0.22D;
    private static final double MAX_DIVE = 0.4D;
    private static final double GLIDE_SINK = 0.1D;
    private static final double GLIDE_MIN_SPEED = 0.12D;
    private static final double LANDING_SINK = 0.15D;
    private static final float TURN_FAST = 12.0F;
    private static final float TURN_SLOW = 45.0F;
    private static final float TURN_WALK = 30.0F;
    private static final double AIR_DRAG = 0.91D;
    private static final double VERTICAL_DRAG = 0.98D;
    private static final int MIN_GLIDE_TICKS = 12;
    private static final int MIN_FLAP_TICKS = 10;

    private final CrowEntity crow;
    private int wingLockTicks;
    private int cruiseTicks;
    private boolean cruiseGliding;
    private int takeoffSoundCooldown;

    public CrowMoveControl(CrowEntity crow) {
        super(crow);
        this.crow = crow;
    }

    @Override
    public void tick() {
        if (takeoffSoundCooldown > 0) takeoffSoundCooldown--;
        if (wingLockTicks > 0) wingLockTicks--;

        if (crow.isPassenger()) {
            crow.setNoGravity(false);
            setWingState(false, true);
            return;
        }

        if (operation == Operation.MOVE_TO) {
            operation = Operation.WAIT;
            double dx = wantedX - crow.getX();
            double dy = wantedY - crow.getY();
            double dz = wantedZ - crow.getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);

            if (horizontal * horizontal + dy * dy < 2.5E-7D) {
                crow.setZza(0.0F);
                crow.setYya(0.0F);
                return;
            }

            if (shouldWalk(dy)) {
                walk(dx, dy, dz);
            } else {
                fly(dx, dy, dz, horizontal);
            }
            return;
        }

        if (operation == Operation.WAIT) {
            crow.setZza(0.0F);
            crow.setYya(0.0F);
            crow.setXxa(0.0F);
            drift();
            return;
        }

        super.tick();
    }

    private boolean shouldWalk(double nodeDy) {
        if (!crow.onGround() || speedModifier >= 1.5D) return false;
        if (nodeDy > 1.2D || nodeDy < -1.5D) return false;

        BlockPos destination = crow.getNavigation().getTargetPos();
        if (destination == null) return false;

        double tx = destination.getX() + 0.5D - crow.getX();
        double ty = destination.getY() - crow.getY();
        double tz = destination.getZ() + 0.5D - crow.getZ();
        return tx * tx + tz * tz < WALK_RANGE_SQR && ty < 1.5D && ty > -2.0D;
    }

    private void walk(double dx, double dy, double dz) {
        crow.setNoGravity(false);
        setWingState(false, true);
        float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
        crow.setYRot(rotlerp(crow.getYRot(), targetYaw, TURN_WALK));
        crow.setSpeed((float) (speedModifier * crow.getAttributeValue(Attributes.MOVEMENT_SPEED) * WALK_SCALE));
        crow.setYya(0.0F);
        if (dy > crow.maxUpStep() + 0.01D) {
            crow.getJumpControl().jump();
        }
    }

    private void fly(double dx, double dy, double dz, double horizontal) {
        crow.setNoGravity(true);
        crow.setZza(0.0F);
        crow.setYya(0.0F);
        crow.setXxa(0.0F);

        Vec3 current = crow.getDeltaMovement();
        double currentHorizontal = current.horizontalDistance();

        if (horizontal > 1.0E-4D) {
            float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            float maxTurn = (float) Mth.clampedLerp(TURN_SLOW, TURN_FAST, currentHorizontal / 0.3D);
            crow.setYRot(rotlerp(crow.getYRot(), targetYaw, maxTurn));
        }

        float yawRad = crow.getYRot() * Mth.DEG_TO_RAD;
        double facingX = -Mth.sin(yawRad);
        double facingZ = Mth.cos(yawRad);
        double dirX = facingX;
        double dirZ = facingZ;
        double alignmentFactor = 0.0D;

        if (horizontal > 1.0E-4D) {
            double precision = Mth.clamp(1.0D - (horizontal - 0.5D) / 1.5D, 0.0D, 1.0D);
            dirX = Mth.lerp(precision, facingX, dx / horizontal);
            dirZ = Mth.lerp(precision, facingZ, dz / horizontal);
            double alignment = (facingX * dx + facingZ * dz) / horizontal;
            alignmentFactor = Mth.clamp((alignment + 0.3D) / 1.3D, 0.15D, 1.0D);
        }

        double cruise = crow.getAttributeValue(Attributes.FLYING_SPEED) * speedModifier * CRUISE_SCALE;
        double targetHorizontal = Math.min(cruise, Math.max(MIN_ARRIVE_SPEED, horizontal * 0.25D)) * alignmentFactor;
        double targetVertical = Mth.clamp(dy * 0.3D, -MAX_DIVE, MAX_CLIMB);

        if (targetVertical > 0.0D) {
            targetHorizontal *= 1.0D - targetVertical * 1.4D;
        } else {
            targetHorizontal *= 1.0D - targetVertical * 0.6D;
        }

        boolean takingOff = crow.onGround();
        if (takingOff) {
            targetVertical = Math.max(targetVertical, dy > 0.0D ? 0.32D : 0.18D);
            playTakeoffSound();
        }

        double newX = current.x + (dirX * targetHorizontal - current.x) * HORIZONTAL_ACCEL;
        double newZ = current.z + (dirZ * targetHorizontal - current.z) * HORIZONTAL_ACCEL;
        double newY = takingOff ? targetVertical : current.y + (targetVertical - current.y) * VERTICAL_ACCEL;
        applyVelocity(newX, newY, newZ);

        if (takingOff) {
            setWingState(false, true);
        } else if (newY > 0.05D || targetHorizontal > currentHorizontal + 0.05D || currentHorizontal < 0.08D) {
            setWingState(false, false);
        } else if (targetVertical < -0.05D) {
            setWingState(true, false);
        } else {
            setWingState(nextCruisePhase(), false);
        }
    }

    private void drift() {
        if (crow.onGround() || crow.isInWater() || crow.isInLava()) {
            crow.setNoGravity(false);
            setWingState(false, true);
            return;
        }

        crow.setNoGravity(true);
        Vec3 current = crow.getDeltaMovement();

        if (isNearGround()) {
            applyVelocity(current.x * 0.6D, current.y + (-LANDING_SINK - current.y) * 0.3D, current.z * 0.6D);
            setWingState(false, false);
            return;
        }

        float yawRad = crow.getYRot() * Mth.DEG_TO_RAD;
        double speed = Math.max(GLIDE_MIN_SPEED, current.horizontalDistance() * 0.97D);
        double newX = current.x + (-Mth.sin(yawRad) * speed - current.x) * 0.1D;
        double newZ = current.z + (Mth.cos(yawRad) * speed - current.z) * 0.1D;
        double newY = current.y + (-GLIDE_SINK - current.y) * 0.15D;
        applyVelocity(newX, newY, newZ);
        setWingState(true, false);
    }

    private boolean nextCruisePhase() {
        if (--cruiseTicks <= 0) {
            cruiseGliding = !cruiseGliding;
            cruiseTicks = cruiseGliding ? 15 + crow.getRandom().nextInt(25) : 25 + crow.getRandom().nextInt(30);
        }
        return cruiseGliding;
    }

    private void setWingState(boolean gliding, boolean force) {
        if (crow.isGliding() == gliding) return;
        if (!force && wingLockTicks > 0) return;
        crow.setGliding(gliding);
        wingLockTicks = gliding ? MIN_GLIDE_TICKS : MIN_FLAP_TICKS;
    }

    private boolean isNearGround() {
        return !crow.level().noCollision(crow, crow.getBoundingBox().move(0.0D, -0.75D, 0.0D));
    }

    private void applyVelocity(double x, double y, double z) {
        crow.setDeltaMovement(x / AIR_DRAG, y / VERTICAL_DRAG, z / AIR_DRAG);
    }

    private void playTakeoffSound() {
        if (takeoffSoundCooldown > 0 || crow.isSilent()) return;
        takeoffSoundCooldown = 20;
        crow.playSound(SoundEvents.PARROT_FLY, 0.5F, 0.7F + crow.getRandom().nextFloat() * 0.2F);
    }
}