package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class JarItemModelProvider implements DataProvider {

    private static final List<String> JAR_NAMES = List.of(
            "jar",
            "apple_jam",
            "blueberry_jam",
            "cherry_jam",
            "grape_jam",
            "raspberry_jam",
            "sweet_berry_jam",
            "glow_berry_jam",
            "melon_jam",
            "peanut_butter",
            "pickled_beetroots",
            "pickled_cabbage",
            "pickled_carrots",
            "pickled_onions",
            "pickled_potatoes"
    );

    private final PackOutput.PathProvider itemModelPath;

    public JarItemModelProvider(PackOutput output) {
        this.itemModelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (String name : JAR_NAMES) {
            if (VintageItemModelProvider.JARS.contains(name)) {
                continue;
            }

            JsonObject twoD = new JsonObject();
            twoD.addProperty("parent", "minecraft:item/generated");
            JsonObject twoDTextures = new JsonObject();
            twoDTextures.addProperty("layer0", HearthAndHarvest.MODID + ":item/" + name);
            twoD.add("textures", twoDTextures);

            futures.add(DataProvider.saveStable(cache, twoD,
                    itemModelPath.json(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "2d_" + name))));

            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:item/generated");
            root.addProperty("loader", "neoforge:separate_transforms");

            JsonObject rootTextures = new JsonObject();
            rootTextures.addProperty("particle", HearthAndHarvest.MODID + ":item/" + name);
            rootTextures.addProperty("layer0", HearthAndHarvest.MODID + ":item/" + name);
            root.add("textures", rootTextures);

            JsonObject base = new JsonObject();
            base.addProperty("parent", HearthAndHarvest.MODID + ":block/" + name);
            root.add("base", base);

            JsonObject perspectives = new JsonObject();
            for (String ctx : List.of("gui", "ground", "fixed")) {
                JsonObject p = new JsonObject();
                p.addProperty("parent", HearthAndHarvest.MODID + ":item/2d_" + name);
                perspectives.add(ctx, p);
            }
            root.add("perspectives", perspectives);

            futures.add(DataProvider.saveStable(cache, root,
                    itemModelPath.json(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, name))));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Jar Item Models";
    }
}