package alabaster.hearthandharvest.common.block.entity.inventory;

import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KegItemHandler implements IItemHandler {
    private final IItemHandler inventory;
    private final Direction side;

    public KegItemHandler(IItemHandler inventory, @Nullable Direction side) {
        this.inventory = inventory;
        this.side = side;
    }

    private static boolean isOutput(int slot) {
        return slot == KegBlockEntity.OUTPUT_SLOT_ONE
                || slot == KegBlockEntity.OUTPUT_SLOT_TWO
                || slot == KegBlockEntity.CONTAINER_OUTPUT_SLOT;
    }

    private boolean canInsert(int slot) {
        if (isOutput(slot)) return false;
        if (side == null) return true;

        return switch (side) {
            case UP -> slot == KegBlockEntity.CONTAINER_INPUT_SLOT;
            case DOWN -> false;
            default -> slot == KegBlockEntity.INPUT_SLOT_ONE || slot == KegBlockEntity.INPUT_SLOT_TWO;
        };
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return canInsert(slot) ? inventory.insertItem(slot, stack, simulate) : stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return isOutput(slot) ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return canInsert(slot) && inventory.isItemValid(slot, stack);
    }
}