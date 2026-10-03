package alabaster.hearthandharvest.common.block.entity.inventory;

import alabaster.hearthandharvest.common.block.entity.SprinklerBlockEntity;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class SprinklerItemHandler implements IItemHandler {
    private final SprinklerBlockEntity sprinkler;

    public SprinklerItemHandler(SprinklerBlockEntity sprinkler) {
        this.sprinkler = sprinkler;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return sprinkler.getFertilizerSlot().get(0);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!isItemValid(slot, stack)) return stack;

        ItemStack stored = sprinkler.getFertilizerSlot().get(0);
        int space = (stored.isEmpty() ? stack.getMaxStackSize() : stored.getMaxStackSize() - stored.getCount());
        if (space <= 0) return stack;
        if (!stored.isEmpty() && !ItemStack.isSameItemSameComponents(stored, stack)) return stack;

        int accepted = Math.min(space, stack.getCount());
        if (!simulate) {
            if (stored.isEmpty()) {
                sprinkler.getFertilizerSlot().set(0, stack.copyWithCount(accepted));
            } else {
                stored.grow(accepted);
            }
            sprinkler.setChanged();
        }
        return accepted >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - accepted);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stored = sprinkler.getFertilizerSlot().get(0);
        if (stored.isEmpty() || amount <= 0) return ItemStack.EMPTY;

        int taken = Math.min(amount, stored.getCount());
        ItemStack result = stored.copyWithCount(taken);
        if (!simulate) {
            stored.shrink(taken);
            sprinkler.setChanged();
        }
        return result;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.is(HHModTags.BONEMEAL_SUBSTITUTES);
    }
}