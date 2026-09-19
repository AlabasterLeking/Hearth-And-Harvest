package alabaster.hearthandharvest.common.entity.crow;

import net.minecraft.util.Mth;

public class CrowFlightPose {
    private static final float SMOOTHING = 0.2F;
    private static final float FLAP_SMOOTHING = 0.1F;
    private static final float CLIMB_FLAP_SPEED = 1.35F;
    private static final int AIRBORNE_GRACE = 3;

    private float pitch;
    private float pitchO;
    private float roll;
    private float rollO;
    private float flapSpeed = 1.0F;
    private int airborneTicks;

    public void tick(CrowEntity crow) {
        airborneTicks = crow.onGround() || crow.isPassenger() ? 0 : airborneTicks + 1;
        pitchO = pitch;
        rollO = roll;

        float targetPitch = 0.0F;
        float targetRoll = 0.0F;
        float targetFlapSpeed = 1.0F;

        if (isVisuallyFlying(crow) && !crow.isInWater()) {
            double vy = crow.getY() - crow.yo;
            double vh = Math.sqrt(Mth.square(crow.getX() - crow.xo) + Mth.square(crow.getZ() - crow.zo));
            if (vh + Math.abs(vy) > 0.02D) {
                targetPitch = (float) Mth.clamp(Mth.atan2(vy, vh) * Mth.RAD_TO_DEG * 0.7D, -35.0D, 30.0D);
            }
            targetRoll = Mth.clamp(Mth.wrapDegrees(crow.getYRot() - crow.yRotO) * 3.0F, -45.0F, 45.0F);
            if (vy > 0.05D) targetFlapSpeed = CLIMB_FLAP_SPEED;
        }

        pitch += (targetPitch - pitch) * SMOOTHING;
        roll += (targetRoll - roll) * SMOOTHING;
        flapSpeed += (targetFlapSpeed - flapSpeed) * FLAP_SMOOTHING;
    }

    public boolean isVisuallyFlying(CrowEntity crow) {
        if (crow.isInSittingPose() || crow.isPassenger()) return false;
        return airborneTicks > AIRBORNE_GRACE || (airborneTicks > 0 && crow.isGliding());
    }

    public float pitch(float partialTick) {
        return Mth.lerp(partialTick, pitchO, pitch);
    }

    public float roll(float partialTick) {
        return Mth.lerp(partialTick, rollO, roll);
    }

    public float flapSpeed() {
        return flapSpeed;
    }
}