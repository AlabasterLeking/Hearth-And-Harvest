package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS;

    static {
        BLOCKS = DeferredRegister.create(Registries.BLOCK, HearthAndHarvest.MODID);
    }
}