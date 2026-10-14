package alabaster.hearthandharvest.common.mixin.accessor;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChickenModel.class)
public interface ChickenModelAccessor {
    @Accessor("rightLeg")
    ModelPart hearthandharvest$getRightLeg();

    @Accessor("leftLeg")
    ModelPart hearthandharvest$getLeftLeg();
}
