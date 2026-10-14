package alabaster.hearthandharvest.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ClarityEffect extends MobEffect {

    public ClarityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x64b1f1);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
