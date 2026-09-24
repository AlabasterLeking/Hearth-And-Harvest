package alabaster.hearthandharvest.common.block.entity.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class CaskItemHandler implements IItemHandler {
    private static final int SLOT_OUTPUT = 4;
    private static final int SLOTS_INPUT = 3;
    private final IItemHandler itemHandler;
    private final Direction side;
    private final BooleanSupplier sealed;

    public CaskItemHandler(IItemHandler itemHandler, @Nullable Direction side, BooleanSupplier sealed) {
        this.itemHandler = itemHandler;
        this.side = side;
        this.sealed = sealed;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!canInsert(slot)) {
            return stack;
        }
        return itemHandler.insertItem(slot, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            return slot < SLOTS_INPUT && !sealed.getAsBoolean() ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot == SLOT_OUTPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return canInsert(slot);
    }

    private boolean canInsert(int slot) {
        if (sealed.getAsBoolean()) return false;
        if (side == null) return slot < SLOT_OUTPUT;

        return switch (side) {
            case UP -> slot < SLOT_OUTPUT;
            case NORTH -> slot == 0;
            case EAST -> slot == 1;
            case SOUTH -> slot == 2;
            case WEST -> slot == 3;
            default -> false;
        };
    }
}