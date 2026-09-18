package alabaster.hearthandharvest.common.worldgen.structure;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Optional;

public class LilliputLaneStructure extends Structure {
    public static final MapCodec<LilliputLaneStructure> CODEC = JigsawStructure.CODEC.xmap(LilliputLaneStructure::new, structure -> structure.jigsaw);

    private final JigsawStructure jigsaw;

    public LilliputLaneStructure(JigsawStructure jigsaw) {
        super(new StructureSettings(jigsaw.biomes(), jigsaw.spawnOverrides(), jigsaw.step(), jigsaw.terrainAdaptation()));
        this.jigsaw = jigsaw;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!Config.GENERATE_LILLIPUT_LANE.get()) return Optional.empty();
        return jigsaw.findValidGenerationPoint(context);
    }

    @Override
    public StructureType<?> type() {
        return HHModStructures.LILLIPUT_LANE.get();
    }
}