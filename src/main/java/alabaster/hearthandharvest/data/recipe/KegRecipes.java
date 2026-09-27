package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModFluids;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.data.builder.KegRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.minecraft.world.item.Items;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import net.neoforged.neoforge.fluids.FluidStack;

public class KegRecipes {
    private static final int BOTTLE = 250;
    private static final int WINE_TIME = 1200;
    private static final float WINE_EXP = 0.35F;

    private static final int BUCKET = 1000;
    private static final int CHEESE_TIME = 2400;
    private static final float CHEESE_EXP = 1.0F;

    public static void register(RecipeOutput output) {
        cheese(output, "unripe_cheddar_cheese_wheel", new FluidStack(NeoForgeMod.MILK.value(), BUCKET),
                HHModItems.UNRIPE_CHEDDAR_CHEESE_WHEEL.get(), Items.MILK_BUCKET);
        cheese(output, "unripe_goat_cheese_wheel", new FluidStack(HHModFluids.GOAT_MILK.source().get(), BUCKET),
                HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get(), HHModItems.GOAT_MILK_BOTTLE.get());

        wine(output, "red_grape_wine", HHModFluids.RED_GRAPE_JUICE, HHModFluids.RED_GRAPE_WINE, HHModItems.RED_GRAPE_JUICE.get());
        wine(output, "green_grape_wine", HHModFluids.GREEN_GRAPE_JUICE, HHModFluids.GREEN_GRAPE_WINE, HHModItems.GREEN_GRAPE_JUICE.get());
        wine(output, "blueberry_wine", HHModFluids.BLUEBERRY_JUICE, HHModFluids.BLUEBERRY_WINE, HHModItems.BLUEBERRY_JUICE.get());
        wine(output, "raspberry_wine", HHModFluids.RASPBERRY_JUICE, HHModFluids.RASPBERRY_WINE, HHModItems.RASPBERRY_JUICE.get());
        wine(output, "cherry_wine", HHModFluids.CHERRY_JUICE, HHModFluids.CHERRY_WINE, HHModItems.CHERRY_JUICE.get());
        wine(output, "sweet_berry_wine", HHModFluids.SWEET_BERRY_JUICE, HHModFluids.SWEET_BERRY_WINE, HHModItems.SWEET_BERRY_JUICE.get());
        wine(output, "glow_berry_wine", HHModFluids.GLOW_BERRY_JUICE, HHModFluids.GLOW_BERRY_WINE, HHModItems.GLOW_BERRY_JUICE.get());
        wine(output, "hard_cider", HHModFluids.APPLE_CIDER, HHModFluids.HARD_CIDER, HHModItems.CASK.get());
    }

    private static void wine(RecipeOutput output, String name, HHModFluids.FluidEntry juice, HHModFluids.FluidEntry wine, Item unlock) {
        new KegRecipeBuilder(WINE_TIME, WINE_EXP)
                .inputFluid(new FluidStack(juice.source().get(), BOTTLE))
                .resultFluid(new FluidStack(wine.source().get(), BOTTLE))
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .unlockedByItems("has_juice", unlock)
                .build(output, name);
    }

    private static void cheese(RecipeOutput output, String name, FluidStack milk, Item wheel, Item unlock) {
        new KegRecipeBuilder(CHEESE_TIME, CHEESE_EXP)
                .inputFluid(milk)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .resultItem(wheel, 1)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .unlockedByItems("has_milk", unlock)
                .build(output, name);
    }
}