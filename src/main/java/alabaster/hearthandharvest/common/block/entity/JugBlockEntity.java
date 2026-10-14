package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.item.JugBlockItem;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class JugBlockEntity extends HHSyncedBlockEntity {

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
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("FluidTank")) {
            fluidTank.readFromNBT(compound.getCompound("FluidTank"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        CompoundTag tankTag = new CompoundTag();
        fluidTank.writeToNBT(tankTag);
        compound.put("FluidTank", tankTag);
    }

    private CompoundTag writeUpdateTag(CompoundTag compound) {
        super.saveAdditional(compound);
        CompoundTag tankTag = new CompoundTag();
        fluidTank.writeToNBT(tankTag);
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
            public void onContentsChanged() {
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
    public CompoundTag getUpdateTag() {
        return writeUpdateTag(new CompoundTag());
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}