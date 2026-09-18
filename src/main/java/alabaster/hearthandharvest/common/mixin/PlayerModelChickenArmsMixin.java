package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.event.ChickenGlideEvents;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelChickenArmsMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void hearthandharvest$raiseArmsForChicken(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!(entity instanceof Player player) || !ChickenGlideEvents.isHoldingChicken(player)) return;

        PlayerModel<?> model = (PlayerModel<?>) (Object) this;
        model.rightArm.xRot = (float) -Math.PI;
        model.rightArm.yRot = 0.0F;
        model.rightArm.zRot = 0.0F;
        model.leftArm.xRot = (float) -Math.PI;
        model.leftArm.yRot = 0.0F;
        model.leftArm.zRot = 0.0F;
        model.rightSleeve.copyFrom(model.rightArm);
        model.leftSleeve.copyFrom(model.leftArm);
    }
}