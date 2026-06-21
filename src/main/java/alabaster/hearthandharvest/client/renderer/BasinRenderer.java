package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.entity.BasinBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

    private static final float INNER_MIN =  2f / 16f;
    private static final float INNER_MAX = 14f / 16f;
    private static final float FLUID_BOTTOM = 1f / 16f;
    private static final float FLUID_TOP = 15f / 16f;

    public BasinRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(BasinBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        FluidStack fluid = be.tank.getFluid();
        if (fluid.isEmpty()) return;

        float fillFraction = (float) fluid.getAmount() / BasinBlockEntity.CAPACITY;
        float surfaceY = FLUID_BOTTOM + fillFraction * (FLUID_TOP - FLUID_BOTTOM);

        Level level = be.getLevel();
        int biomeColor = level != null ? BiomeColors.getAverageWaterColor(level, be.getBlockPos()) : 0x3F76E4;
        float r = ((biomeColor >> 16) & 0xFF) / 255f;
        float g = ((biomeColor >>  8) & 0xFF) / 255f;
        float b = ( biomeColor & 0xFF) / 255f;
        float a = 0.8f;

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ext.getStillTexture(fluid));

        float du = sprite.getU1() - sprite.getU0();
        float dv = sprite.getV1() - sprite.getV0();
        float u0 = sprite.getU0(), u1 = sprite.getU0() + du * (INNER_MAX - INNER_MIN);
        float v0 = sprite.getV0(), v1 = sprite.getV0() + dv * (INNER_MAX - INNER_MIN);

        Matrix4f pose = poseStack.last().pose();
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));

        vc.addVertex(pose, INNER_MIN, surfaceY, INNER_MIN).setColor(r,g,b,a).setUv(u0,v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MIN, surfaceY, INNER_MAX).setColor(r,g,b,a).setUv(u0,v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MAX, surfaceY, INNER_MAX).setColor(r,g,b,a).setUv(u1,v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MAX, surfaceY, INNER_MIN).setColor(r,g,b,a).setUv(u1,v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
    }
}