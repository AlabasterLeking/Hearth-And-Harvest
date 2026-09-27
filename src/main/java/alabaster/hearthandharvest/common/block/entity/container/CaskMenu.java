package alabaster.hearthandharvest.common.block.entity.container;

import alabaster.hearthandharvest.common.block.CaskBlock;
import alabaster.hearthandharvest.common.block.entity.CaskBlockEntity;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Objects;

public class CaskMenu extends RecipeBookMenu<RecipeWrapper, CaskRecipe> {

    public final CaskBlockEntity blockEntity;
    public final ItemStackHandler inventory;
    private final ContainerData caskData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;

    public CaskMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(6));
    }

    public CaskMenu(final int windowId, final Inventory playerInventory, final CaskBlockEntity blockEntity, ContainerData caskDataIn) {
        super(HHModMenuTypes.CASK_MENU.get(), windowId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.caskData = caskDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Ingredient Slots - 2 Rows x 2 Columns
        int startX = 8;
        int startY = 18;
        int inputStartX = 21;
        int inputStartY = 26;
        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                this.addSlot(new SlotItemHandler(inventory, (row * 2) + column,
                        inputStartX + (column * borderSlotSize),
                        inputStartY + (row * borderSlotSize)) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !CaskMenu.this.blockEntity.isSealed() && super.mayPlace(stack);
                    }

                    @Override
                    public boolean mayPickup(Player player) {
                        return !CaskMenu.this.blockEntity.isSealed() && super.mayPickup(player);
                    }
                });
            }
        }

        // Output
        int outputStartX = 119;
        int outputStartY = 26;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                this.addSlot(new CaskResultSlot(playerInventory.player, blockEntity, inventory,
                        CaskBlockEntity.FIRST_OUTPUT_SLOT + (row * 2) + column,
                        outputStartX + (column * borderSlotSize),
                        outputStartY + (row * borderSlotSize)));
            }
        }

        // Main Player Inventory
        int startPlayerInvY = startY * 4 + 12;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
                        startPlayerInvY + (row * borderSlotSize)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
        }

        this.addDataSlots(caskDataIn);
    }

    private static CaskBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof CaskBlockEntity) {
            return (CaskBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, HHModBlocks.CASK.get());
    }

    public static final int SEAL_BUTTON_ID = 0;

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != SEAL_BUTTON_ID || blockEntity.getLevel() == null) return false;
        if (!blockEntity.getLevel().isClientSide()) {
            CaskBlock.toggleSealed(blockEntity.getLevel(), blockEntity.getBlockPos());
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int indexInputs = CaskBlockEntity.INPUT_SLOTS;
        int indexOutput = CaskBlockEntity.FIRST_OUTPUT_SLOT;
        int startPlayerInv = CaskBlockEntity.INVENTORY_SIZE;
        int endPlayerInv = startPlayerInv + 36;
        ItemStack slotStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            slotStackCopy = slotStack.copy();
            if (index >= indexOutput && index < startPlayerInv) {
                if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= startPlayerInv) {
                if (!this.moveItemStackTo(slotStack, 0, indexInputs, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == slotStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }
        return slotStackCopy;
    }

    public float getProgression() {
        return this.caskData.get(2);
    }

    public int getRemainingSeconds() {
        return this.caskData.get(3);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            helper.accountSimpleStack(inventory.getStackInSlot(i));
        }
    }

    @Override
    public void clearCraftingContent() {
        for (int i = 0; i < 4; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(RecipeHolder<CaskRecipe> recipe) {
        return recipe.value().matches(new RecipeWrapper(inventory), level);
    }

    @Override
    public int getResultSlotIndex() {
        return 4;
    }

    @Override
    public int getGridWidth() {
        return 2;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("HEARTHANDHARVEST_AGING");
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot < (getGridWidth() * getGridHeight());
    }
}