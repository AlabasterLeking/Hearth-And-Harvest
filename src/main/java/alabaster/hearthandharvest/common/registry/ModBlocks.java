package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, HearthAndHarvest.MODID);

    // Workstations
    public static final Supplier<Block> TREE_TAPPER = BLOCKS.register("tree_tapper",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));


    // Crop Storage
    public static final Supplier<Block> RASPBERRY_CRATE = BLOCKS.register("raspberry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> BLUEBERRY_CRATE = BLOCKS.register("blueberry_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GRAPE_CRATE = BLOCKS.register("grape_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> PEANUT_CRATE = BLOCKS.register("peanut_crate",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> SALT_BAG = BLOCKS.register("salt_bag",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final Supplier<Block> COTTON_BALE = BLOCKS.register("cotton_bale",
            () -> new HayBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final Supplier<Block> SPOOL = BLOCKS.register("spool",
            () -> new HayBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));

    // Half-Slab Crates
    public static final Supplier<Block> EGG_CRATE = BLOCKS.register("egg_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> MILK_CRATE = BLOCKS.register("milk_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> GOAT_MILK_CRATE = BLOCKS.register("goat_milk_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> MEAD_CRATE = BLOCKS.register("mead_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> WINE_CRATE = BLOCKS.register("wine_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> WATER_CRATE = BLOCKS.register("water_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> HONEY_CRATE = BLOCKS.register("honey_crate",
            () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
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
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.RASPBERRY_PIE_SLICE));
    public static final Supplier<Block> BLUEBERRY_PIE = BLOCKS.register("blueberry_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.BLUEBERRY_PIE_SLICE));
    public static final Supplier<Block> GRAPE_PIE = BLOCKS.register("grape_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.GRAPE_PIE_SLICE));
    public static final Supplier<Block> CHICKEN_POT_PIE = BLOCKS.register("chicken_pot_pie",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.CHICKEN_POT_PIE_SLICE));

    // Cheese
    public static final Supplier<Block> CHEESE_WHEEL = BLOCKS.register("cheese_wheel",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.CHEESE_SLICE));
    public static final Supplier<Block> GOAT_CHEESE_WHEEL = BLOCKS.register("goat_cheese_wheel",
            () -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.GOAT_CHEESE_SLICE));
}