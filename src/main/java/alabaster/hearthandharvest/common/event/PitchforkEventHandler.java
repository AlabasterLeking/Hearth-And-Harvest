package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import vectorwing.farmersdelight.common.registry.ModItems;

@EventBusSubscriber(modid = "hearthandharvest", bus = EventBusSubscriber.Bus.GAME)
public class PitchforkEventHandler {

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!event.getTool().is(HHModItems.PITCHFORK.get())) return;
        BlockState state = event.getState();
        if (!isGrassOrCrop(state)) return;
        event.getDrops().clear();
        BlockPos pos = event.getPos();
        int count = 2 + event.getLevel().random.nextInt(3);
        ItemStack straw = new ItemStack(ModItems.STRAW.get(), count);
        event.getDrops().add(new ItemEntity(event.getLevel(),
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, straw));
    }

    private static boolean isGrassOrCrop(BlockState state) {
        return state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(BlockTags.CROPS)
                || state.is(Blocks.WHEAT);
    }
}