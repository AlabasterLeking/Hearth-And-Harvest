package alabaster.hearthandharvest.client.renderer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class CrateItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static CrateItemRenderer instance;

    public static CrateItemRenderer getInstance() {
        if (instance == null) {
            Minecraft mc = Minecraft.getInstance();
            instance = new CrateItemRenderer(
                    mc.getBlockEntityRenderDispatcher(),
                    mc.getEntityModels()
            );
        }
        return instance;
    }

    private final CrateBlockEntity dummyEntity;
    private final CrateRenderer crateRenderer;

    private CrateItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
        BlockState defaultState = HHModBlocks.CRATE.get().defaultBlockState();
        this.dummyEntity = new CrateBlockEntity(BlockPos.ZERO, defaultState);
        Minecraft mc = Minecraft.getInstance();
        this.crateRenderer = new CrateRenderer(new BlockEntityRendererProvider.Context(
                dispatcher,
                mc.getBlockRenderer(),
                mc.getItemRenderer(),
                mc.getEntityRenderDispatcher(),
                models,
                mc.font
                ));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();

        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data != null) {
            dummyEntity.loadItemsFromTag(data);
        } else {
            dummyEntity.clearContent();
        }

        BlockState renderState = dummyEntity.getBlockState();

        pose.pushPose();
        pose.translate(0.0, 0.25, 0.0);

        mc.getBlockRenderer().renderSingleBlock(
                renderState, pose, buf, light, overlay, ModelData.EMPTY, null
        );

        crateRenderer.render(dummyEntity, 0f, pose, buf, light, overlay);

        pose.popPose();
    }
}