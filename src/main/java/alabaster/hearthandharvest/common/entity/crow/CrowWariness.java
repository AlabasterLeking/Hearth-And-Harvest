package alabaster.hearthandharvest.common.entity.crow;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class CrowWariness {
    public static final int ALARM_DURATION = 120;
    private static final int FULL_TRUST = 200;

    @Nullable
    private LivingEntity alarmSource;
    private int alarmTicks;
    private boolean freshAlarm;
    private int trust;
    private boolean beingTempted;

    public void tick() {
        if (alarmTicks > 0 && --alarmTicks == 0) {
            alarmSource = null;
        }
        if (!beingTempted && trust > 0) {
            trust--;
        }
    }

    public void alarm(LivingEntity source) {
        alarmSource = source;
        alarmTicks = ALARM_DURATION;
        freshAlarm = true;
        trust = 0;
    }

    public boolean isAlarmed() {
        return alarmTicks > 0 && alarmSource != null && alarmSource.isAlive();
    }

    @Nullable
    public LivingEntity getAlarmSource() {
        return isAlarmed() ? alarmSource : null;
    }

    public boolean consumeFreshAlarm() {
        boolean fresh = freshAlarm;
        freshAlarm = false;
        return fresh && isAlarmed();
    }

    public void setBeingTempted(boolean beingTempted) {
        this.beingTempted = beingTempted;
    }

    public void addTrust(int amount) {
        trust = Mth.clamp(trust + amount, 0, FULL_TRUST);
    }

    public boolean isFullyTrusting() {
        return trust >= FULL_TRUST;
    }

    public double comfortDistance() {
        return Mth.lerp(progress(), 4.0D, 1.5D);
    }

    public double waryDistance() {
        return Mth.lerp(progress(), 2.5D, 1.0D);
    }

    private float progress() {
        return (float) trust / FULL_TRUST;
    }
}