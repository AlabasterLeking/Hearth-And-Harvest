package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.entity.ManureDropHelper;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class SheepGrassManureMixin {
    @Inject(method = "ate()V", at = @At("HEAD"))
    private void onAteGrass(CallbackInfo ci) {
        Sheep self = (Sheep)(Object)this;
        if (self.level().isClientSide) return;
        ManureDropHelper.schedulePoop(self);
    }
}