package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.worldgen.structure.corn_maze.CornMazeStructurePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModStructurePieces {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, HearthAndHarvest.MODID);

    public static final Supplier<StructurePieceType> CORN_MAZE_PIECE =
            STRUCTURE_PIECES.register("corn_maze_piece", () -> CornMazeStructurePiece::new);

    public static void register(IEventBus eventBus) {
        STRUCTURE_PIECES.register(eventBus);
    }
}
