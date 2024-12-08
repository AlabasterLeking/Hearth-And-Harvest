package alabaster.hearthandharvest.common.register;

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

    public static Supplier<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        Supplier<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    // Helper methods
    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    // Crops
    public static final Supplier<Item> RASPBERRY = registerWithTab("raspberry",
            () -> new Item(basicItem()));

}