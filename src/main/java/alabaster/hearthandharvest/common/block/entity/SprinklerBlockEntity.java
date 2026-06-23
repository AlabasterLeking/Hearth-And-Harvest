package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.block.IHarvestable;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class SprinklerBlockEntity extends BlockEntity {

    public static final int CAPACITY = 10_000;
    public static final int HYDRATE_RADIUS = 7;

    private static final int HYDRATE_INTERVAL = 40;
    private static final int BONEMEAL_INTERVAL = 100;
    private static final int WATER_PER_CYCLE = 10;
    private static final int WATER_PER_FIRE = 100;

    public final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(Fluids.WATER);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide)
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };

    private final NonNullList<ItemStack> fertilizerSlot = NonNullList.withSize(1, ItemStack.EMPTY);
    private int hydrateTimer;
    private int bonemealTimer;

    public SprinklerBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.SPRINKLER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SprinklerBlockEntity be) {
        if (++be.hydrateTimer >= HYDRATE_INTERVAL) {
            be.hydrateTimer = 0;
            if (be.tank.getFluidAmount() >= WATER_PER_CYCLE)
                be.hydrateFarmland((ServerLevel) level, pos);
            if (be.tank.getFluidAmount() >= WATER_PER_FIRE)
                be.extinguishFires((ServerLevel) level, pos);
        }
        if (++be.bonemealTimer >= BONEMEAL_INTERVAL) {
            be.bonemealTimer = 0;
            if (!be.fertilizerSlot.get(0).isEmpty())
                be.tryApplyFertilizer((ServerLevel) level, pos);
        }
    }

    private boolean hydrateFarmland(ServerLevel level, BlockPos center) {
        boolean hydrated = false;
        List<BlockPos> splashTargets = new ArrayList<>();
        for (BlockPos scan : BlockPos.betweenClosed(
                center.offset(-HYDRATE_RADIUS, -1, -HYDRATE_RADIUS),
                center.offset(HYDRATE_RADIUS, 1, HYDRATE_RADIUS))) {
            BlockState s = level.getBlockState(scan);
            if (s.hasProperty(BlockStateProperties.MOISTURE) && s.getValue(BlockStateProperties.MOISTURE) < 7) {
                level.setBlock(scan, s.setValue(BlockStateProperties.MOISTURE, 7), Block.UPDATE_CLIENTS);
                hydrated = true;
                if (splashTargets.size() < 6) splashTargets.add(scan.immutable());
            }
        }
        if (!hydrated) return false;
        tank.drain(WATER_PER_CYCLE, IFluidHandler.FluidAction.EXECUTE);
        for (BlockPos wp : splashTargets)
            level.sendParticles(ParticleTypes.SPLASH,
                    wp.getX() + 0.5, wp.getY() + 1.0, wp.getZ() + 0.5,
                    15, 0.4, 0.05, 0.4, 0.1);
        return true;
    }

    private void extinguishFires(ServerLevel level, BlockPos center) {
        for (BlockPos scan : BlockPos.betweenClosed(
                center.offset(-HYDRATE_RADIUS, -1, -HYDRATE_RADIUS),
                center.offset(HYDRATE_RADIUS, 3, HYDRATE_RADIUS))) {
            if (tank.getFluidAmount() < WATER_PER_FIRE) break;
            if (!(level.getBlockState(scan).getBlock() instanceof BaseFireBlock)) continue;
            level.removeBlock(scan, false);
            level.playSound(null, scan, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
            tank.drain(WATER_PER_FIRE, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void tryApplyFertilizer(ServerLevel level, BlockPos center) {
        int r = HYDRATE_RADIUS;
        for (int attempt = 0; attempt < 3; attempt++) {
            int x = center.getX() + level.random.nextInt(r * 2 + 1) - r;
            int z = center.getZ() + level.random.nextInt(r * 2 + 1) - r;
            for (int dy = 2; dy >= 0; dy--) {
                BlockPos candidate = new BlockPos(x, center.getY() + dy, z);
                BlockState s = level.getBlockState(candidate);
                if (!isCrop(s)) continue;
                if (!(s.getBlock() instanceof BonemealableBlock bonemealable)) continue;
                if (!bonemealable.isValidBonemealTarget(level, candidate, s)) continue;
                if (bonemealable.isBonemealSuccess(level, level.random, candidate, s))
                    bonemealable.performBonemeal(level, level.random, candidate, s);
                fertilizerSlot.get(0).shrink(1);
                setChanged();
                level.playSound(null, candidate, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        candidate.getX() + 0.5, candidate.getY() + 0.5, candidate.getZ() + 0.5,
                        8, 0.4, 0.4, 0.4, 0.0);
                return;
            }
        }
    }

    private static boolean isCrop(BlockState state) {
        return state.is(BlockTags.CROPS)
                || state.getBlock() instanceof CropBlock
                || state.getBlock() instanceof IHarvestable;
    }

    public int addFertilizer(ItemStack stack, boolean isCreative) {
        if (!stack.is(Items.BONE_MEAL)) return 0;
        ItemStack stored = fertilizerSlot.get(0);
        if (stored.isEmpty()) {
            fertilizerSlot.set(0, stack.copyWithCount(1));
            if (!isCreative) stack.shrink(1);
            setChanged();
            return 1;
        }
        if (!stored.is(stack.getItem())) return 0;
        int space = stored.getMaxStackSize() - stored.getCount();
        if (space <= 0) return 0;
        int toAdd = Math.min(stack.getCount(), space);
        stored.grow(toAdd);
        if (!isCreative) stack.shrink(toAdd);
        setChanged();
        return toAdd;
    }

    public ItemStack getFertilizer() {
        return fertilizerSlot.get(0);
    }

    public FluidTank getFluidTank() {
        return tank;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
        ContainerHelper.saveAllItems(tag, fertilizerSlot, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Tank")) tank.readFromNBT(registries, tag.getCompound("Tank"));
        ContainerHelper.loadAllItems(tag, fertilizerSlot, registries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
        ContainerHelper.saveAllItems(tag, fertilizerSlot, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}