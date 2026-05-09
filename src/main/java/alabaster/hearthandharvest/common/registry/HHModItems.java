package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.HHFoodValues;
import alabaster.hearthandharvest.common.item.*;
import com.google.common.collect.Sets;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.registry.ModMaterials;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class HHModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HearthAndHarvest.MODID);
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_BLOCKS = Sets.newLinkedHashSet();

    public static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    public static RegistryObject<Item> registerWithBlockTab(final String name, final Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_BLOCKS.add(block);
        return block;
    }

    // Helper methods
    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return new Item.Properties().food(food);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    public static Item.Properties drinkItem() {
        return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Item.Properties jarItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(HHModItems.JAR.get()).stacksTo(16);
    }

    // Tools
    public static final RegistryObject<Item> FLINT_CLEAVER = registerWithTab("flint_cleaver",
            () -> new KnifeItem(ModMaterials.FLINT, 2.0F, -3.0F, basicItem()));
    public static final RegistryObject<Item> IRON_CLEAVER = registerWithTab("iron_cleaver",
            () -> new KnifeItem(Tiers.IRON, 2.0F, -3.0F, basicItem()));
    public static final RegistryObject<Item> DIAMOND_CLEAVER = registerWithTab("diamond_cleaver",
            () -> new KnifeItem(Tiers.DIAMOND, 2.0F, -3.0F, basicItem()));
    public static final RegistryObject<Item> NETHERITE_CLEAVER = registerWithTab("netherite_cleaver",
            () -> new KnifeItem(Tiers.NETHERITE, 2.0F, -3.0F, basicItem().fireResistant()));
    public static final RegistryObject<Item> GOLDEN_CLEAVER = registerWithTab("golden_cleaver",
            () -> new KnifeItem(Tiers.GOLD, 2.0F, -3.0F, basicItem()));

    public static final RegistryObject<Item> WATERING_CAN = registerWithTab("watering_can",
            () -> new WateringCanItem(basicItem()));
    public static final RegistryObject<Item> UNIVERSAL_FEED = registerWithTab("universal_feed",
            () -> new UniversalFeedItem(basicItem()));

    // Workstations
    public static final RegistryObject<Item> TREE_TAPPER = registerWithTab("tree_tapper",
            () -> new BlockItem(HHModBlocks.TREE_TAPPER.get(), basicItem()));
    public static final RegistryObject<Item> CASK = registerWithTab("cask",
            () -> new BlockItem(HHModBlocks.CASK.get(), basicItem()));
    public static final RegistryObject<Item> JUG = registerWithTab("jug",
            () -> new JugBlockItem(HHModBlocks.JUG.get(), basicItem()));

    public static final Supplier<Item> COUNTER = registerWithBlockTab("counter",
            () -> new BlockItem(HHModBlocks.COUNTER.get(), basicItem()));
    public static final Supplier<Item> DRAWER = registerWithBlockTab("drawer",
            () -> new BlockItem(HHModBlocks.DRAWER.get(), basicItem()));
    public static final Supplier<Item> BASIN = registerWithBlockTab("basin",
            () -> new BlockItem(HHModBlocks.BASIN.get(), basicItem()));

    // Half-Cabinets
    public static final Supplier<Item> OAK_HALF_CABINET = registerWithBlockTab("oak_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.OAK_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> SPRUCE_HALF_CABINET = registerWithBlockTab("spruce_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.SPRUCE_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> BIRCH_HALF_CABINET = registerWithBlockTab("birch_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.BIRCH_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> JUNGLE_HALF_CABINET = registerWithBlockTab("jungle_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.JUNGLE_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> ACACIA_HALF_CABINET = registerWithBlockTab("acacia_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.ACACIA_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> DARK_OAK_HALF_CABINET = registerWithBlockTab("dark_oak_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.DARK_OAK_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> MANGROVE_HALF_CABINET = registerWithBlockTab("mangrove_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.MANGROVE_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> CHERRY_HALF_CABINET = registerWithBlockTab("cherry_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.CHERRY_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> BAMBOO_HALF_CABINET = registerWithBlockTab("bamboo_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.BAMBOO_HALF_CABINET.get(), basicItem(), 300));
    public static final Supplier<Item> CRIMSON_HALF_CABINET = registerWithBlockTab("crimson_half_cabinet",
            () -> new BlockItem(HHModBlocks.CRIMSON_HALF_CABINET.get(), basicItem()));
    public static final Supplier<Item> WARPED_HALF_CABINET = registerWithBlockTab("warped_half_cabinet",
            () -> new BlockItem(HHModBlocks.WARPED_HALF_CABINET.get(), basicItem()));

    // Wine Rack
    public static final Supplier<Item> OAK_WINE_RACK = registerWithBlockTab("oak_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.OAK_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> SPRUCE_WINE_RACK = registerWithBlockTab("spruce_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.SPRUCE_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> BIRCH_WINE_RACK = registerWithBlockTab("birch_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.BIRCH_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> JUNGLE_WINE_RACK = registerWithBlockTab("jungle_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.JUNGLE_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> ACACIA_WINE_RACK = registerWithBlockTab("acacia_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.ACACIA_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> DARK_OAK_WINE_RACK = registerWithBlockTab("dark_oak_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.DARK_OAK_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> MANGROVE_WINE_RACK = registerWithBlockTab("mangrove_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.MANGROVE_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> CHERRY_WINE_RACK = registerWithBlockTab("cherry_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.CHERRY_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> BAMBOO_WINE_RACK = registerWithBlockTab("bamboo_wine_rack",
            () -> new FuelBlockItem(HHModBlocks.BAMBOO_WINE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> CRIMSON_WINE_RACK = registerWithBlockTab("crimson_wine_rack",
            () -> new BlockItem(HHModBlocks.CRIMSON_WINE_RACK.get(), basicItem()));
    public static final Supplier<Item> WARPED_WINE_RACK = registerWithBlockTab("warped_wine_rack",
            () -> new BlockItem(HHModBlocks.WARPED_WINE_RACK.get(), basicItem()));

    // Crops
    public static final RegistryObject<Item> BLUEBERRIES = ModList.get().isLoaded("berry_good")
            ? registerWithTab("blueberries", () -> new Item(foodItem(HHFoodValues.BLUEBERRIES)))
            : registerWithTab("blueberries", () -> new ItemNameBlockItem(HHModBlocks.BLUEBERRY_BUSH.get(), foodItem(HHFoodValues.BLUEBERRIES)));
    public static final RegistryObject<Item> CHERRY = registerWithTab("cherry",
            () -> new Item(foodItem(HHFoodValues.CHERRY)));
    public static final RegistryObject<Item> RASPBERRY = ModList.get().isLoaded("berry_good")
            ? registerWithTab("raspberry", () -> new Item(foodItem(HHFoodValues.RASPBERRY)))
            :  registerWithTab("raspberry", () -> new ItemNameBlockItem(HHModBlocks.RASPBERRY_BUSH.get(), foodItem(HHFoodValues.RASPBERRY)));
    public static final RegistryObject<Item> RED_GRAPES = registerWithTab("red_grapes",
            () -> new ItemNameBlockItem(HHModBlocks.BUDDING_RED_GRAPE_CROP.get(), foodItem(HHFoodValues.GRAPES)));
    public static final RegistryObject<Item> GREEN_GRAPES = registerWithTab("green_grapes",
            () -> new ItemNameBlockItem(HHModBlocks.BUDDING_GREEN_GRAPE_CROP.get(), foodItem(HHFoodValues.GRAPES)));
    public static final RegistryObject<Item> PEANUT = registerWithTab("peanut",
            () -> new ItemNameBlockItem(HHModBlocks.PEANUT_CROP.get(), foodItem(HHFoodValues.PEANUT)));
    public static final RegistryObject<Item> COTTON_SEEDS = registerWithTab("cotton_seeds",
            () -> new ItemNameBlockItem(HHModBlocks.COTTON_CROP.get(), basicItem()));
    public static final RegistryObject<Item> COTTON = registerWithTab("cotton",
            () -> new Item(basicItem()));

    // Berry Good Pips
    public static final RegistryObject<Item> BLUEBERRY_PIPS = ModList.get().isLoaded("berry_good")
            ? registerWithTab("blueberry_pips",
            () -> new ItemNameBlockItem(HHModBlocks.BLUEBERRY_BUSH.get(), basicItem()))
            : null;
    public static final RegistryObject<Item> RASPBERRY_PIPS = ModList.get().isLoaded("berry_good")
            ? registerWithTab("raspberry_pips",
            () -> new ItemNameBlockItem(HHModBlocks.RASPBERRY_BUSH.get(), basicItem()))
            : null;

    // Wild Crops
    public static final RegistryObject<Item> WILD_RED_GRAPES = registerWithTab("wild_red_grapes",
            () -> new BlockItem(HHModBlocks.WILD_RED_GRAPES.get(), basicItem()));
    public static final RegistryObject<Item> WILD_GREEN_GRAPES = registerWithTab("wild_green_grapes",
            () -> new BlockItem(HHModBlocks.WILD_GREEN_GRAPES.get(), basicItem()));
    public static final RegistryObject<Item> WILD_COTTON = registerWithTab("wild_cotton",
            () -> new BlockItem(HHModBlocks.WILD_COTTON.get(), basicItem()));
    public static final RegistryObject<Item> WILD_PEANUTS = registerWithTab("wild_peanuts",
            () -> new BlockItem(HHModBlocks.WILD_PEANUTS.get(), basicItem()));
    
    // Storage Blocks

    // Crates
    public static final RegistryObject<Item> BLUEBERRY_CRATE = registerWithBlockTab("blueberry_crate",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> CHERRY_CRATE = registerWithBlockTab("cherry_crate",
            () -> new BlockItem(HHModBlocks.CHERRY_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> RASPBERRY_CRATE = registerWithBlockTab("raspberry_crate",
            () -> new BlockItem(HHModBlocks.RASPBERRY_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> RED_GRAPE_CRATE = registerWithBlockTab("red_grape_crate",
            () -> new BlockItem(HHModBlocks.RED_GRAPE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GREEN_GRAPE_CRATE = registerWithBlockTab("green_grape_crate",
            () -> new BlockItem(HHModBlocks.GREEN_GRAPE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> PEANUT_CRATE = registerWithBlockTab("peanut_crate",
            () -> new BlockItem(HHModBlocks.PEANUT_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> APPLE_CRATE = registerWithBlockTab("apple_crate",
            () -> new BlockItem(HHModBlocks.APPLE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GOLDEN_APPLE_CRATE = registerWithBlockTab("golden_apple_crate",
            () -> new BlockItem(HHModBlocks.GOLDEN_APPLE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GOLDEN_CARROT_CRATE = registerWithBlockTab("golden_carrot_crate",
            () -> new BlockItem(HHModBlocks.GOLDEN_CARROT_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> POISONOUS_POTATO_CRATE = registerWithBlockTab("poisonous_potato_crate",
            () -> new BlockItem(HHModBlocks.POISONOUS_POTATO_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GLOW_BERRY_CRATE = registerWithBlockTab("glow_berry_crate",
            () -> new BlockItem(HHModBlocks.GLOW_BERRY_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> SWEET_BERRY_CRATE = registerWithBlockTab("sweet_berry_crate",
            () -> new BlockItem(HHModBlocks.SWEET_BERRY_CRATE.get(), basicItem()));
    
    // Bags
    public static final RegistryObject<Item> SALT_BAG = registerWithBlockTab("salt_bag",
            () -> new BlockItem(HHModBlocks.SALT_BAG.get(), basicItem()));
    public static final RegistryObject<Item> SUGAR_BAG = registerWithBlockTab("sugar_bag",
            () -> new BlockItem(HHModBlocks.SUGAR_BAG.get(), basicItem()));
    public static final RegistryObject<Item> COCOA_BEAN_BAG = registerWithBlockTab("cocoa_bean_bag",
            () -> new BlockItem(HHModBlocks.COCOA_BEAN_BAG.get(), basicItem()));
    public static final RegistryObject<Item> GUNPOWDER_BAG = registerWithBlockTab("gunpowder_bag",
            () -> new BlockItem(HHModBlocks.GUNPOWDER_BAG.get(), basicItem()));
    
    // Misc
    public static final RegistryObject<Item> COTTON_BALE = registerWithBlockTab("cotton_bale",
            () -> new BlockItem(HHModBlocks.COTTON_BALE.get(), basicItem()));
    public static final RegistryObject<Item> SPOOL = registerWithBlockTab("spool",
            () -> new BlockItem(HHModBlocks.SPOOL.get(), basicItem()));
    public static final RegistryObject<Item> ROPE_COIL = registerWithBlockTab("rope_coil",
            () -> new BlockItem(HHModBlocks.ROPE_COIL.get(), basicItem()));

    // Half-Slab Crates
    public static final RegistryObject<Item> EGG_CRATE = registerWithBlockTab("egg_crate",
            () -> new BlockItem(HHModBlocks.EGG_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> TURTLE_EGG_CRATE = registerWithBlockTab("turtle_egg_crate",
            () -> new BlockItem(HHModBlocks.TURTLE_EGG_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> MILK_CRATE = registerWithBlockTab("milk_crate",
            () -> new BlockItem(HHModBlocks.MILK_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GOAT_MILK_CRATE = registerWithBlockTab("goat_milk_crate",
            () -> new BlockItem(HHModBlocks.GOAT_MILK_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> RED_GRAPE_WINE_CRATE = registerWithBlockTab("red_grape_wine_crate",
            () -> new BlockItem(HHModBlocks.RED_GRAPE_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> GREEN_GRAPE_WINE_CRATE = registerWithBlockTab("green_grape_wine_crate",
            () -> new BlockItem(HHModBlocks.GREEN_GRAPE_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> RASPBERRY_WINE_CRATE = registerWithBlockTab("raspberry_wine_crate",
            () -> new BlockItem(HHModBlocks.RASPBERRY_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> BLUEBERRY_WINE_CRATE = registerWithBlockTab("blueberry_wine_crate",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> CHERRY_WINE_CRATE = registerWithBlockTab("cherry_wine_crate",
            () -> new BlockItem(HHModBlocks.CHERRY_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> SWEET_BERRY_WINE_CRATE = registerWithBlockTab("sweet_berry_wine_crate",
            () -> new BlockItem(HHModBlocks.SWEET_BERRY_WINE_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> MEAD_CRATE = registerWithBlockTab("mead_crate",
            () -> new BlockItem(HHModBlocks.MEAD_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> WATER_CRATE = registerWithBlockTab("water_crate",
            () -> new BlockItem(HHModBlocks.WATER_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> HONEY_CRATE = registerWithBlockTab("honey_crate",
            () -> new BlockItem(HHModBlocks.HONEY_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> SYRUP_CRATE = registerWithBlockTab("syrup_crate",
            () -> new BlockItem(HHModBlocks.SYRUP_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> BROWN_MUSHROOM_CRATE = registerWithBlockTab("brown_mushroom_crate",
            () -> new BlockItem(HHModBlocks.BROWN_MUSHROOM_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> RED_MUSHROOM_CRATE = registerWithBlockTab("red_mushroom_crate",
            () -> new BlockItem(HHModBlocks.RED_MUSHROOM_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> CRIMSON_FUNGUS_CRATE = registerWithBlockTab("crimson_fungus_crate",
            () -> new BlockItem(HHModBlocks.CRIMSON_FUNGUS_CRATE.get(), basicItem()));
    public static final RegistryObject<Item> WARPED_FUNGUS_CRATE = registerWithBlockTab("warped_fungus_crate",
            () -> new BlockItem(HHModBlocks.WARPED_FUNGUS_CRATE.get(), basicItem()));

    // Drinks
    public static final Supplier<Item> MEAD = registerWithTab("mead",
            () -> new WineBottleItem(()-> HHModFluids.MEAD.get(), drinkItem().food(HHFoodValues.MEAD).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> BLUEBERRY_WINE = registerWithTab("blueberry_wine",
            () -> new WineBottleItem(()-> HHModFluids.BLUEBERRY_WINE.get(), drinkItem().food(HHFoodValues.BLUEBERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> CHERRY_WINE = registerWithTab("cherry_wine",
            () -> new WineBottleItem(()-> HHModFluids.CHERRY_WINE.get(), drinkItem().food(HHFoodValues.CHERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> RASPBERRY_WINE = registerWithTab("raspberry_wine",
            () -> new WineBottleItem(()-> HHModFluids.RASPBERRY_WINE.get(), drinkItem().food(HHFoodValues.RASPBERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> RED_GRAPE_WINE = registerWithTab("red_grape_wine",
            () -> new WineBottleItem(()-> HHModFluids.RED_GRAPE_WINE.get(), drinkItem().food(HHFoodValues.RED_GRAPE_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> GREEN_GRAPE_WINE = registerWithTab("green_grape_wine",
            () -> new WineBottleItem(()-> HHModFluids.GREEN_GRAPE_WINE.get(), drinkItem().food(HHFoodValues.GREEN_GRAPE_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> SWEET_BERRY_WINE = registerWithTab("sweet_berry_wine",
            () -> new WineBottleItem(()-> HHModFluids.SWEET_BERRY_WINE.get(), drinkItem().food(HHFoodValues.SWEET_BERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final RegistryObject<Item> GOAT_MILK_BOTTLE = registerWithTab("goat_milk_bottle",
            () -> new MilkBottleItem(drinkItem().food(HHFoodValues.GOAT_MILK_BOTTLE)));
    public static final RegistryObject<Item> BLUEBERRY_JUICE = registerWithTab("blueberry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.BLUEBERRY_JUICE), false, false));
    public static final RegistryObject<Item> CHERRY_JUICE = registerWithTab("cherry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.CHERRY_JUICE), false, false));
    public static final RegistryObject<Item> RASPBERRY_JUICE = registerWithTab("raspberry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.RASPBERRY_JUICE), false, false));
    public static final RegistryObject<Item> RED_GRAPE_JUICE = registerWithTab("red_grape_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.GRAPE_JUICE), false, false));
    public static final RegistryObject<Item> GREEN_GRAPE_JUICE = registerWithTab("green_grape_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.GRAPE_JUICE), false, false));

    // Jar Items
    public static final RegistryObject<Item> JAR = registerWithTab("jar",
            () -> new BlockItem(HHModBlocks.JAR.get(), basicItem()));
    public static final RegistryObject<Item> BLUEBERRY_JAM = registerWithTab("blueberry_jam",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_JAM.get(), jarItem(HHFoodValues.BLUEBERRY_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> CHERRY_JAM = registerWithTab("cherry_jam",
            () -> new BlockItem(HHModBlocks.CHERRY_JAM.get(), jarItem(HHFoodValues.CHERRY_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> RASPBERRY_JAM = registerWithTab("raspberry_jam",
            () -> new BlockItem(HHModBlocks.RASPBERRY_JAM.get(), jarItem(HHFoodValues.RASPBERRY_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> GRAPE_JAM = registerWithTab("grape_jam",
            () -> new BlockItem(HHModBlocks.GRAPE_JAM.get(), jarItem(HHFoodValues.GRAPE_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> APPLE_JAM = registerWithTab("apple_jam",
            () -> new BlockItem(HHModBlocks.APPLE_JAM.get(), jarItem(HHFoodValues.APPLE_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> SWEET_BERRY_JAM = registerWithTab("sweet_berry_jam",
            () -> new BlockItem(HHModBlocks.SWEET_BERRY_JAM.get(), jarItem(HHFoodValues.SWEET_BERRY_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> GLOW_BERRY_JAM = registerWithTab("glow_berry_jam",
            () -> new BlockItem(HHModBlocks.GLOW_BERRY_JAM.get(), jarItem(HHFoodValues.GLOW_BERRY_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> MELON_JAM = registerWithTab("melon_jam",
            () -> new BlockItem(HHModBlocks.MELON_JAM.get(), jarItem(HHFoodValues.MELON_JAM).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PEANUT_BUTTER = registerWithTab("peanut_butter",
            () -> new BlockItem(HHModBlocks.PEANUT_BUTTER.get(), jarItem(HHFoodValues.PEANUT_BUTTER).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PICKLED_BEETROOTS = registerWithTab("pickled_beetroots",
            () -> new BlockItem(HHModBlocks.PICKLED_BEETROOTS.get(), jarItem(HHFoodValues.PICKLED_BEETROOTS).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PICKLED_CABBAGE = registerWithTab("pickled_cabbage",
            () -> new BlockItem(HHModBlocks.PICKLED_CABBAGE.get(), jarItem(HHFoodValues.PICKLED_CABBAGE).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PICKLED_CARROTS = registerWithTab("pickled_carrots",
            () -> new BlockItem(HHModBlocks.PICKLED_CARROTS.get(), jarItem(HHFoodValues.PICKLED_CARROTS).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PICKLED_ONIONS = registerWithTab("pickled_onions",
            () -> new BlockItem(HHModBlocks.PICKLED_ONIONS.get(), jarItem(HHFoodValues.PICKLED_ONIONS).craftRemainder(HHModItems.JAR.get())));
    public static final RegistryObject<Item> PICKLED_POTATOES = registerWithTab("pickled_potatoes",
            () -> new BlockItem(HHModBlocks.PICKLED_POTATOES.get(), jarItem(HHFoodValues.PICKLED_POTATOES).craftRemainder(HHModItems.JAR.get())));

    // Sweets
    public static final RegistryObject<Item> CARAMEL = registerWithTab("caramel",
            () -> new Item(foodItem(HHFoodValues.CARAMEL)));
    public static final RegistryObject<Item> CARAMEL_APPLE = registerWithTab("caramel_apple",
            () -> new Item(foodItem(HHFoodValues.CARAMEL_APPLE)));
    public static final RegistryObject<Item> CHOCOLATE_BAR = registerWithTab("chocolate_bar",
            () -> new Item(foodItem(HHFoodValues.CHOCOLATE_BAR)));
    public static final RegistryObject<Item> COTTON_CANDY = registerWithTab("cotton_candy",
            () -> new Item(foodItem(HHFoodValues.COTTON_CANDY)));
    public static final RegistryObject<Item> BLUEBERRY_MUFFIN = registerWithTab("blueberry_muffin",
            () -> new Item(foodItem(HHFoodValues.BLUEBERRY_MUFFIN)));
    public static final RegistryObject<Item> PEANUT_BUTTER_COOKIE = registerWithTab("peanut_butter_cookie",
            () -> new Item(foodItem(HHFoodValues.PEANUT_BUTTER_COOKIE)));
    public static final RegistryObject<Item> TRAIL_MIX = registerWithTab("trail_mix",
            () -> new Item(foodItem(HHFoodValues.TRAIL_MIX)));
    public static final RegistryObject<Item> ROASTED_PEANUTS = registerWithTab("roasted_peanuts",
            () -> new Item(foodItem(HHFoodValues.ROASTED_PEANUTS)));

    public static final RegistryObject<Item> MARSHMALLOW_STICK = registerWithTab("marshmallow_stick",
            () -> new MarshmallowStickItem(foodItem(HHFoodValues.MARSHMALLOW_STICK).craftRemainder(Items.STICK).stacksTo(1)));
    public static final RegistryObject<Item> ROASTED_MARSHMALLOW_STICK = registerWithTab("roasted_marshmallow_stick",
            () -> new MarshmallowStickItem(foodItem(HHFoodValues.ROASTED_MARSHMALLOW_STICK).craftRemainder(Items.STICK).stacksTo(1)));
    public static final RegistryObject<Item> CHARRED_MARSHMALLOW_STICK = registerWithTab("charred_marshmallow_stick",
            () -> new MarshmallowStickItem(foodItem(HHFoodValues.CHARRED_MARSHMALLOW_STICK).craftRemainder(Items.STICK).stacksTo(1)));

    public static final Supplier<Item> SMORE = registerWithTab("smore",
            () -> new MarshmallowStickItem(foodItem(HHFoodValues.SMORE)));

    // Sap and Syrup
    public static final Supplier<Item> SAP_BUCKET = registerWithTab("sap_bucket",
            () -> new BucketItem(HHModFluids.SAP.get(), basicItem().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> SYRUP_BOTTLE = registerWithTab("syrup_bottle",
            () -> new Item(basicItem().craftRemainder(Items.GLASS_BOTTLE)));

    // Pies
    public static final RegistryObject<Item> BLUEBERRY_PIE = registerWithTab("blueberry_pie",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_PIE.get(), basicItem()));
    public static final RegistryObject<Item> BLUEBERRY_PIE_SLICE = registerWithTab("blueberry_pie_slice",
            () -> new Item(foodItem(HHFoodValues.BLUEBERRY_PIE_SLICE)));
    public static final RegistryObject<Item> RASPBERRY_PIE = registerWithTab("raspberry_pie",
            () -> new BlockItem(HHModBlocks.RASPBERRY_PIE.get(), basicItem()));
    public static final RegistryObject<Item> RASPBERRY_PIE_SLICE = registerWithTab("raspberry_pie_slice",
            () -> new Item(foodItem(HHFoodValues.RASPBERRY_PIE_SLICE)));
    public static final RegistryObject<Item> GRAPE_PIE = registerWithTab("grape_pie",
            () -> new BlockItem(HHModBlocks.GRAPE_PIE.get(), basicItem()));
    public static final RegistryObject<Item> GRAPE_PIE_SLICE = registerWithTab("grape_pie_slice",
            () -> new Item(foodItem(HHFoodValues.GRAPE_PIE_SLICE)));
    public static final RegistryObject<Item> PEANUT_BUTTER_PIE = registerWithTab("peanut_butter_pie",
            () -> new BlockItem(HHModBlocks.PEANUT_BUTTER_PIE.get(), basicItem()));
    public static final RegistryObject<Item> PEANUT_BUTTER_PIE_SLICE = registerWithTab("peanut_butter_pie_slice",
            () -> new Item(foodItem(HHFoodValues.PEANUT_BUTTER_PIE_SLICE)));
    public static final RegistryObject<Item> CHICKEN_POT_PIE = registerWithTab("chicken_pot_pie",
            () -> new BlockItem(HHModBlocks.CHICKEN_POT_PIE.get(), basicItem()));
    public static final RegistryObject<Item> CHICKEN_POT_PIE_SLICE = registerWithTab("chicken_pot_pie_slice",
            () -> new Item(foodItem(HHFoodValues.CHICKEN_POT_PIE_SLICE)));
    public static final RegistryObject<Item> CARROT_CAKE = registerWithTab("carrot_cake",
            () -> new BlockItem(HHModBlocks.CARROT_CAKE.get(), basicItem()));
    public static final RegistryObject<Item> CARROT_CAKE_SLICE = registerWithTab("carrot_cake_slice",
            () -> new Item(foodItem(HHFoodValues.CARROT_CAKE_SLICE)));


    // Ingredients

    public static final RegistryObject<Item> UNRIPE_CHEDDAR_CHEESE_WHEEL = registerWithTab("unripe_cheddar_cheese_wheel",
            () -> new BlockItem(HHModBlocks.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(), basicItem()));
    public static final RegistryObject<Item> CHEDDAR_CHEESE_WHEEL = registerWithTab("cheddar_cheese_wheel",
            () -> new BlockItem(HHModBlocks.CHEDDAR_CHEESE_WHEEL.get(), basicItem()));
    public static final RegistryObject<Item> CHEDDAR_CHEESE_SLICE = registerWithTab("cheddar_cheese_slice",
            () -> new Item(foodItem(HHFoodValues.CHEDDAR_CHEESE_SLICE)));

    public static final RegistryObject<Item> UNRIPE_GOAT_CHEESE_WHEEL = registerWithTab("unripe_goat_cheese_wheel",
            () -> new BlockItem(HHModBlocks.UNRIPE_GOAT_CHEESE_WHEEL.get(), basicItem()));
    public static final RegistryObject<Item> GOAT_CHEESE_WHEEL = registerWithTab("goat_cheese_wheel",
            () -> new BlockItem(HHModBlocks.GOAT_CHEESE_WHEEL.get(), basicItem()));
    public static final RegistryObject<Item> GOAT_CHEESE_SLICE = registerWithTab("goat_cheese_slice",
            () -> new Item(foodItem(HHFoodValues.GOAT_CHEESE_SLICE)));

    public static final RegistryObject<Item> RAW_SAUSAGE = registerWithTab("raw_sausage",
            () -> new Item(foodItem(HHFoodValues.RAW_SAUSAGE)));
    public static final RegistryObject<Item> COOKED_SAUSAGE = registerWithTab("cooked_sausage",
            () -> new Item(foodItem(HHFoodValues.COOKED_SAUSAGE)));
    public static final RegistryObject<Item> SKEWERED_SAUSAGE = registerWithTab("skewered_sausage",
            () -> new Item(foodItem(HHFoodValues.SKEWERED_SAUSAGE)));
    public static final RegistryObject<Item> JERKY = registerWithTab("jerky",
            () -> new Item(foodItem(HHFoodValues.JERKY)));
    public static final RegistryObject<Item> BATTER = registerWithTab("batter",
            () -> new Item(basicItem().craftRemainder(Items.BOWL)));
    public static final RegistryObject<Item> SALT = registerWithTab("salt",
            () -> new Item(basicItem()));
    public static final RegistryObject<Item> RAISINS = registerWithTab("raisins",
            () -> new Item(foodItem(HHFoodValues.RAISINS)));
    public static final RegistryObject<Item> SUNFLOWER_SEEDS = registerWithTab("sunflower_seeds",
            () -> new Item(foodItem(HHFoodValues.SUNFLOWER_SEEDS)));

    // Meals
    public static final RegistryObject<Item> ONION_SOUP = registerWithTab("onion_soup",
            () -> new Item(bowlFoodItem(HHFoodValues.ONION_SOUP)));
    public static final RegistryObject<Item> MACARONI_AND_CHEESE = registerWithTab("macaroni_and_cheese",
            () -> new Item(bowlFoodItem(HHFoodValues.MACARONI_AND_CHEESE)));
    public static final RegistryObject<Item> MASHED_POTATOES = registerWithTab("mashed_potatoes",
            () -> new Item(bowlFoodItem(HHFoodValues.MASHED_POTATOES)));
    public static final RegistryObject<Item> PEANUT_BUTTER_AND_JELLY_SANDWICH = registerWithTab("peanut_butter_and_jelly_sandwich",
            () -> new Item(foodItem(HHFoodValues.PEANUT_BUTTER_AND_JELLY_SANDWICH)));
    public static final RegistryObject<Item> WAFFLE = registerWithTab("waffle",
            () -> new Item(foodItem(HHFoodValues.WAFFLE)));
    public static final RegistryObject<Item> BISCUITS_AND_GRAVY = registerWithTab("biscuits_and_gravy",
            () -> new Item(bowlFoodItem(HHFoodValues.BISCUITS_AND_GRAVY)));
}