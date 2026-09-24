package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.common.block.CrateBlock;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import javax.annotation.Nullable;

public class CrateRenderer implements BlockEntityRenderer<CrateBlockEntity> {

    private static final double PX = 1.0 / 16.0;
    private static final double SPACING = PX;
    private static final double SLOT_SIZE = 4 * PX;

    private final ItemRenderer itemRenderer;

    public CrateRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CrateBlockEntity slab, float partialTick, PoseStack pose, MultiBufferSource buf, int light, int overlay) {

        BlockState state = slab.getBlockState();
        SlabType type = state.getValue(CrateBlock.TYPE);

        Direction facing = state.getValue(CrateBlock.FACING);
        float yRot = switch (facing) {
            case EAST -> -90f;
            case SOUTH -> 180f;
            case WEST -> 90f;
            default ->  0f;
        };

        pose.pushPose();
        pose.translate(0.5, 0.0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(yRot));
        pose.translate(-0.5, 0.0, -0.5);

        if (type == SlabType.BOTTOM || type == SlabType.DOUBLE) {
            renderHalf(slab, 0, 0.5, pose, buf, light, overlay);
        }
        if (type == SlabType.DOUBLE) {
            renderHalf(slab, CrateBlockEntity.SLOTS_PER_HALF, 1.0, pose, buf, light, overlay);
        }
        if (type == SlabType.TOP) {
            renderHalf(slab, 0, 1.0, pose, buf, light, overlay);
        }

        pose.popPose();
    }

    private void renderHalf(CrateBlockEntity slab, int slotOffset, double surfaceY, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
            ItemStack stack = slab.getItem(slotOffset + i);
            if (stack.isEmpty()) continue;

            BakedModel model = DisplayModels.get(stack);
            if (model == null) continue;

            int col = i % 3;
            int row = i / 3;

            double x = SPACING + col * (SLOT_SIZE + SPACING) + SLOT_SIZE / 2.0;
            double z = SPACING + row * (SLOT_SIZE + SPACING) + SLOT_SIZE / 2.0;
            double y = surfaceY + PX;

            pose.pushPose();
            pose.translate(x, y, z);
            itemRenderer.render(stack, ItemDisplayContext.FIXED, false, pose, buf, light, overlay, model);
            BakedModel vintage = DisplayModels.vintageOverlay("bottle", stack);
            if (vintage != null) {
                itemRenderer.render(stack, ItemDisplayContext.FIXED, false, pose, buf, light, overlay, vintage);
            }
            pose.popPose();
        }
    }
}