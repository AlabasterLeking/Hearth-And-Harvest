package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.entity.TroughBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class TroughRenderer implements BlockEntityRenderer<TroughBlockEntity> {

    private static final float INNER_MIN = 2f / 16f;
    private static final float INNER_MAX = 14f / 16f;
    private static final float SCATTER_MIN = 7f / 16f;
    private static final float SCATTER_MAX = 9f / 16f;
    private static final float SCATTER_SIZE = SCATTER_MAX - SCATTER_MIN;

    private static final float FLOOR_Y = 2f / 16f + 0.002f;
    private static final float FLUID_MIN_Y = 2f / 16f + 0.01f;
    private static final float FLUID_MAX_Y = 7.4f / 16f;
    private static final float MAX_PILE_HEIGHT = 6f / 16f;

    public TroughRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(TroughBlockEntity be, float partialTick, PoseStack ps, MultiBufferSource buf, int packedLight, int packedOverlay) {
        FluidStack fluid = be.getFluidTank().getFluid();
        ItemStack item = be.getItemHandler().getStackInSlot(0);

        if (!fluid.isEmpty()) {
            renderFluidSurface(be, ps, buf, packedLight, packedOverlay);
        } else if (!item.isEmpty()) {
            renderScatteredItems(be, item, ps, buf, packedLight, packedOverlay);
        }
    }

    private static MultiBufferSource wrapSolid(MultiBufferSource buf) {
        return renderType -> {
            if (renderType == RenderType.translucent()
                    || renderType == RenderType.translucentMovingBlock()
                    || renderType.toString().contains("translucent")) {
                return buf.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS));
            }
            return buf.getBuffer(renderType);
        };
    }

    private void renderScatteredItems(TroughBlockEntity be, ItemStack stack, PoseStack ps, MultiBufferSource buf, int packedLight, int packedOverlay) {
        long seed = be.getBlockPos().asLong();
        MultiBufferSource solidBuf = wrapSolid(buf);
        int count = stack.getCount();
        float fillFrac = Math.min(count / (float) TroughBlockEntity.ITEM_SLOT_LIMIT, 1f);

        for (int i = 0; i < count; i++) {
            float[] pos = itemPosition(seed, i);
            float offsetX = SCATTER_MIN + pos[0] * SCATTER_SIZE;
            float offsetZ = SCATTER_MIN + pos[1] * SCATTER_SIZE;
            float rotation = pos[2] * 360f;
            float itemY = FLOOR_Y + pos[3] * fillFrac * MAX_PILE_HEIGHT;

            ps.pushPose();
            ps.translate(offsetX, itemY, offsetZ);
            ps.mulPose(Axis.YP.rotationDegrees(rotation));
            ps.mulPose(Axis.XP.rotationDegrees(-90f));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, ps, solidBuf, null, i);
            ps.popPose();
        }
    }

    private static float[] itemPosition(long worldSeed, int index) {
        long r = worldSeed ^ (index * 0x9e3779b97f4a7c15L);
        r = r * 0x6c62272e07bb0142L + 0x62b821756295c58dL;
        float px = ((r >>> 33) & 0xFFFFL) / 65535f;
        r = r * 0x6c62272e07bb0142L + 0x62b821756295c58dL;
        float pz = ((r >>> 33) & 0xFFFFL) / 65535f;
        r = r * 0x6c62272e07bb0142L + 0x62b821756295c58dL;
        float rot = ((r >>> 33) & 0xFFFFL) / 65535f;
        r = r * 0x6c62272e07bb0142L + 0x62b821756295c58dL;
        float py = ((r >>> 33) & 0xFFFFL) / 65535f;
        return new float[]{ px, pz, rot, py };
    }

    private void renderFluidSurface(TroughBlockEntity be, PoseStack ps, MultiBufferSource buf, int packedLight, int overlay) {
        FluidStack fluid = be.getFluidTank().getFluid();
        if (fluid.isEmpty()) return;

        float fill = (float) be.getFluidTank().getFluidAmount() / (float) be.getFluidTank().getCapacity();
        if (fill <= 0f) return;

        float surfaceY = FLUID_MIN_Y + fill * (FLUID_MAX_Y - FLUID_MIN_Y);

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTex = ext.getStillTexture(fluid);
        if (stillTex == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTex);

        int color = ext.getTintColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >>  8) & 0xFF) / 255f;
        float b = ( color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0f) a = 0.75f;

        VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
        Matrix4f m = ps.last().pose();
        int ov = OverlayTexture.NO_OVERLAY;
        float u0 = sprite.getU0(), u1 = sprite.getU1();
        float v0 = sprite.getV0(), v1 = sprite.getV1();

        emitFluidQuad(vc, m, INNER_MIN, INNER_MAX, INNER_MIN, INNER_MAX,
                surfaceY, r, g, b, a, u0, u1, v0, v1, ov, packedLight);
    }

    private static void emitFluidQuad(VertexConsumer vc, Matrix4f m, float x0, float x1, float z0, float z1, float y, float r, float g, float b, float a, float u0, float u1, float v0, float v1, int overlay, int light) {
        vc.addVertex(m, x0, y, z0).setColor(r,g,b,a).setUv(u0,v0).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x0, y, z1).setColor(r,g,b,a).setUv(u0,v1).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x1, y, z1).setColor(r,g,b,a).setUv(u1,v1).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x1, y, z0).setColor(r,g,b,a).setUv(u1,v0).setOverlay(overlay).setLight(light).setNormal(0,1,0);
    }
}