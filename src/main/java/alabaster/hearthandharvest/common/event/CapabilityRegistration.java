package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.StompingBasinBlockEntity;
import alabaster.hearthandharvest.common.item.JugBlockItem;
import alabaster.hearthandharvest.common.registry.HHDataMaps;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilityRegistration {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        // Stomping Basin
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                HHModBlockEntities.STOMPING_BASIN.get(),
                (be, side) -> {
                    StompingBasinBlockEntity controller = be.getControllerBE();
                    return controller != null ? controller.getItemHandler() : be.getItemHandler();
                }
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.STOMPING_BASIN.get(),
                (be, side) -> be.getFluidHandlerForCapability()
        );

        // Trough block entity
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.TROUGH.get(),
                (be, side) -> be.getFluidTank()
        );

        // Tree tapper block entity
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.TREE_TAPPER.get(),
                (be, side) -> be.tank
        );

        // Sink block entity
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.BASIN.get(),
                (be, side) -> be.tank
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.SPRINKLER.get(),
                (be, side) -> be.tank
        );

        // Jug block entity
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                HHModBlockEntities.JUG.get(),
                (be, side) -> be.getFluidTank()
        );

        // Jug item
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new FluidHandlerItemStack(
                        HHModDataComponents.JUG_FLUID, stack, JugBlockItem.JUG_CAPACITY),
                HHModItems.JUG.get()
        );

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                CapabilityRegistration::bottleHandler,
                bottleCandidates()
        );
    }

    private static Item[] bottleCandidates() {
        List<Item> items = new ArrayList<>();
        for (var holder : HHModItems.ITEMS.getEntries()) {
            items.add(holder.get());
        }
        items.add(ModItems.APPLE_CIDER.get());
        items.add(ModItems.MELON_JUICE.get());
        return items.toArray(new Item[0]);
    }

    @Nullable
    private static IFluidHandlerItem bottleHandler(ItemStack stack, Void ctx) {
        Fluid fluid = HHDataMaps.getFluidForBottle(stack.getItem());
        if (fluid == null) return null;
        return new BottleFluidHandler(stack, fluid);
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

        private boolean isFullBottle() {
            return container.getCount() == 1 && container.getItem() == bottleItem;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            if (!isFullBottle()) return FluidStack.EMPTY;
            return new FluidStack(containedFluid, HHDataMaps.BOTTLE_VOLUME);
        }

        @Override
        public int getTankCapacity(int tank) {
            return HHDataMaps.BOTTLE_VOLUME;
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
            if (!isFullBottle()) return FluidStack.EMPTY;
            if (maxDrain < HHDataMaps.BOTTLE_VOLUME) return FluidStack.EMPTY;
            if (action.execute()) {
                container = new ItemStack(Items.GLASS_BOTTLE);
            }
            return new FluidStack(containedFluid, HHDataMaps.BOTTLE_VOLUME);
        }

        @Override
        public ItemStack getContainer() {
            return container;
        }
    }
}
