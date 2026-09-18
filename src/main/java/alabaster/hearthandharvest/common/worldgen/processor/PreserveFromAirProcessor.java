package alabaster.hearthandharvest.common.worldgen.processor;

import alabaster.hearthandharvest.common.registry.HHModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class PreserveFromAirProcessor extends StructureProcessor {
    public static final MapCodec<PreserveFromAirProcessor> CODEC = TagKey.hashedCodec(Registries.BLOCK)
            .xmap(PreserveFromAirProcessor::new, processor -> processor.preserved)
            .fieldOf("preserved");

    private final TagKey<Block> preserved;

    public PreserveFromAirProcessor(TagKey<Block> preserved) {
        this.preserved = preserved;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (!relativeBlockInfo.state().isAir()) return relativeBlockInfo;

        BoundingBox box = settings.getBoundingBox();
        if (box != null && !box.isInside(relativeBlockInfo.pos())) return relativeBlockInfo;

        return level.getBlockState(relativeBlockInfo.pos()).is(preserved) ? null : relativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return HHModStructures.PRESERVE_FROM_AIR.get();
    }
}