package alabaster.hearthandharvest.common.worldgen.structure.corn_maze;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class CornMazeStructure extends Structure {

    public static final MapCodec<CornMazeStructure> CODEC = simpleCodec(CornMazeStructure::new);

    private static final int MAX_HEIGHT_VARIANCE = 5;

    public CornMazeStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public StructureType<?> type() {
        return HHModStructures.CORN_MAZE.get();
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!Config.GENERATE_CORN_MAZES.get()) return Optional.empty();

        RandomSource random = context.random();
        int w = 23 + random.nextInt(7) * 2;
        int h = 23 + random.nextInt(7) * 2;

        int baseX = context.chunkPos().getMinBlockX();
        int baseZ = context.chunkPos().getMinBlockZ();

        if (!isValidPlacement(context, baseX, baseZ, w, h)) return Optional.empty();

        int surfaceY = context.chunkGenerator().getBaseHeight(
                baseX + w / 2,
                baseZ + h / 2,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );

        BlockPos origin = new BlockPos(baseX, surfaceY, baseZ);
        return Optional.of(new GenerationStub(origin, builder -> buildMazePieces(builder, origin, w, h)));
    }

    private boolean isValidPlacement(GenerationContext context, int baseX, int baseZ, int w, int h) {
        ChunkGenerator gen = context.chunkGenerator();
        var heightAccessor = context.heightAccessor();
        RandomState randomState = context.randomState();
        int seaLevel = gen.getSeaLevel();

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int sx = 0; sx <= w; sx += 4) {
            for (int sz = 0; sz <= h; sz += 4) {
                int x = baseX + sx;
                int z = baseZ + sz;

                int surface = gen.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
                int floor = gen.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState);

                if (surface != floor) return false;
                if (surface <= seaLevel + 2) return false;

                minY = Math.min(minY, surface);
                maxY = Math.max(maxY, surface);
            }
        }

        return (maxY - minY) <= MAX_HEIGHT_VARIANCE;
    }

    private void buildMazePieces(StructurePiecesBuilder builder, BlockPos origin, int w, int h) {
        builder.addPiece(new CornMazeStructurePiece(origin, w, h));
    }
}