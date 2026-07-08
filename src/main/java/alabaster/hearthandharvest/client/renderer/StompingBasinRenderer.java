package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.MultiblockPart;
import alabaster.hearthandharvest.common.block.entity.StompingBasinBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class StompingBasinRenderer implements BlockEntityRenderer<StompingBasinBlockEntity> {

    private static final float INNER_MIN = 2f / 16f;
    private static final float INNER_MAX = 14f / 16f;
    private static final float SCATTER_MIN = 7f / 16f;
    private static final float SCATTER_MAX = 9f / 16f;
    private static final float SCATTER_SIZE = SCATTER_MAX - SCATTER_MIN;

    private static final float BIG_INNER_MIN = 2f / 16f;
    private static final float BIG_INNER_MAX = 30f / 16f;

    private static final float BIG_SCATTER_MIN = 5f / 16f;
    private static final float BIG_SCATTER_MAX = 27f / 16f;
    private static final float BIG_SCATTER_SIZE = BIG_SCATTER_MAX - BIG_SCATTER_MIN;

    private static final float FLOOR_Y = 1f / 16f + 0.002f;
    private static final float FLUID_MIN_Y = 1f / 16f + 0.01f;
    private static final float FLUID_MAX_Y = 11f / 16f;
    private static final float ITEM_Y_STEP = 0.001f;

    private static final ModelResourceLocation MODEL_NW = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_nw"));
    private static final ModelResourceLocation MODEL_NE = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_ne"));
    private static final ModelResourceLocation MODEL_SW = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_sw"));
    private static final ModelResourceLocation MODEL_SE = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_se"));

    public StompingBasinRenderer(BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void render(StompingBasinBlockEntity be, float partialTick, PoseStack ps, MultiBufferSource buf, int packedLight, int packedOverlay) {
        MultiblockPart role = be.getMultiblockRole();

        if (role == MultiblockPart.MEMBER) {
            renderQuadrantModel(be, resolveQuadrantModel(be), ps, buf, packedLight, packedOverlay);
            return;
        }

        boolean combined = role == MultiblockPart.CONTROLLER;

        if (combined) renderQuadrantModel(be, MODEL_NW, ps, buf, packedLight, packedOverlay);

        renderScatteredItems(be, ps, buf, packedLight, packedOverlay, combined);
        renderFluidSurface(be, ps, buf, packedLight, combined);
    }

    private ModelResourceLocation resolveQuadrantModel(StompingBasinBlockEntity be) {
        BlockPos controllerPos = be.getControllerPos();
        if (controllerPos == null) return MODEL_NW;
        BlockPos pos = be.getBlockPos();
        int dx = pos.getX() - controllerPos.getX();
        int dz = pos.getZ() - controllerPos.getZ();
        if (dx == 1 && dz == 0) return MODEL_NE;
        if (dx == 0 && dz == 1) return MODEL_SW;
        if (dx == 1 && dz == 1) return MODEL_SE;
        return MODEL_NW;
    }

    private void renderQuadrantModel(StompingBasinBlockEntity be, ModelResourceLocation modelId, PoseStack ps, MultiBufferSource buf, int packedLight, int packedOverlay) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelId);
        VertexConsumer vc = buf.getBuffer(RenderType.solid());
        RandomSource random = RandomSource.create();
        Level level = be.getLevel();

        if (level != null) {
            BlockPos pos = be.getBlockPos();
            BlockState state = level.getBlockState(pos);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                    .tesselateBlock(level, model, state, pos, ps, vc, false, random, 42L, packedOverlay);
        } else {
            random.setSeed(42L);
            for (BakedQuad quad : model.getQuads(null, null, random, ModelData.EMPTY, null)) {
                vc.putBulkData(ps.last(), quad, 1f, 1f, 1f, 1f, packedLight, packedOverlay);
            }
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

    private static int quadrantLight(StompingBasinBlockEntity controller, int dx, int dz) {
        Level level = controller.getLevel();
        if (level == null) return 0;
        return LevelRenderer.getLightColor(level, controller.getBlockPos().offset(dx, 0, dz));
    }

    private void renderScatteredItems(StompingBasinBlockEntity source, PoseStack ps, MultiBufferSource buf, int packedLight, int packedOverlay, boolean combined) {
        float scatterMin  = combined ? BIG_SCATTER_MIN : SCATTER_MIN;
        float scatterSize = combined ? BIG_SCATTER_SIZE : SCATTER_SIZE;
        long seed = source.getBlockPos().asLong();
        int renderIndex = 0;

        MultiBufferSource solidBuf = wrapSolid(buf);

        int lightNW = combined ? quadrantLight(source, 0, 0) : packedLight;
        int lightNE = combined ? quadrantLight(source, 1, 0) : packedLight;
        int lightSW = combined ? quadrantLight(source, 0, 1) : packedLight;
        int lightSE = combined ? quadrantLight(source, 1, 1) : packedLight;

        for (int slot = 0; slot < source.getItemHandler().getSlots(); slot++) {
            ItemStack stack = source.getItemHandler().getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            for (int i = 0; i < stack.getCount(); i++, renderIndex++) {
                float[] pos = itemPosition(seed, renderIndex);
                float offsetX = scatterMin + pos[0] * scatterSize;
                float offsetZ = scatterMin + pos[1] * scatterSize;
                float rotation = pos[2] * 360f;
                float itemY = FLOOR_Y + renderIndex * ITEM_Y_STEP;

                int itemLight = offsetZ >= 1f
                        ? (offsetX >= 1f ? lightSE : lightSW)
                        : (offsetX >= 1f ? lightNE : lightNW);

                ps.pushPose();
                ps.translate(offsetX, itemY, offsetZ);
                ps.mulPose(Axis.YP.rotationDegrees(rotation));
                ps.mulPose(Axis.XP.rotationDegrees(-90f));
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, itemLight, packedOverlay, ps, solidBuf, null, renderIndex);
                ps.popPose();
            }
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
        return new float[]{ px, pz, rot };
    }

    private void renderFluidSurface(StompingBasinBlockEntity source, PoseStack ps, MultiBufferSource buf, int packedLight, boolean combined) {
        FluidStack fluid = source.getFluidTank().getFluid();
        if (fluid.isEmpty()) return;

        float fill = (float) source.getFluidTank().getFluidAmount() / (float) source.getFluidTank().getCapacity();
        if (fill <= 0f) return;

        float surfaceY = FLUID_MIN_Y + fill * (FLUID_MAX_Y - FLUID_MIN_Y);

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTex = ext.getStillTexture(fluid);
        if (stillTex == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTex);

        int color = ext.getTintColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >>  8) & 0xFF) / 255f;
        float b = ( color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0f) a = 0.75f;

        VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
        Matrix4f m = ps.last().pose();
        int ov = OverlayTexture.NO_OVERLAY;

        if (combined) {
            float mid = 1f;
            emitFluidQuad(vc, m, BIG_INNER_MIN, mid, BIG_INNER_MIN, mid, surfaceY, r, g, b, a, sprite, ov, quadrantLight(source, 0, 0)); // NW
            emitFluidQuad(vc, m, mid, BIG_INNER_MAX, BIG_INNER_MIN, mid, surfaceY, r, g, b, a, sprite, ov, quadrantLight(source, 1, 0)); // NE
            emitFluidQuad(vc, m, BIG_INNER_MIN, mid, mid, BIG_INNER_MAX, surfaceY, r, g, b, a, sprite, ov, quadrantLight(source, 0, 1)); // SW
            emitFluidQuad(vc, m, mid, BIG_INNER_MAX, mid, BIG_INNER_MAX, surfaceY, r, g, b, a, sprite, ov, quadrantLight(source, 1, 1)); // SE
        } else {
            emitFluidQuad(vc, m, INNER_MIN, INNER_MAX, INNER_MIN, INNER_MAX, surfaceY, r, g, b, a, sprite, ov, packedLight);
        }
    }

    private static void emitFluidQuad(VertexConsumer vc, Matrix4f m, float x0, float x1, float z0, float z1, float y, float r, float g, float b, float a, TextureAtlasSprite sprite, int overlay, int light) {
        float du = sprite.getU1() - sprite.getU0();
        float dv = sprite.getV1() - sprite.getV0();
        float u0 = sprite.getU0(), u1 = sprite.getU0() + du * (x1 - x0);
        float v0 = sprite.getV0(), v1 = sprite.getV0() + dv * (z1 - z0);
        vc.addVertex(m, x0, y, z0).setColor(r,g,b,a).setUv(u0,v0).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x0, y, z1).setColor(r,g,b,a).setUv(u0,v1).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x1, y, z1).setColor(r,g,b,a).setUv(u1,v1).setOverlay(overlay).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, x1, y, z0).setColor(r,g,b,a).setUv(u1,v0).setOverlay(overlay).setLight(light).setNormal(0,1,0);
    }

    @Override
    public boolean shouldRenderOffScreen(StompingBasinBlockEntity be) {
        return be.getMultiblockRole() != MultiblockPart.NONE;
    }
}