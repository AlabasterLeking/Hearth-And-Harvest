package alabaster.hearthandharvest.common.entity.crow;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrowOnShoulderLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final CrowModel<CrowEntity> crowModel;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HearthAndHarvest.MODID, "textures/entity/crow.png");

    public CrowOnShoulderLayer(PlayerRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.crowModel = new CrowModel<>(modelSet.bakeLayer(CrowModel.LAYER_LOCATION));
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        renderCrow(poseStack, buffer, packedLight, player, true, netHeadYaw, headPitch);
        renderCrow(poseStack, buffer, packedLight, player, false, netHeadYaw, headPitch);
    }

    private void renderCrow(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            AbstractClientPlayer player,
            boolean left,
            float netHeadYaw,
            float headPitch
    ) {
        CompoundTag tag = left
                ? player.getShoulderEntityLeft()
                : player.getShoulderEntityRight();

        if (!tag.contains("id")) return;
        if (!tag.getString("id").equals(
                BuiltInRegistries.ENTITY_TYPE.getKey(HHModEntities.CROW.get()).toString()
        )) return;

        poseStack.pushPose();

        poseStack.translate(
                left ? 0.4D : -0.35D,
                player.isCrouching() ? -1.3D : -1.5D,
                0.0D
        );

        crowModel.root().getAllParts().forEach(part -> part.resetPose());
        
        float clampedYaw = Mth.clamp(netHeadYaw, -30f, 30f);
        float clampedPitch = Mth.clamp(headPitch, -25f, 45f);
        crowModel.head.yRot = clampedYaw * ((float) Math.PI / 180F);
        crowModel.head.xRot = clampedPitch * ((float) Math.PI / 180F);

        VertexConsumer vertexConsumer = buffer.getBuffer(crowModel.renderType(TEXTURE));
        crowModel.renderToBuffer(
                poseStack,
                vertexConsumer,
                light,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F
        );

        poseStack.popPose();
    }
}