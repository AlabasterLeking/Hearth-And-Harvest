package alabaster.hearthandharvest.common.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Optional;

public class LilliputLaneStructure extends Structure {
    public static final Codec<LilliputLaneStructure> CODEC = jigsawMapCodec().xmap(LilliputLaneStructure::new, structure -> structure.jigsaw).codec();

    private final JigsawStructure jigsaw;

    public LilliputLaneStructure(JigsawStructure jigsaw) {
        super(new StructureSettings(jigsaw.biomes(), jigsaw.spawnOverrides(), jigsaw.step(), jigsaw.terrainAdaptation()));
        this.jigsaw = jigsaw;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!Config.GENERATE_LILLIPUT_LANE.get()) return Optional.empty();
        return jigsaw.findValidGenerationPoint(context);
    }

    @Override
    public StructureType<?> type() {
        return HHModStructures.LILLIPUT_LANE.get();
    }

    private static MapCodec<JigsawStructure> jigsawMapCodec() {
        if (JigsawStructure.CODEC instanceof MapCodec.MapCodecCodec<JigsawStructure> mapCodecCodec) return mapCodecCodec.codec();
        throw new IllegalStateException("JigsawStructure.CODEC is not a map codec");
    }
}
