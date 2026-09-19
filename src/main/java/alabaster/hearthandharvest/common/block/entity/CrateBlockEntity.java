package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.block.CrateBlock;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class CrateBlockEntity extends HHSyncedBlockEntity implements Clearable, Container {

    public static final int SLOTS_PER_HALF = 9;
    public static final int TOTAL_SLOTS = SLOTS_PER_HALF * 2;

    private final NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.CRATE.get(), pos, state);
    }

    @Nullable
    public IItemHandler getItemHandler(@Nullable Direction side) {
        return side == Direction.DOWN ? extractHandler : insertHandler;
    }

    private int activeSlots() {
        BlockState state = getBlockState();
        if (state.hasProperty(CrateBlock.TYPE)
                && state.getValue(CrateBlock.TYPE) == SlabType.DOUBLE) {
            return TOTAL_SLOTS;
        }
        return SLOTS_PER_HALF;
    }

    private final IItemHandler insertHandler = new IItemHandler() {
        @Override public int getSlots() { return activeSlots(); }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot < getSlots() ? items.get(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot >= getSlots() || !isItemValid(slot, stack)) return stack;
            if (!items.get(slot).isEmpty()) return stack;

            if (!simulate) {
                items.set(slot, stack.copyWithCount(1));
                setChanged();
            }
            if (stack.getCount() == 1) return ItemStack.EMPTY;
            ItemStack remainder = stack.copy();
            remainder.shrink(1);
            return remainder;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override public int getSlotLimit(int slot) { return 1; }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(HHModTags.BOTTLES) || stack.is(HHModTags.CRATEABLE_ITEMS);
        }
    };

    private final IItemHandler extractHandler = new IItemHandler() {
        @Override public int getSlots() { return SLOTS_PER_HALF; }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot < SLOTS_PER_HALF ? items.get(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= SLOTS_PER_HALF) return ItemStack.EMPTY;
            ItemStack current = items.get(slot);
            if (current.isEmpty()) return ItemStack.EMPTY;

            int taken = Math.min(amount, current.getCount());
            ItemStack result = current.copyWithCount(taken);
            if (!simulate) {
                items.set(slot, current.getCount() > taken
                        ? current.copyWithCount(current.getCount() - taken)
                        : ItemStack.EMPTY);
                setChanged();
            }
            return result;
        }

        @Override public int getSlotLimit(int slot) { return 1; }
        @Override public boolean isItemValid(int slot, ItemStack s) { return false; }
    };

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void loadItemsFromTag(CompoundTag tag, HolderLookup.Provider lookup) {
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, lookup);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index < activeSlots()
                && items.get(index).isEmpty()
                && (stack.is(HHModTags.BOTTLES) || stack.is(HHModTags.CRATEABLE_ITEMS));
    }

    @Override public int getContainerSize() {
        return activeSlots();
    }

    @Override public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        boolean wasEmpty = items.get(index).isEmpty();
        boolean willBeEmpty = stack.isEmpty();
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
        if (level != null && !level.isClientSide) {
            if (wasEmpty && !willBeEmpty) {
                level.playSound(null, worldPosition,
                        HHModSounds.BOTTLE_INSERT.get(),
                        SoundSource.BLOCKS, 0.6f,
                        0.9f + level.random.nextFloat() * 0.2f);
            } else if (!wasEmpty && willBeEmpty) {
                level.playSound(null, worldPosition,
                        HHModSounds.BOTTLE_REMOVE.get(),
                        SoundSource.BLOCKS, 0.6f,
                        0.9f + level.random.nextFloat() * 0.2f);
            }
        }
    }

    @Override public void clearContent() {
        items.clear();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        ContainerHelper.saveAllItems(tag, this.items, lookup);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, lookup);
    }

}