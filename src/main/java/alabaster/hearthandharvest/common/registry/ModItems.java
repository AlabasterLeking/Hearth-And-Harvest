package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
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

    // Crops
    public static final Supplier<Item> RASPBERRY = registerWithTab("raspberry",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BLUEBERRIES = registerWithTab("blueberries",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GRAPES = registerWithTab("grapes",
            () -> new Item(basicItem()));
    public static final Supplier<Item> PEANUT = registerWithTab("peanut",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COTTON = registerWithTab("cotton",
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

    // Pickled Vegetables

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

    // Ingredients
    public static final Supplier<Item> CHEESE_WHEEL = registerWithTab("cheese_wheel",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CHEESE_SLICE = registerWithTab("cheese_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_WHEEL = registerWithTab("goat_cheese_wheel",
            () -> new Item(basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_SLICE = registerWithTab("goat_cheese_slice",
            () -> new Item(basicItem()));
    public static final Supplier<Item> RAW_SAUSAGE = registerWithTab("raw_sausage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> COOKED_SAUSAGE = registerWithTab("cooked_sausage",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BATTER = registerWithTab("batter",
            () -> new Item(basicItem()));
    public static final Supplier<Item> SALT = registerWithTab("salt",
            () -> new Item(basicItem()));
}