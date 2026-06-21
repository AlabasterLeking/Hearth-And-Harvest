package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModItems;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
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

        // Specific Cases
        items.remove(HHModItems.CROW_SPAWN_EGG.get());
        items.remove(HHModItems.SCARECROW.get());
        items.remove(HHModItems.SALT_DRIP.get());
        items.remove(HHModItems.PITCHFORK.get());
        items.remove(HHModItems.FARMERS_HAT.get());

        // Workstations / Functional
        blockBasedModel(HHModItems.TREE_TAPPER.get(),"");
        items.remove(HHModItems.TREE_TAPPER.get());
        blockBasedModel(HHModItems.CASK.get(),"");
        items.remove(HHModItems.CASK.get());
        blockBasedModel(HHModItems.STOMPING_BASIN.get(),"");
        items.remove(HHModItems.STOMPING_BASIN.get());
        blockBasedModel(HHModItems.JUG.get(),"");
        items.remove(HHModItems.JUG.get());
        blockBasedModel(HHModItems.COUNTER.get(),"");
        items.remove(HHModItems.COUNTER.get());
        blockBasedModel(HHModItems.DRAWER.get(),"");
        items.remove(HHModItems.DRAWER.get());
        blockBasedModel(HHModItems.BASIN.get(),"");
        items.remove(HHModItems.BASIN.get());

        items.remove(HHModItems.CRATE.get());

        blockBasedModel(HHModItems.NEST.get(),"");
        items.remove(HHModItems.NEST.get());
        blockBasedModel(HHModItems.HAY_RUG.get(),"");
        items.remove(HHModItems.HAY_RUG.get());
        blockBasedModel(HHModItems.STRAW_RUG.get(),"");
        items.remove(HHModItems.STRAW_RUG.get());

        // Half Cabinets
        blockBasedModel(HHModItems.OAK_HALF_CABINET.get(),"");
        items.remove(HHModItems.OAK_HALF_CABINET.get());
        blockBasedModel(HHModItems.BIRCH_HALF_CABINET.get(),"");
        items.remove(HHModItems.BIRCH_HALF_CABINET.get());
        blockBasedModel(HHModItems.SPRUCE_HALF_CABINET.get(),"");
        items.remove(HHModItems.SPRUCE_HALF_CABINET.get());
        blockBasedModel(HHModItems.JUNGLE_HALF_CABINET.get(),"");
        items.remove(HHModItems.JUNGLE_HALF_CABINET.get());
        blockBasedModel(HHModItems.ACACIA_HALF_CABINET.get(),"");
        items.remove(HHModItems.ACACIA_HALF_CABINET.get());
        blockBasedModel(HHModItems.DARK_OAK_HALF_CABINET.get(),"");
        items.remove(HHModItems.DARK_OAK_HALF_CABINET.get());
        blockBasedModel(HHModItems.MANGROVE_HALF_CABINET.get(),"");
        items.remove(HHModItems.MANGROVE_HALF_CABINET.get());
        blockBasedModel(HHModItems.CHERRY_HALF_CABINET.get(),"");
        items.remove(HHModItems.CHERRY_HALF_CABINET.get());
        blockBasedModel(HHModItems.BAMBOO_HALF_CABINET.get(),"");
        items.remove(HHModItems.BAMBOO_HALF_CABINET.get());
        blockBasedModel(HHModItems.CRIMSON_HALF_CABINET.get(),"");
        items.remove(HHModItems.CRIMSON_HALF_CABINET.get());
        blockBasedModel(HHModItems.WARPED_HALF_CABINET.get(),"");
        items.remove(HHModItems.WARPED_HALF_CABINET.get());

        // Bottle Racks
        blockBasedModel(HHModItems.OAK_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.OAK_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.BIRCH_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.BIRCH_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.SPRUCE_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.SPRUCE_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.JUNGLE_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.JUNGLE_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.ACACIA_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.ACACIA_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.DARK_OAK_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.DARK_OAK_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.MANGROVE_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.MANGROVE_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.CHERRY_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.CHERRY_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.BAMBOO_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.BAMBOO_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.CRIMSON_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.CRIMSON_BOTTLE_RACK.get());
        blockBasedModel(HHModItems.WARPED_BOTTLE_RACK.get(),"");
        items.remove(HHModItems.WARPED_BOTTLE_RACK.get());

        // Campfire Roastables
        items.remove(HHModItems.MARSHMALLOW_STICK.get());
        items.remove(HHModItems.ROASTED_MARSHMALLOW_STICK.get());
        items.remove(HHModItems.CHARRED_MARSHMALLOW_STICK.get());

        // Bottles
        items.remove(HHModItems.BLUEBERRY_WINE.get());
        items.remove(HHModItems.CHERRY_WINE.get());
        items.remove(HHModItems.RASPBERRY_WINE.get());
        items.remove(HHModItems.GREEN_GRAPE_WINE.get());
        items.remove(HHModItems.RED_GRAPE_WINE.get());
        items.remove(HHModItems.SWEET_BERRY_WINE.get());
        items.remove(HHModItems.GLOW_BERRY_WINE.get());
        items.remove(HHModItems.MELON_WINE.get());
        items.remove(HHModItems.MEAD.get());
        items.remove(HHModItems.HARD_CIDER.get());
        items.remove(HHModItems.ROOT_BEER.get());

        // Watering Can
        items.remove(HHModItems.WATERING_CAN.get());

        // Slab Crates
        blockBasedModel(HHModItems.BROWN_MUSHROOM_CRATE.get(), "_bottom");
        items.remove(HHModItems.BROWN_MUSHROOM_CRATE.get());
        blockBasedModel(HHModItems.RED_MUSHROOM_CRATE.get(), "_bottom");
        items.remove(HHModItems.RED_MUSHROOM_CRATE.get());
        blockBasedModel(HHModItems.CRIMSON_FUNGUS_CRATE.get(), "_bottom");
        items.remove(HHModItems.CRIMSON_FUNGUS_CRATE.get());
        blockBasedModel(HHModItems.WARPED_FUNGUS_CRATE.get(), "_bottom");
        items.remove(HHModItems.WARPED_FUNGUS_CRATE.get());
        blockBasedModel(HHModItems.SALT_LAMP.get(),"_unlit");
        items.remove(HHModItems.SALT_LAMP.get());

        // Jars
        items.remove(HHModItems.JAR.get());
        items.remove(HHModItems.BLUEBERRY_JAM.get());
        items.remove(HHModItems.CHERRY_JAM.get());
        items.remove(HHModItems.GRAPE_JAM.get());
        items.remove(HHModItems.RASPBERRY_JAM.get());
        items.remove(HHModItems.APPLE_JAM.get());
        items.remove(HHModItems.SWEET_BERRY_JAM.get());
        items.remove(HHModItems.GLOW_BERRY_JAM.get());
        items.remove(HHModItems.MELON_JAM.get());
        items.remove(HHModItems.PEANUT_BUTTER.get());
        items.remove(HHModItems.PICKLED_BEETROOTS.get());
        items.remove(HHModItems.PICKLED_CABBAGE.get());
        items.remove(HHModItems.PICKLED_CARROTS.get());
        items.remove(HHModItems.PICKLED_ONIONS.get());
        items.remove(HHModItems.PICKLED_POTATOES.get());

        // Walls
        wallItemModel(HHModItems.SALT_WALL.get(), resourceBlock("salt_block"));
        items.remove(HHModItems.SALT_WALL.get());
        wallItemModel(HHModItems.POLISHED_SALT_WALL.get(), resourceBlock("polished_salt_block"));
        items.remove(HHModItems.POLISHED_SALT_WALL.get());
        wallItemModel(HHModItems.MANURE_BRICK_WALL.get(), resourceBlock("manure_bricks"));
        items.remove(HHModItems.MANURE_BRICK_WALL.get());
        wallItemModel(HHModItems.POLISHED_MANURE_WALL.get(), resourceBlock("polished_manure"));
        items.remove(HHModItems.POLISHED_MANURE_WALL.get());

        // Blocks with special item sprites
        Set<Item> spriteBlockItems = Sets.newHashSet(
                HHModItems.TROUGH.get(),
                HHModItems.TRELLIS.get(),
                HHModItems.BAMBOO_TRELLIS.get(),
                HHModItems.STRIPPED_BAMBOO_TRELLIS.get(),
                HHModItems.SUNFLOWER_SEEDS.get(),
                HHModItems.RASPBERRY.get(),
                HHModItems.BLUEBERRIES.get(),
                HHModItems.RED_GRAPES.get(),
                HHModItems.GREEN_GRAPES.get(),
                HHModItems.PEANUT.get(),
                HHModItems.COTTON_SEEDS.get(),
                HHModItems.CORN_KERNELS.get(),
                HHModItems.RASPBERRY_PIE.get(),
                HHModItems.BLUEBERRY_PIE.get(),
                HHModItems.GRAPE_PIE.get(),
                HHModItems.PEANUT_BUTTER_PIE.get(),
                HHModItems.CHICKEN_POT_PIE.get(),
                HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(),
                HHModItems.CHEDDAR_CHEESE_WHEEL.get(),
                HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get(),
                HHModItems.GOAT_CHEESE_WHEEL.get(),
                HHModItems.CARROT_CAKE.get()
        );
        takeAll(items, spriteBlockItems.toArray(new Item[0])).forEach(item -> withExistingParent(itemName(item), GENERATED).texture("layer0", resourceItem(itemName(item))));

        // Blocks with flat block textures for their items
        Set<Item> flatBlockItems = Sets.newHashSet(
                HHModItems.WILD_RED_GRAPES.get(),
                HHModItems.WILD_GREEN_GRAPES.get(),
                HHModItems.WILD_COTTON.get(),
                HHModItems.WILD_PEANUTS.get(),
                HHModItems.YELLOW_MUM.get(),
                HHModItems.ORANGE_MUM.get(),
                HHModItems.RED_MUM.get(),
                HHModItems.BLUE_MUM.get(),
                HHModItems.LIGHT_BLUE_MUM.get(),
                HHModItems.PURPLE_MUM.get(),
                HHModItems.PINK_MUM.get(),
                HHModItems.WHITE_MUM.get()
        );

        takeAll(items, flatBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(item, resourceBlock(itemName(item))));
        // Blocks whose items look alike
        takeAll(items, i -> i instanceof BlockItem).forEach(item -> blockBasedModel(item, ""));

        // Handheld items
        Set<Item> handheldItems = Sets.newHashSet(
                HHModItems.FLINT_CLEAVER.get(),
                HHModItems.IRON_CLEAVER.get(),
                HHModItems.DIAMOND_CLEAVER.get(),
                HHModItems.GOLDEN_CLEAVER.get(),
                HHModItems.NETHERITE_CLEAVER.get(),
                HHModItems.UNCOOKED_CORN_ON_THE_COB.get(),
                HHModItems.COOKED_CORN_ON_THE_COB.get(),
                HHModItems.RAW_SKEWERED_SAUSAGE.get(),
                HHModItems.SKEWERED_SAUSAGE.get()
        );
        takeAll(items, handheldItems.toArray(new Item[0])).forEach(item -> itemHandheldModel(item, resourceItem(itemName(item))));

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

    public void wallItemModel(Item item, ResourceLocation wallTexture) {
        withExistingParent(itemName(item), "minecraft:block/wall_inventory")
                .texture("wall", wallTexture);
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