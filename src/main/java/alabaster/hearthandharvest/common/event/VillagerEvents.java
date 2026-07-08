package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import vectorwing.farmersdelight.common.Configuration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
@ParametersAreNonnullByDefault

public class VillagerEvents
{
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (!Configuration.ENABLE_FARMERS_BUY_FD_CROPS.get()) return;

        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        VillagerProfession profession = event.getType();
        ResourceLocation professionKey = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession);
        if (professionKey == null) return;
        if (professionKey.getPath().equals("farmer")) {
            trades.get(1).add(emeraldForItemsTrade(HHModItems.BLUEBERRIES.get(), 26, 16, 2));
            trades.get(1).add(emeraldForItemsTrade(HHModItems.RASPBERRY.get(), 26, 16, 2));
            trades.get(1).add(emeraldForItemsTrade(HHModItems.CHERRY.get(), 10, 16, 2));
            trades.get(1).add(emeraldForItemsTrade(HHModItems.PEANUT.get(), 16, 16, 2));
            trades.get(1).add(emeraldForItemsTrade(HHModItems.COTTON.get(), 16, 16, 2));
            trades.get(1).add(itemForEmeraldTrade(2, HHModItems.MANURE.get(), 8, 16, 2));
            trades.get(2).add(emeraldForItemsTrade(HHModItems.CORN.get(), 24, 16, 5));
            trades.get(2).add(emeraldForItemsTrade(HHModItems.RED_GRAPES.get(), 24, 16, 5));
            trades.get(2).add(emeraldForItemsTrade(HHModItems.GREEN_GRAPES.get(), 24, 16, 5));
            trades.get(3).add(itemForEmeraldTrade(8, HHModItems.SCARECROW.get(), 8, 24));
            trades.get(3).add(itemForEmeraldTrade(3, HHModItems.NEST.get(), 32, 20));
            trades.get(3).add(itemForEmeraldTrade(4, HHModItems.SPRINKLER.get(), 10, 24));
            trades.get(4).add(itemForEmeraldTrade(4, HHModItems.FARMERS_HAT.get(), 8, 30));
            trades.get(4).add(itemForEmeraldTrade(4, HHModItems.PITCHFORK.get(), 8, 30));
        }
        if (professionKey.getPath().equals("mason")) {
            trades.get(2).add(itemForEmeraldTrade(1, HHModItems.MANURE_BRICK_BLOCK.get(), 8, 16, 5));
            trades.get(2).add(itemForEmeraldTrade(1, HHModItems.POLISHED_MANURE.get(), 8,16, 5));
        }
    }

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        if (Configuration.ENABLE_WANDERING_TRADER_SELLS_FD_ITEMS.get()) {
            List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
            trades.add(itemForEmeraldTrade(1, HHModItems.RED_GRAPES.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.GREEN_GRAPES.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.BLUEBERRIES.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.RASPBERRY.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.PEANUT.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.COTTON_SEEDS.get(), 1, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.CORN_KERNELS.get(), 1, 12));

            trades.add(itemForEmeraldTrade(1, HHModItems.YELLOW_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.ORANGE_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.RED_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.BLUE_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.LIGHT_BLUE_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.PURPLE_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.PINK_MUM.get(), 4, 8, 12));
            trades.add(itemForEmeraldTrade(1, HHModItems.WHITE_MUM.get(), 4, 8, 12));
        }
    }

    public static BasicItemListing emeraldForItemsTrade(ItemLike item, int count, int maxTrades, int xp) {
        return new BasicItemListing(new ItemStack(item, count), new ItemStack(Items.EMERALD), maxTrades, xp, 0.05F);
    }

    public static BasicItemListing itemForEmeraldTrade(int price, ItemLike item, int maxTrades, int xp) {
        return itemForEmeraldTrade(price, item, 1, maxTrades, xp);
    }

    public static BasicItemListing itemForEmeraldTrade(int price, ItemLike item, int count, int maxTrades, int xp) {
        return new BasicItemListing(price, new ItemStack(item, count), maxTrades, xp, 0.05F);
    }
}