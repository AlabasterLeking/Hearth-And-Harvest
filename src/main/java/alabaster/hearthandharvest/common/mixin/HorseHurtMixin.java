package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.registry.HHModAttachments;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class HorseHurtMixin {

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void horseshoeProtection(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!(self instanceof AbstractHorse horse)) return;
        if (horse.getData(HHModAttachments.HORSESHOE_ITEM).isEmpty()) return;
        if (source.is(DamageTypes.CACTUS) || source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            cir.setReturnValue(false);
        }
    }
}