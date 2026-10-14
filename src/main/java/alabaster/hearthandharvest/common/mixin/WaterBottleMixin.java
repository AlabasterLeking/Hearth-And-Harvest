package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.Config;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class WaterBottleMixin {

    @ModifyReturnValue(
            method = "getMaxStackSize",
            at = @At("RETURN")
    )
    private int restrictNonWaterPotions(int original) {
        ItemStack stack = (ItemStack)(Object)this;

        if (!stack.is(Items.POTION)) {
            return original;
        }

        try {
            if (!Config.STACK_WATER_BOTTLES.get()) return original;
        } catch (IllegalStateException e) {
            return original;
        }

        return PotionUtils.getPotion(stack) == Potions.WATER ? 16 : 1;
    }
}