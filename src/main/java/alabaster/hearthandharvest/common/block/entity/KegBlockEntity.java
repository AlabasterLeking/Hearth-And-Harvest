package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.container.KegMenu;
import alabaster.hearthandharvest.common.crafting.FermentingRecipe;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class KegBlockEntity extends SyncedBlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_ONE = 0;
    public static final int INPUT_SLOT_TWO = 1;
    public static final int OUTPUT_SLOT_ONE = 2;
    public static final int OUTPUT_SLOT_TWO = 3;
    public static final int CONTAINER_INPUT_SLOT = 4;
    public static final int CONTAINER_OUTPUT_SLOT = 5;
    public static final int INVENTORY_SIZE = 6;

    public static final int TANK_CAPACITY = 1000;
    public static final int PROGRESS_SCALE = 1000;

    private final ItemStackHandler inventory = createHandler();
    private final FluidTank inputTank = createTank();
    private final FluidTank outputTank = createTank();
    private final ContainerData kegData = createData();

    private int fermentTime;
    private int fermentTimeTotal;
    private boolean fillMode;
    private boolean fermenting;

    public KegBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.KEG.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HHModBlockEntities.KEG.get(), (be, side) -> be.inventory);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HHModBlockEntities.KEG.get(),
                (be, side) -> side == Direction.DOWN ? be.outputTank : be.inputTank);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
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

    private FluidTank createTank() {
        return new FluidTank(TANK_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                inventoryChanged();
            }
        };
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

    public boolean isFillMode() {
        return fillMode;
    }

    public void toggleFillMode() {
        fillMode = !fillMode;
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
        if (level.isClientSide) return;

        keg.handleContainerSlot();

        boolean wasFermenting = keg.fermenting;
        RecipeHolder<FermentingRecipe> match = keg.findRecipe();

        if (match != null && keg.canOutput(match.value())) {
            keg.fermentTimeTotal = match.value().getFermentTime();
            if (++keg.fermentTime >= keg.fermentTimeTotal) {
                keg.fermentTime = 0;
                keg.craft(match.value());
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
        FluidActionResult simulated = fillMode
                ? FluidUtil.tryFillContainer(single, outputTank, TANK_CAPACITY, null, false)
                : FluidUtil.tryEmptyContainer(single, inputTank, TANK_CAPACITY, null, false);
        if (!simulated.isSuccess() || !canStoreContainer(simulated.getResult())) return;

        FluidActionResult executed = fillMode
                ? FluidUtil.tryFillContainer(single, outputTank, TANK_CAPACITY, null, true)
                : FluidUtil.tryEmptyContainer(single, inputTank, TANK_CAPACITY, null, true);
        if (!executed.isSuccess()) return;

        storeContainer(executed.getResult());
        container.shrink(1);
        inventory.setStackInSlot(CONTAINER_INPUT_SLOT, container);
    }

    private boolean canStoreContainer(ItemStack stack) {
        if (stack.isEmpty()) return true;
        ItemStack stored = inventory.getStackInSlot(CONTAINER_OUTPUT_SLOT);
        if (stored.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(stored, stack)
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
    private RecipeHolder<FermentingRecipe> findRecipe() {
        if (level == null) return null;

        List<ItemStack> inputs = List.of(
                inventory.getStackInSlot(INPUT_SLOT_ONE),
                inventory.getStackInSlot(INPUT_SLOT_TWO)
        );

        for (RecipeHolder<FermentingRecipe> holder : level.getRecipeManager().getAllRecipesFor(HHModRecipeTypes.FERMENTING.get())) {
            FermentingRecipe recipe = holder.value();
            if (recipe.matchesFluid(inputTank.getFluid()) && recipe.matchesItems(inputs)) {
                return holder;
            }
        }
        return null;
    }

    private boolean canOutput(FermentingRecipe recipe) {
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
            if (ItemStack.isSameItemSameComponents(stored, stack)
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
            if (ItemStack.isSameItemSameComponents(stored, stack)
                    && stored.getCount() + stack.getCount() <= stored.getMaxStackSize()) {
                stored.grow(stack.getCount());
                return true;
            }
        }
        return false;
    }

    private void craft(FermentingRecipe recipe) {
        FluidStack inputFluid = recipe.getInputFluid();
        if (!inputFluid.isEmpty()) {
            inputTank.drain(inputFluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
        }

        for (int slot : new int[]{INPUT_SLOT_ONE, INPUT_SLOT_TWO}) {
            ItemStack stored = inventory.getStackInSlot(slot);
            if (!stored.isEmpty()) {
                stored.shrink(1);
            }
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
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        inputTank.readFromNBT(registries, tag.getCompound("InputTank"));
        outputTank.readFromNBT(registries, tag.getCompound("OutputTank"));
        fermentTime = tag.getInt("FermentTime");
        fermentTimeTotal = tag.getInt("FermentTimeTotal");
        fillMode = tag.getBoolean("FillMode");
        fermenting = tag.getBoolean("Fermenting");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeShared(tag, registries);
        tag.putInt("FermentTime", fermentTime);
        tag.putInt("FermentTimeTotal", fermentTimeTotal);
    }

    private CompoundTag writeShared(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.put("InputTank", inputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("OutputTank", outputTank.writeToNBT(registries, new CompoundTag()));
        tag.putBoolean("FillMode", fillMode);
        tag.putBoolean("Fermenting", fermenting);
        return tag;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeShared(new CompoundTag(), registries);
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
                return 4;
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