package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.item.VintageHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VintageItemModelProvider implements DataProvider {
    public static final List<String> BOTTLES = List.of(
            "mead", "hard_cider", "root_beer", "beer",
            "blueberry_wine", "cherry_wine", "raspberry_wine", "red_grape_wine",
            "green_grape_wine", "sweet_berry_wine", "glow_berry_wine", "melon_wine"
    );

    public static final List<String> JARS = List.of(
            "pickled_beetroots", "pickled_cabbage", "pickled_carrots",
            "pickled_onions", "pickled_potatoes"
    );

    private final PackOutput.PathProvider itemModelPath;

    public VintageItemModelProvider(PackOutput output) {
        this.itemModelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (String name : BOTTLES) {
            futures.addAll(models(cache, name, "block/" + name + "_bottle", "bottle", false));
        }
        for (String name : JARS) {
            futures.addAll(models(cache, name, "block/" + name, "jar", true));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private List<CompletableFuture<?>> models(CachedOutput cache, String name, String baseModel, String overlay, boolean freshOverlay) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        JsonObject root = separateTransforms(name, baseModel, null, overlay, "item/2d_" + name);
        root.add("overrides", overrides(name));
        futures.add(save(cache, name, root));

        if (freshOverlay) {
            futures.add(save(cache, "2d_" + name, flat(name, "item/vintage/" + overlay + "_fresh")));
        }

        for (int vintage = 1; vintage <= VintageHelper.MAX_VINTAGE; vintage++) {
            String stage = VintageHelper.STAGE_NAMES[vintage - 1];
            String flatPath = "vintage/2d_" + name + "_" + stage;

            futures.add(save(cache, flatPath, flat(name, "item/vintage/" + overlay + "_" + stage)));
            futures.add(save(cache, "vintage/" + name + "_" + stage,
                    separateTransforms(name, baseModel, stage, overlay, "item/" + flatPath)));
        }
        return futures;
    }

    private JsonObject separateTransforms(String name, String baseModel, String stage, String overlay, String flatModel) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        model.addProperty("loader", "neoforge:separate_transforms");

        JsonObject textures = new JsonObject();
        textures.addProperty("particle", HearthAndHarvest.MODID + ":item/" + name);
        model.add("textures", textures);

        model.add("base", baseModel(baseModel, stage, overlay));

        JsonObject perspectives = new JsonObject();
        for (String context : List.of("gui", "ground", "fixed")) {
            JsonObject entry = new JsonObject();
            entry.addProperty("parent", HearthAndHarvest.MODID + ":" + flatModel);
            perspectives.add(context, entry);
        }
        model.add("perspectives", perspectives);
        return model;
    }

    private JsonObject baseModel(String baseModel, String stage, String overlay) {
        JsonObject base = new JsonObject();
        if (stage == null) {
            base.addProperty("parent", HearthAndHarvest.MODID + ":" + baseModel);
            return base;
        }

        JsonObject bottle = new JsonObject();
        bottle.addProperty("parent", HearthAndHarvest.MODID + ":" + baseModel);

        JsonObject seal = new JsonObject();
        seal.addProperty("parent", HearthAndHarvest.MODID + ":display/vintage/" + overlay + "_" + stage);

        JsonObject children = new JsonObject();
        children.add("base", bottle);
        children.add("seal", seal);

        base.addProperty("loader", "neoforge:composite");
        base.add("children", children);
        return base;
    }

    private JsonObject flat(String name, String overlayTexture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", HearthAndHarvest.MODID + ":item/" + name);
        if (overlayTexture != null) {
            textures.addProperty("layer1", HearthAndHarvest.MODID + ":" + overlayTexture);
        }
        model.add("textures", textures);
        return model;
    }

    private JsonArray overrides(String name) {
        JsonArray overrides = new JsonArray();
        for (int vintage = 1; vintage <= VintageHelper.MAX_VINTAGE; vintage++) {
            String stage = VintageHelper.STAGE_NAMES[vintage - 1];

            JsonObject predicate = new JsonObject();
            predicate.addProperty(HearthAndHarvest.MODID + ":vintage", vintage);

            JsonObject override = new JsonObject();
            override.add("predicate", predicate);
            override.addProperty("model", HearthAndHarvest.MODID + ":item/vintage/" + name + "_" + stage);
            overrides.add(override);
        }
        return overrides;
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject model) {
        return DataProvider.saveStable(cache, model,
                itemModelPath.json(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path)));
    }

    @Override
    public String getName() {
        return "Vintage Item Models";
    }
}