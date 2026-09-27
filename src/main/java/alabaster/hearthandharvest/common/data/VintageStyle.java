package alabaster.hearthandharvest.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;

public record VintageStyle(boolean ageable, boolean showFresh, String overlay, boolean drawModel,
                           Map<String, ResourceLocation> models) {
    public static final String NO_OVERLAY = "none";
    public static final String DEFAULT_NAMESPACE = "hearthandharvest";

    public static final Codec<VintageStyle> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("ageable", true).forGetter(VintageStyle::ageable),
                    Codec.BOOL.optionalFieldOf("show_fresh", false).forGetter(VintageStyle::showFresh),
                    Codec.STRING.optionalFieldOf("overlay", NO_OVERLAY).forGetter(VintageStyle::overlay),
                    Codec.BOOL.optionalFieldOf("draw_model", true).forGetter(VintageStyle::drawModel),
                    Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).optionalFieldOf("models", Map.of()).forGetter(VintageStyle::models)
            ).apply(instance, VintageStyle::new)
    );

    public VintageStyle(boolean ageable, boolean showFresh, String overlay, boolean drawModel) {
        this(ageable, showFresh, overlay, drawModel, Map.of());
    }

    public boolean hasOverlay() {
        return !NO_OVERLAY.equals(overlay) || !models.isEmpty();
    }

    @Nullable
    public ResourceLocation modelFor(String stage) {
        ResourceLocation custom = models.get(stage);
        if (custom != null) return custom;
        if (NO_OVERLAY.equals(overlay)) return null;

        ResourceLocation prefix = prefix();
        return ResourceLocation.fromNamespaceAndPath(prefix.getNamespace(), "display/vintage/" + prefix.getPath() + "_" + stage);
    }

    private ResourceLocation prefix() {
        return overlay.indexOf(':') >= 0
                ? ResourceLocation.parse(overlay)
                : ResourceLocation.fromNamespaceAndPath(DEFAULT_NAMESPACE, overlay);
    }
}