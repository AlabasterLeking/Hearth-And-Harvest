package alabaster.hearthandharvest.common.entity.pitchfork;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ThrownPitchforkModel extends EntityModel<ThrownPitchfork> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "thrown_pitchfork"), "main"
	);

	private final ModelPart bb_main;

	public ThrownPitchforkModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-0.5F, -24.0F, -0.5F, 1.0F, 24.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 0).addBox(-2.5F, -25.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 3).addBox(1.5F, -30.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 3).addBox(-2.5F, -30.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 3).addBox(-0.5F, -30.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(ThrownPitchfork entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
	}
}