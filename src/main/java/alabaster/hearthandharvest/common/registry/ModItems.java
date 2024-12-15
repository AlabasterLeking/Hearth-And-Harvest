package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, HearthAndHarvest.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    public static Supplier<Item> registerWithTab(String name, Supplier<Item> supplier) {
        Supplier<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    // Helper methods
    public static Item.Properties basicItem() {
        return (new Item.Properties());
    }

    // Workstations
    public static final Supplier<Item> TREE_TAPPER = registerWithTab("tree_tapper",
            () -> new BlockItem(ModBlocks.TREE_TAPPER.get(), basicItem()));

    // Crops
    public static final Supplier<Item> RASPBERRY = registerWithTab("raspberry",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRIES = registerWithTab("blueberries",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GRAPES = registerWithTab("grapes",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PEANUT = registerWithTab("peanut",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COTTON_SEEDS = registerWithTab("cotton_seeds",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COTTON = registerWithTab("cotton",
            () -> new Item(basicItem()));
    
    // Storage Blocks

    // Crates
    public static final Supplier<Item> RASPBERRY_CRATE = registerWithTab("raspberry_crate",
            () -> new BlockItem(ModBlocks.RASPBERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> BLUEBERRY_CRATE = registerWithTab("blueberry_crate",
            () -> new BlockItem(ModBlocks.BLUEBERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> GRAPE_CRATE = registerWithTab("grape_crate",
            () -> new BlockItem(ModBlocks.GRAPE_CRATE.get(), basicItem()));
    public static final Supplier<Item> PEANUT_CRATE = registerWithTab("peanut_crate",
            () -> new BlockItem(ModBlocks.PEANUT_CRATE.get(), basicItem()));
    public static final Supplier<Item> SALT_BAG = registerWithTab("salt_bag",
            () -> new BlockItem(ModBlocks.SALT_BAG.get(), basicItem()));
    public static final Supplier<Item> COTTON_BALE = registerWithTab("cotton_bale",
            () -> new BlockItem(ModBlocks.COTTON_BALE.get(), basicItem()));
    public static final Supplier<Item> SPOOL = registerWithTab("spool",
            () -> new BlockItem(ModBlocks.SPOOL.get(), basicItem()));
    public static final Supplier<Item> EGG_CRATE = registerWithTab("egg_crate",
            () -> new BlockItem(ModBlocks.EGG_CRATE.get(), basicItem()));
    public static final Supplier<Item> MILK_CRATE = registerWithTab("milk_crate",
            () -> new BlockItem(ModBlocks.MILK_CRATE.get(), basicItem()));
    public static final Supplier<Item> GOAT_MILK_CRATE = registerWithTab("goat_milk_crate",
            () -> new BlockItem(ModBlocks.GOAT_MILK_CRATE.get(), basicItem()));
    public static final Supplier<Item> MEAD_CRATE = registerWithTab("mead_crate",
            () -> new BlockItem(ModBlocks.MEAD_CRATE.get(), basicItem()));
    public static final Supplier<Item> WINE_CRATE = registerWithTab("wine_crate",
            () -> new BlockItem(ModBlocks.WINE_CRATE.get(), basicItem()));
    public static final Supplier<Item> WATER_CRATE = registerWithTab("water_crate",
            () -> new BlockItem(ModBlocks.WATER_CRATE.get(), basicItem()));
    public static final Supplier<Item> HONEY_CRATE = registerWithTab("honey_crate",
            () -> new BlockItem(ModBlocks.HONEY_CRATE.get(), basicItem()));
    public static final Supplier<Item> BROWN_MUSHROOM_CRATE = registerWithTab("brown_mushroom_crate",
            () -> new BlockItem(ModBlocks.BROWN_MUSHROOM_CRATE.get(), basicItem()));
    public static final Supplier<Item> RED_MUSHROOM_CRATE = registerWithTab("red_mushroom_crate",
            () -> new BlockItem(ModBlocks.RED_MUSHROOM_CRATE.get(), basicItem()));
    public static final Supplier<Item> CRIMSON_FUNGUS_CRATE = registerWithTab("crimson_fungus_crate",
            () -> new BlockItem(ModBlocks.CRIMSON_FUNGUS_CRATE.get(), basicItem()));
    public static final Supplier<Item> WARPED_FUNGUS_CRATE = registerWithTab("warped_fungus_crate",
            () -> new BlockItem(ModBlocks.WARPED_FUNGUS_CRATE.get(), basicItem()));

    // Tools
    public static final Supplier<Item> WOODEN_BUCKET = registerWithTab("wooden_bucket",
            () -> new Item(basicItem()));

    // Drinks
    public static final Supplier<Item> MEAD = registerWithTab("mead",
            () -> new Item(basicItem()));
    public static final Supplier<Item> WINE = registerWithTab("wine",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GOAT_MILK_BOTTLE = registerWithTab("goat_milk_bottle",
            () -> new Item(basicItem()));
    public static final Supplier<Item> RASPBERRY_JUICE = registerWithTab("raspberry_juice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRY_JUICE = registerWithTab("blueberry_juice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GRAPE_JUICE = registerWithTab("grape_juice",
            () -> new Item(basicItem()));

    // Jams
    public static final Supplier<Item> RASPBERRY_JAM = registerWithTab("raspberry_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRY_JAM = registerWithTab("blueberry_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GRAPE_JAM = registerWithTab("grape_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> APPLE_JAM = registerWithTab("apple_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SWEET_BERRY_JAM = registerWithTab("sweet_berry_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GLOW_BERRY_JAM = registerWithTab("glow_berry_jam",
            () -> new Item(basicItem()));
    public static final Supplier<Item> MELON_JAM = registerWithTab("melon_jam",
            () -> new Item(basicItem()));

    // Pickled Vegetables
    public static final Supplier<Item> PICKLED_BEETROOTS = registerWithTab("pickled_beetroots",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PICKLED_CABBAGE = registerWithTab("pickled_cabbage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PICKLED_CARROTS = registerWithTab("pickled_carrots",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PICKLED_ONIONS = registerWithTab("pickled_onions",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PICKLED_POTATOES = registerWithTab("pickled_potatoes",
            () -> new Item(basicItem()));

    // Sweets
    public static final Supplier<Item> CARAMEL = registerWithTab("caramel",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CHOCOLATE_BAR = registerWithTab("chocolate_bar",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COTTON_CANDY = registerWithTab("cotton_candy",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRY_MUFFIN = registerWithTab("blueberry_muffin",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PEANUT_BUTTER_COOKIE = registerWithTab("peanut_butter_cookie",
            () -> new Item(basicItem()));

    // Sap and Syrup
    public static final Supplier<Item> SAP_BUCKET = registerWithTab("sap_bucket",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SYRUP_BOTTLE = registerWithTab("syrup_bottle",
            () -> new Item(basicItem()));

    // Pies
    public static final Supplier<Item> RASPBERRY_PIE = registerWithTab("raspberry_pie",
            () -> new BlockItem(ModBlocks.RASPBERRY_PIE.get(), basicItem()));
    public static final Supplier<Item> RASPBERRY_PIE_SLICE = registerWithTab("raspberry_pie_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRY_PIE = registerWithTab("blueberry_pie",
            () -> new BlockItem(ModBlocks.BLUEBERRY_PIE.get(), basicItem()));
    public static final Supplier<Item> BLUEBERRY_PIE_SLICE = registerWithTab("blueberry_pie_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GRAPE_PIE = registerWithTab("grape_pie",
            () -> new BlockItem(ModBlocks.GRAPE_PIE.get(), basicItem()));
    public static final Supplier<Item> GRAPE_PIE_SLICE = registerWithTab("grape_pie_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CHICKEN_POT_PIE = registerWithTab("chicken_pot_pie",
            () -> new BlockItem(ModBlocks.CHICKEN_POT_PIE.get(), basicItem()));
    public static final Supplier<Item> CHICKEN_POT_PIE_SLICE = registerWithTab("chicken_pot_pie_slice",
            () -> new Item(basicItem()));


    // Ingredients
    public static final Supplier<Item> PEANUT_BUTTER = registerWithTab("peanut_butter",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CHEESE_WHEEL = registerWithTab("cheese_wheel",
            () -> new BlockItem(ModBlocks.CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> CHEESE_SLICE = registerWithTab("cheese_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_WHEEL = registerWithTab("goat_cheese_wheel",
            () -> new BlockItem(ModBlocks.GOAT_CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_SLICE = registerWithTab("goat_cheese_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> RAW_SAUSAGE = registerWithTab("raw_sausage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COOKED_SAUSAGE = registerWithTab("cooked_sausage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SKEWERED_SAUSAGE = registerWithTab("skewered_sausage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> JERKY = registerWithTab("jerky",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BATTER = registerWithTab("batter",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SALT = registerWithTab("salt",
            () -> new Item(basicItem()));
    public static final Supplier<Item> RAISINS = registerWithTab("raisins",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SUNFLOWER_SEEDS = registerWithTab("sunflower_seeds",
            () -> new Item(basicItem()));

    // Meals
    public static final Supplier<Item> MACARONI_AND_CHEESE = registerWithTab("macaroni_and_cheese",
            () -> new Item(basicItem()));
    public static final Supplier<Item> MASHED_POTATOES = registerWithTab("mashed_potatoes",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PEANUT_BUTTER_AND_JELLY_SANDWICH = registerWithTab("peanut_butter_and_jelly_sandwich",
            () -> new Item(basicItem()));
    public static final Supplier<Item> WAFFLE = registerWithTab("waffle",
            () -> new Item(basicItem()));
}