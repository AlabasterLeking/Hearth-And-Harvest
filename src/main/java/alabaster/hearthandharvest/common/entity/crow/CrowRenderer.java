package alabaster.hearthandharvest.common.entity.crow;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrowRenderer extends MobRenderer<CrowEntity, CrowModel<CrowEntity>> {
    public CrowRenderer(EntityRendererProvider.Context context) {
        super(context, new CrowModel<>(context.bakeLayer(CrowModel.LAYER_LOCATION)), 0.15f);
        this.addLayer(new CrowHeldItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(CrowEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "textures/entity/crow.png");
    }

    @Override
    protected void setupRotations(CrowEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        float pitch = Mth.lerp(partialTick, entity.flightPitchO, entity.flightPitch);
        float roll = Mth.lerp(partialTick, entity.flightRollO, entity.flightRoll);
        if (Math.abs(pitch) < 0.01F && Math.abs(roll) < 0.01F) return;

        float pivot = entity.getBbHeight() * 0.5F;
        poseStack.translate(0.0F, pivot, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-roll));
        poseStack.translate(0.0F, -pivot, 0.0F);
    }

    @Override
    public void render(CrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}