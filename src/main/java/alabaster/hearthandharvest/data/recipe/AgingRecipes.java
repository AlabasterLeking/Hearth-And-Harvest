package alabaster.hearthandharvest.data.recipe;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHCommonTags;
import alabaster.hearthandharvest.data.builder.CaskRecipeBuilder;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import alabaster.hearthandharvest.common.crafting.VintageIngredient;
import alabaster.hearthandharvest.common.item.VintageHelper;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class AgingRecipes {
    public static final int FAST_AGING = 300;      // 1.25 minutes
    public static final int NORMAL_AGING = 600;    // 2.5 minutes
    public static final int SLOW_AGING = 1200;     // 5 minutes
    public static final int VERY_SLOW = 2400;      // 10 minutes

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;


    public static void register(RecipeOutput output) {
        vintageRecipes(output);
        ageCheese(output);
        ageMisc(output);
    }

    private static void ageCheese(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);

        CaskRecipeBuilder.caskRecipe(HHModItems.GOAT_CHEESE_WHEEL.get(), 1, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .unlockedByAnyIngredient(HHModItems.UNRIPE_GOAT_CHEESE_WHEEL.get())
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output);
    }

    private static void ageMisc(RecipeOutput output) {
        CaskRecipeBuilder.caskRecipe(HHModItems.JERKY.get(), 3, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .addIngredient(Items.ROTTEN_FLESH)
                .unlockedByAnyIngredient(Items.ROTTEN_FLESH)
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"jerky_from_rotten_flesh");

        CaskRecipeBuilder.caskRecipe(HHModItems.JERKY.get(), 3, SLOW_AGING, MEDIUM_EXP)
                .addIngredient(HHCommonTags.DUSTS_SALT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .addIngredient(Tags.Items.FOODS_RAW_MEAT)
                .unlockedByAnyIngredient()
                .setRecipeBookTab(CaskRecipeBookTab.MEALS)
                .build(output,"jerky_from_raw_meat");
    }

    private static final int[] VINTAGE_TIMES = {24000, 48000, 72000};

    private static void vintageRecipes(RecipeOutput output) {
        vintages(output, CaskRecipeBookTab.DRINKS, List.of(
                HHModItems.BLUEBERRY_WINE, HHModItems.CHERRY_WINE, HHModItems.GLOW_BERRY_WINE,
                HHModItems.GREEN_GRAPE_WINE, HHModItems.HARD_CIDER, HHModItems.MEAD,
                HHModItems.MELON_WINE, HHModItems.RASPBERRY_WINE, HHModItems.RED_GRAPE_WINE,
                HHModItems.ROOT_BEER, HHModItems.BEER, HHModItems.SWEET_BERRY_WINE));
        vintages(output, CaskRecipeBookTab.MEALS, List.of(
                HHModItems.PICKLED_BEETROOTS, HHModItems.PICKLED_CABBAGE, HHModItems.PICKLED_CARROTS,
                HHModItems.PICKLED_ONIONS, HHModItems.PICKLED_POTATOES,
                HHModItems.CHEESE_WHEEL, HHModItems.GOAT_CHEESE_WHEEL));
    }

    private static void vintages(RecipeOutput output, CaskRecipeBookTab tab, List<Supplier<Item>> ageables) {
        for (Supplier<Item> supplier : ageables) {
            Item item = supplier.get();
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();

            for (int vintage = 1; vintage <= VintageHelper.MAX_VINTAGE; vintage++) {
                ItemStack result = VintageHelper.withVintage(new ItemStack(item), vintage);
                String stage = VintageHelper.STAGE_NAMES[vintage - 1];

                new CaskRecipeBuilder(result, VINTAGE_TIMES[vintage - 1], MEDIUM_EXP)
                        .addIngredient(new VintageIngredient(item, vintage - 1).toVanilla())
                        .unlockedByItems("has_" + name, item)
                        .setRecipeBookTab(tab)
                        .save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, name + "_" + stage));
            }
        }
    }
}