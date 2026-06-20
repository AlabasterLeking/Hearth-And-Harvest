package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModFluids;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class TreeTapperBlockEntity extends BlockEntity {
    public static final int CAPACITY = 1000;
    private static final int CHECK_INTERVAL = 40;  // ticks between fill attempts (~2 seconds)
    private static final int FILL_PER_CHECK  = 10; // mB added per successful check (100 steps to fill)

    private int tickCounter;

    public final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == HHModFluids.SAP.source().get();
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
        }
    };

    public TreeTapperBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.TREE_TAPPER.get(), pos, state);
    }

    public boolean isFull() {
        return tank.getFluidAmount() >= CAPACITY;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TreeTapperBlockEntity be) {
        if (++be.tickCounter < CHECK_INTERVAL) return;
        be.tickCounter = 0;
        if (be.isFull()) return;
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (!level.getBlockState(pos.relative(dir)).is(HHModTags.TAPPABLE)) return;
        if (level.random.nextFloat() > Config.TREE_TAPPER_BASE_CHANCE.get().floatValue()) return;
        be.tank.fill(new FluidStack(HHModFluids.SAP.source().get(), FILL_PER_CHECK), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Tank")) tank.readFromNBT(registries, tag.getCompound("Tank"));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}