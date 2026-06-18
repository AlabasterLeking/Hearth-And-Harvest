package alabaster.hearthandharvest.common.entity.pitchfork;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownPitchfork extends AbstractArrow {

    public ThrownPitchfork(EntityType<? extends ThrownPitchfork> type, Level level) {
        super(type, level);
    }

    public ThrownPitchfork(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_PITCHFORK.get(), shooter, level, stack, null);
        this.setBaseDamage(8.0);
        this.pickup = Pickup.ALLOWED;
    }

    @Override
    public byte getPierceLevel() {
        return 2;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(HHModItems.PITCHFORK.get());
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.addEffect(new MobEffectInstance(HHModEffects.PINNED, 6000, 0));
        }
    }
}