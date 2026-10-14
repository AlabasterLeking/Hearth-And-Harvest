package alabaster.hearthandharvest.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class VintageBlockItem extends BlockItem {
    public VintageBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return VintageHelper.vintageName(stack, super.getName(stack));
    }
}