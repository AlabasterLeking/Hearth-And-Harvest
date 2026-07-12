package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "hearthandharvest", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ComposterEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 30% chance
            ComposterBlock.COMPOSTABLES.put(HHModItems.COTTON_SEEDS.get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.SUNFLOWER_SEEDS.get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.BLUEBERRIES.get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.RASPBERRY.get().asItem(), 0.3F);

            // 50% chance
            ComposterBlock.COMPOSTABLES.put(HHModItems.COTTON_BALE.get().asItem(), 0.5F);

            // 65% chance
            ComposterBlock.COMPOSTABLES.put(HHModItems.WILD_COTTON.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.WILD_PEANUTS.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.WILD_GREEN_GRAPES.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.WILD_RED_GRAPES.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.GREEN_GRAPES.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.RED_GRAPES.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.COTTON.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.PEANUT.get().asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.CHERRY.get().asItem(), 0.65F);

            // 85% chance
            ComposterBlock.COMPOSTABLES.put(HHModItems.BLUEBERRY_PIE_SLICE.get().asItem(), 0.85F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.RASPBERRY_PIE_SLICE.get().asItem(), 0.85F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.GRAPE_PIE_SLICE.get().asItem(), 0.85F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.PEANUT_BUTTER_PIE_SLICE.get().asItem(), 0.85F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.CHICKEN_POT_PIE_SLICE.get().asItem(), 0.85F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.CARROT_CAKE_SLICE.get().asItem(), 0.85F);

            // 100% chance
            ComposterBlock.COMPOSTABLES.put(HHModItems.BLUEBERRY_PIE.get().asItem(), 1.0F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.RASPBERRY_PIE.get().asItem(), 1.0F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.GRAPE_PIE.get().asItem(), 1.0F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.PEANUT_BUTTER_PIE.get().asItem(), 1.0F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.CHICKEN_POT_PIE.get().asItem(), 1.0F);
            ComposterBlock.COMPOSTABLES.put(HHModItems.CARROT_CAKE.get().asItem(), 1.0F);
        });
    }
}