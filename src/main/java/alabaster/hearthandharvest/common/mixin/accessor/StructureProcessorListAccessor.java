package alabaster.hearthandharvest.common.mixin.accessor;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureProcessorList.class)
public interface StructureProcessorListAccessor {
    @Accessor("list")
    List<StructureProcessor> hearthandharvest$getList();

    @Mutable
    @Accessor("list")
    void hearthandharvest$setList(List<StructureProcessor> list);
}
