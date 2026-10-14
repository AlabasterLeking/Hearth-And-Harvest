package alabaster.hearthandharvest.common.mixin;

import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.Set;

@Mixin(BlockEntityType.class)
public class CabinetBlockEntityTypeMixin {
    @Unique
    private static Set<Block> hearthandharvest$cabinets;

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void hearthandharvest$allowExtraCabinets(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this != ModBlockEntityTypes.CABINET.get()) return;
        if (hearthandharvest$cabinets == null) hearthandharvest$cabinets = Set.copyOf(HHModBlockEntities.extraCabinetBlocks());
        if (hearthandharvest$cabinets.contains(state.getBlock())) cir.setReturnValue(true);
    }
}
