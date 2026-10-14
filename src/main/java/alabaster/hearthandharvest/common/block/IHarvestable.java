package alabaster.hearthandharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IHarvestable {
    boolean isHarvestReady(BlockState state);
    void harvestBlock(BlockState state, Level level, BlockPos pos, Player player, ItemStack tool);
}