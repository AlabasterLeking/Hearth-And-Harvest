package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class BasinBlockEntity extends HHSyncedBlockEntity {
    public static final int CAPACITY = 1000;
    public static final int BOTTLE_AMOUNT = 250;

    public final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }

        @Override
        public void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
                if (tank.getFluidAmount() < CAPACITY) {
                    level.scheduleTick(worldPosition, getBlockState().getBlock(), 60);
                }
            }
        }
    };

    public BasinBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.BASIN.get(), pos, state);
    }

    public boolean isFull()  { return tank.getFluidAmount() >= CAPACITY; }
    public boolean isEmpty() { return tank.getFluidAmount() == 0; }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", tank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Tank")) tank.readFromNBT(tag.getCompound("Tank"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("Tank", tank.writeToNBT(new CompoundTag()));
        return tag;
    }

}