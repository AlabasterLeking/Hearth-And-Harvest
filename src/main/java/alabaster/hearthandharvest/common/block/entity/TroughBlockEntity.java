package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.ManureDropHelper;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class TroughBlockEntity extends BlockEntity {

    public static final int TANK_CAPACITY = 1000;
    public static final int ITEM_SLOT_LIMIT = 64;

    private static final int GROWTH_BOOST_PER_INTERVAL = 16;
    private static final int WATER_COST_PER_BOOST = 10;
    private static final int RAIN_FILL_PER_SECOND = 4;

    private final FluidTank fluidTank = new FluidTank(TANK_CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(Fluids.WATER) && itemHandler.getStackInSlot(0).isEmpty();
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            syncToClient();
            if (level != null && !level.isClientSide) {
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
        }
    };

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); syncToClient();
        }

        @Override
        public int getSlotLimit(int slot) { return ITEM_SLOT_LIMIT; }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return fluidTank.isEmpty() && isAnimalFood(stack);
        }
    };

    public TroughBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.TROUGH.get(), pos, state);
    }

    public static boolean isAnimalFood(ItemStack stack) {
        return stack.is(ItemTags.CHICKEN_FOOD)
                || stack.is(ItemTags.COW_FOOD)
                || stack.is(ItemTags.GOAT_FOOD)
                || stack.is(ItemTags.SHEEP_FOOD)
                || stack.is(ItemTags.PIG_FOOD)
                || stack.is(ItemTags.WOLF_FOOD)
                || stack.is(ItemTags.CAT_FOOD)
                || stack.is(ItemTags.HORSE_FOOD)
                || stack.is(ItemTags.CAMEL_FOOD);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TroughBlockEntity be) {
        // Rain fills the water trough if exposed to sky (1 bucket in ~4 min of rain)
        if (level.getGameTime() % 20 == 0 && be.itemHandler.getStackInSlot(0).isEmpty() && level.isRainingAt(pos.above())) {
            be.fluidTank.fill(new FluidStack(Fluids.WATER, RAIN_FILL_PER_SECOND), IFluidHandler.FluidAction.EXECUTE);
        }

        if (level.getGameTime() % 80 != 0) return;

        AABB effectArea = AABB.ofSize(Vec3.atCenterOf(pos), 10, 4, 10);

        // Water trough: boost growth of nearby baby animals by ~20%
        if (!be.fluidTank.isEmpty()) {
            List<AgeableMob> babies = level.getEntitiesOfClass(AgeableMob.class, effectArea, AgeableMob::isBaby);
            for (AgeableMob baby : babies) {
                if (be.fluidTank.getFluidAmount() < WATER_COST_PER_BOOST) break;
                baby.setAge(Math.min(0, baby.getAge() + GROWTH_BOOST_PER_INTERVAL));
                be.fluidTank.drain(WATER_COST_PER_BOOST, IFluidHandler.FluidAction.EXECUTE);
            }
        }

        // Food trough: put nearby adult animals into love mode, respecting the cap
        ItemStack food = be.itemHandler.getStackInSlot(0);
        if (!food.isEmpty()) {
            int cap = Config.TROUGH_ANIMAL_CAP.get();
            AABB capArea = AABB.ofSize(Vec3.atCenterOf(pos), 20, 8, 20);
            int nearbyCount = level.getEntitiesOfClass(Animal.class, capArea).size();

            if (nearbyCount < cap) {
                ItemStack finalFood = food;
                List<Animal> breedable = level.getEntitiesOfClass(Animal.class, effectArea,
                        a -> a.getAge() == 0 && a.canFallInLove() && a.isFood(finalFood));

                for (Animal animal : breedable) {
                    food = be.itemHandler.getStackInSlot(0);
                    if (nearbyCount >= cap || food.isEmpty()) break;
                    animal.setInLove((ServerPlayer) null);
                    ManureDropHelper.schedulePoop(animal);
                    be.itemHandler.extractItem(0, 1, false);
                    level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS,
                            0.5f, 0.8f + level.random.nextFloat() * 0.4f);
                    nearbyCount++;
                }
            }
        }
    }

    public ItemStack insertItem(ItemStack toInsert) {
        if (toInsert.isEmpty() || !fluidTank.isEmpty()) return toInsert;
        return itemHandler.insertItem(0, toInsert, false);
    }

    public void extractOne(Player player) {
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        if (!inSlot.isEmpty()) giveOrDrop(player, itemHandler.extractItem(0, 1, false));
    }

    public void extractAll(Player player) {
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        if (!inSlot.isEmpty()) giveOrDrop(player, itemHandler.extractItem(0, inSlot.getCount(), false));
    }

    private void giveOrDrop(Player player, ItemStack stack) {
        if (stack.isEmpty()) return;
        if (!player.addItem(stack)) {
            ItemEntity entity = new ItemEntity(level,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5,
                    stack, 0, 0.1, 0);
            entity.setPickUpDelay(10);
            level.addFreshEntity(entity);
        }
    }

    public void dropContents() {
        if (level == null) return;
        ItemStack items = itemHandler.getStackInSlot(0);
        if (!items.isEmpty()) {
            Containers.dropItemStack(level,
                    worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), items);
        }
    }

    public FluidTank getFluidTank() { return fluidTank; }
    public ItemStackHandler getItemHandler() { return itemHandler; }

    void syncToClient() {
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
        tag.put("Tank", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.put("Items", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Tank")) fluidTank.readFromNBT(registries, tag.getCompound("Tank"));
        if (tag.contains("Items")) itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
    }
}