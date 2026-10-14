package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.registry.HHModBlocks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AlterGroundDecorator.class)
public class AlterGroundMulchMixin {
    @WrapOperation(method = "placeBlockAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/TreeDecorator$Context;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private void hearthandharvest$keepMulch(TreeDecorator.Context context, BlockPos pos, BlockState state, Operation<Void> original) {
        if (context.level().isStateAtPosition(pos, existing -> existing.is(HHModBlocks.MULCH.get()))) {
            state = HHModBlocks.MULCH.get().defaultBlockState();
        }
        original.call(context, pos, state);
    }
}
