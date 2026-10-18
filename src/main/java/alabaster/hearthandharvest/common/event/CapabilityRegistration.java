package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.block.entity.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiFunction;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.StompingBasinBlockEntity;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import alabaster.hearthandharvest.common.item.JugBlockItem;
import alabaster.hearthandharvest.common.registry.HHDataMaps;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;


@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CapabilityRegistration {
    private static final ResourceLocation KEY = new ResourceLocation(HearthAndHarvest.MODID, "handlers");

    @SubscribeEvent
    public static void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();

        // Nest Block
        if (blockEntity instanceof NestBlockEntity be) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.ITEM_HANDLER ? be.getInventory() : null);
        }

        // Stomping Basin
        else if (blockEntity instanceof StompingBasinBlockEntity be) {
            attach(event, (cap, side) -> {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    StompingBasinBlockEntity controller = be.getControllerBE();
                    return controller != null ? controller.getItemHandler() : be.getItemHandler();
                }
                if (cap == ForgeCapabilities.FLUID_HANDLER) return be.getFluidHandlerForCapability();
                return null;
            });
        }

        // Trough block entity
        else if (blockEntity instanceof TroughBlockEntity be) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER ? be.getFluidTank() : null);
        }

        // Tree tapper block entity
        else if (blockEntity instanceof TreeTapperBlockEntity be) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER ? be.tank : null);
        }

        // Sink block entity
        else if (blockEntity instanceof BasinBlockEntity be) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER ? be.tank : null);
        }

        // Sprinkler block entity
        else if (blockEntity instanceof SprinklerBlockEntity be) {
            attach(event, (cap, side) -> {
                if (cap == ForgeCapabilities.FLUID_HANDLER) return be.tank;
                if (cap == ForgeCapabilities.ITEM_HANDLER) return be.getFertilizerHandler();
                return null;
            });
        }

        // Jug block entity
        else if (blockEntity instanceof JugBlockEntity be) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER ? be.getFluidTank() : null);
        }

        else if (blockEntity instanceof KegBlockEntity be) {
            attach(event, be::getHandler);
        }

        else if (blockEntity instanceof CaskBlockEntity be) {
            attach(event, be::getHandler);
        }
    }

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();

        // Jug item
        if (item == HHModItems.JUG.get()) {
            IFluidHandlerItem handler = new JugFluidHandler(stack, JugBlockItem.JUG_CAPACITY);
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER_ITEM ? handler : null);
            return;
        }

        if (item == Items.GLASS_BOTTLE) {
            IFluidHandlerItem handler = new EmptyBottleFluidHandler(stack);
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER_ITEM ? handler : null);
            return;
        }

        if (item == Items.POTION) {
            attach(event, (cap, side) -> cap == ForgeCapabilities.FLUID_HANDLER_ITEM && HHFluidHandling.isWaterBottle(stack) ? new BottleFluidHandler(stack, Fluids.WATER) : null);
            return;
        }

        // Bottles into fluid handlers
        if (item != Items.AIR) {
            attach(event, (cap, side) -> {
                if (cap != ForgeCapabilities.FLUID_HANDLER_ITEM) return null;
                Fluid fluid = HHDataMaps.getFluidForBottle(stack.getItem());
                return fluid == null ? null : new BottleFluidHandler(stack, fluid);
            });
        }
    }

    private static void attach(AttachCapabilitiesEvent<?> event, BiFunction<Capability<?>, Direction, Object> lookup) {
        event.addCapability(KEY, new ICapabilityProvider() {
            @Override
            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                Object handler = lookup.apply(cap, side);
                return handler == null ? LazyOptional.empty() : LazyOptional.of(() -> handler).cast();
            }
        });
    }

    private static class JugFluidHandler extends FluidHandlerItemStack {
        JugFluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public @NotNull FluidStack getFluid() {
            FluidStack fluid = HHModDataComponents.JUG_FLUID.get(container);
            return fluid == null ? FluidStack.EMPTY : fluid;
        }

        @Override
        public void setFluid(FluidStack fluid) {
            if (fluid.isEmpty()) HHModDataComponents.JUG_FLUID.remove(container);
            else HHModDataComponents.JUG_FLUID.set(container, fluid);
        }

        @Override
        public void setContainerToEmpty() {
            HHModDataComponents.JUG_FLUID.remove(container);
        }
    }

    private static class EmptyBottleFluidHandler implements IFluidHandlerItem {
        private ItemStack container;

        EmptyBottleFluidHandler(ItemStack stack) {
            this.container = stack;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return HHDataMaps.BOTTLE_VOLUME;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return stack.getFluid().isSame(Fluids.WATER) || HHDataMaps.getBottleForFluid(stack.getFluid()) != null;
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (container.getCount() != 1) return 0;

            ItemStack filled;
            if (resource.getFluid().isSame(Fluids.WATER)) {
                filled = HHFluidHandling.waterBottle();
            } else {
                Item bottle = HHDataMaps.getBottleForFluid(resource.getFluid());
                if (bottle == null) return 0;
                filled = new ItemStack(bottle);
            }

            int volume = HHDataMaps.getBottleVolume(filled);
            if (resource.getAmount() < volume) return 0;

            if (action.execute()) {
                container = filled;
            }
            return volume;
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public ItemStack getContainer() {
            return container;
        }
    }

    private static class BottleFluidHandler implements IFluidHandlerItem {
        private final Item bottleItem;
        private final Fluid containedFluid;
        private ItemStack container;

        BottleFluidHandler(ItemStack stack, Fluid containedFluid) {
            this.bottleItem = stack.getItem();
            this.containedFluid = containedFluid;
            this.container = stack;
        }

        private boolean hasContents() {
            return container.getCount() == 1
                    && container.getItem() == bottleItem
                    && (bottleItem != Items.POTION || HHFluidHandling.isWaterBottle(container));
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            if (!hasContents()) return FluidStack.EMPTY;
            return new FluidStack(containedFluid, HHDataMaps.getBottleVolume(container));
        }

        @Override
        public int getTankCapacity(int tank) {
            return HHDataMaps.getBottleVolume(bottleItem);
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack f) {
            return f.getFluid().isSame(containedFluid);
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!resource.getFluid().isSame(containedFluid)) return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (!hasContents()) return FluidStack.EMPTY;
            int volume = HHDataMaps.getBottleVolume(container);
            if (maxDrain < volume) return FluidStack.EMPTY;
            if (action.execute()) {
                container = new ItemStack(Items.GLASS_BOTTLE);
            }
            return new FluidStack(containedFluid, volume);
        }

        @Override
        public ItemStack getContainer() {
            return container;
        }
    }
}