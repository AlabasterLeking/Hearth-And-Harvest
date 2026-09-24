package alabaster.hearthandharvest.common.block.entity.container;

import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.datafixers.util.Pair;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import alabaster.hearthandharvest.common.crafting.FermentingRecipe;

import java.util.List;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class KegMenu extends RecipeBookMenu<RecipeWrapper, FermentingRecipe> {
    public static final int MODE_BUTTON_ID = 0;
    public static final ResourceLocation BOTTLE_SLOT_ICON = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "gui/bottle_slot");

    public final KegBlockEntity blockEntity;
    private final ContainerData kegData;
    private final ContainerLevelAccess access;

    public KegMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(windowId, playerInventory, getBlockEntity(playerInventory, buf), new SimpleContainerData(4));
    }

    public KegMenu(int windowId, Inventory playerInventory, KegBlockEntity blockEntity, ContainerData kegData) {
        super(HHModMenuTypes.KEG_MENU.get(), windowId);
        this.blockEntity = blockEntity;
        this.kegData = kegData;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), KegBlockEntity.INPUT_SLOT_ONE, 32, 32));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), KegBlockEntity.INPUT_SLOT_TWO, 32, 59));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), KegBlockEntity.CONTAINER_INPUT_SLOT, 69, 25) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(TextureAtlas.LOCATION_BLOCKS, BOTTLE_SLOT_ICON);
            }
        });
        this.addSlot(new KegResultSlot(blockEntity, KegBlockEntity.CONTAINER_OUTPUT_SLOT, 91, 25));
        this.addSlot(new KegResultSlot(blockEntity, KegBlockEntity.OUTPUT_SLOT_ONE, 128, 32));
        this.addSlot(new KegResultSlot(blockEntity, KegBlockEntity.OUTPUT_SLOT_TWO, 128, 59));

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + row * 9 + column, 8 + column * 18, 102 + row * 18));
            }
        }
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 160));
        }

        this.addDataSlots(kegData);
    }

    private static KegBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        if (be instanceof KegBlockEntity keg) return keg;
        throw new IllegalStateException("Keg block entity missing at " + pos);
    }

    public int getProgressScaled(int pixels) {
        int progress = kegData.get(0);
        int total = kegData.get(1);
        return total <= 0 ? 0 : Math.min(pixels, progress * pixels / total);
    }

    public int getRemainingSeconds() {
        return kegData.get(2);
    }

    public boolean isFermenting() {
        return kegData.get(3) != 0;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != MODE_BUTTON_ID) return false;
        blockEntity.toggleFillMode();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < KegBlockEntity.INVENTORY_SIZE) {
                if (!this.moveItemStackTo(stack, KegBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        for (int slot = 0; slot < KegBlockEntity.INVENTORY_SIZE; slot++) {
            helper.accountSimpleStack(blockEntity.getInventory().getStackInSlot(slot));
        }
    }

    @Override
    public void clearCraftingContent() {
        blockEntity.getInventory().setStackInSlot(KegBlockEntity.INPUT_SLOT_ONE, ItemStack.EMPTY);
        blockEntity.getInventory().setStackInSlot(KegBlockEntity.INPUT_SLOT_TWO, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(RecipeHolder<FermentingRecipe> recipe) {
        return recipe.value().matchesItems(List.of(
                blockEntity.getInventory().getStackInSlot(KegBlockEntity.INPUT_SLOT_ONE),
                blockEntity.getInventory().getStackInSlot(KegBlockEntity.INPUT_SLOT_TWO)));
    }

    @Override
    public int getResultSlotIndex() {
        return 4;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return KegBlockEntity.INVENTORY_SIZE;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("HEARTHANDHARVEST_FERMENTING");
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot < getGridWidth() * getGridHeight();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, HHModBlocks.KEG.get());
    }

    private static class KegResultSlot extends SlotItemHandler {
        KegResultSlot(KegBlockEntity blockEntity, int slot, int x, int y) {
            super(blockEntity.getInventory(), slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}