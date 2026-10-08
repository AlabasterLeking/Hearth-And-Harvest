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
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import vectorwing.farmersdelight.common.registry.ModItems;

public class KegRecipes {
    private static final int BOTTLE = 250;
    private static final int WINE_TIME = 1200;
    private static final float WINE_EXP = 0.35F;

    private static final int BUCKET = 1000;
    private static final int CHEESE_TIME = 2400;
    private static final float CHEESE_EXP = 1.0F;

    private static final int BREW_TIME = 1200;
    private static final float BREW_EXP = 0.35F;

    private static final int BRINE_TIME = 600;
    private static final float BRINE_EXP = 0.1F;

    private static final int PICKLE_TIME = 1200;
    private static final float PICKLE_EXP = 1.0F;

    public static void register(RecipeOutput output) {
        cheese(output, "unripe_cheese_wheel", new FluidStack(NeoForgeMod.MILK.value(), BUCKET),
                HHModItems.UNRIPE_CHEESE_WHEEL.get(), Items.MILK_BUCKET);
        cheese(output, "unripe_goat_cheese_wheel", new FluidStack(HHModFluids.GOAT_MILK.source().get(), BUCKET),
                HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get(), HHModItems.GOAT_MILK_BOTTLE.get());

        wine(output, "red_grape_wine", HHModFluids.RED_GRAPE_JUICE, HHModFluids.RED_GRAPE_WINE, HHModItems.RED_GRAPE_JUICE.get());
        wine(output, "green_grape_wine", HHModFluids.GREEN_GRAPE_JUICE, HHModFluids.GREEN_GRAPE_WINE, HHModItems.GREEN_GRAPE_JUICE.get());
        wine(output, "blueberry_wine", HHModFluids.BLUEBERRY_JUICE, HHModFluids.BLUEBERRY_WINE, HHModItems.BLUEBERRY_JUICE.get());
        wine(output, "raspberry_wine", HHModFluids.RASPBERRY_JUICE, HHModFluids.RASPBERRY_WINE, HHModItems.RASPBERRY_JUICE.get());
        wine(output, "cherry_wine", HHModFluids.CHERRY_JUICE, HHModFluids.CHERRY_WINE, HHModItems.CHERRY_JUICE.get());
        wine(output, "sweet_berry_wine", HHModFluids.SWEET_BERRY_JUICE, HHModFluids.SWEET_BERRY_WINE, HHModItems.SWEET_BERRY_JUICE.get());
        wine(output, "glow_berry_wine", HHModFluids.GLOW_BERRY_JUICE, HHModFluids.GLOW_BERRY_WINE, HHModItems.GLOW_BERRY_JUICE.get());
        wine(output, "melon_wine", HHModFluids.MELON_JUICE, HHModFluids.MELON_WINE, ModItems.MELON_JUICE.get());
        wine(output, "hard_cider", HHModFluids.APPLE_CIDER, HHModFluids.HARD_CIDER, ModItems.APPLE_CIDER.get());

        brews(output);
        brine(output);
        pickles(output);
    }

    private static void brews(RecipeOutput output) {
        new KegRecipeBuilder(BREW_TIME, BREW_EXP)
                .inputFluid(new FluidStack(Fluids.WATER, BOTTLE))
                .addIngredient(Items.HONEY_BOTTLE)
                .addIngredient(Items.SUGAR)
                .resultFluid(new FluidStack(HHModFluids.MEAD.source().get(), BOTTLE))
                .resultItem(Items.GLASS_BOTTLE, 1)
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .unlockedByItems("has_honey_bottle", Items.HONEY_BOTTLE)
                .build(output, "mead_from_honey_bottle");


        new KegRecipeBuilder(BREW_TIME, BREW_EXP)
                .inputFluid(new FluidStack(Fluids.WATER, BOTTLE))
                .addIngredient(Items.HANGING_ROOTS, 2)
                .addIngredient(Items.SUGAR)
                .resultFluid(new FluidStack(HHModFluids.ROOT_BEER.source().get(), BOTTLE))
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .unlockedByItems("has_hanging_roots", Items.HANGING_ROOTS)
                .build(output, "root_beer");

        new KegRecipeBuilder(BREW_TIME, BREW_EXP)
                .inputFluid(new FluidStack(Fluids.WATER, BOTTLE))
                .addIngredient(Tags.Items.CROPS_WHEAT, 2)
                .addIngredient(HHCommonTags.CROPS_HOPS)
                .resultFluid(new FluidStack(HHModFluids.BEER.source().get(), BOTTLE))
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .unlockedByItems("has_hops", HHModItems.HOPS.get())
                .build(output, "beer");

        new KegRecipeBuilder(BREW_TIME, BREW_EXP)
                .inputFluid(new FluidStack(Fluids.WATER, BOTTLE))
                .addIngredient(HHCommonTags.FLOURS_CORN, 2)
                .addIngredient(Items.SUGAR)
                .resultFluid(new FluidStack(HHModFluids.MOONSHINE.source().get(), BOTTLE))
                .setRecipeBookTab(CaskRecipeBookTab.DRINKS)
                .unlockedByItems("has_corn_meal", HHModItems.CORN_MEAL.get())
                .build(output, "moonshine");
    }

    private static void brine(RecipeOutput output) {
        new KegRecipeBuilder(BRINE_TIME, BRINE_EXP)
                .inputFluid(new FluidStack(Fluids.WATER, BUCKET))
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .resultFluid(new FluidStack(HHModFluids.BRINE.source().get(), BUCKET))
                .setRecipeBookTab(CaskRecipeBookTab.MISC)
                .unlockedByItems("has_salt", HHModItems.SALT.get())
                .build(output, "brine");
    }

    private static void pickles(RecipeOutput output) {
        pickle(output, HHModItems.PICKLED_CARROTS.get(), Items.CARROT);
        pickle(output, HHModItems.PICKLED_POTATOES.get(), Items.POTATO);
        pickle(output, HHModItems.PICKLED_ONIONS.get(), ModItems.ONION.get());
        pickle(output, HHModItems.PICKLED_CABBAGE.get(), ModItems.CABBAGE.get());
        pickle(output, HHModItems.PICKLED_BEETROOTS.get(), Items.BEETROOT);
    }

    private static void pickle(RecipeOutput output, Item result, Item vegetable) {
        new KegRecipeBuilder(PICKLE_TIME, PICKLE_EXP)
                .inputFluid(new FluidStack(HHModFluids.BRINE.source().get(), BOTTLE))
                .addIngredient(vegetable, 2)
                .addIngredient(HHModItems.JAR.get())
                .resultItem(result, 1)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .unlockedByItems("has_vegetable", vegetable)
                .build(output);
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