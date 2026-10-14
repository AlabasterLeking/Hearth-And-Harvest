package alabaster.hearthandharvest.common.item;

import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import alabaster.hearthandharvest.common.block.entity.CaskBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class CaskBlockItem extends BlockItem {
    public CaskBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        if (tag == null) return;

        if (tag.getBoolean("Sealed")) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.cask.sealed").withStyle(ChatFormatting.AQUA));
        }

        int remaining = tag.getInt("AgeTimeTotal") - tag.getInt("AgeTime");
        if (tag.getInt("AgeTimeTotal") > 0 && remaining > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.cask.remaining", remaining / 20)
                    .withStyle(ChatFormatting.GRAY));
        }

        ItemStackHandler inventory = new ItemStackHandler(CaskBlockEntity.INVENTORY_SIZE);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        appendContents(tooltip, inventory);
    }

    static void appendContents(List<Component> tooltip, ItemStackHandler inventory) {
        NonNullList<ItemStack> contents = NonNullList.create();
        for (int slot = 0; slot < inventory.getSlots(); ++slot) {
            ItemStack stored = inventory.getStackInSlot(slot);
            if (!stored.isEmpty()) contents.add(stored);
        }
        if (contents.isEmpty()) return;

        int shown = Math.min(contents.size(), 4);
        for (int i = 0; i < shown; ++i) {
            ItemStack stored = contents.get(i);
            tooltip.add(Component.translatable("tooltip.hearthandharvest.contents.entry",
                    stored.getHoverName(), stored.getCount()).withStyle(ChatFormatting.GRAY));
        }
        if (contents.size() > shown) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.contents.more", contents.size() - shown)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}