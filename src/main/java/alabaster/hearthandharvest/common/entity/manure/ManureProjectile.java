package alabaster.hearthandharvest.common.entity.manure;

import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.common.registry.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ManureProjectile extends ThrowableItemProjectile {

    public ManureProjectile(EntityType<? extends ManureProjectile> type, Level level) {
        super(type, level);
    }

    public ManureProjectile(Level level, LivingEntity shooter) {
        super(HHModEntities.MANURE_PROJECTILE.get(), shooter, level);
    }

    @Override
    public Item getDefaultItem() {
        return HHModItems.MANURE.get();
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        if (hit instanceof LivingEntity target && hit != getOwner()) {
            target.addEffect(new MobEffectInstance(HHModEffects.PUNGENT.get(), 100, 0));
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            HHModAttachments.MANURE_FLY_TICKS.set(target, 200);
            HHSimpleTrigger.trigger(HHModTriggers.MANURE_HIT, getOwner());
        }
        splat();
        discard();
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        splat();
        discard();
    }

    private void splat() {
        level().playSound(null, getX(), getY(), getZ(),
                HHModSounds.MANURE_HIT.get(), SoundSource.NEUTRAL,
                1.0f, 0.6f + level().random.nextFloat() * 0.4f);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(HHModParticleTypes.FLIES.get(),
                    getX(), getY(), getZ(), 12, 0.3, 0.3, 0.3, 0.05);
        }
    }
}