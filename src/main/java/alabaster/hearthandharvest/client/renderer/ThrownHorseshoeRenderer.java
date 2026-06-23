package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.entity.cleaver.ThrownCleaver;
import alabaster.hearthandharvest.common.entity.horseshoe.ThrownHorseshoe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownHorseshoeRenderer extends EntityRenderer<ThrownHorseshoe> {

    private final ItemRenderer itemRenderer;

    public ThrownHorseshoeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownHorseshoe entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

        float stuckAngle = entity.getStuckAngle();
        float spin = stuckAngle >= 0 ? stuckAngle : (entity.tickCount + partialTick) * 36.0f;
        poseStack.mulPose(Axis.ZP.rotationDegrees(spin));

        poseStack.scale(0.5f, 0.5f, 0.5f);

        itemRenderer.renderStatic(null, entity.getHorseshoeStack(), ItemDisplayContext.FIXED,
                false, poseStack, buffer, entity.level(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownHorseshoe entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}