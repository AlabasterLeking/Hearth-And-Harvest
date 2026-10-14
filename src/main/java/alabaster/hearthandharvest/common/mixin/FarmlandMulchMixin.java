package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.block.MulchHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public class FarmlandMulchMixin {
    @Inject(method = "isNearWater", at = @At("RETURN"), cancellable = true)
    private static void checkForMulch(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (MulchHelper.isNearMulch(level, pos)) cir.setReturnValue(true);
    }
}
