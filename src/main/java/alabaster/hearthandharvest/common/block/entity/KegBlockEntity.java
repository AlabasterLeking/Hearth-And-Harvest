package alabaster.hearthandharvest.common.block.entity;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import alabaster.hearthandharvest.common.block.MultiblockPart;
import net.minecraft.world.Containers;
import alabaster.hearthandharvest.common.block.entity.container.KegMenu;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import alabaster.hearthandharvest.common.block.entity.inventory.KegItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.List;

public class KegBlockEntity extends SyncedBlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_ONE = 0;
    public static final int INPUT_SLOT_TWO = 1;
    public static final int OUTPUT_SLOT_ONE = 2;
    public static final int OUTPUT_SLOT_TWO = 3;
    public static final int CONTAINER_INPUT_SLOT = 4;
    public static final int CONTAINER_OUTPUT_SLOT = 5;
    public static final int INVENTORY_SIZE = 6;

    public static final int TANK_CAPACITY = 1000;
    public static final int MULTIBLOCK_SIZE = 8;
    public static final int MODE_DRAIN = 0;
    public static final int MODE_FILL = 1;
    public static final int MODE_EMPTY = 2;
    public static final String[] MODE_NAMES = {"drain", "fill", "drain_tank"};
    public static final int PROGRESS_SCALE = 1000;

    private final ItemStackHandler inventory = createHandler();
    private final Map<Direction, IItemHandler> sidedInventory = new EnumMap<>(Direction.class);
    private final IItemHandler defaultInventory = new KegItemHandler(inventory, null);
    private final ResizableFluidTank inputTank = createTank();
    private final ResizableFluidTank outputTank = createTank();
    private final ContainerData kegData = createData();

    private MultiblockPart role = MultiblockPart.NONE;
    private BlockPos controllerPos;
    private int fermentTime;
    private int fermentTimeTotal;
    private int mode = MODE_DRAIN;
    private boolean fermenting;

    public KegBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.KEG.get(), pos, state);
        for (Direction direction : Direction.values()) {
            sidedInventory.put(direction, new KegItemHandler(inventory, direction));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().getLightEngine().checkBlock(worldPosition);
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            public void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == OUTPUT_SLOT_ONE || slot == OUTPUT_SLOT_TWO || slot == CONTAINER_OUTPUT_SLOT) return false;
                if (slot == CONTAINER_INPUT_SLOT) return FluidUtil.getFluidHandler(stack).isPresent();
                return true;
            }
        };
    }

    private ResizableFluidTank createTank() {
        return new ResizableFluidTank(TANK_CAPACITY);
    }

    private class ResizableFluidTank extends FluidTank {
        ResizableFluidTank(int capacity) {
            super(capacity);
        }

        @Override
        public void onContentsChanged() {
            inventoryChanged();
        }

        void setEffectiveCapacity(int value) {
            this.capacity = value;
        }
    }

    public MultiblockPart getRole() {
        return role;
    }

    public boolean isMember() {
        return role == MultiblockPart.MEMBER;
    }

    public int batchSize() {
        return role == MultiblockPart.CONTROLLER ? MULTIBLOCK_SIZE : 1;
    }

    @Nullable
    public KegBlockEntity controller() {
        if (role != MultiblockPart.MEMBER || level == null || controllerPos == null) return this;
        return level.getBlockEntity(controllerPos) instanceof KegBlockEntity keg ? keg : null;
    }

    public void formAsController(List<KegBlockEntity> members) {
        this.role = MultiblockPart.CONTROLLER;
        this.controllerPos = null;
        inputTank.setEffectiveCapacity(TANK_CAPACITY * MULTIBLOCK_SIZE);
        outputTank.setEffectiveCapacity(TANK_CAPACITY * MULTIBLOCK_SIZE);

        for (KegBlockEntity member : members) {
            member.moveContentsInto(this);
        }

        inventoryChanged();
        refreshCapabilities();
    }

    private void refreshCapabilities() {
        invalidateCaps();
        reviveCaps();
    }

    public void formAsMember(BlockPos controllerPos) {
        this.role = MultiblockPart.MEMBER;
        this.controllerPos = controllerPos;
        this.fermentTime = 0;
        this.fermenting = false;
        inventoryChanged();
        refreshCapabilities();
    }

    public void dissolve() {
        this.role = MultiblockPart.NONE;
        this.controllerPos = null;
        this.fermentTime = 0;
        this.fermenting = false;

        trimTank(inputTank);
        trimTank(outputTank);
        inventoryChanged();
        refreshCapabilities();
    }

    private void trimTank(ResizableFluidTank tank) {
        tank.setEffectiveCapacity(TANK_CAPACITY);
        if (tank.getFluidAmount() > TANK_CAPACITY) {
            tank.setFluid(new FluidStack(tank.getFluid(), TANK_CAPACITY));
        }
    }

    private void moveContentsInto(KegBlockEntity target) {
        for (int slot = 0; slot < INVENTORY_SIZE; ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            ItemStack remainder = target.inventory.insertItem(slot, stack.copy(), false);
            inventory.setStackInSlot(slot, ItemStack.EMPTY);
            if (!remainder.isEmpty()) dropAtKeg(remainder);
        }

        moveFluidInto(inputTank, target.inputTank);
        moveFluidInto(outputTank, target.outputTank);
        inventoryChanged();
    }

    private void moveFluidInto(ResizableFluidTank from, ResizableFluidTank to) {
        FluidStack fluid = from.getFluid().copy();
        if (fluid.isEmpty()) return;

        int accepted = to.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
        from.setFluid(accepted >= fluid.getAmount() ? FluidStack.EMPTY : new FluidStack(fluid, fluid.getAmount() - accepted));
    }

    private void dropAtKeg(ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        Containers.dropItemStack(level, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, stack);
    }

    public Object getHandler(Capability<?> cap, Direction side) {
        KegBlockEntity keg = controller();
        if (keg == null) return null;
        if (cap == ForgeCapabilities.ITEM_HANDLER) return side == null ? keg.defaultInventory : keg.sidedInventory.get(side);
        if (cap == ForgeCapabilities.FLUID_HANDLER) return side == Direction.DOWN ? keg.outputTank : keg.inputTank;
        return null;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public ContainerData getKegData() {
        return kegData;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (this.mode == mode) return;
        this.mode = mode;
        inventoryChanged();
    }

    public void cycleMode() {
        mode = (mode + 1) % MODE_NAMES.length;
        inventoryChanged();
    }

    public boolean isFermenting() {
        return fermenting;
    }

    public int getRemainingSeconds() {
        if (fermentTimeTotal <= 0 || fermentTime <= 0) return 0;
        return (fermentTimeTotal - fermentTime) / 20;
    }

    public static void fermentingTick(Level level, BlockPos pos, BlockState state, KegBlockEntity keg) {
        if (level.isClientSide || keg.isMember()) return;

        keg.handleContainerSlot();

        boolean wasFermenting = keg.fermenting;
        KegRecipe match = keg.findRecipe();

        if (match != null && keg.canOutput(match)) {
            keg.fermentTimeTotal = match.getFermentTime();
            if (++keg.fermentTime >= keg.fermentTimeTotal) {
                keg.fermentTime = 0;
                keg.craft(match);
            }
            keg.fermenting = true;
        } else {
            keg.fermentTime = 0;
            keg.fermenting = false;
        }

        if (wasFermenting != keg.fermenting) {
            keg.inventoryChanged();
        } else {
            keg.setChanged();
        }
    }

    private void handleContainerSlot() {
        ItemStack container = inventory.getStackInSlot(CONTAINER_INPUT_SLOT);
        if (container.isEmpty()) return;

        ItemStack single = container.copyWithCount(1);
        FluidActionResult simulated = transfer(single, false);
        if (!simulated.isSuccess() || !canStoreContainer(simulated.getResult())) return;

        FluidActionResult executed = transfer(single, true);
        if (!executed.isSuccess()) return;

        storeContainer(executed.getResult());
        container.shrink(1);
        inventory.setStackInSlot(CONTAINER_INPUT_SLOT, container);
    }

    private FluidActionResult transfer(ItemStack container, boolean execute) {
        return switch (mode) {
            case MODE_FILL -> FluidUtil.tryFillContainer(container, outputTank, TANK_CAPACITY, null, execute);
            case MODE_EMPTY -> FluidUtil.tryFillContainer(container, inputTank, TANK_CAPACITY, null, execute);
            default -> FluidUtil.tryEmptyContainer(container, inputTank, TANK_CAPACITY, null, execute);
        };
    }

    private boolean canStoreContainer(ItemStack stack) {
        if (stack.isEmpty()) return true;
        ItemStack stored = inventory.getStackInSlot(CONTAINER_OUTPUT_SLOT);
        if (stored.isEmpty()) return true;
        return ItemStack.isSameItemSameTags(stored, stack)
                && stored.getCount() + stack.getCount() <= stored.getMaxStackSize();
    }

    private void storeContainer(ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemStack stored = inventory.getStackInSlot(CONTAINER_OUTPUT_SLOT);
        if (stored.isEmpty()) {
            inventory.setStackInSlot(CONTAINER_OUTPUT_SLOT, stack.copy());
        } else {
            stored.grow(stack.getCount());
        }
    }

    @Nullable
    private KegRecipe findRecipe() {
        if (level == null) return null;

        List<ItemStack> inputs = List.of(
                inventory.getStackInSlot(INPUT_SLOT_ONE),
                inventory.getStackInSlot(INPUT_SLOT_TWO)
        );

        for (KegRecipe holder : level.getRecipeManager().getAllRecipesFor(HHModRecipeTypes.FERMENTING.get())) {
            KegRecipe recipe = holder;
            if (recipe.matchesFluid(inputTank.getFluid()) && recipe.matchesItems(inputs)) {
                return holder;
            }
        }
        return null;
    }

    private boolean canOutput(KegRecipe recipe) {
        FluidStack resultFluid = recipe.getResultFluid();
        if (!resultFluid.isEmpty() && outputTank.fill(resultFluid, IFluidHandler.FluidAction.SIMULATE) < resultFluid.getAmount()) {
            return false;
        }

        ItemStack resultItem = recipe.getResultItem();
        return resultItem.isEmpty() || hasRoomFor(resultItem);
    }

    private boolean hasRoomFor(ItemStack stack) {
        for (int slot : new int[]{OUTPUT_SLOT_ONE, OUTPUT_SLOT_TWO}) {
            ItemStack stored = inventory.getStackInSlot(slot);
            if (stored.isEmpty()) return true;
            if (ItemStack.isSameItemSameTags(stored, stack)
                    && stored.getCount() + stack.getCount() <= stored.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private boolean storeResult(ItemStack stack) {
        if (stack.isEmpty()) return true;

        for (int slot : new int[]{OUTPUT_SLOT_ONE, OUTPUT_SLOT_TWO}) {
            ItemStack stored = inventory.getStackInSlot(slot);
            if (stored.isEmpty()) {
                inventory.setStackInSlot(slot, stack.copy());
                return true;
            }
            if (ItemStack.isSameItemSameTags(stored, stack)
                    && stored.getCount() + stack.getCount() <= stored.getMaxStackSize()) {
                stored.grow(stack.getCount());
                return true;
            }
        }
        return false;
    }

    private void craft(KegRecipe recipe) {
        int[] inputSlots = {INPUT_SLOT_ONE, INPUT_SLOT_TWO};
        List<ItemStack> inputs = List.of(inventory.getStackInSlot(INPUT_SLOT_ONE), inventory.getStackInSlot(INPUT_SLOT_TWO));
        int[] usage = recipe.getItemUsage(inputs);
        if (usage == null) return;

        if (recipe.getInputAmount() > 0) {
            inputTank.drain(recipe.getInputAmount(), IFluidHandler.FluidAction.EXECUTE);
        }

        for (int i = 0; i < inputSlots.length; i++) {
            if (usage[i] <= 0) continue;
            ItemStack stored = inventory.getStackInSlot(inputSlots[i]).copy();
            stored.shrink(usage[i]);
            inventory.setStackInSlot(inputSlots[i], stored);
        }

        FluidStack resultFluid = recipe.getResultFluid();
        if (!resultFluid.isEmpty()) {
            outputTank.fill(resultFluid.copy(), IFluidHandler.FluidAction.EXECUTE);
        }

        storeResult(recipe.getResultItem().copy());
        inventoryChanged();
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.add(inventory.getStackInSlot(i));
        }
        return drops;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        inputTank.readFromNBT(tag.getCompound("InputTank"));
        outputTank.readFromNBT(tag.getCompound("OutputTank"));
        fermentTime = tag.getInt("FermentTime");
        fermentTimeTotal = tag.getInt("FermentTimeTotal");
        mode = Mth.clamp(tag.getInt("Mode"), 0, MODE_NAMES.length - 1);
        fermenting = tag.getBoolean("Fermenting");
        role = MultiblockPart.byName(tag.getString("MultiblockRole"));
        controllerPos = tag.contains("ControllerPos") ? BlockPos.of(tag.getLong("ControllerPos")) : null;

        int capacity = role == MultiblockPart.CONTROLLER ? TANK_CAPACITY * MULTIBLOCK_SIZE : TANK_CAPACITY;
        inputTank.setEffectiveCapacity(capacity);
        outputTank.setEffectiveCapacity(capacity);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        writeShared(tag);
        tag.putInt("FermentTime", fermentTime);
        tag.putInt("FermentTimeTotal", fermentTimeTotal);
    }

    private CompoundTag writeShared(CompoundTag tag) {
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("InputTank", inputTank.writeToNBT(new CompoundTag()));
        tag.put("OutputTank", outputTank.writeToNBT(new CompoundTag()));
        tag.putInt("Mode", mode);
        tag.putBoolean("Fermenting", fermenting);
        tag.putString("MultiblockRole", role.getSerializedName());
        if (controllerPos != null) tag.putLong("ControllerPos", controllerPos.asLong());
        return tag;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeShared(new CompoundTag());
    }

    private ContainerData createData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                int total = KegBlockEntity.this.fermentTimeTotal;
                return switch (index) {
                    case 0 -> total <= 0 ? 0 : (int) Math.min(PROGRESS_SCALE, (long) KegBlockEntity.this.fermentTime * PROGRESS_SCALE / total);
                    case 1 -> total <= 0 ? 0 : PROGRESS_SCALE;
                    case 2 -> KegBlockEntity.this.getRemainingSeconds();
                    case 3 -> KegBlockEntity.this.fermenting ? 1 : 0;
                    case 4 -> KegBlockEntity.this.mode;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> KegBlockEntity.this.fermentTime = value;
                    case 1 -> KegBlockEntity.this.fermentTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.hearthandharvest.keg");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new KegMenu(id, playerInventory, this, kegData);
    }
}