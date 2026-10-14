package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.registry.HHModItems;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {

    @Inject(method = "handleEating", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private void handleEatingMixin(Player player, ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local LocalFloatRef amountHealed, @Local(ordinal = 0) LocalIntRef secondsAged, @Local(ordinal = 1) LocalIntRef temperAdded) {
        if (stack.is(HHModItems.CORN.get()) || stack.is(HHModItems.HOPS.get())) {
            amountHealed.set(2.0F);
            secondsAged.set(20);
            temperAdded.set(3);
        }
    }
}