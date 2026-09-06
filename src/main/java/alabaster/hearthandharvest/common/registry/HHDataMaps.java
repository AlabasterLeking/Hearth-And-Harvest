package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class HHDataMaps {

    public static final int BOTTLE_VOLUME = 250;

    public static final DataMapType<Fluid, Item> FLUID_BOTTLE = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "fluid_bottle"),
            Registries.FLUID,
            BuiltInRegistries.ITEM.byNameCodec()
    ).build();

    private static volatile Map<Item, Fluid> BOTTLE_TO_FLUID = Map.of();

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(FLUID_BOTTLE);
    }

    @Nullable
    public static Item getBottleForFluid(Fluid fluid) {
        if (fluid == null) return null;
        Fluid source = fluid instanceof FlowingFluid flowing ? flowing.getSource() : fluid;
        Item bottle = BuiltInRegistries.FLUID.wrapAsHolder(source).getData(FLUID_BOTTLE);
        return bottle == Items.AIR ? null : bottle;
    }

    @Nullable
    public static Fluid getFluidForBottle(Item item) {
        if (item == null || item == Items.AIR) return null;
        return BOTTLE_TO_FLUID.get(item);
    }

    private static void rebuildReverseLookup() {
        Map<Item, Fluid> map = new IdentityHashMap<>();
        for (Holder.Reference<Fluid> holder : BuiltInRegistries.FLUID.holders().toList()) {
            Item bottle = holder.getData(FLUID_BOTTLE);
            if (bottle == null || bottle == Items.AIR) continue;
            map.putIfAbsent(bottle, holder.value());
        }
        BOTTLE_TO_FLUID = map;
    }

    @EventBusSubscriber(modid = HearthAndHarvest.MODID)
    public static class Reload {
        @SubscribeEvent
        public static void onDataMapsUpdated(DataMapsUpdatedEvent event) {
            event.ifRegistry(Registries.FLUID, registry -> rebuildReverseLookup());
        }
    }
}