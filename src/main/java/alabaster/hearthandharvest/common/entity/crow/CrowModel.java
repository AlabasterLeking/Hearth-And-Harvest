package alabaster.hearthandharvest.common.entity.crow;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrowModel<T extends CrowEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "crow"), "main");
    private final ModelPart crow;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart torso;
    private final ModelPart legs;
    private final ModelPart rightleg;
    private final ModelPart leftleg;
    private final ModelPart wings;
    private final ModelPart leftwing;
    private final ModelPart rightwing;
    final ModelPart head;
    private final ModelPart beak;
    private final ModelPart upperbeak;
    private final ModelPart lowerbeak;

    public CrowModel(ModelPart root) {
        this.crow = root.getChild("crow");
        this.body = this.crow.getChild("body");
        this.tail = this.body.getChild("tail");
        this.torso = this.body.getChild("torso");
        this.legs = this.crow.getChild("legs");
        this.rightleg = this.legs.getChild("rightleg");
        this.leftleg = this.legs.getChild("leftleg");
        this.wings = this.crow.getChild("wings");
        this.leftwing = this.wings.getChild("leftwing");
        this.rightwing = this.wings.getChild("rightwing");
        this.head = this.crow.getChild("head");
        this.beak = this.head.getChild("beak");
        this.upperbeak = this.beak.getChild("upperbeak");
        this.lowerbeak = this.beak.getChild("lowerbeak");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition crow = partdefinition.addOrReplaceChild("crow", CubeListBuilder.create(), PartPose.offset(4.5F, 20.0F, 2.25F));

        PartDefinition body = crow.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-5.0F, -0.2705F, 0.7721F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(12, 8).addBox(-1.5F, -1.0F, 0.35F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2705F, -0.7721F, -0.6545F, 0.0F, 0.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso_r1 = torso.addOrReplaceChild("torso_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -1.0F, -4.25F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition legs = crow.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-5.0F, 1.0F, -1.5F));

        PartDefinition rightleg = legs.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(10, 14).addBox(-0.5F, -2.0F, 0.25F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(9, 18).addBox(-0.5F, 2.0F, -0.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.0F, 0.0F));

        PartDefinition leftleg = legs.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(10, 14).addBox(-0.5F, -2.0F, 0.25F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(9, 18).addBox(-0.5F, 2.0F, -0.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.0F));

        PartDefinition wings = crow.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, 0.0F));

        PartDefinition leftwing = wings.addOrReplaceChild("leftwing", CubeListBuilder.create(), PartPose.offset(2.5F, -1.6766F, -3.1459F));

        PartDefinition leftwing_r1 = leftwing.addOrReplaceChild("leftwing_r1", CubeListBuilder.create().texOffs(12, 12).addBox(-4.0F, -2.0F, -4.25F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 2.6766F, 3.1459F, -0.3054F, 0.0F, 0.0F));

        PartDefinition rightwing = wings.addOrReplaceChild("rightwing", CubeListBuilder.create(), PartPose.offset(-2.5F, -1.6766F, -3.1459F));

        PartDefinition rightwing_r1 = rightwing.addOrReplaceChild("rightwing_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -2.0F, -4.25F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 2.6766F, 3.1459F, -0.3054F, 0.0F, 0.0F));

        PartDefinition head = crow.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, -1.75F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.25F, -5.25F, 0.3054F, 0.0F, 0.0F));

        PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition upperbeak = beak.addOrReplaceChild("upperbeak", CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, 0.25F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition lowerbeak = beak.addOrReplaceChild("lowerbeak", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition tounge_r1 = lowerbeak.addOrReplaceChild("tounge_r1", CubeListBuilder.create().texOffs(16, 4).addBox(-0.5F, 0.75F, -3.0F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9962F, 0.0872F, -0.0436F, 0.0F, 0.0F));

        PartDefinition partlowerbeak_r1 = lowerbeak.addOrReplaceChild("partlowerbeak_r1", CubeListBuilder.create().texOffs(18, 4).addBox(-0.5F, 0.25F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9962F, 0.0872F, -0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(CrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        if (!entity.isVisuallyFlying()) {
            this.animateWalk(CrowAnimations.walking, limbSwing, limbSwingAmount, 2f, 5f);
        }
        this.animate(entity.idleAnimationState, CrowAnimations.idle, ageInTicks, 1f);
        this.animate(entity.flyingAnimationState, CrowAnimations.flying, ageInTicks, entity.getFlapAnimationSpeed());
        this.animate(entity.glidingAnimationState, CrowAnimations.gliding, ageInTicks, 1f);
        this.animate(entity.sittingAnimationState, CrowAnimations.sitting, ageInTicks, 1f);
        if (!entity.getMainHandItem().isEmpty()) {
            this.lowerbeak.xRot += 0.35F;
        }
    }

    public void translateToBeak(PoseStack poseStack) {
        this.crow.translateAndRotate(poseStack);
        this.head.translateAndRotate(poseStack);
        this.beak.translateAndRotate(poseStack);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        crow.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float) Math.PI / 180f);
        this.head.xRot = headPitch * ((float) Math.PI / 180f);
    }

    @Override
    public ModelPart root() {
        return crow;
    }
}