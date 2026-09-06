package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.block.MultiblockPart;
import alabaster.hearthandharvest.common.crafting.StompingBasinRecipe;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StompingBasinBlockEntity extends BlockEntity  {

    public static final int ITEM_SLOTS = 4;

    public static final int SOLO_ITEM_LIMIT = 64;
    public static final int COMBINED_ITEM_LIMIT = 256;

    public static final int SOLO_TANK_CAPACITY = 8000;
    public static final int COMBINED_TANK_CAPACITY = 32000;

    private MultiblockPart role = MultiblockPart.NONE;
    @Nullable private BlockPos controllerPos = null;

    private final Map<UUID, Long> stompCooldowns = new HashMap<>();

    private int itemSlotLimit = SOLO_ITEM_LIMIT;
    private final VariableStackHandler itemHandler = new VariableStackHandler();
    private final ResizableFluidTank fluidTank = new ResizableFluidTank(SOLO_TANK_CAPACITY);

    private class VariableStackHandler extends ItemStackHandler {
        VariableStackHandler() { super(ITEM_SLOTS); }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); syncToClient();
        }

        @Override
        public int getSlotLimit(int slot) {
            return itemSlotLimit;
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return getSlotLimit(slot);
        }

        private static final String KEY_ITEMS = "Items";
        private static final String KEY_SLOT = "Slot";
        private static final String KEY_STACK = "Stack";
        private static final String KEY_EXTRA_COUNT = "ExtraCount";

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider registries) {
            ListTag listTag = new ListTag();
            for (int i = 0; i < getSlots(); i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack.isEmpty()) continue;
                CompoundTag entry = new CompoundTag();
                entry.putByte(KEY_SLOT, (byte) i);
                ItemStack single = stack.copyWithCount(1);
                entry.put(KEY_STACK, single.save(registries));
                entry.putInt(KEY_EXTRA_COUNT, stack.getCount());
                listTag.add(entry);
            }
            CompoundTag tag = new CompoundTag();
            tag.put(KEY_ITEMS, listTag);
            tag.putInt("Size", getSlots());
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
            for (int i = 0; i < getSlots(); i++) {
                setStackInSlot(i, ItemStack.EMPTY);
            }

            ListTag listTag = tag.getList(KEY_ITEMS, Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag entry = listTag.getCompound(i);
                int slot = entry.getByte(KEY_SLOT) & 0xFF;
                if (slot >= getSlots()) continue;

                if (entry.contains(KEY_STACK)) {
                    ItemStack stack = ItemStack.parseOptional(registries, entry.getCompound(KEY_STACK));
                    if (!stack.isEmpty()) {
                        int count = entry.contains(KEY_EXTRA_COUNT)
                                ? entry.getInt(KEY_EXTRA_COUNT)
                                : stack.getCount();
                        stack.setCount(count);
                        setStackInSlot(slot, stack);
                    }
                } else {
                    setStackInSlot(slot, ItemStack.parseOptional(registries, entry));
                }
            }
        }
    }

    private class ResizableFluidTank extends FluidTank {
        ResizableFluidTank(int capacity) {
            super(capacity);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            syncToClient();
            if (level != null && !level.isClientSide) {
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
        }

        void setEffectiveCapacity(int cap) {
            this.capacity = cap;
        }
    }

    public StompingBasinBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.STOMPING_BASIN.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StompingBasinBlockEntity be) { }

    public ItemStack insertItem(ItemStack toInsert) {
        if (toInsert.isEmpty()) return ItemStack.EMPTY;

        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            if (controller != null) return controller.insertItem(toInsert);
            return toInsert;
        }

        for (int i = 0; i < itemHandler.getSlots() && !toInsert.isEmpty(); i++) {
            ItemStack inSlot = itemHandler.getStackInSlot(i);
            if (!inSlot.isEmpty() && ItemStack.isSameItemSameComponents(inSlot, toInsert)) {
                toInsert = itemHandler.insertItem(i, toInsert, false);
            }
        }
        for (int i = 0; i < itemHandler.getSlots() && !toInsert.isEmpty(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                toInsert = itemHandler.insertItem(i, toInsert, false);
            }
        }
        return toInsert;
    }

    public void extractOne(Player player) {
        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            if (controller != null) controller.extractOne(player);
            return;
        }
        for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
            ItemStack inSlot = itemHandler.getStackInSlot(i);
            if (!inSlot.isEmpty()) {
                giveOrDrop(player, itemHandler.extractItem(i, 1, false));
                return;
            }
        }
    }

    public void extractAll(Player player) {
        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            if (controller != null) controller.extractAll(player);
            return;
        }
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack inSlot = itemHandler.getStackInSlot(i);
            if (!inSlot.isEmpty()) {
                giveOrDrop(player, itemHandler.extractItem(i, inSlot.getCount(), false));
            }
        }
    }

    private void giveOrDrop(Player player, ItemStack stack) {
        if (stack.isEmpty()) return;
        if (!player.addItem(stack)) dropAtBasin(stack);
    }

    public void tryProcess(LivingEntity entity) {
        if (level == null || level.isClientSide) return;

        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            if (controller != null) controller.tryProcess(entity);
            return;
        }

        long now = level.getGameTime();
        stompCooldowns.values().removeIf(lastTime -> now - lastTime >= 5);
        Long last = stompCooldowns.get(entity.getUUID());
        if (last != null && now - last < 5) return;
        stompCooldowns.put(entity.getUUID(), now);

        RecipeWrapper wrapper = new RecipeWrapper(itemHandler);
        if (wrapper.isEmpty()) return;

        Optional<StompingBasinRecipe> match = level.getRecipeManager()
                .getAllRecipesFor(HHModRecipeTypes.STOMPING.get())
                .stream()
                .map(holder -> holder.value())
                .filter(r -> r.matches(wrapper, level))
                .findFirst();

        if (match.isEmpty()) return;

        StompingBasinRecipe recipe = match.get();

        FluidStack resultFluid = recipe.getResultFluid();
        if (!resultFluid.isEmpty()) {
            int accepted = fluidTank.fill(resultFluid.copy(), IFluidHandler.FluidAction.SIMULATE);
            if (accepted < resultFluid.getAmount()) return;
        }

        double soundX = worldPosition.getX() + (role == MultiblockPart.CONTROLLER ? 1.0 : 0.5);
        double soundY = worldPosition.getY() + 0.5;
        double soundZ = worldPosition.getZ() + (role == MultiblockPart.CONTROLLER ? 1.0 : 0.5);
        level.playSound(null, soundX, soundY, soundZ, HHModSounds.STOMPING_BASIN_STOMP.get(), SoundSource.BLOCKS, 0.6f, 0.7f);

        for (Ingredient ingredient : recipe.getIngredients()) {
            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                ItemStack slotStack = itemHandler.getStackInSlot(slot);
                if (!slotStack.isEmpty() && ingredient.test(slotStack)) {
                    itemHandler.extractItem(slot, 1, false);
                    break;
                }
            }
        }

        if (!resultFluid.isEmpty()) {
            fluidTank.fill(resultFluid.copy(), IFluidHandler.FluidAction.EXECUTE);
        }

        ItemStack resultItem = recipe.getResultItem();
        if (!resultItem.isEmpty()) dropAtBasin(resultItem.copy());

        setChanged();
        syncToClient();
    }

    private void dropAtBasin(ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.25;
        double z = worldPosition.getZ() + 0.5;
        ItemEntity entity = new ItemEntity(level, x, y, z, stack, 0, 0, 0);
        entity.setPickUpDelay(10);
        level.addFreshEntity(entity);
    }

    public void formAsController(StompingBasinBlockEntity ne, StompingBasinBlockEntity sw, StompingBasinBlockEntity se) {
        this.role = MultiblockPart.CONTROLLER;
        this.itemSlotLimit = COMBINED_ITEM_LIMIT;
        fluidTank.setEffectiveCapacity(COMBINED_TANK_CAPACITY);

        for (StompingBasinBlockEntity member : new StompingBasinBlockEntity[]{ne, sw, se}) {
            for (int s = 0; s < member.itemHandler.getSlots(); s++) {
                ItemStack memberItems = member.itemHandler.getStackInSlot(s);
                if (memberItems.isEmpty()) continue;
                ItemStack remainder = insertItem(memberItems.copy());
                if (!remainder.isEmpty() && level != null) dropAtBasin(remainder.copy());
                member.itemHandler.setStackInSlot(s, ItemStack.EMPTY);
            }

            FluidStack memberFluid = member.fluidTank.getFluid().copy();
            if (!memberFluid.isEmpty()) {
                int accepted = fluidTank.fill(memberFluid, IFluidHandler.FluidAction.EXECUTE);
                if (accepted >= memberFluid.getAmount()) {
                    member.fluidTank.setFluid(FluidStack.EMPTY);
                } else {
                    member.fluidTank.setFluid(memberFluid.copyWithAmount(memberFluid.getAmount() - accepted));
                }
            }

            member.setChanged();
            member.syncToClient();
        }

        setChanged();
        syncToClient();
    }

    public void formAsMember(BlockPos controllerPos) {
        this.role = MultiblockPart.MEMBER;
        this.controllerPos = controllerPos;
        setChanged();
        syncToClient();
    }

    public void dissolve(List<StompingBasinBlockEntity> survivors) {
        this.role = MultiblockPart.NONE;
        this.controllerPos = null;
        this.itemSlotLimit = SOLO_ITEM_LIMIT;

        for (int s = 0; s < itemHandler.getSlots(); s++) {
            ItemStack stack = itemHandler.getStackInSlot(s);
            if (!stack.isEmpty() && stack.getCount() > SOLO_ITEM_LIMIT) {
                ItemStack overflow = stack.copyWithCount(stack.getCount() - SOLO_ITEM_LIMIT);
                itemHandler.setStackInSlot(s, stack.copyWithCount(SOLO_ITEM_LIMIT));
                if (level != null) dropAtBasin(overflow);
            }
        }

        fluidTank.setEffectiveCapacity(SOLO_TANK_CAPACITY);
        int overflow = fluidTank.getFluidAmount() - SOLO_TANK_CAPACITY;
        if (overflow > 0) {
            FluidStack held = fluidTank.getFluid().copy();
            FluidStack moving = held.copyWithAmount(overflow);
            fluidTank.setFluid(held.copyWithAmount(SOLO_TANK_CAPACITY));

            for (StompingBasinBlockEntity survivor : survivors) {
                if (moving.isEmpty()) break;
                if (survivor == this) continue;
                survivor.fluidTank.setEffectiveCapacity(SOLO_TANK_CAPACITY);
                int accepted = survivor.fluidTank.fill(moving.copy(), IFluidHandler.FluidAction.EXECUTE);
                if (accepted > 0) {
                    moving.shrink(accepted);
                    survivor.setChanged();
                    survivor.syncToClient();
                }
            }

        }

        setChanged();
        syncToClient();
    }


    public void dissolveAsMember() {
        this.role = MultiblockPart.NONE;
        this.controllerPos = null;
        setChanged();
        syncToClient();
    }

    public void dropContents() {
        if (level == null) return;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack s = itemHandler.getStackInSlot(i);
            if (!s.isEmpty()) {
                Containers.dropItemStack(level,
                        worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), s);
            }
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (pkt.getTag() != null) loadAdditional(pkt.getTag(), lookupProvider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.put("Tank", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putString("MultiblockRole", role.getSerializedName());
        tag.putInt("ItemSlotLimit", itemSlotLimit);
        tag.putInt("FluidCapacity", fluidTank.getCapacity());
        if (controllerPos != null) {
            tag.put("MasterPos", NbtUtils.writeBlockPos(controllerPos));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        if (tag.contains("Tank")) fluidTank.readFromNBT(registries, tag.getCompound("Tank"));
        if (tag.contains("MultiblockRole")) role = MultiblockPart.byName(tag.getString("MultiblockRole"));
        if (tag.contains("ItemSlotLimit")) itemSlotLimit = tag.getInt("ItemSlotLimit");
        if (tag.contains("FluidCapacity")) fluidTank.setEffectiveCapacity(tag.getInt("FluidCapacity"));
        if (tag.contains("MasterPos")) controllerPos = NbtUtils.readBlockPos(tag, "MasterPos").orElse(null);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public FluidTank getFluidTank() {
        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            if (controller != null) return controller.fluidTank;
        }
        return fluidTank;
    }

    @Nullable
    public FluidTank getFluidHandlerForCapability() {
        if (role == MultiblockPart.MEMBER) {
            StompingBasinBlockEntity controller = getControllerBE();
            return controller != null ? controller.fluidTank : null;
        }
        return fluidTank;
    }

    public MultiblockPart getMultiblockRole() {
        return role;
    }

    @Nullable
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Nullable
    public StompingBasinBlockEntity getControllerBE() {
        if (controllerPos == null || level == null) return null;
        BlockEntity be = level.getBlockEntity(controllerPos);
        return be instanceof StompingBasinBlockEntity sbe ? sbe : null;
    }
}