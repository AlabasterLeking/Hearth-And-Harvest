package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.brewing.PotionMix;
import alabaster.hearthandharvest.common.registry.HHModPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionBrewingRecipesEvent {

    @SubscribeEvent
    public static void registerBrewingRecipes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Ingredient potato = Ingredient.of(Items.POISONOUS_POTATO);
            Ingredient redstone = Ingredient.of(Items.REDSTONE);
            Ingredient glowstone = Ingredient.of(Items.GLOWSTONE_DUST);
            Ingredient spidereye = Ingredient.of(Items.FERMENTED_SPIDER_EYE);

            BrewingRecipeRegistry.addRecipe(new PotionMix(() -> Potions.AWKWARD, potato, HHModPotions.PUNGENT_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, redstone, HHModPotions.STRONG_PUNGENT_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, glowstone, HHModPotions.LONG_PUNGENT_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, spidereye, HHModPotions.TEMPTING_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.TEMPTING_POTION, redstone, HHModPotions.STRONG_TEMPTING_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.TEMPTING_POTION, glowstone, HHModPotions.LONG_TEMPTING_POTION));
        });
    }
}
