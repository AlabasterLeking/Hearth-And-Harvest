package alabaster.hearthandharvest.client.renderer;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.item.VintageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;

public final class DisplayModels {
    public static final String FOLDER = "display";

    private DisplayModels() {
    }

    @Nullable
    public static BakedModel get(ItemStack stack) {
        if (stack.isEmpty()) return null;
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return get(ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), FOLDER + "/" + itemId.getPath()));
    }

    @Nullable
    public static BakedModel vintageOverlay(String kind, ItemStack stack) {
        String stage = VintageHelper.overlayStage(kind, stack);
        if (stage == null) return null;
        return get(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, FOLDER + "/vintage/" + kind + "_" + stage));
    }

    @Nullable
    public static BakedModel get(ResourceLocation modelId) {
        ModelManager models = Minecraft.getInstance().getModelManager();
        BakedModel model = models.getModel(ModelResourceLocation.standalone(modelId));
        return model == models.getMissingModel() ? null : model;
    }
}