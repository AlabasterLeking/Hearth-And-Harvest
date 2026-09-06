package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.item.JugBlockItem;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class JugBlockEntity extends BlockEntity {

    private final FluidTank fluidTank;

    public JugBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.JUG.get(), pos, state);
        this.fluidTank = createFluidTank();
    }

    public FluidStack getOutput() {
        return fluidTank.getFluid();
    }

    public int getFluidAmount() {
        return fluidTank.getFluidAmount();
    }

    public int getFluidCapacity() {
        return fluidTank.getCapacity();
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        if (compound.contains("FluidTank")) {
            fluidTank.readFromNBT(provider, compound.getCompound("FluidTank"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        CompoundTag tankTag = new CompoundTag();
        fluidTank.writeToNBT(provider, tankTag);
        compound.put("FluidTank", tankTag);
    }

    private CompoundTag writeUpdateTag(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        CompoundTag tankTag = new CompoundTag();
        fluidTank.writeToNBT(provider, tankTag);
        compound.put("FluidTank", tankTag);
        return compound;
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        return fluidTank.fill(resource, action);
    }

    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        return fluidTank.drain(maxDrain, action);
    }

    private FluidTank createFluidTank() {
        return new FluidTank(JugBlockItem.JUG_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                setChanged();
                syncToClient();
                if (level != null && !level.isClientSide) {
                    level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
                }
            }
        };
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return writeUpdateTag(new CompoundTag(), provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (pkt.getTag() != null) loadAdditional(pkt.getTag(), lookupProvider);
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
