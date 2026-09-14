package alabaster.hearthandharvest.common.brewing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.function.Supplier;

public class PotionMix implements IBrewingRecipe {
    private final Supplier<Potion> from;
    private final Ingredient reagent;
    private final Supplier<Potion> to;

    public PotionMix(Supplier<Potion> from, Ingredient reagent, Supplier<Potion> to) {
        this.from = from;
        this.reagent = reagent;
        this.to = to;
    }

    @Override
    public boolean isInput(ItemStack stack) {
        Item item = stack.getItem();
        if (item != Items.POTION && item != Items.SPLASH_POTION && item != Items.LINGERING_POTION) {
            return false;
        }
        return PotionUtils.getPotion(stack) == from.get();
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return reagent.test(stack);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }
        return PotionUtils.setPotion(new ItemStack(input.getItem()), to.get());
    }
}