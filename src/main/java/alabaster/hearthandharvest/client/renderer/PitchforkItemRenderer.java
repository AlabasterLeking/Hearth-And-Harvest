package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchforkModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PitchforkItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            HearthAndHarvest.MODID, "textures/entity/pitchfork.png");

    private static PitchforkItemRenderer INSTANCE;
    private final ThrownPitchforkModel model;

    public PitchforkItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new ThrownPitchforkModel(modelSet.bakeLayer(ThrownPitchforkModel.LAYER_LOCATION));
    }

    public static PitchforkItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PitchforkItemRenderer(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        }
        return INSTANCE;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1.0f, -1.0f, -1.0f);
        model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutout(TEXTURE)),
                light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}