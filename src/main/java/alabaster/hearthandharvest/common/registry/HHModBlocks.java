package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.*;
import alabaster.hearthandharvest.common.block.trellis.GrapeTrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.CanvasRugBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.WildCropBlock;

import java.util.function.Supplier;

public class HHModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, HearthAndHarvest.MODID);

    // Workstations
    public static final Supplier<Block> TREE_TAPPER = BLOCKS.register("tree_tapper",
            () -> new TreeTapperBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD).randomTicks()));
    public static final Supplier<Block> CASK = BLOCKS.register("cask",
            () -> new CaskBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD).randomTicks()));
    public static final Supplier<Block> JUG = BLOCKS.register("jug",
            () -> new JugBlock(Block.Properties.ofFullCopy(Blocks.IRON_BARS).strength(2.0F, 3.0F).sound(SoundType.METAL)));
    public static final Supplier<Block> TROUGH = BLOCKS.register("trough",
            () -> new TroughBlock(Block.Properties.ofFullCopy(Blocks.CAULDRON)));


    public static final Supplier<Block> SAP_CAULDRON = BLOCKS.register("sap_cauldron",
            () -> new SapCauldronBlock(Block.Properties.ofFullCopy(Blocks.CAULDRON).strength(2.0F, 3.0F).sound(SoundType.METAL).randomTicks()));

    public static final Supplier<Block> COUNTER = BLOCKS.register("counter",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.BRICKS)));
    public static final Supplier<Block> DRAWER = BLOCKS.register("drawer",
            () -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BRICKS)));
    public static final Supplier<Block> BASIN = BLOCKS.register("basin",
            () -> new BasinBlock(Block.Properties.ofFullCopy(Blocks.BRICKS).randomTicks()));
    public static final Supplier<Block> NEST = BLOCKS.register("nest",
            () -> new NestBlock(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK)));
    public static final Supplier<Block> HAY_RUG = BLOCKS.register("hay_rug",
            () -> new CanvasRugBlock(Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F)));
    public static final Supplier<Block> STRAW_RUG = BLOCKS.register("straw_rug",
            () -> new CanvasRugBlock(Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F)));

    public static final Supplier<Block> SCARECROW = BLOCKS.register("scarecrow",
            () -> new ScarecrowBlock(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK)));

    public static final Supplier<Block> STOMPING_BASIN = BLOCKS.register("stomping_basin",
            () -> new StompingBasinBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));

    // Trellises
    public static final Supplier<TrellisBlock> TRELLIS = BLOCKS.register("trellis",
            () -> new TrellisBlock(Block.Properties.of().strength(2.0F).sound(SoundType.WOOD).noOcclusion().forceSolidOff(),
                    HHModBlocks::getGrapeTrellis));

    public static final Supplier<GrapeTrellisBlock> GRAPE_TRELLIS = BLOCKS.register("grape_trellis",
            () -> new GrapeTrellisBlock(Block.Properties.of().strength(2.0F).sound(SoundType.WOOD).noOcclusion().forceSolidOff(),
                    HHModBlocks::getTrellis));

    // This portion is needed to map transforming from trellis/grape trellises
    private static Block getTrellis() { return TRELLIS.get(); }
    private static Block getGrapeTrellis() { return GRAPE_TRELLIS.get(); }

    // Half-Cabinets
    public static final Supplier<Block> OAK_HALF_CABINET = BLOCKS.register("oak_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> SPRUCE_HALF_CABINET = BLOCKS.register("spruce_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> BIRCH_HALF_CABINET = BLOCKS.register("birch_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> JUNGLE_HALF_CABINET = BLOCKS.register("jungle_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> ACACIA_HALF_CABINET = BLOCKS.register("acacia_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> DARK_OAK_HALF_CABINET = BLOCKS.register("dark_oak_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> MANGROVE_HALF_CABINET = BLOCKS.register("mangrove_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> CHERRY_HALF_CABINET = BLOCKS.register("cherry_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD)));
    public static final Supplier<Block> BAMBOO_HALF_CABINET = BLOCKS.register("bamboo_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD)));
    public static final Supplier<Block> CRIMSON_HALF_CABINET = BLOCKS.register("crimson_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
    public static final Supplier<Block> WARPED_HALF_CABINET = BLOCKS.register("warped_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));

    // Crabbers Delight Compat
    public static final Supplier<Block> PALM_HALF_CABINET = ModList.get().isLoaded("crabbersdelight")
            ? BLOCKS.register("palm_half_cabinet",
            () -> new HalfCabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)))
            : null;

    public static final Supplier<Block> CRATE = BLOCKS.register("crate",
            () -> new CrateBlock(Block.Properties.ofFullCopy(Blocks.BARREL).noOcclusion()));

    // Bottle Racks
    public static final Supplier<Block> OAK_BOTTLE_RACK = BLOCKS.register("oak_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> SPRUCE_BOTTLE_RACK = BLOCKS.register("spruce_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> BIRCH_BOTTLE_RACK = BLOCKS.register("birch_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> JUNGLE_BOTTLE_RACK = BLOCKS.register("jungle_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> ACACIA_BOTTLE_RACK = BLOCKS.register("acacia_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> DARK_OAK_BOTTLE_RACK = BLOCKS.register("dark_oak_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> MANGROVE_BOTTLE_RACK = BLOCKS.register("mangrove_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
    public static final Supplier<Block> CHERRY_BOTTLE_RACK = BLOCKS.register("cherry_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD)));
    public static final Supplier<Block> BAMBOO_BOTTLE_RACK = BLOCKS.register("bamboo_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD)));
    public static final Supplier<Block> CRIMSON_BOTTLE_RACK = BLOCKS.register("crimson_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
    public static final Supplier<Block> WARPED_BOTTLE_RACK = BLOCKS.register("warped_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));

    // Crabbers Delight Compat
    public static final Supplier<Block> PALM_BOTTLE_RACK = ModList.get().isLoaded("crabbersdelight")
            ? BLOCKS.register("palm_bottle_rack",
            () -> new BottleRackBlock(Block.Properties.ofFullCopy(Blocks.BARREL)))
            : null;

    // Wild Crops
    public static final Supplier<Block> WILD_RED_GRAPES = BLOCKS.register("wild_red_grapes",
            () -> new WildCropBlock(MobEffects.MOVEMENT_SPEED, 10, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
    public static final Supplier<Block> WILD_GREEN_GRAPES = BLOCKS.register("wild_green_grapes",
            () -> new WildCropBlock(MobEffects.MOVEMENT_SPEED, 10, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
    public static final Supplier<Block> WILD_COTTON = BLOCKS.register("wild_cotton",
            () -> new WildCropBlock(MobEffects.MOVEMENT_SPEED, 10, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
    public static final Supplier<Block> WILD_PEANUTS = BLOCKS.register("wild_peanuts",
            () -> new WildCropBlock(MobEffects.MOVEMENT_SPEED, 10, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));

    // Flowers
    public static final Supplier<Block> YELLOW_MUM = BLOCKS.register("yellow_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> ORANGE_MUM = BLOCKS.register("orange_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> RED_MUM = BLOCKS.register("red_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> BLUE_MUM = BLOCKS.register("blue_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> LIGHT_BLUE_MUM = BLOCKS.register("light_blue_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> PURPLE_MUM = BLOCKS.register("purple_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> PINK_MUM = BLOCKS.register("pink_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));
    public static final Supplier<Block> WHITE_MUM = BLOCKS.register("white_mum",
            () -> new FlowerBlock(MobEffects.HEAL, 10, Block.Properties.ofFullCopy(Blocks.POPPY)));

    // Potted Flowers
    public static final Supplier<Block> POTTED_YELLOW_MUM = BLOCKS.register("potted_yellow_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.YELLOW_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_ORANGE_MUM = BLOCKS.register("potted_orange_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.ORANGE_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_RED_MUM = BLOCKS.register("potted_red_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.RED_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_BLUE_MUM = BLOCKS.register("potted_blue_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.BLUE_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_LIGHT_BLUE_MUM = BLOCKS.register("potted_light_blue_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.LIGHT_BLUE_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_PURPLE_MUM = BLOCKS.register("potted_purple_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.PURPLE_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_PINK_MUM = BLOCKS.register("potted_pink_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.PINK_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final Supplier<Block> POTTED_WHITE_MUM = BLOCKS.register("potted_white_mum",
            () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, () -> HHModBlocks.WHITE_MUM.get(), Block.Properties.ofFullCopy(Blocks.POTTED_POPPY)));

    // Crops
    public static final Supplier<Block> RASPBERRY_BUSH = BLOCKS.register("raspberry_bush",
            () -> new RaspberryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final Supplier<Block> BLUEBERRY_BUSH = BLOCKS.register("blueberry_bush",
            () -> new BlueberryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final Supplier<Block> PEANUT_CROP = BLOCKS.register("peanuts",
            () -> new PeanutBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
    public static final Supplier<Block> COTTON_CROP = BLOCKS.register("cotton",
            () -> new CottonBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
    public static final Supplier<Block> CORN_STALK = BLOCKS.register("corn_stalk",
            () -> new CornStalkBlock());

    // Crates
    public static final Supplier<Block> BLUEBERRY_CRATE = BLOCKS.register("blueberry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> CHERRY_CRATE = BLOCKS.register("cherry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> RED_GRAPE_CRATE = BLOCKS.register("red_grape_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GREEN_GRAPE_CRATE = BLOCKS.register("green_grape_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> RASPBERRY_CRATE = BLOCKS.register("raspberry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> PEANUT_CRATE = BLOCKS.register("peanut_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> CORN_CRATE = BLOCKS.register("corn_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> APPLE_CRATE = BLOCKS.register("apple_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GOLDEN_APPLE_CRATE = BLOCKS.register("golden_apple_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GOLDEN_CARROT_CRATE = BLOCKS.register("golden_carrot_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> POISONOUS_POTATO_CRATE = BLOCKS.register("poisonous_potato_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> ROTTEN_TOMATO_CRATE = BLOCKS.register("rotten_tomato_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GLOW_BERRY_CRATE = BLOCKS.register("glow_berry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).lightLevel(state -> 10)));
    public static final Supplier<Block> SWEET_BERRY_CRATE = BLOCKS.register("sweet_berry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    
    // Bags
    public static final Supplier<Block> SALT_BAG = BLOCKS.register("salt_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> SUGAR_BAG = BLOCKS.register("sugar_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> COCOA_BEAN_BAG = BLOCKS.register("cocoa_bean_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> GUNPOWDER_BAG = BLOCKS.register("gunpowder_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> CORN_KERNEL_BAG = BLOCKS.register("corn_kernel_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> FLOUR_BAG = BLOCKS.register("flour_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> MANURE_BAG = BLOCKS.register("manure_bag",
            () -> new ManureBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    
    // Misc Storage Blocks
    public static final Supplier<Block> COTTON_BALE = BLOCKS.register("cotton_bale",
            () -> new HayBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> SUGAR_CANE_BUNDLE = BLOCKS.register("sugar_cane_bundle",
            () -> new HayBlock(Block.Properties.ofFullCopy(Blocks.SUGAR_CANE)));
    public static final Supplier<Block> SPOOL = BLOCKS.register("spool",
            () -> new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final Supplier<Block> ROPE_COIL = BLOCKS.register("rope_coil",
            () -> new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final Supplier<Block> CORN_HUSK_BUNDLE = BLOCKS.register("corn_husk_bundle",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.DRIED_KELP_BLOCK).strength(2.0F, 3.0F).sound(SoundType.CROP)));
    public static final Supplier<Block> CHARCOAL_BLOCK = BLOCKS.register("charcoal_block",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.COAL_BLOCK)));
    public static final Supplier<Block> STICK_BRUSH = BLOCKS.register("stick_brush",
            () -> new HayBlock(Block.Properties.ofFullCopy(Blocks.MANGROVE_ROOTS)));
    public static final Supplier<Block> MULCH = BLOCKS.register("mulch",
            () -> new MulchBlock(Block.Properties.ofFullCopy(Blocks.MANGROVE_ROOTS).strength(1.0F, 1.5F).sound(SoundType.WET_GRASS).randomTicks()));

    // Half-Slab Crates
    public static final Supplier<Block> BROWN_MUSHROOM_CRATE = BLOCKS.register("brown_mushroom_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> RED_MUSHROOM_CRATE = BLOCKS.register("red_mushroom_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> CRIMSON_FUNGUS_CRATE = BLOCKS.register("crimson_fungus_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> WARPED_FUNGUS_CRATE = BLOCKS.register("warped_fungus_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

    // Pies
    public static final Supplier<Block> RASPBERRY_PIE = BLOCKS.register("raspberry_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), HHModItems.RASPBERRY_PIE_SLICE));
    public static final Supplier<Block> BLUEBERRY_PIE = BLOCKS.register("blueberry_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), HHModItems.BLUEBERRY_PIE_SLICE));
    public static final Supplier<Block> GRAPE_PIE = BLOCKS.register("grape_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), HHModItems.GRAPE_PIE_SLICE));
    public static final Supplier<Block> PEANUT_BUTTER_PIE = BLOCKS.register("peanut_butter_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), HHModItems.PEANUT_BUTTER_PIE_SLICE));
    public static final Supplier<Block> CHICKEN_POT_PIE = BLOCKS.register("chicken_pot_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), HHModItems.CHICKEN_POT_PIE_SLICE));
    public static final Supplier<Block> CARROT_CAKE = BLOCKS.register("carrot_cake",
            () -> new SliceableCakeBlock(Block.Properties.ofFullCopy(Blocks.CAKE),  HHModItems.CARROT_CAKE_SLICE));

    // Jars
    public static final Supplier<Block> EMPTY_JAR_DISPLAY = BLOCKS.register("empty_jar_display",
            () -> new JarBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion()));
    public static final Supplier<Block> JAR = BLOCKS.register("jar",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> BLUEBERRY_JAM = BLOCKS.register("blueberry_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> CHERRY_JAM = BLOCKS.register("cherry_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> GRAPE_JAM = BLOCKS.register("grape_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> RASPBERRY_JAM = BLOCKS.register("raspberry_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> APPLE_JAM = BLOCKS.register("apple_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> SWEET_BERRY_JAM = BLOCKS.register("sweet_berry_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> GLOW_BERRY_JAM = BLOCKS.register("glow_berry_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).lightLevel(state -> 8).noOcclusion()));
    public static final Supplier<Block> MELON_JAM = BLOCKS.register("melon_jam",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PEANUT_BUTTER = BLOCKS.register("peanut_butter",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PICKLED_BEETROOTS = BLOCKS.register("pickled_beetroots",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PICKLED_CABBAGE = BLOCKS.register("pickled_cabbage",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PICKLED_CARROTS = BLOCKS.register("pickled_carrots",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PICKLED_ONIONS = BLOCKS.register("pickled_onions",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));
    public static final Supplier<Block> PICKLED_POTATOES = BLOCKS.register("pickled_potatoes",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS).strength(0.5F, 2.0F).sound(SoundType.GLASS).noOcclusion()));

    // Cheese
    public static final Supplier<Block> UNRIPE_CHEDDAR_CHEESE_WHEEL = BLOCKS.register("unripe_cheddar_cheese_wheel",
            () -> new UnripeCheeseWheelBlock(HHModBlocks.CHEDDAR_CHEESE_WHEEL, Block.Properties.ofFullCopy(Blocks.CAKE)));
    public static final Supplier<Block> CHEDDAR_CHEESE_WHEEL = BLOCKS.register("cheddar_cheese_wheel",
            () -> new CheeseWheelBlock(HHModItems.CHEDDAR_CHEESE_SLICE, Block.Properties.ofFullCopy(Blocks.CAKE)));

    public static final Supplier<Block> UNRIPE_GOAT_CHEESE_WHEEL = BLOCKS.register("unripe_goat_cheese_wheel",
            () -> new UnripeCheeseWheelBlock(HHModBlocks.GOAT_CHEESE_WHEEL, Block.Properties.ofFullCopy(Blocks.CAKE)));
    public static final Supplier<Block> GOAT_CHEESE_WHEEL = BLOCKS.register("goat_cheese_wheel",
            () -> new CheeseWheelBlock(HHModItems.GOAT_CHEESE_SLICE, Block.Properties.ofFullCopy(Blocks.CAKE)));

    // Salt Blocks
    public static final Supplier<Block> SALT_BLOCK = BLOCKS.register("salt_block",
            () -> new SaltBlock(Block.Properties.ofFullCopy(Blocks.GRAVEL).strength(2.0F, 3.0F).sound(SoundType.TUFF).randomTicks()));
    public static final Supplier<Block> LIGHTLY_LICKED_SALT_BLOCK = BLOCKS.register("lightly_licked_salt_block",
            () -> new SaltBlock(Block.Properties.ofFullCopy(Blocks.GRAVEL).strength(2.0F, 3.0F).sound(SoundType.TUFF).randomTicks()));
    public static final Supplier<Block> WELL_LICKED_SALT_BLOCK = BLOCKS.register("well_licked_salt_block",
            () -> new SaltBlock(Block.Properties.ofFullCopy(Blocks.GRAVEL).strength(2.0F, 3.0F).sound(SoundType.TUFF).randomTicks()));
    public static final Supplier<Block> HEAVILY_LICKED_SALT_BLOCK = BLOCKS.register("heavily_licked_salt_block",
            () -> new SaltBlock(Block.Properties.ofFullCopy(Blocks.GRAVEL).strength(2.0F, 3.0F).sound(SoundType.TUFF).randomTicks()));

    public static final Supplier<Block> POLISHED_SALT_BLOCK = BLOCKS.register("polished_salt_block",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GRAVEL).strength(2.5F, 3.0F).sound(SoundType.POLISHED_TUFF)));
    
    public static final Supplier<Block> SALT_STAIRS = BLOCKS.register("salt_stairs",
            () -> new StairBlock(HHModBlocks.SALT_BLOCK.get().defaultBlockState(), Block.Properties.ofFullCopy(HHModBlocks.SALT_BLOCK.get())));
    public static final Supplier<Block> POLISHED_SALT_STAIRS = BLOCKS.register("polished_salt_stairs",
            () -> new StairBlock(HHModBlocks.POLISHED_SALT_BLOCK.get().defaultBlockState(), Block.Properties.ofFullCopy(HHModBlocks.POLISHED_SALT_BLOCK.get())));

    public static final Supplier<Block> SALT_SLAB = BLOCKS.register("salt_slab",
            () -> new SlabBlock(Block.Properties.ofFullCopy(HHModBlocks.SALT_BLOCK.get())));
    public static final Supplier<Block> POLISHED_SALT_SLAB = BLOCKS.register("polished_salt_slab",
            () -> new SlabBlock(Block.Properties.ofFullCopy(HHModBlocks.POLISHED_SALT_BLOCK.get())));
    
    public static final Supplier<Block> SALT_WALL = BLOCKS.register("salt_wall",
            () -> new WallBlock(Block.Properties.ofFullCopy(HHModBlocks.SALT_BLOCK.get())));
    public static final Supplier<Block> POLISHED_SALT_WALL = BLOCKS.register("polished_salt_wall",
            () -> new WallBlock(Block.Properties.ofFullCopy(HHModBlocks.POLISHED_SALT_BLOCK.get())));

    public static final Supplier<Block> SALT_DRIP = BLOCKS.register("salt_drip",
            () -> new SaltDripBlock(BlockBehaviour.Properties.of()
                    .strength(1.0f, 1.0f).sound(SoundType.GRAVEL).noOcclusion()));

    public static final Supplier<Block> SALT_LAMP = BLOCKS.register("salt_lamp",
            () -> new SaltLampBlock(Block.Properties.of()
                    .strength(1.5f).sound(SoundType.STONE)
                    .lightLevel(state -> state.getValue(SaltLampBlock.LIT) ? 15 : 0)
                    .noOcclusion()));

    // Manure
    public static final Supplier<Block> MANURE_BLOCK = BLOCKS.register("manure_block",
            () -> new ManureBlock(Block.Properties.ofFullCopy(Blocks.MUD).strength(1.0F, 2.0F).sound(SoundType.MUD).randomTicks()));

    public static final Supplier<Block> MANURE_BRICKS_BLOCK = BLOCKS.register("manure_bricks",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.MUD_BRICKS).strength(2.5F, 3.0F).sound(SoundType.MUD_BRICKS).randomTicks()));
    public static final Supplier<Block> POLISHED_MANURE = BLOCKS.register("polished_manure",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.MUD_BRICKS).strength(2.5F, 3.0F).sound(SoundType.MUD_BRICKS)));

    public static final Supplier<Block> MANURE_BRICK_STAIRS = BLOCKS.register("manure_brick_stairs",
            () -> new StairBlock(HHModBlocks.MANURE_BRICKS_BLOCK.get().defaultBlockState(), Block.Properties.ofFullCopy(HHModBlocks.MANURE_BRICKS_BLOCK.get())));
    public static final Supplier<Block> POLISHED_MANURE_STAIRS = BLOCKS.register("polished_manure_stairs",
            () -> new StairBlock(HHModBlocks.POLISHED_MANURE.get().defaultBlockState(), Block.Properties.ofFullCopy(HHModBlocks.POLISHED_MANURE.get())));

    public static final Supplier<Block> MANURE_BRICK_SLAB = BLOCKS.register("manure_brick_slab",
            () -> new SlabBlock(Block.Properties.ofFullCopy(HHModBlocks.MANURE_BRICKS_BLOCK.get())));
    public static final Supplier<Block> POLISHED_MANURE_SLAB = BLOCKS.register("polished_manure_slab",
            () -> new SlabBlock(Block.Properties.ofFullCopy(HHModBlocks.POLISHED_MANURE.get())));

    public static final Supplier<Block> MANURE_BRICK_WALL = BLOCKS.register("manure_brick_wall",
            () -> new WallBlock(Block.Properties.ofFullCopy(HHModBlocks.MANURE_BRICKS_BLOCK.get())));
    public static final Supplier<Block> POLISHED_MANURE_WALL = BLOCKS.register("polished_manure_wall",
            () -> new WallBlock(Block.Properties.ofFullCopy(HHModBlocks.POLISHED_MANURE.get())));
}