package alabaster.hearthandharvest.common.registry;

import java.util.List;
import java.io.Reader;
import net.minecraft.util.GsonHelper;
import net.minecraft.server.packs.resources.Resource;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.data.VintageStyle;
import alabaster.hearthandharvest.common.item.WineBottleItem;
import alabaster.hearthandharvest.common.network.HHModNetworking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class HHDataMaps extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();

    public static final int BOTTLE_VOLUME = 250;

    public static int getBottleVolume(Item item) {
        return item instanceof WineBottleItem wine ? wine.getMaxGlasses() * BOTTLE_VOLUME : BOTTLE_VOLUME;
    }

    public static int getBottleVolume(ItemStack stack) {
        return stack.getItem() instanceof WineBottleItem wine ? wine.getGlasses(stack) * BOTTLE_VOLUME : BOTTLE_VOLUME;
    }

    public static int getBottleVolumeForFluid(Fluid fluid) {
        Item bottle = getBottleForFluid(fluid);
        return bottle == null ? BOTTLE_VOLUME : getBottleVolume(bottle);
    }

    private static final String FLUID_BOTTLE = "fluid/fluid_bottle";
    private static final String VINTAGE_STYLE = "item/vintage_style";
    private static final String COMPOSTABLES = "item/compostables";

    private static volatile Map<Fluid, Item> FLUID_TO_BOTTLE = Map.of();
    private static volatile Map<Item, Fluid> BOTTLE_TO_FLUID = Map.of();
    private static volatile Map<Item, VintageStyle> VINTAGE_STYLES = Map.of();
    private static volatile Map<ResourceLocation, String> SYNCED = Map.of();

    private static final String DIRECTORY = "data_maps";

    public HHDataMaps() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonElement> merged = new HashMap<>();
        for (Map.Entry<ResourceLocation, List<Resource>> entry : resourceManager.listResourceStacks(DIRECTORY, file -> file.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation file = entry.getKey();
            String path = file.getPath();
            ResourceLocation id = new ResourceLocation(file.getNamespace(), path.substring(DIRECTORY.length() + 1, path.length() - 5));
            JsonObject values = new JsonObject();
            for (Resource resource : entry.getValue()) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject json = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                    if (json == null) continue;
                    if (GsonHelper.getAsBoolean(json, "replace", false)) values = new JsonObject();
                    for (Map.Entry<String, JsonElement> value : GsonHelper.getAsJsonObject(json, "values", new JsonObject()).entrySet())
                        values.add(value.getKey(), value.getValue());
                    if (json.has("remove")) {
                        for (JsonElement removed : json.getAsJsonArray("remove")) {
                            String key = removed.isJsonObject() ? GsonHelper.getAsString(removed.getAsJsonObject(), "key") : removed.getAsString();
                            values.remove(key);
                        }
                    }
                } catch (Exception e) {
                    HearthAndHarvest.LOGGER.error("Failed to load data map {} from pack {}", id, resource.sourcePackId(), e);
                }
            }
            JsonObject result = new JsonObject();
            result.add("values", values);
            merged.put(id, result);
        }
        return merged;
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new HHDataMaps());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        HHModNetworking.DataMapSyncPacket packet = new HHModNetworking.DataMapSyncPacket(SYNCED);
        if (event.getPlayer() != null) HHModNetworking.sendToPlayer(event.getPlayer(), packet);
        else HHModNetworking.sendToAll(packet);
    }

    @Override
    public void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, String> synced = new HashMap<>();
        Map<ResourceLocation, JsonElement> relevant = new HashMap<>();
        entries.forEach((id, json) -> {
            String path = id.getPath();
            if (path.equals(FLUID_BOTTLE) || path.equals(VINTAGE_STYLE)) {
                synced.put(id, json.toString());
                relevant.put(id, json);
            } else if (path.equals(COMPOSTABLES)) {
                applyCompostables(json);
            }
        });
        SYNCED = synced;
        rebuild(relevant);
    }

    public static void applySynced(Map<ResourceLocation, String> entries) {
        Map<ResourceLocation, JsonElement> parsed = new HashMap<>();
        entries.forEach((id, json) -> parsed.put(id, JsonParser.parseString(json)));
        rebuild(parsed);
    }

    private static void rebuild(Map<ResourceLocation, JsonElement> entries) {
        Map<Fluid, Item> fluidToBottle = new IdentityHashMap<>();
        Map<Item, VintageStyle> vintage = new IdentityHashMap<>();
        entries.forEach((id, json) -> {
            JsonObject values = json.getAsJsonObject().getAsJsonObject("values");
            if (values == null) return;
            if (id.getPath().equals(FLUID_BOTTLE)) {
                values.entrySet().forEach(e -> {
                    Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(e.getKey()));
                    Item bottle = BuiltInRegistries.ITEM.get(new ResourceLocation(unwrap(e.getValue()).getAsString()));
                    if (fluid != Fluids.EMPTY && bottle != Items.AIR) fluidToBottle.put(fluid, bottle);
                });
            } else if (id.getPath().equals(VINTAGE_STYLE)) {
                values.entrySet().forEach(e -> {
                    Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(e.getKey()));
                    if (item == Items.AIR) return;
                    VintageStyle.CODEC.parse(JsonOps.INSTANCE, unwrap(e.getValue())).result().ifPresent(style -> vintage.put(item, style));
                });
            }
        });
        Map<Item, Fluid> reverse = new IdentityHashMap<>();
        fluidToBottle.forEach((fluid, bottle) -> reverse.putIfAbsent(bottle, fluid));
        FLUID_TO_BOTTLE = fluidToBottle;
        BOTTLE_TO_FLUID = reverse;
        VINTAGE_STYLES = vintage;
        HearthAndHarvest.LOGGER.info("Loaded {} fluid bottle mappings and {} vintage styles", fluidToBottle.size(), vintage.size());
    }

    private static void applyCompostables(JsonElement json) {
        JsonObject values = json.getAsJsonObject().getAsJsonObject("values");
        if (values == null) return;
        values.entrySet().forEach(e -> {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(e.getKey()));
            if (item == Items.AIR) return;
            JsonElement value = unwrap(e.getValue());
            float chance = value.isJsonObject() ? value.getAsJsonObject().get("chance").getAsFloat() : value.getAsFloat();
            ComposterBlock.COMPOSTABLES.put(item, chance);
        });
    }

    private static JsonElement unwrap(JsonElement element) {
        if (element.isJsonObject() && element.getAsJsonObject().has("value") && element.getAsJsonObject().size() <= 2) {
            return element.getAsJsonObject().get("value");
        }
        return element;
    }

    @Nullable
    public static VintageStyle getVintageStyle(Item item) {
        if (item == null || item == Items.AIR) return null;
        return VINTAGE_STYLES.get(item);
    }

    @Nullable
    public static Item getBottleForFluid(Fluid fluid) {
        if (fluid == null) return null;
        Fluid source = fluid instanceof FlowingFluid flowing ? flowing.getSource() : fluid;
        return FLUID_TO_BOTTLE.get(source);
    }

    @Nullable
    public static Fluid getFluidForBottle(Item item) {
        if (item == null || item == Items.AIR) return null;
        return BOTTLE_TO_FLUID.get(item);
    }
}