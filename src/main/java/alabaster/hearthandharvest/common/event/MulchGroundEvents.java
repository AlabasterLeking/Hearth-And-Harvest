package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.AlterGroundEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class MulchGroundEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAlterGround(AlterGroundEvent event) {
        LevelSimulatedReader level = event.getContext().level();
        AlterGroundEvent.StateProvider previous = event.getStateProvider();
        event.setStateProvider((random, pos) -> {
            BlockState replacement = previous.getState(random, pos);
            for (int i = 2; i >= -3; i--) {
                BlockPos target = pos.above(i);
                if (Feature.isGrassOrDirt(level, target)) {
                    return level.isStateAtPosition(target, state -> state.is(HHModBlocks.MULCH.get()))
                            ? HHModBlocks.MULCH.get().defaultBlockState()
                            : replacement;
                }
                if (!level.isStateAtPosition(target, BlockState::isAir) && i < 0) break;
            }
            return replacement;
        });
    }
}