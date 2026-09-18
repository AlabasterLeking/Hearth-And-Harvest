package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.worldgen.processor.JarRotationProcessor;
import alabaster.hearthandharvest.common.worldgen.processor.PreserveFromAirProcessor;
import alabaster.hearthandharvest.common.worldgen.structure.LilliputLaneStructure;
import alabaster.hearthandharvest.common.worldgen.structure.corn_maze.CornMazeStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, HearthAndHarvest.MODID);

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, HearthAndHarvest.MODID);

    public static final Supplier<StructureType<CornMazeStructure>> CORN_MAZE =
            STRUCTURES.register("corn_maze", () -> () -> CornMazeStructure.CODEC);

    public static final Supplier<StructureType<LilliputLaneStructure>> LILLIPUT_LANE =
            STRUCTURES.register("lilliput_lane", () -> () -> LilliputLaneStructure.CODEC);

    public static final Supplier<StructureProcessorType<JarRotationProcessor>> JAR_ROTATION =
            PROCESSORS.register("jar_rotation", () -> () -> JarRotationProcessor.CODEC);

    public static final Supplier<StructureProcessorType<PreserveFromAirProcessor>> PRESERVE_FROM_AIR =
            PROCESSORS.register("preserve_from_air", () -> () -> PreserveFromAirProcessor.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
        PROCESSORS.register(eventBus);
    }
}