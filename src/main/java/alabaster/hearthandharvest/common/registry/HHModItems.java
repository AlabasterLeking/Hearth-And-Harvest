package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.HHFoodValues;
import alabaster.hearthandharvest.common.block.trellis.TrellisMaterial;
import alabaster.hearthandharvest.common.item.*;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.*;
import vectorwing.farmersdelight.common.registry.ModMaterials;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class HHModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, HearthAndHarvest.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_BLOCKS = Sets.newLinkedHashSet();

    public static Supplier<Item> registerWithTab(String name, Supplier<Item> supplier) {
        Supplier<Item> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    public static Supplier<Item> registerWithBlockTab(String name, Supplier<Item> supplier) {
        Supplier<Item> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_BLOCKS.add(item);
        return item;
    }

    public static Supplier<Item> registerWithNoTab(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    // Helper methods
    public static Item.Properties basicItem() {
        return (new Item.Properties());
    }

    public static Item.Properties cleaverItem(Tier tier) {
        return new Item.Properties().attributes(CleaverItem.createAttributes(tier, 2.0F, -3.0F));
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
    public static final Supplier<Item> FLINT_CLEAVER = registerWithTab("flint_cleaver",
            () -> new CleaverItem(ModMaterials.FLINT, cleaverItem(ModMaterials.FLINT)));
    public static final Supplier<Item> IRON_CLEAVER = registerWithTab("iron_cleaver",
            () -> new CleaverItem(Tiers.IRON, cleaverItem(Tiers.IRON)));
    public static final Supplier<Item> DIAMOND_CLEAVER = registerWithTab("diamond_cleaver",
            () -> new CleaverItem(Tiers.DIAMOND, cleaverItem(Tiers.DIAMOND)));
    public static final Supplier<Item> NETHERITE_CLEAVER = registerWithTab("netherite_cleaver",
            () -> new CleaverItem(Tiers.NETHERITE, cleaverItem(Tiers.NETHERITE).fireResistant()));
    public static final Supplier<Item> GOLDEN_CLEAVER = registerWithTab("golden_cleaver",
            () -> new CleaverItem(Tiers.GOLD, cleaverItem(Tiers.GOLD)));

    public static final Supplier<Item> PITCHFORK = registerWithTab("pitchfork",
            () -> new PitchforkItem(new Item.Properties()
                    .durability(250)
                    .attributes(PitchforkItem.createAttributes())));

    public static final Supplier<Item> WATERING_CAN = registerWithTab("watering_can",
            () -> new WateringCanItem(basicItem()));
    public static final Supplier<Item> UNIVERSAL_FEED = registerWithTab("universal_feed",
            () -> new UniversalFeedItem(basicItem()));
    public static final Supplier<Item> SEED_POUCH = registerWithTab("seed_pouch",
            () -> new SeedPouchItem(basicItem().stacksTo(1)));

    public static final Supplier<Item> FARMERS_HAT = registerWithTab("farmers_hat",
            () -> new FarmersHatItem(basicItem().stacksTo(1)));

    // Spawn Eggs
    public static final Supplier<Item> CROW_SPAWN_EGG = registerWithTab("crow_spawn_egg",
            () -> new DeferredSpawnEggItem(HHModEntities.CROW, 0x1c2030,0x0d111c,
                    new Item.Properties()));
    public static final Supplier<Item> CROW_FEATHER = registerWithTab("crow_feather",
            () -> new Item(basicItem()));

    // Workstations
    public static final Supplier<Item> TREE_TAPPER = registerWithTab("tree_tapper",
            () -> new BlockItem(HHModBlocks.TREE_TAPPER.get(), basicItem()));
    public static final Supplier<Item> CASK = registerWithTab("cask",
            () -> new BlockItem(HHModBlocks.CASK.get(), basicItem()));
    public static final Supplier<Item> STOMPING_BASIN = registerWithTab("stomping_basin",
            () -> new BlockItem(HHModBlocks.STOMPING_BASIN.get(), basicItem()));
    public static final Supplier<Item> JUG = registerWithTab("jug",
            () -> new JugBlockItem(HHModBlocks.JUG.get(), basicItem()));
    public static final Supplier<Item> TROUGH = registerWithTab("trough",
            () -> new BlockItem(HHModBlocks.TROUGH.get(), basicItem()));

    public static final Supplier<Item> COUNTER = registerWithBlockTab("counter",
            () -> new BlockItem(HHModBlocks.COUNTER.get(), basicItem()));
    public static final Supplier<Item> DRAWER = registerWithBlockTab("drawer",
            () -> new BlockItem(HHModBlocks.DRAWER.get(), basicItem()));
    public static final Supplier<Item> BASIN = registerWithBlockTab("basin",
            () -> new BlockItem(HHModBlocks.BASIN.get(), basicItem()));

    public static final Supplier<Item> NEST = registerWithBlockTab("nest",
            () -> new BlockItem(HHModBlocks.NEST.get(), basicItem()));

    public static final Supplier<Item> HAY_RUG = registerWithBlockTab("hay_rug",
            () -> new BlockItem(HHModBlocks.HAY_RUG.get(), basicItem()));
    public static final Supplier<Item> STRAW_RUG = registerWithBlockTab("straw_rug",
            () -> new BlockItem(HHModBlocks.STRAW_RUG.get(), basicItem()));

    public static final Supplier<Item> SCARECROW = registerWithBlockTab("scarecrow",
            () -> new BlockItem(HHModBlocks.SCARECROW.get(), basicItem()));

    public static final Supplier<Item> TRELLIS = registerWithBlockTab("trellis",
            () -> new TrellisBlockItem(HHModBlocks.TRELLIS.get(), TrellisMaterial.STICK, basicItem()));
    public static final Supplier<Item> BAMBOO_TRELLIS = registerWithBlockTab("bamboo_trellis",
            () -> new TrellisBlockItem(HHModBlocks.TRELLIS.get(), TrellisMaterial.BAMBOO, basicItem()));
    public static final Supplier<Item> STRIPPED_BAMBOO_TRELLIS = registerWithBlockTab("stripped_bamboo_trellis",
            () -> new TrellisBlockItem(HHModBlocks.TRELLIS.get(), TrellisMaterial.STRIPPED_BAMBOO, basicItem()));

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

    // Crabbers Delight Compat
    public static final Supplier<Item> PALM_HALF_CABINET = ModList.get().isLoaded("crabbersdelight")
            ? registerWithBlockTab("palm_half_cabinet",
            () -> new FuelBlockItem(HHModBlocks.PALM_HALF_CABINET.get(), basicItem(), 300))
            : null;

    public static final Supplier<Item> CRATE = registerWithBlockTab("crate",
            () -> new CrateBlockItem(HHModBlocks.CRATE.get(), basicItem().stacksTo(16)));

    // Bottle Racks
    public static final Supplier<Item> OAK_BOTTLE_RACK = registerWithBlockTab("oak_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.OAK_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> SPRUCE_BOTTLE_RACK = registerWithBlockTab("spruce_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.SPRUCE_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> BIRCH_BOTTLE_RACK = registerWithBlockTab("birch_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.BIRCH_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> JUNGLE_BOTTLE_RACK = registerWithBlockTab("jungle_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.JUNGLE_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> ACACIA_BOTTLE_RACK = registerWithBlockTab("acacia_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.ACACIA_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> DARK_OAK_BOTTLE_RACK = registerWithBlockTab("dark_oak_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.DARK_OAK_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> MANGROVE_BOTTLE_RACK = registerWithBlockTab("mangrove_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.MANGROVE_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> CHERRY_BOTTLE_RACK = registerWithBlockTab("cherry_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.CHERRY_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> BAMBOO_BOTTLE_RACK = registerWithBlockTab("bamboo_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.BAMBOO_BOTTLE_RACK.get(), basicItem(), 300));
    public static final Supplier<Item> CRIMSON_BOTTLE_RACK = registerWithBlockTab("crimson_bottle_rack",
            () -> new BlockItem(HHModBlocks.CRIMSON_BOTTLE_RACK.get(), basicItem()));
    public static final Supplier<Item> WARPED_BOTTLE_RACK = registerWithBlockTab("warped_bottle_rack",
            () -> new BlockItem(HHModBlocks.WARPED_BOTTLE_RACK.get(), basicItem()));

    // Crabbers Delight Compat
    public static final Supplier<Item> PALM_BOTTLE_RACK = ModList.get().isLoaded("crabbersdelight")
            ? registerWithBlockTab("palm_bottle_rack",
            () -> new FuelBlockItem(HHModBlocks.PALM_BOTTLE_RACK.get(), basicItem(), 300))
            : null;

    // Crops
    public static final Supplier<Item> BLUEBERRIES = ModList.get().isLoaded("berry_good")
            ? registerWithTab("blueberries", () -> new Item(foodItem(HHFoodValues.BLUEBERRIES)))
            : registerWithTab("blueberries", () -> new ItemNameBlockItem(HHModBlocks.BLUEBERRY_BUSH.get(), foodItem(HHFoodValues.BLUEBERRIES)));
    public static final Supplier<Item> CHERRY = registerWithTab("cherry",
            () -> new Item(foodItem(HHFoodValues.CHERRY)));
    public static final Supplier<Item> RASPBERRY = ModList.get().isLoaded("berry_good")
            ? registerWithTab("raspberry", () -> new Item(foodItem(HHFoodValues.RASPBERRY)))
            :  registerWithTab("raspberry", () -> new ItemNameBlockItem(HHModBlocks.RASPBERRY_BUSH.get(), foodItem(HHFoodValues.RASPBERRY)));
    public static final Supplier<Item> RED_GRAPES = registerWithTab("red_grapes",
            () -> new Item(foodItem(HHFoodValues.GRAPES)));
    public static final Supplier<Item> GREEN_GRAPES = registerWithTab("green_grapes",
            () -> new Item(foodItem(HHFoodValues.GRAPES)));
    public static final Supplier<Item> PEANUT = registerWithTab("peanut",
            () -> new ItemNameBlockItem(HHModBlocks.PEANUT_CROP.get(), foodItem(HHFoodValues.PEANUT)));
    public static final Supplier<Item> COTTON_SEEDS = registerWithTab("cotton_seeds",
            () -> new ItemNameBlockItem(HHModBlocks.COTTON_CROP.get(), basicItem()));
    public static final Supplier<Item> COTTON = registerWithTab("cotton",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CORN = registerWithTab("corn",
            () -> new ConsumableItem(foodItem(HHFoodValues.CORN)));
    public static final Supplier<Item> CORN_KERNELS = registerWithTab("corn_kernels",
            () -> new ItemNameBlockItem(HHModBlocks.CORN_STALK.get(), basicItem()));
    public static final Supplier<Item> CORN_HUSK = registerWithTab("corn_husk",
            () -> new Item(basicItem()));

    // Berry Good Pips
    public static final Supplier<Item> BLUEBERRY_PIPS = ModList.get().isLoaded("berry_good")
            ? registerWithTab("blueberry_pips",
            () -> new ItemNameBlockItem(HHModBlocks.BLUEBERRY_BUSH.get(), basicItem()))
            : null;
    public static final Supplier<Item> RASPBERRY_PIPS = ModList.get().isLoaded("berry_good")
            ? registerWithTab("raspberry_pips",
            () -> new ItemNameBlockItem(HHModBlocks.RASPBERRY_BUSH.get(), basicItem()))
            : null;

    // Wild Crops
    public static final Supplier<Item> WILD_RED_GRAPES = registerWithTab("wild_red_grapes",
            () -> new BlockItem(HHModBlocks.WILD_RED_GRAPES.get(), basicItem()));
    public static final Supplier<Item> WILD_GREEN_GRAPES = registerWithTab("wild_green_grapes",
            () -> new BlockItem(HHModBlocks.WILD_GREEN_GRAPES.get(), basicItem()));
    public static final Supplier<Item> WILD_COTTON = registerWithTab("wild_cotton",
            () -> new BlockItem(HHModBlocks.WILD_COTTON.get(), basicItem()));
    public static final Supplier<Item> WILD_PEANUTS = registerWithTab("wild_peanuts",
            () -> new BlockItem(HHModBlocks.WILD_PEANUTS.get(), basicItem()));

    // Flowers
    public static final Supplier<Item> YELLOW_MUM = registerWithTab("yellow_mum",
            () -> new BlockItem(HHModBlocks.YELLOW_MUM.get(), basicItem()));
    public static final Supplier<Item> ORANGE_MUM = registerWithTab("orange_mum",
            () -> new BlockItem(HHModBlocks.ORANGE_MUM.get(), basicItem()));
    public static final Supplier<Item> RED_MUM = registerWithTab("red_mum",
            () -> new BlockItem(HHModBlocks.RED_MUM.get(), basicItem()));
    public static final Supplier<Item> BLUE_MUM = registerWithTab("blue_mum",
            () -> new BlockItem(HHModBlocks.BLUE_MUM.get(), basicItem()));
    public static final Supplier<Item> LIGHT_BLUE_MUM = registerWithTab("light_blue_mum",
            () -> new BlockItem(HHModBlocks.LIGHT_BLUE_MUM.get(), basicItem()));
    public static final Supplier<Item> PURPLE_MUM = registerWithTab("purple_mum",
            () -> new BlockItem(HHModBlocks.PURPLE_MUM.get(), basicItem()));
    public static final Supplier<Item> PINK_MUM = registerWithTab("pink_mum",
            () -> new BlockItem(HHModBlocks.PINK_MUM.get(), basicItem()));
    public static final Supplier<Item> WHITE_MUM = registerWithTab("white_mum",
            () -> new BlockItem(HHModBlocks.WHITE_MUM.get(), basicItem()));
    
    // Crates
    public static final Supplier<Item> BLUEBERRY_CRATE = registerWithBlockTab("blueberry_crate",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> CHERRY_CRATE = registerWithBlockTab("cherry_crate",
            () -> new BlockItem(HHModBlocks.CHERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> RASPBERRY_CRATE = registerWithBlockTab("raspberry_crate",
            () -> new BlockItem(HHModBlocks.RASPBERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> RED_GRAPE_CRATE = registerWithBlockTab("red_grape_crate",
            () -> new BlockItem(HHModBlocks.RED_GRAPE_CRATE.get(), basicItem()));
    public static final Supplier<Item> GREEN_GRAPE_CRATE = registerWithBlockTab("green_grape_crate",
            () -> new BlockItem(HHModBlocks.GREEN_GRAPE_CRATE.get(), basicItem()));
    public static final Supplier<Item> PEANUT_CRATE = registerWithBlockTab("peanut_crate",
            () -> new BlockItem(HHModBlocks.PEANUT_CRATE.get(), basicItem()));
    public static final Supplier<Item> CORN_CRATE = registerWithBlockTab("corn_crate",
            () -> new BlockItem(HHModBlocks.CORN_CRATE.get(), basicItem()));
    public static final Supplier<Item> APPLE_CRATE = registerWithBlockTab("apple_crate",
            () -> new BlockItem(HHModBlocks.APPLE_CRATE.get(), basicItem()));
    public static final Supplier<Item> GOLDEN_APPLE_CRATE = registerWithBlockTab("golden_apple_crate",
            () -> new BlockItem(HHModBlocks.GOLDEN_APPLE_CRATE.get(), basicItem()));
    public static final Supplier<Item> GOLDEN_CARROT_CRATE = registerWithBlockTab("golden_carrot_crate",
            () -> new BlockItem(HHModBlocks.GOLDEN_CARROT_CRATE.get(), basicItem()));
    public static final Supplier<Item> GLISTERING_MELON_CRATE = registerWithBlockTab("glistering_melon_crate",
            () -> new BlockItem(HHModBlocks.GLISTERING_MELON_CRATE.get(), basicItem()));
    public static final Supplier<Item> POISONOUS_POTATO_CRATE = registerWithBlockTab("poisonous_potato_crate",
            () -> new BlockItem(HHModBlocks.POISONOUS_POTATO_CRATE.get(), basicItem()));
    public static final Supplier<Item> ROTTEN_TOMATO_CRATE = registerWithBlockTab("rotten_tomato_crate",
            () -> new BlockItem(HHModBlocks.ROTTEN_TOMATO_CRATE.get(), basicItem()));
    public static final Supplier<Item> GLOW_BERRY_CRATE = registerWithBlockTab("glow_berry_crate",
            () -> new BlockItem(HHModBlocks.GLOW_BERRY_CRATE.get(), basicItem()));
    public static final Supplier<Item> SWEET_BERRY_CRATE = registerWithBlockTab("sweet_berry_crate",
            () -> new BlockItem(HHModBlocks.SWEET_BERRY_CRATE.get(), basicItem()));
    
    // Bags
    public static final Supplier<Item> SALT_BAG = registerWithBlockTab("salt_bag",
            () -> new BlockItem(HHModBlocks.SALT_BAG.get(), basicItem()));
    public static final Supplier<Item> SUGAR_BAG = registerWithBlockTab("sugar_bag",
            () -> new BlockItem(HHModBlocks.SUGAR_BAG.get(), basicItem()));
    public static final Supplier<Item> COCOA_BEAN_BAG = registerWithBlockTab("cocoa_bean_bag",
            () -> new BlockItem(HHModBlocks.COCOA_BEAN_BAG.get(), basicItem()));
    public static final Supplier<Item> GUNPOWDER_BAG = registerWithBlockTab("gunpowder_bag",
            () -> new BlockItem(HHModBlocks.GUNPOWDER_BAG.get(), basicItem()));
    public static final Supplier<Item> CORN_KERNEL_BAG = registerWithBlockTab("corn_kernel_bag",
            () -> new BlockItem(HHModBlocks.CORN_KERNEL_BAG.get(), basicItem()));
    public static final Supplier<Item> FLOUR_BAG = registerWithBlockTab("flour_bag",
            () -> new BlockItem(HHModBlocks.FLOUR_BAG.get(), basicItem()));
    public static final Supplier<Item> MANURE_BAG = registerWithBlockTab("manure_bag",
            () -> new BlockItem(HHModBlocks.MANURE_BAG.get(), basicItem()));
    
    // Misc
    public static final Supplier<Item> COTTON_BALE = registerWithBlockTab("cotton_bale",
            () -> new BlockItem(HHModBlocks.COTTON_BALE.get(), basicItem()));
    public static final Supplier<Item> SUGAR_CANE_BUNDLE = registerWithBlockTab("sugar_cane_bundle",
            () -> new BlockItem(HHModBlocks.SUGAR_CANE_BUNDLE.get(), basicItem()));
    public static final Supplier<Item> SPOOL = registerWithBlockTab("spool",
            () -> new BlockItem(HHModBlocks.SPOOL.get(), basicItem()));
    public static final Supplier<Item> ROPE_COIL = registerWithBlockTab("rope_coil",
            () -> new BlockItem(HHModBlocks.ROPE_COIL.get(), basicItem()));
    public static final Supplier<Item> CORN_HUSK_BUNDLE = registerWithBlockTab("corn_husk_bundle",
            () -> new FuelBlockItem(HHModBlocks.CORN_HUSK_BUNDLE.get(), basicItem(), 4000 ));
    public static final Supplier<Item> CHARCOAL_BLOCK = registerWithBlockTab("charcoal_block",
            () -> new FuelBlockItem(HHModBlocks.CHARCOAL_BLOCK.get(), basicItem(), 16000));
    public static final Supplier<Item> STICK_BRUSH = registerWithBlockTab("stick_brush",
            () -> new FuelBlockItem(HHModBlocks.STICK_BRUSH.get(), basicItem(), 1000));
    public static final Supplier<Item> MULCH = registerWithBlockTab("mulch",
            () -> new FuelBlockItem(HHModBlocks.MULCH.get(), basicItem(), 1000));

    // Salt
    public static final Supplier<Item> SALT_BLOCK = registerWithBlockTab("salt_block",
            () -> new SaltBlockItem(HHModBlocks.SALT_BLOCK.get(), basicItem()));
    public static final Supplier<Item> LIGHTLY_LICKED_SALT_BLOCK = registerWithBlockTab("lightly_licked_salt_block",
            () -> new SaltBlockItem(HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get(), basicItem()));
    public static final Supplier<Item> WELL_LICKED_SALT_BLOCK = registerWithBlockTab("well_licked_salt_block",
            () -> new SaltBlockItem(HHModBlocks.WELL_LICKED_SALT_BLOCK.get(), basicItem()));
    public static final Supplier<Item> HEAVILY_LICKED_SALT_BLOCK = registerWithBlockTab("heavily_licked_salt_block",
            () -> new SaltBlockItem(HHModBlocks.HEAVILY_LICKED_SALT_BLOCK.get(), basicItem()));
    public static final Supplier<Item> POLISHED_SALT_BLOCK = registerWithBlockTab("polished_salt_block",
            () -> new BlockItem(HHModBlocks.POLISHED_SALT_BLOCK.get(), basicItem()));
    public static final Supplier<Item> SALT_STAIRS = registerWithBlockTab("salt_stairs",
            () -> new BlockItem(HHModBlocks.SALT_STAIRS.get(), basicItem()));
    public static final Supplier<Item> POLISHED_SALT_STAIRS = registerWithBlockTab("polished_salt_stairs",
            () -> new BlockItem(HHModBlocks.POLISHED_SALT_STAIRS.get(), basicItem()));
    public static final Supplier<Item> SALT_SLAB = registerWithBlockTab("salt_slab",
            () -> new BlockItem(HHModBlocks.SALT_SLAB.get(), basicItem()));
    public static final Supplier<Item> POLISHED_SALT_SLAB = registerWithBlockTab("polished_salt_slab",
            () -> new BlockItem(HHModBlocks.POLISHED_SALT_SLAB.get(), basicItem()));
    public static final Supplier<Item> SALT_WALL = registerWithBlockTab("salt_wall",
            () -> new BlockItem(HHModBlocks.SALT_WALL.get(), basicItem()));
    public static final Supplier<Item> POLISHED_SALT_WALL = registerWithBlockTab("polished_salt_wall",
            () -> new BlockItem(HHModBlocks.POLISHED_SALT_WALL.get(), basicItem()));
    public static final Supplier<Item> SALT_DRIP = registerWithBlockTab("salt_drip",
            () -> new BlockItem(HHModBlocks.SALT_DRIP.get(), basicItem()));
    public static final Supplier<Item> SALT_LAMP = registerWithBlockTab("salt_lamp",
            () -> new BlockItem(HHModBlocks.SALT_LAMP.get(), basicItem()));
    
    // Manure
    public static final Supplier<Item> MANURE = registerWithTab("manure",
            () -> new ManureItem(basicItem()));
    public static final Supplier<Item> WET_MANURE_BRICK = registerWithTab("wet_manure_brick",
            () -> new Item(basicItem()));
    public static final Supplier<Item> MANURE_BRICK = registerWithTab("manure_brick",
            () -> new Item(basicItem()));
    public static final Supplier<Item> MANURE_BLOCK = registerWithBlockTab("manure_block",
            () -> new BlockItem(HHModBlocks.MANURE_BLOCK.get(), basicItem()));
    public static final Supplier<Item> MANURE_BRICK_BLOCK = registerWithBlockTab("manure_bricks",
            () -> new BlockItem(HHModBlocks.MANURE_BRICKS_BLOCK.get(), basicItem()));
    public static final Supplier<Item> POLISHED_MANURE = registerWithBlockTab("polished_manure",
            () -> new BlockItem(HHModBlocks.POLISHED_MANURE.get(), basicItem()));
    public static final Supplier<Item> MANURE_BRICK_STAIRS = registerWithBlockTab("manure_brick_stairs",
            () -> new BlockItem(HHModBlocks.MANURE_BRICK_STAIRS.get(), basicItem()));
    public static final Supplier<Item> POLISHED_MANURE_STAIRS = registerWithBlockTab("polished_manure_stairs",
            () -> new BlockItem(HHModBlocks.POLISHED_MANURE_STAIRS.get(), basicItem()));
    public static final Supplier<Item> MANURE_BRICK_SLAB = registerWithBlockTab("manure_brick_slab",
            () -> new BlockItem(HHModBlocks.MANURE_BRICK_SLAB.get(), basicItem()));
    public static final Supplier<Item> POLISHED_MANURE_SLAB = registerWithBlockTab("polished_manure_slab",
            () -> new BlockItem(HHModBlocks.POLISHED_MANURE_SLAB.get(), basicItem()));
    public static final Supplier<Item> MANURE_BRICK_WALL = registerWithBlockTab("manure_brick_wall",
            () -> new BlockItem(HHModBlocks.MANURE_BRICK_WALL.get(), basicItem()));
    public static final Supplier<Item> POLISHED_MANURE_WALL = registerWithBlockTab("polished_manure_wall",
            () -> new BlockItem(HHModBlocks.POLISHED_MANURE_WALL.get(), basicItem()));

    // Half-Slab Crates
    public static final Supplier<Item> BROWN_MUSHROOM_CRATE = registerWithBlockTab("brown_mushroom_crate",
            () -> new BlockItem(HHModBlocks.BROWN_MUSHROOM_CRATE.get(), basicItem()));
    public static final Supplier<Item> RED_MUSHROOM_CRATE = registerWithBlockTab("red_mushroom_crate",
            () -> new BlockItem(HHModBlocks.RED_MUSHROOM_CRATE.get(), basicItem()));
    public static final Supplier<Item> CRIMSON_FUNGUS_CRATE = registerWithBlockTab("crimson_fungus_crate",
            () -> new BlockItem(HHModBlocks.CRIMSON_FUNGUS_CRATE.get(), basicItem()));
    public static final Supplier<Item> WARPED_FUNGUS_CRATE = registerWithBlockTab("warped_fungus_crate",
            () -> new BlockItem(HHModBlocks.WARPED_FUNGUS_CRATE.get(), basicItem()));

    // Drinks
    public static final Supplier<Item> MEAD = registerWithTab("mead",
            () -> new WineBottleItem(HHModFluids.MEAD.source()::get, drinkItem().food(HHFoodValues.MEAD).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> HARD_CIDER = registerWithTab("hard_cider",
            () -> new WineBottleItem(HHModFluids.HARD_CIDER.source()::get, drinkItem().food(HHFoodValues.HARD_CIDER).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> ROOT_BEER = registerWithTab("root_beer",
            () -> new WineBottleItem(HHModFluids.ROOT_BEER.source()::get, drinkItem().food(HHFoodValues.ROOT_BEER).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> BLUEBERRY_WINE = registerWithTab("blueberry_wine",
            () -> new WineBottleItem(HHModFluids.BLUEBERRY_WINE.source()::get, drinkItem().food(HHFoodValues.BLUEBERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> CHERRY_WINE = registerWithTab("cherry_wine",
            () -> new WineBottleItem(HHModFluids.CHERRY_WINE.source()::get, drinkItem().food(HHFoodValues.CHERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> RASPBERRY_WINE = registerWithTab("raspberry_wine",
            () -> new WineBottleItem(HHModFluids.RASPBERRY_WINE.source()::get, drinkItem().food(HHFoodValues.RASPBERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> RED_GRAPE_WINE = registerWithTab("red_grape_wine",
            () -> new WineBottleItem(HHModFluids.RED_GRAPE_WINE.source()::get, drinkItem().food(HHFoodValues.RED_GRAPE_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> GREEN_GRAPE_WINE = registerWithTab("green_grape_wine",
            () -> new WineBottleItem(HHModFluids.GREEN_GRAPE_WINE.source()::get, drinkItem().food(HHFoodValues.GREEN_GRAPE_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> SWEET_BERRY_WINE = registerWithTab("sweet_berry_wine",
            () -> new WineBottleItem(HHModFluids.SWEET_BERRY_WINE.source()::get, drinkItem().food(HHFoodValues.SWEET_BERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> GLOW_BERRY_WINE = registerWithTab("glow_berry_wine",
            () -> new WineBottleItem(HHModFluids.GLOW_BERRY_WINE.source()::get, drinkItem().food(HHFoodValues.GLOW_BERRY_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> MELON_WINE = registerWithTab("melon_wine",
            () -> new WineBottleItem(HHModFluids.MELON_WINE.source()::get, drinkItem().food(HHFoodValues.MELON_WINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> MOONSHINE = registerWithTab("moonshine",
            () -> new WineBottleItem(HHModFluids.MOONSHINE.source()::get, drinkItem().food(HHFoodValues.MOONSHINE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), true, false));
    public static final Supplier<Item> CHOCOLATE_MILK_BOTTLE = registerWithTab("chocolate_milk_bottle",
            () -> new MilkBottleItem(drinkItem().food(HHFoodValues.CHOCOLATE_MILK_BOTTLE)));
    public static final Supplier<Item> GOAT_MILK_BOTTLE = registerWithTab("goat_milk_bottle",
            () -> new MilkBottleItem(drinkItem().food(HHFoodValues.GOAT_MILK_BOTTLE)));
    public static final Supplier<Item> BLUEBERRY_JUICE = registerWithTab("blueberry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.BLUEBERRY_JUICE), true, false));
    public static final Supplier<Item> CHERRY_JUICE = registerWithTab("cherry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.CHERRY_JUICE), true, false));
    public static final Supplier<Item> RASPBERRY_JUICE = registerWithTab("raspberry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.RASPBERRY_JUICE), true, false));
    public static final Supplier<Item> RED_GRAPE_JUICE = registerWithTab("red_grape_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.RED_GRAPE_JUICE), true, false));
    public static final Supplier<Item> GREEN_GRAPE_JUICE = registerWithTab("green_grape_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.GREEN_GRAPE_JUICE), true, false));
    public static final Supplier<Item> SWEET_BERRY_JUICE = registerWithTab("sweet_berry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.SWEET_BERRY_JUICE), true, false));
    public static final Supplier<Item> GLOW_BERRY_JUICE = registerWithTab("glow_berry_juice",
            () -> new DrinkableItem(drinkItem().food(HHFoodValues.GLOW_BERRY_JUICE), true, false));

    // Jar Items
    public static final Supplier<Item> JAR = registerWithTab("jar",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.JAR.get(), basicItem()));
    public static final Supplier<Item> BLUEBERRY_JAM = registerWithTab("blueberry_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.BLUEBERRY_JAM.get(), jarItem(HHFoodValues.BLUEBERRY_JAM)));
    public static final Supplier<Item> CHERRY_JAM = registerWithTab("cherry_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.CHERRY_JAM.get(), jarItem(HHFoodValues.BLUEBERRY_JAM)));
    public static final Supplier<Item> RASPBERRY_JAM = registerWithTab("raspberry_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.RASPBERRY_JAM.get(), jarItem(HHFoodValues.RASPBERRY_JAM)));
    public static final Supplier<Item> GRAPE_JAM = registerWithTab("grape_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.GRAPE_JAM.get(), jarItem(HHFoodValues.GRAPE_JAM)));
    public static final Supplier<Item> APPLE_JAM = registerWithTab("apple_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.APPLE_JAM.get(), jarItem(HHFoodValues.APPLE_JAM)));
    public static final Supplier<Item> SWEET_BERRY_JAM = registerWithTab("sweet_berry_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.SWEET_BERRY_JAM.get(), jarItem(HHFoodValues.SWEET_BERRY_JAM)));
    public static final Supplier<Item> GLOW_BERRY_JAM = registerWithTab("glow_berry_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.GLOW_BERRY_JAM.get(), jarItem(HHFoodValues.GLOW_BERRY_JAM)));
    public static final Supplier<Item> MELON_JAM = registerWithTab("melon_jam",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.MELON_JAM.get(), jarItem(HHFoodValues.MELON_JAM)));
    public static final Supplier<Item> PEANUT_BUTTER = registerWithTab("peanut_butter",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PEANUT_BUTTER.get(), jarItem(HHFoodValues.PEANUT_BUTTER)));
    public static final Supplier<Item> PICKLED_BEETROOTS = registerWithTab("pickled_beetroots",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PICKLED_BEETROOTS.get(), jarItem(HHFoodValues.PICKLED_BEETROOTS)));
    public static final Supplier<Item> PICKLED_CABBAGE = registerWithTab("pickled_cabbage",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PICKLED_CABBAGE.get(), jarItem(HHFoodValues.PICKLED_CABBAGE)));
    public static final Supplier<Item> PICKLED_CARROTS = registerWithTab("pickled_carrots",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PICKLED_CARROTS.get(), jarItem(HHFoodValues.PICKLED_CARROTS)));
    public static final Supplier<Item> PICKLED_ONIONS = registerWithTab("pickled_onions",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PICKLED_ONIONS.get(), jarItem(HHFoodValues.PICKLED_ONIONS)));
    public static final Supplier<Item> PICKLED_POTATOES = registerWithTab("pickled_potatoes",
            () -> new JarBlockItem(HHModBlocks.EMPTY_JAR_DISPLAY.get(), HHModBlocks.PICKLED_POTATOES.get(), jarItem(HHFoodValues.PICKLED_POTATOES)));

    // Sweets
    public static final Supplier<Item> CARAMEL = registerWithTab("caramel",
            () -> new ConsumableItem(foodItem(HHFoodValues.CARAMEL)));
    public static final Supplier<Item> CARAMEL_APPLE = registerWithTab("caramel_apple",
            () -> new ConsumableItem(foodItem(HHFoodValues.CARAMEL_APPLE)));
    public static final Supplier<Item> CHOCOLATE_BAR = registerWithTab("chocolate_bar",
            () -> new ConsumableItem(foodItem(HHFoodValues.CHOCOLATE_BAR)));
    public static final Supplier<Item> COTTON_CANDY = registerWithTab("cotton_candy",
            () -> new ConsumableItem(foodItem(HHFoodValues.COTTON_CANDY)));
    public static final Supplier<Item> BLUEBERRY_MUFFIN = registerWithTab("blueberry_muffin",
            () -> new ConsumableItem(foodItem(HHFoodValues.BLUEBERRY_MUFFIN)));
    public static final Supplier<Item> RASPBERRY_SCONE = registerWithTab("raspberry_scone",
            () -> new ConsumableItem(foodItem(HHFoodValues.BLUEBERRY_MUFFIN)));
    public static final Supplier<Item> PEANUT_BUTTER_COOKIE = registerWithTab("peanut_butter_cookie",
            () -> new ConsumableItem(foodItem(HHFoodValues.PEANUT_BUTTER_COOKIE)));
    public static final Supplier<Item> TRAIL_MIX = registerWithTab("trail_mix",
            () -> new ConsumableItem(foodItem(HHFoodValues.TRAIL_MIX)));
    public static final Supplier<Item> ROASTED_PEANUTS = registerWithTab("roasted_peanuts",
            () -> new ConsumableItem(foodItem(HHFoodValues.ROASTED_PEANUTS)));
    public static final Supplier<Item> MARSHMALLOW_STICK = registerWithTab("marshmallow_stick",
            () -> new RoastableItem(
                    foodItem(HHFoodValues.MARSHMALLOW_STICK).stacksTo(1).craftRemainder(Items.STICK),
                    HHModItems.ROASTED_MARSHMALLOW_STICK,
                    5,
                    Component.literal("Hold near a campfire to cook").withStyle(ChatFormatting.GRAY))
    );
    public static final Supplier<Item> ROASTED_MARSHMALLOW_STICK = registerWithTab("roasted_marshmallow_stick",
            () -> new RoastableItem(foodItem(HHFoodValues.ROASTED_MARSHMALLOW_STICK).stacksTo(1).craftRemainder(Items.STICK),
                    HHModItems.CHARRED_MARSHMALLOW_STICK,
                    10,
                    Component.literal("Perfectly roasted!").withStyle(ChatFormatting.GOLD))
    );
    public static final Supplier<Item> CHARRED_MARSHMALLOW_STICK = registerWithTab("charred_marshmallow_stick",
            () -> new RoastableItem(
                    foodItem(HHFoodValues.CHARRED_MARSHMALLOW_STICK).stacksTo(1).craftRemainder(Items.STICK),
                    null,
                    5,
                    Component.literal("Oh, it's burnt...").withStyle(ChatFormatting.DARK_GRAY))
    );
    public static final Supplier<Item> SMORE = registerWithTab("smore",
            () -> new ConsumableItem(foodItem(HHFoodValues.SMORE)));
    public static final Supplier<Item> SUGAR_CUBES = registerWithTab("sugar_cubes",
            () -> new SugarCubesItem(foodItem(HHFoodValues.SUGAR_CUBES)));

    // Sap and Syrup
    public static final Supplier<Item> SAP_BUCKET = registerWithTab("sap_bucket",
            () -> new BucketItem(HHModFluids.SAP.source().get(), basicItem().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Supplier<Item> SYRUP_BOTTLE = registerWithTab("syrup_bottle",
            () -> new Item(basicItem().craftRemainder(Items.GLASS_BOTTLE)));
    public static final Supplier<Item> MAPLE_COOKIE = registerWithTab("maple_cookie",
            () -> new ConsumableItem(foodItem(HHFoodValues.TORTILLA)));

    // Pies
    public static final Supplier<Item> BLUEBERRY_PIE = registerWithTab("blueberry_pie",
            () -> new BlockItem(HHModBlocks.BLUEBERRY_PIE.get(), basicItem()));
    public static final Supplier<Item> BLUEBERRY_PIE_SLICE = registerWithTab("blueberry_pie_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.BLUEBERRY_PIE_SLICE)));
    public static final Supplier<Item> RASPBERRY_PIE = registerWithTab("raspberry_pie",
            () -> new BlockItem(HHModBlocks.RASPBERRY_PIE.get(), basicItem()));
    public static final Supplier<Item> RASPBERRY_PIE_SLICE = registerWithTab("raspberry_pie_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.RASPBERRY_PIE_SLICE)));
    public static final Supplier<Item> GRAPE_PIE = registerWithTab("grape_pie",
            () -> new BlockItem(HHModBlocks.GRAPE_PIE.get(), basicItem()));
    public static final Supplier<Item> GRAPE_PIE_SLICE = registerWithTab("grape_pie_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.GRAPE_PIE_SLICE)));
    public static final Supplier<Item> PEANUT_BUTTER_PIE = registerWithTab("peanut_butter_pie",
            () -> new BlockItem(HHModBlocks.PEANUT_BUTTER_PIE.get(), basicItem()));
    public static final Supplier<Item> PEANUT_BUTTER_PIE_SLICE = registerWithTab("peanut_butter_pie_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.PEANUT_BUTTER_PIE_SLICE)));
    public static final Supplier<Item> CHICKEN_POT_PIE = registerWithTab("chicken_pot_pie",
            () -> new BlockItem(HHModBlocks.CHICKEN_POT_PIE.get(), basicItem()));
    public static final Supplier<Item> CHICKEN_POT_PIE_SLICE = registerWithTab("chicken_pot_pie_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.CHICKEN_POT_PIE_SLICE)));
    public static final Supplier<Item> CARROT_CAKE = registerWithTab("carrot_cake",
            () -> new BlockItem(HHModBlocks.CARROT_CAKE.get(), basicItem()));
    public static final Supplier<Item> CARROT_CAKE_SLICE = registerWithTab("carrot_cake_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.CARROT_CAKE_SLICE)));

    // Ingredients
    public static final Supplier<Item> COOKING_OIL = registerWithTab("cooking_oil",
            () -> new BucketItem(HHModFluids.COOKING_OIL.source().get(), basicItem().craftRemainder(Items.GLASS_BOTTLE)));
    public static final Supplier<Item> BUTTER = registerWithTab("butter",
            () -> new Item(basicItem()));
    public static final Supplier<Item> FLOUR = registerWithTab("flour",
            () -> new Item(basicItem()));
    public static final Supplier<Item> CORN_MEAL = registerWithTab("corn_meal",
            () -> new Item(basicItem()));
    public static final Supplier<Item> BATTER = registerWithTab("batter",
            () -> new ConsumableItem(basicItem().craftRemainder(Items.BOWL)));
    public static final Supplier<Item> SALT = registerWithTab("salt",
            () -> new Item(basicItem()));
    public static final Supplier<Item> TORTILLA = registerWithTab("tortilla",
            () -> new ConsumableItem(foodItem(HHFoodValues.TORTILLA)));

    public static final Supplier<Item> UNRIPE_CHEDDAR_CHEESE_WHEEL = registerWithTab("unripe_cheddar_cheese_wheel",
            () -> new BlockItem(HHModBlocks.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> CHEDDAR_CHEESE_WHEEL = registerWithTab("cheddar_cheese_wheel",
            () -> new BlockItem(HHModBlocks.CHEDDAR_CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> CHEDDAR_CHEESE_SLICE = registerWithTab("cheddar_cheese_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.CHEDDAR_CHEESE_SLICE)));
    public static final Supplier<Item> UNRIPE_GOAT_CHEESE_WHEEL = registerWithTab("unripe_goat_cheese_wheel",
            () -> new BlockItem(HHModBlocks.UNRIPE_GOAT_CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_WHEEL = registerWithTab("goat_cheese_wheel",
            () -> new BlockItem(HHModBlocks.GOAT_CHEESE_WHEEL.get(), basicItem()));
    public static final Supplier<Item> GOAT_CHEESE_SLICE = registerWithTab("goat_cheese_slice",
            () -> new ConsumableItem(foodItem(HHFoodValues.GOAT_CHEESE_SLICE)));

    // Foods
    public static final Supplier<Item> RAW_SAUSAGE = registerWithTab("raw_sausage",
            () -> new ConsumableItem(foodItem(HHFoodValues.RAW_SAUSAGE)));
    public static final Supplier<Item> COOKED_SAUSAGE = registerWithTab("cooked_sausage",
            () -> new ConsumableItem(foodItem(HHFoodValues.COOKED_SAUSAGE)));
    public static final Supplier<Item> RAW_SKEWERED_SAUSAGE = registerWithTab("raw_skewered_sausage",
            () -> new RoastableItem(foodItem(HHFoodValues.RAW_SKEWERED_SAUSAGE).stacksTo(1).craftRemainder(Items.STICK),
                    HHModItems.SKEWERED_SAUSAGE,
                    10,
                    Component.literal("Hold near a campfire to cook").withStyle(ChatFormatting.GRAY))
    );
    public static final Supplier<Item> SKEWERED_SAUSAGE = registerWithTab("skewered_sausage",
            () -> new ConsumableItem(foodItem(HHFoodValues.SKEWERED_SAUSAGE).craftRemainder(Items.STICK)));
    public static final Supplier<Item> JERKY = registerWithTab("jerky",
            () -> new ConsumableItem(foodItem(HHFoodValues.JERKY)));
    public static final Supplier<Item> TACO = registerWithTab("taco",
            () -> new ConsumableItem(foodItem(HHFoodValues.TACO)));
    public static final Supplier<Item> RAISINS = registerWithTab("raisins",
            () -> new ConsumableItem(foodItem(HHFoodValues.RAISINS)));
    public static final Supplier<Item> SUNFLOWER_SEEDS = registerWithTab("sunflower_seeds",
            () -> new SunflowerSeedItem(foodItem(HHFoodValues.SUNFLOWER_SEEDS)));
    public static final Supplier<Item> POPCORN = registerWithTab("popcorn",
            () -> new ConsumableItem(foodItem(HHFoodValues.POPCORN)));
    public static final Supplier<Item> UNCOOKED_CORN_ON_THE_COB = registerWithTab("uncooked_corn_on_the_cob",
            () -> new RoastableItem(foodItem(HHFoodValues.UNCOOKED_CORN_ON_THE_COB).stacksTo(1).craftRemainder(Items.STICK),
                    HHModItems.COOKED_CORN_ON_THE_COB,
                    10,
                    Component.literal("Hold near a campfire to cook").withStyle(ChatFormatting.GRAY))
    );
    public static final Supplier<Item> COOKED_CORN_ON_THE_COB = registerWithTab("cooked_corn_on_the_cob",
            () -> new RoastableItem(
                    foodItem(HHFoodValues.COOKED_CORN_ON_THE_COB).craftRemainder(Items.STICK),
                    null,
                    0,
                    Component.literal("Hot and buttery!").withStyle(ChatFormatting.GOLD))
    );
    public static final Supplier<Item> BAKED_APPLE = registerWithTab("baked_apple",
            () -> new ConsumableItem(foodItem(HHFoodValues.CIDER_DONUT)));
    public static final Supplier<Item> CIDER_DONUT = registerWithTab("cider_donut",
            () -> new ConsumableItem(foodItem(HHFoodValues.CIDER_DONUT)));
    public static final Supplier<Item> CANDY_CORN = registerWithTab("candy_corn",
            () -> new ConsumableItem(foodItem(HHFoodValues.CANDY_CORN)));
    public static final Supplier<Item> CORN_BREAD = registerWithTab("corn_bread",
            () -> new ConsumableItem(foodItem(HHFoodValues.CORN_BREAD)));
    public static final Supplier<Item> CORN_STEW = registerWithTab("corn_stew",
            () -> new ConsumableItem(bowlFoodItem(HHFoodValues.CORN_STEW)));
    public static final Supplier<Item> TAMALE = registerWithTab("tamale",
            () -> new ConsumableItem(foodItem(HHFoodValues.TAMALE)));
    public static final Supplier<Item> ELOTE = registerWithTab("elote",
            () -> new ConsumableItem(foodItem(HHFoodValues.ELOTE).stacksTo(1).craftRemainder(Items.STICK)));

    // Meals
    public static final Supplier<Item> MACARONI_AND_CHEESE = registerWithTab("macaroni_and_cheese",
            () -> new ConsumableItem(bowlFoodItem(HHFoodValues.MACARONI_AND_CHEESE)));
    public static final Supplier<Item> MASHED_POTATOES = registerWithTab("mashed_potatoes",
            () -> new ConsumableItem(bowlFoodItem(HHFoodValues.MASHED_POTATOES)));
    public static final Supplier<Item> PEANUT_BUTTER_AND_JELLY_SANDWICH = registerWithTab("peanut_butter_and_jelly_sandwich",
            () -> new ConsumableItem(foodItem(HHFoodValues.PEANUT_BUTTER_AND_JELLY_SANDWICH)));
    public static final Supplier<Item> WAFFLE = registerWithTab("waffle",
            () -> new ConsumableItem(foodItem(HHFoodValues.WAFFLE)));
    public static final Supplier<Item> BISCUITS_AND_GRAVY = registerWithTab("biscuits_and_gravy",
            () -> new ConsumableItem(bowlFoodItem(HHFoodValues.BISCUITS_AND_GRAVY)));
    public static final Supplier<Item> GLAZED_CARROTS = registerWithTab("glazed_carrots",
            () -> new ConsumableItem(bowlFoodItem(HHFoodValues.GLAZED_CARROTS)));
}