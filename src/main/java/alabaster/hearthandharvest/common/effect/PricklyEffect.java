package alabaster.hearthandharvest.common.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PricklyEffect extends MobEffect {

    public PricklyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xbf2529);
    }

    @Override
    public void applyEffectTick(LivingEntity holder, int amplifier) {
        if (holder.level().isClientSide) return;

        float damage = 1.0f + amplifier;
        AABB contact = holder.getBoundingBox().inflate(0.1);

        List<LivingEntity> touching = holder.level().getEntitiesOfClass(
                LivingEntity.class, contact,
                e -> e != holder && e.isAlive()
        );

        DamageSource source = holder.damageSources().thorns(holder);
        for (LivingEntity target : touching) {
            target.hurt(source, damage);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}