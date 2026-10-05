package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class KegBlockItem extends BlockItem {
    public KegBlockItem(Block block, Properties properties) {
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

        appendTank(tooltip, registries, tag, "InputTank", "tooltip.hearthandharvest.keg.input");
        appendTank(tooltip, registries, tag, "OutputTank", "tooltip.hearthandharvest.keg.output");

        int remaining = tag.getInt("FermentTimeTotal") - tag.getInt("FermentTime");
        if (tag.getInt("FermentTimeTotal") > 0 && remaining > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.keg.remaining", remaining / 20)
                    .withStyle(ChatFormatting.GRAY));
        }

        ItemStackHandler inventory = new ItemStackHandler(KegBlockEntity.INVENTORY_SIZE);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        CaskBlockItem.appendContents(tooltip, inventory);
    }

    private static void appendTank(List<Component> tooltip, HolderLookup.Provider registries, CompoundTag tag, String key, String translation) {
        if (!tag.contains(key)) return;

        FluidTank tank = new FluidTank(KegBlockEntity.TANK_CAPACITY * KegBlockEntity.MULTIBLOCK_SIZE);
        tank.readFromNBT(registries, tag.getCompound(key));
        if (tank.isEmpty()) return;

        tooltip.add(Component.translatable(translation, tank.getFluid().getHoverName(), tank.getFluidAmount())
                .withStyle(ChatFormatting.AQUA));
    }
}