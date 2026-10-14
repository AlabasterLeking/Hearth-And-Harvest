package alabaster.hearthandharvest.common.item;

import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class KegBlockItem extends BlockItem {
    public KegBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        if (tag == null) return;

        appendTank(tooltip, tag, "InputTank", "tooltip.hearthandharvest.keg.input");
        appendTank(tooltip, tag, "OutputTank", "tooltip.hearthandharvest.keg.output");

        int remaining = tag.getInt("FermentTimeTotal") - tag.getInt("FermentTime");
        if (tag.getInt("FermentTimeTotal") > 0 && remaining > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.keg.remaining", remaining / 20)
                    .withStyle(ChatFormatting.GRAY));
        }

        ItemStackHandler inventory = new ItemStackHandler(KegBlockEntity.INVENTORY_SIZE);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        CaskBlockItem.appendContents(tooltip, inventory);
    }

    private static void appendTank(List<Component> tooltip, CompoundTag tag, String key, String translation) {
        if (!tag.contains(key)) return;

        FluidTank tank = new FluidTank(KegBlockEntity.TANK_CAPACITY * KegBlockEntity.MULTIBLOCK_SIZE);
        tank.readFromNBT(tag.getCompound(key));
        if (tank.isEmpty()) return;

        tooltip.add(Component.translatable(translation, tank.getFluid().getDisplayName(), tank.getFluidAmount())
                .withStyle(ChatFormatting.AQUA));
    }
}