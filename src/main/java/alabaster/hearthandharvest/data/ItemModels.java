package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemModels extends ItemModelProvider
{
    public static final String GENERATED = "item/generated";
    public static final String HANDHELD = "item/handheld";

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HearthAndHarvest.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = BuiltInRegistries.ITEM.stream().filter(i -> HearthAndHarvest.MODID.equals(BuiltInRegistries.ITEM.getKey(i).getNamespace()))
                .collect(Collectors.toSet());

        // Generated items
        items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
    }

    public void blockBasedModel(Item item, String suffix) {
        withExistingParent(itemName(item), resourceBlock(itemName(item) + suffix));
    }

    public void itemHandheldModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), HANDHELD).texture("layer0", texture);
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }

    private String itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/" + path);
    }

    public ResourceLocation resourceItem(String path) {
        return ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "item/" + path);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                HearthAndHarvest.LOGGER.warn("Item {} not found in set", item);
            }
        }
        if (!src.removeAll(ret)) {
            HearthAndHarvest.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            HearthAndHarvest.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }
        return ret;
    }
}