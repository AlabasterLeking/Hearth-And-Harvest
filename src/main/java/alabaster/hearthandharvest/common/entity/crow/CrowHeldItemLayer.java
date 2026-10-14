package alabaster.hearthandharvest.common.entity.crow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrowHeldItemLayer extends RenderLayer<CrowEntity, CrowModel<CrowEntity>> {
    private static final float BEAK_Y = 0.25F / 16.0F;
    private static final float BEAK_Z = -3.25F / 16.0F;
    private static final float ITEM_SCALE = 0.5F;

    private final ItemInHandRenderer itemInHandRenderer;

    public CrowHeldItemLayer(RenderLayerParent<CrowEntity, CrowModel<CrowEntity>> parent, ItemInHandRenderer itemInHandRenderer) {
        super(parent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, CrowEntity crow, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = crow.getMainHandItem();
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        this.getParentModel().translateToBeak(poseStack);
        poseStack.translate(0.0F, BEAK_Y, BEAK_Z);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        this.itemInHandRenderer.renderItem(crow, stack, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}