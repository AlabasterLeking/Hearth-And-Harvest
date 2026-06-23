package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.entity.SprinklerBlockEntity;
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

public class SprinklerRenderer implements BlockEntityRenderer<SprinklerBlockEntity> {

    // Basin interior from model: walls at x/z 2-3 and 13-14, floor top at y=1, walls top at y=8.
    private static final float INNER_MIN    =  3f / 16f;
    private static final float INNER_MAX    = 13f / 16f;
    private static final float FLUID_BOTTOM =  1f / 16f;
    private static final float FLUID_TOP    =  7f / 16f;

    public SprinklerRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(SprinklerBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        FluidStack fluid = be.tank.getFluid();
        if (fluid.isEmpty()) return;

        float fill = (float) fluid.getAmount() / SprinklerBlockEntity.CAPACITY;
        float surfaceY = FLUID_BOTTOM + fill * (FLUID_TOP - FLUID_BOTTOM);

        Level level = be.getLevel();
        int biomeColor = level != null
                ? BiomeColors.getAverageWaterColor(level, be.getBlockPos())
                : 0x3F76E4;
        float r = ((biomeColor >> 16) & 0xFF) / 255f;
        float g = ((biomeColor >>  8) & 0xFF) / 255f;
        float b = ( biomeColor        & 0xFF) / 255f;

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ext.getStillTexture(fluid));

        float du = sprite.getU1() - sprite.getU0();
        float dv = sprite.getV1() - sprite.getV0();
        float u0 = sprite.getU0(), u1 = u0 + du * (INNER_MAX - INNER_MIN);
        float v0 = sprite.getV0(), v1 = v0 + dv * (INNER_MAX - INNER_MIN);

        Matrix4f pose = poseStack.last().pose();
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));

        vc.addVertex(pose, INNER_MIN, surfaceY, INNER_MIN).setColor(r,g,b,0.8f).setUv(u0,v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MIN, surfaceY, INNER_MAX).setColor(r,g,b,0.8f).setUv(u0,v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MAX, surfaceY, INNER_MAX).setColor(r,g,b,0.8f).setUv(u1,v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
        vc.addVertex(pose, INNER_MAX, surfaceY, INNER_MIN).setColor(r,g,b,0.8f).setUv(u1,v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0,1,0);
    }
}