package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchfork;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchforkModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownPitchforkRenderer extends EntityRenderer<ThrownPitchfork> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            HearthAndHarvest.MODID, "textures/entity/pitchfork.png"
    );

    private final ThrownPitchforkModel model;

    public ThrownPitchforkRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ThrownPitchforkModel(ctx.bakeLayer(ThrownPitchforkModel.LAYER_LOCATION));
    }

    @Override
    public void render(ThrownPitchfork entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0f));

        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownPitchfork entity) {
        return TEXTURE;
    }
}