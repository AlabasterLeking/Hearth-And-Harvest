package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.entity.NestBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class NestRenderer implements BlockEntityRenderer<NestBlockEntity> {
    private static final ModelResourceLocation EGG_MODEL = ModelResourceLocation.standalone(
            ResourceLocation.withDefaultNamespace("display/egg"));

    private static final float CORNER = 0.12F;
    private static final float[][] CORNERS = {
            {CORNER, CORNER},
            {CORNER, -CORNER},
            {-CORNER, -CORNER},
            {-CORNER, CORNER}
    };

    private static final float EGG_SCALE = 0.7F;
    private static final float EGG_HEIGHT = 1.0F / 16.0F;
    private static final float ITEM_SCALE = 0.5F;
    private static final float ITEM_HEIGHT = 1.0F / 16.0F;

    private final ItemRenderer itemRenderer;

    public NestRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(NestBlockEntity nest, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        for (int slot = 0; slot < NestBlockEntity.SLOTS; slot++) {
            ItemStack stack = nest.getInventory().getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            float[] corner = CORNERS[slot];
            boolean egg = stack.is(Tags.Items.EGGS);
            BakedModel model = egg ? eggModel() : null;

            poseStack.pushPose();
            poseStack.translate(0.5F + corner[0], egg ? EGG_HEIGHT : ITEM_HEIGHT, 0.5F + corner[1]);

            if (model != null) {
                poseStack.mulPose(Axis.YP.rotationDegrees(15.0F + (90.0F * slot)));
                poseStack.scale(EGG_SCALE, EGG_SCALE, EGG_SCALE);
                poseStack.translate(0.0F, 0.5F, 0.0F);
                itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, model);
            } else {
                poseStack.mulPose(Axis.YP.rotationDegrees(15.0F + (90.0F * slot)));
                poseStack.mulPose(Axis.XP.rotationDegrees(75.0F));
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
                poseStack.translate(-0.2F, 0.15F, -0.2F);
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, nest.getLevel(), slot * 1013);
            }

            poseStack.popPose();
        }
    }

    @Nullable
    private static BakedModel eggModel() {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(EGG_MODEL);
        return model == Minecraft.getInstance().getModelManager().getMissingModel() ? null : model;
    }
}