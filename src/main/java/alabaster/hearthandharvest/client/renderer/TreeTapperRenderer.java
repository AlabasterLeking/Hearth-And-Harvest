package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.TreeTapperBlock;
import alabaster.hearthandharvest.common.block.entity.TreeTapperBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class TreeTapperRenderer implements BlockEntityRenderer<TreeTapperBlockEntity> {

    private static final float MIN_X = 4f / 16f;
    private static final float MAX_X = 12f / 16f;
    private static final float MIN_Z = 7f / 16f;
    private static final float MAX_Z = 15f / 16f;
    private static final float FLUID_BOTTOM = 3f / 16f;
    private static final float FLUID_TOP = 11f / 16f;

    public TreeTapperRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(TreeTapperBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        FluidStack fluid = be.tank.getFluid();
        if (fluid.isEmpty()) return;

        Direction facing = be.getBlockState().getValue(TreeTapperBlock.FACING);
        float fillFraction = (float) fluid.getAmount() / TreeTapperBlockEntity.CAPACITY;
        float fluidY = FLUID_BOTTOM + fillFraction * (FLUID_TOP - FLUID_BOTTOM);

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ext.getStillTexture(fluid));

        int color = ext.getTintColor(fluid);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b =  color & 0xFF;
        int a = (color >> 24) & 0xFF;
        if (a == 0) a = 255;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(switch (facing) {
            case WEST -> -90f;
            case NORTH -> 180f;
            case EAST -> 90f;
            default -> 0f;
        }));
        poseStack.translate(-0.5, 0, -0.5);

        Matrix4f pose = poseStack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());

        float u0 = sprite.getU0(), u1 = sprite.getU1();
        float v0 = sprite.getV0(), v1 = sprite.getV1();

        // Top face of fluid surface (NW → SW → SE → NE, matching vanilla fluid winding).
        consumer.addVertex(pose, MIN_X, fluidY, MIN_Z).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0, 1, 0);
        consumer.addVertex(pose, MIN_X, fluidY, MAX_Z).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0, 1, 0);
        consumer.addVertex(pose, MAX_X, fluidY, MAX_Z).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0, 1, 0);
        consumer.addVertex(pose, MAX_X, fluidY, MIN_Z).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0, 1, 0);

        poseStack.popPose();
    }
}