package alabaster.hearthandharvest.common.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class HHFluidType extends BaseFlowingFluid {
    private final boolean source;

    public HHFluidType(BaseFlowingFluid.Properties properties, boolean source) {
        super(properties);
        this.source = source;
    }

    @Override public Fluid getSource() {
        return source ? this : super.getSource();
    }

    @Override public Fluid getFlowing() {
        return source ? super.getFlowing() : this;
    }

    @Override public boolean isSource(FluidState fluidState) {
        return source;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return source ? 8 : fluidState.getValue(LEVEL);
    }
}