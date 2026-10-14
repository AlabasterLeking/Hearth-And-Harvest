package alabaster.hearthandharvest.common.registry;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraft.core.registries.Registries;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.fluid.HHFluidType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class HHModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, HearthAndHarvest.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, HearthAndHarvest.MODID);

    private static FluidType.Properties defaultProps() {
        return FluidType.Properties.create().viscosity(2000).density(1400);
    }

    private static FluidType.Properties dairyProps() {
        return FluidType.Properties.create().viscosity(3000).density(1600);
    }

    private static FluidEntry registerFluid(String name, FluidType.Properties typeProps) {
        Supplier<FluidType> type = FLUID_TYPES.register(name, () -> new TexturedFluidType(typeProps, name));
        Supplier<HHFluidType>[] holders = new Supplier[2];
        holders[0] = FLUIDS.register(name, () ->
                new HHFluidType(new ForgeFlowingFluid.Properties(type, holders[0], holders[1]),
                        true));
        holders[1] = FLUIDS.register("flowing_" + name, () ->
                new HHFluidType(new ForgeFlowingFluid.Properties(type, holders[0], holders[1]),
                        false));
        return new FluidEntry(type, holders[0], holders[1]);
    }

    private static FluidEntry registerFluidWithBucket(String name, FluidType.Properties typeProps, Supplier<Item> bucketItem) {
        Supplier<FluidType> type = FLUID_TYPES.register(name, () -> new TexturedFluidType(typeProps, name));
        Supplier<HHFluidType>[] holders = new Supplier[2];
        holders[0] = FLUIDS.register(name, () ->
                new HHFluidType(
                        new ForgeFlowingFluid.Properties(type, holders[0], holders[1]).bucket(bucketItem),
                        true));
        holders[1] = FLUIDS.register("flowing_" + name, () ->
                new HHFluidType(
                        new ForgeFlowingFluid.Properties(type, holders[0], holders[1]),
                        false));
        return new FluidEntry(type, holders[0], holders[1]);
    }

    private static FluidEntry registerFluid(String name) {
        return registerFluid(name, defaultProps());
    }

    private static class TexturedFluidType extends FluidType {
        private final String name;

        TexturedFluidType(Properties properties, String name) {
            super(properties);
            this.name = name;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return new ResourceLocation(HearthAndHarvest.MODID, "block/fluid/" + name + "_still");
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return new ResourceLocation(HearthAndHarvest.MODID, "block/fluid/" + name + "_flow");
                }
            });
        }
    }

    public record FluidEntry(
            Supplier<FluidType> type,
            Supplier<HHFluidType> source,
            Supplier<HHFluidType> flowing
    ) {}

    public static final FluidEntry GOAT_MILK = registerFluidWithBucket("goat_milk", dairyProps(), HHModItems.GOAT_MILK_BUCKET);
    public static final FluidEntry COOKING_OIL = registerFluid("cooking_oil");
    public static final FluidEntry BRINE = registerFluidWithBucket("brine", defaultProps(), HHModItems.BRINE_BUCKET);
    public static final FluidEntry SAP = registerFluidWithBucket("sap", defaultProps(), HHModItems.SAP_BUCKET);
    public static final FluidEntry SYRUP = registerFluid("syrup");
    public static final FluidEntry APPLE_CIDER = registerFluid("apple_cider");
    public static final FluidEntry HARD_CIDER = registerFluid("hard_cider");
    public static final FluidEntry ROOT_BEER = registerFluid("root_beer");
    public static final FluidEntry BEER = registerFluid("beer");
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