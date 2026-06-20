package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.entity.cleaver.ThrownCleaver;
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
import org.joml.Quaternionf;

public class ThrownCleaverRenderer extends EntityRenderer<ThrownCleaver> {

    private static final float SPIN_DEG_PER_TICK = 54f;

    private final ItemRenderer itemRenderer;

    public ThrownCleaverRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ThrownCleaver entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float yr = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float xr = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        float yrRad = yr * Mth.DEG_TO_RAD;

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);

        if (entity.isStuck()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(yr + 90f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
        } else {
            float fdx = Mth.sin(yrRad);
            float fdz = Mth.cos(yrRad);
            float spin = (entity.tickCount + partialTick) * SPIN_DEG_PER_TICK;
            poseStack.mulPose(new Quaternionf().rotateAxis(spin * Mth.DEG_TO_RAD, fdz, 0f, -fdx));
            poseStack.mulPose(Axis.YP.rotationDegrees(yr - 90f));
        }

        itemRenderer.renderStatic(entity.getCleaverStack(), ItemDisplayContext.NONE,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownCleaver entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}