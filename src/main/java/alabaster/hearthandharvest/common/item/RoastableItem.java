package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class RoastableItem extends Item {
    @Nullable
    private final Supplier<Item> cookedItem;
    private final int cookTimeToTransform;
    private final Component tooltip;

    public RoastableItem(Properties properties, @Nullable Supplier<Item> cookedItem, int cookTimeToTransform, Component tooltip) {
        super(properties.component(HHModDataComponents.COOK_TIME.get(), 0));
        this.cookedItem = cookedItem;
        this.cookTimeToTransform = cookTimeToTransform;
        this.tooltip = tooltip;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (cookedItem == null) return;
        if (level.isClientSide || !(entity instanceof Player player)) return;
        if (player.getMainHandItem() != stack && player.getOffhandItem() != stack) return;

        if (level.getGameTime() % 20 != 0) return;
        if (!isPlayerNearHeatSource(player, level)) return;

        int cookTime = stack.get(HHModDataComponents.COOK_TIME.get());
        int newCookTime = cookTime + 1;

        if (newCookTime >= cookTimeToTransform) {
            ItemStack cooked = cookedItem.get().getDefaultInstance();
            if (stack.getCount() > 1) {
                stack.shrink(1);
                stack.set(HHModDataComponents.COOK_TIME.get(), 0);
                giveOrDrop(player, cooked);
            } else {
                replaceItemInHand(player, stack, cooked);
            }
        } else {
            stack.update(HHModDataComponents.COOK_TIME.get(), 0, oldValue -> newCookTime);
        }
    }

    private void giveOrDrop(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private void replaceItemInHand(Player player, ItemStack oldStack, ItemStack newStack) {
        if (player.getMainHandItem() == oldStack) {
            player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
        } else if (player.getOffhandItem() == oldStack) {
            player.setItemInHand(InteractionHand.OFF_HAND, newStack);
        }
    }

    private static boolean isPlayerNearHeatSource(Player player, Level level) {
        if (player.isOnFire()) return true;
        BlockPos pos = player.blockPosition();
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(nearbyPos).is(Blocks.CAMPFIRE)
                    || level.getBlockState(nearbyPos).is(Blocks.FIRE)
                    || level.getBlockState(nearbyPos).is(Blocks.SOUL_CAMPFIRE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipList, TooltipFlag flag) {
        if (tooltip != null) {
            tooltipList.add(tooltip);
        }
        super.appendHoverText(stack, context, tooltipList, flag);
    }
}