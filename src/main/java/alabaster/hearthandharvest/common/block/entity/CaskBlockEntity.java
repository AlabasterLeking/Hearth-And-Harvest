package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.CaskBlock;
import alabaster.hearthandharvest.common.block.entity.container.CaskMenu;
import alabaster.hearthandharvest.common.block.entity.inventory.CaskItemHandler;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CaskBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, RecipeCraftingHolder, Clearable
{
    public static final int MEAL_DISPLAY_SLOT = 4;
    public static final int OUTPUT_SLOT = 4;
    public static final int INVENTORY_SIZE = 5;

    private final ItemStackHandler inventory;
    private final IItemHandler inputHandler;
    private final IItemHandler outputHandler;
    private final Map<Direction, IItemHandler> sideHandlers = new EnumMap<>(Direction.class);

    private int ageTime;
    private int ageTimeTotal;
    private Component customName;

    protected final ContainerData cookingPotData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private final RecipeManager.CachedCheck<RecipeWrapper, CaskRecipe> quickCheck;

    public CaskBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.CASK.get(), pos, state);
        this.inventory = createHandler();
        this.inputHandler = new CaskItemHandler(inventory, Direction.UP);
        this.outputHandler = new CaskItemHandler(inventory, Direction.DOWN);
        for (Direction d : Direction.Plane.HORIZONTAL) {
            this.sideHandlers.put(d, new CaskItemHandler(inventory, d));
        }
        this.cookingPotData = createIntArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(HHModRecipeTypes.AGING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                HHModBlockEntities.CASK.get(),
                (be, context) -> {
                    if (context == null || context == Direction.UP) return be.inputHandler;
                    if (context == Direction.DOWN) return be.outputHandler;
                    return be.sideHandlers.get(context);
                }
        );
    }


    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        ageTime = compound.getInt("AgeTime");
        ageTimeTotal = compound.getInt("AgeTimeTotal");
        if (compound.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"), registries);
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            usedRecipeTracker.put(ResourceLocation.parse(key), compoundRecipes.getInt(key));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("AgeTime", ageTime);
        compound.putInt("AgeTimeTotal", ageTimeTotal);
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName, registries));
        }
        compound.put("Inventory", inventory.serializeNBT(registries));
        CompoundTag compoundRecipes = new CompoundTag();
        usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("Inventory", inventory.serializeNBT(registries));
        return compound;
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, CaskBlockEntity caskBlock) {
        boolean didInventoryChange = false;

        if (caskBlock.hasInput()) {
            Optional<RecipeHolder<CaskRecipe>> recipe = caskBlock.getMatchingRecipe(new RecipeWrapper(caskBlock.inventory));
            if (recipe.isPresent() && caskBlock.canCook(recipe.get().value())) {
                didInventoryChange = caskBlock.processCooking(recipe.get(), caskBlock);
            } else {
                caskBlock.ageTime = 0;
            }
        } else if (caskBlock.ageTime > 0) {
            caskBlock.ageTime = Mth.clamp(caskBlock.ageTime - 2, 0, caskBlock.ageTimeTotal);
        }

        if (didInventoryChange) {
            caskBlock.inventoryChanged();
        }
    }

    private Optional<RecipeHolder<CaskRecipe>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();
        return hasInput() ? quickCheck.getRecipeFor(inventoryWrapper, this.level) : Optional.empty();
    }

    private boolean hasInput() {
        for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    protected boolean canCook(CaskRecipe recipe) {
        if (hasInput()) {
            ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
                if (storedMealStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(storedMealStack, resultStack)) {
                    return false;
                } else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT)) {
                    return true;
                } else {
                    return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    public boolean isProcessingRecipe() {
        return ageTime > 0 && hasInput();
    }

    public int getCurrentLightLevel() {
        return level != null ? level.getBrightness(LightLayer.SKY, worldPosition) : 7;
    }

    public boolean processCooking(RecipeHolder<CaskRecipe> recipe, CaskBlockEntity cask) {
        if (level == null) return false;

        int baseCookTime = recipe.value().getCookTime();
        int lightLevel = getCurrentLightLevel();
        float effectiveMultiplier;
        if (lightLevel <= 5) {
            effectiveMultiplier = 0.5f;
        } else if (lightLevel <= 10) {
            effectiveMultiplier = 1.0f;
        } else {
            effectiveMultiplier = 2.0f;
        }
        int effectiveCookTime = Math.max(1, (int)(baseCookTime * effectiveMultiplier));

        ++ageTime;
        ageTimeTotal = effectiveCookTime;
        if (ageTime < effectiveCookTime) {
            return false;
        }

        ageTime = 0;
        ItemStack resultStack = recipe.value().getResultItem(this.level.registryAccess());
        ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        if (storedMealStack.isEmpty()) {
            inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
        } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
            storedMealStack.grow(resultStack.getCount());
        }
        cask.setRecipeUsed(recipe);

        for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (!slotStack.isEmpty())
                slotStack.shrink(1);
        }
        return true;
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(CaskBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        ItemUtils.spawnItemEntity(level, remainderStack, x, y, z,
                direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.id();
            usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<RecipeHolder<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
        player.awardRecipes(usedRecipes);
        usedRecipeTracker.clear();
    }

    public List<RecipeHolder<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<RecipeHolder<?>> list = Lists.newArrayList();
        if (!(level instanceof ServerLevel serverLevel)) return list;
        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipeTracker.object2IntEntrySet()) {
            serverLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience(serverLevel, pos, entry.getIntValue(), ((CaskRecipe) recipe.value()).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        ExperienceOrb.award(level, pos, expTotal);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getMeal() {
        return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            if (i != MEAL_DISPLAY_SLOT) {
                drops.add(inventory.getStackInSlot(i));
            }
        }
        return drops;
    }

    @Override
    public Component getName() {
        return customName != null ? customName : TextUtils.getTranslation("container.cask");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new CaskMenu(id, player, this, cookingPotData);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeItems(new CompoundTag(), registries);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
        getInventory().setStackInSlot(MEAL_DISPLAY_SLOT, componentInput.getOrDefault(ModDataComponents.MEAL, ItemStackWrapper.EMPTY).getStack());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.customName);
        if (!getMeal().isEmpty()) {
            components.set(ModDataComponents.MEAL, new ItemStackWrapper(getMeal()));
        }
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
        tag.remove("meal");
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE)
        {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    private ContainerData createIntArray() {
        return new ContainerData()
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CaskBlockEntity.this.ageTime;
                    case 1 -> CaskBlockEntity.this.ageTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CaskBlockEntity.this.ageTime = value;
                    case 1 -> CaskBlockEntity.this.ageTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}