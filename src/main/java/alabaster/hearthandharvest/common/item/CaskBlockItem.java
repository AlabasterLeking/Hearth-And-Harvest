package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.entity.CaskBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class CaskBlockItem extends BlockItem {
    public CaskBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) return;

        CompoundTag tag = data.copyTag();
        HolderLookup.Provider registries = context.registries();
        if (registries == null) return;

        if (tag.getBoolean("Sealed")) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.cask.sealed").withStyle(ChatFormatting.AQUA));
        }

        int remaining = tag.getInt("AgeTimeTotal") - tag.getInt("AgeTime");
        if (tag.getInt("AgeTimeTotal") > 0 && remaining > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.cask.remaining", remaining / 20)
                    .withStyle(ChatFormatting.GRAY));
        }

        ItemStackHandler inventory = new ItemStackHandler(CaskBlockEntity.INVENTORY_SIZE);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
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