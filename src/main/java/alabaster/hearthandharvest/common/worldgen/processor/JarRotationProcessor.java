package alabaster.hearthandharvest.common.worldgen.processor;

import alabaster.hearthandharvest.common.block.JarBlock;
import alabaster.hearthandharvest.common.block.entity.JarBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class JarRotationProcessor extends StructureProcessor {
    public static final JarRotationProcessor INSTANCE = new JarRotationProcessor();
    public static final MapCodec<JarRotationProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private JarRotationProcessor() {
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (relativeBlockInfo.nbt() == null || !(relativeBlockInfo.state().getBlock() instanceof JarBlock)) return relativeBlockInfo;
        if (settings.getMirror() == Mirror.NONE && settings.getRotation() == Rotation.NONE) return relativeBlockInfo;
        return new StructureTemplate.StructureBlockInfo(
                relativeBlockInfo.pos(),
                relativeBlockInfo.state(),
                JarBlockEntity.transformTag(relativeBlockInfo.nbt(), settings.getMirror(), settings.getRotation())
        );
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return HHModStructures.JAR_ROTATION.get();
    }
}