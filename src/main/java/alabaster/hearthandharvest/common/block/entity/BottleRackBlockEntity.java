package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.BlockPos;
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

public class BottleRackBlockEntity extends HHSyncedBlockEntity implements Clearable, Container {

    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    public BottleRackBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.BOTTLE_RACK.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
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

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
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