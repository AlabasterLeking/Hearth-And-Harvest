package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.fluid.HHFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class HHModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, HearthAndHarvest.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, HearthAndHarvest.MODID);

    private static FluidType.Properties defaultProps() {
        return FluidType.Properties.create().viscosity(2000).density(1400);
    }

    private static FluidEntry registerFluid(String name, FluidType.Properties typeProps) {
        Supplier<FluidType> type = FLUID_TYPES.register(name, () -> new FluidType(typeProps));
        Supplier<HHFluidType>[] holders = new Supplier[2];
        holders[0] = FLUIDS.register(name, () ->
                new HHFluidType(new BaseFlowingFluid.Properties(type, holders[0], holders[1]),
                        true));
        holders[1] = FLUIDS.register("flowing_" + name, () ->
                new HHFluidType(new BaseFlowingFluid.Properties(type, holders[0], holders[1]),
                        false));
        return new FluidEntry(type, holders[0], holders[1]);
    }

    private static FluidEntry registerFluidWithBucket(String name, FluidType.Properties typeProps, Supplier<Item> bucketItem) {
        Supplier<FluidType> type = FLUID_TYPES.register(name, () -> new FluidType(typeProps));
        Supplier<HHFluidType>[] holders = new Supplier[2];
        holders[0] = FLUIDS.register(name, () ->
                new HHFluidType(
                        new BaseFlowingFluid.Properties(type, holders[0], holders[1]).bucket(bucketItem),
                        true));
        holders[1] = FLUIDS.register("flowing_" + name, () ->
                new HHFluidType(
                        new BaseFlowingFluid.Properties(type, holders[0], holders[1]),
                        false));
        return new FluidEntry(type, holders[0], holders[1]);
    }

    private static FluidEntry registerFluid(String name) {
        return registerFluid(name, defaultProps());
    }

    public record FluidEntry(
            Supplier<FluidType> type,
            Supplier<HHFluidType> source,
            Supplier<HHFluidType> flowing
    ) {}

    public static final FluidEntry COOKING_OIL = registerFluid("cooking_oil");
    public static final FluidEntry SAP = registerFluidWithBucket("sap", defaultProps(), HHModItems.SAP_BUCKET);
    public static final FluidEntry SYRUP = registerFluid("syrup");
    public static final FluidEntry APPLE_CIDER = registerFluid("apple_cider");
    public static final FluidEntry HARD_CIDER = registerFluid("hard_cider");
    public static final FluidEntry ROOT_BEER = registerFluid("root_beer");
    public static final FluidEntry MEAD = registerFluid("mead");
    public static final FluidEntry MOONSHINE = registerFluid("moonshine");
    public static final FluidEntry BLUEBERRY_JUICE = registerFluid("blueberry_juice");
    public static final FluidEntry CHERRY_JUICE = registerFluid("cherry_juice");
    public static final FluidEntry GREEN_GRAPE_JUICE = registerFluid("green_grape_juice");
    public static final FluidEntry RASPBERRY_JUICE = registerFluid("raspberry_juice");
    public static final FluidEntry RED_GRAPE_JUICE = registerFluid("red_grape_juice");
    public static final FluidEntry SWEET_BERRY_JUICE = registerFluid("sweet_berry_juice");
    public static final FluidEntry MELON_JUICE = registerFluid("melon_juice");
    public static final FluidEntry GLOW_BERRY_JUICE = registerFluid("glow_berry_juice", defaultProps().lightLevel(8));
    public static final FluidEntry BLUEBERRY_WINE = registerFluid("blueberry_wine");
    public static final FluidEntry CHERRY_WINE = registerFluid("cherry_wine");
    public static final FluidEntry GREEN_GRAPE_WINE = registerFluid("green_grape_wine");
    public static final FluidEntry RASPBERRY_WINE = registerFluid("raspberry_wine");
    public static final FluidEntry RED_GRAPE_WINE = registerFluid("red_grape_wine");
    public static final FluidEntry SWEET_BERRY_WINE = registerFluid("sweet_berry_wine");
    public static final FluidEntry GLOW_BERRY_WINE = registerFluid("glow_berry_wine");
    public static final FluidEntry MELON_WINE = registerFluid("melon_wine");
}