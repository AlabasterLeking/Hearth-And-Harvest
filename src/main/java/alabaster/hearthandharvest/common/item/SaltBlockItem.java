package alabaster.hearthandharvest.common.item;

import net.minecraft.nbt.CompoundTag;
import alabaster.hearthandharvest.common.block.SaltBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SaltBlockItem extends BlockItem {

    public SaltBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (isWaxed(stack)) {
            String base = getDescriptionId(stack);
            int dot = base.lastIndexOf('.');
            String waxedKey = dot >= 0
                    ? base.substring(0, dot + 1) + "waxed_" + base.substring(dot + 1)
                    : "waxed_" + base;
            return Component.translatable(waxedKey);
        }
        return super.getName(stack);
    }

    @Override
    public BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) return null;
        CompoundTag props = context.getItemInHand().getTagElement(BLOCK_STATE_TAG);
        if (props != null && props.contains("waxed")) {
            state = state.setValue(SaltBlock.WAXED, Boolean.parseBoolean(props.getString("waxed")));
        }
        return state;
    }

    public static boolean isWaxed(ItemStack stack) {
        CompoundTag props = stack.getTagElement(BLOCK_STATE_TAG);
        return props != null && "true".equals(props.getString("waxed"));
    }

    public static ItemStack markWaxed(ItemStack stack) {
        stack.getOrCreateTagElement(BLOCK_STATE_TAG).putString("waxed", "true");
        return stack;
    }
}