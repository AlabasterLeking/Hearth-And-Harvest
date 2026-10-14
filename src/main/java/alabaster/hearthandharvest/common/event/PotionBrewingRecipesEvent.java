package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.brewing.PotionMix;
import alabaster.hearthandharvest.common.registry.HHModItems;
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
    public static void onBrewingRecipeRegister(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Ingredient redstone = Ingredient.of(Items.REDSTONE);
            Ingredient glowstone = Ingredient.of(Items.GLOWSTONE_DUST);
            Ingredient spiderEye = Ingredient.of(Items.FERMENTED_SPIDER_EYE);
            // Pungent
            BrewingRecipeRegistry.addRecipe(new PotionMix(() -> Potions.AWKWARD, Ingredient.of(Items.POISONOUS_POTATO), HHModPotions.PUNGENT_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, redstone, HHModPotions.STRONG_PUNGENT_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, glowstone, HHModPotions.LONG_PUNGENT_POTION));
            // Tempting
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.PUNGENT_POTION, spiderEye, HHModPotions.TEMPTING_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.TEMPTING_POTION, redstone, HHModPotions.STRONG_TEMPTING_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.TEMPTING_POTION, glowstone, HHModPotions.LONG_TEMPTING_POTION));
            // Bad Omen
            BrewingRecipeRegistry.addRecipe(new PotionMix(() -> Potions.AWKWARD, Ingredient.of(HHModItems.CROW_FEATHER.get()), HHModPotions.BAD_OMEN_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.BAD_OMEN_POTION, redstone, HHModPotions.STRONG_BAD_OMEN_POTION));
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.BAD_OMEN_POTION, glowstone, HHModPotions.LONG_BAD_OMEN_POTION));
            // Luck
            BrewingRecipeRegistry.addRecipe(new PotionMix(HHModPotions.BAD_OMEN_POTION, spiderEye, () -> Potions.LUCK));
        });
    }
}
