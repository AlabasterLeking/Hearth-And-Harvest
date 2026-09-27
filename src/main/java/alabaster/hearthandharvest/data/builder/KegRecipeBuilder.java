package alabaster.hearthandharvest.data.builder;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class KegRecipeBuilder implements RecipeBuilder {
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private FluidStack inputFluid = FluidStack.EMPTY;
    private FluidStack resultFluid = FluidStack.EMPTY;
    private ItemStack resultItem = ItemStack.EMPTY;
    private CaskRecipeBookTab tab;
    private final int fermentTime;
    private final float experience;

    public KegRecipeBuilder(int fermentTime, float experience) {
        this.fermentTime = fermentTime;
        this.experience = experience;
    }

    public static KegRecipeBuilder kegRecipe(int fermentTime, float experience) {
        return new KegRecipeBuilder(fermentTime, experience);
    }

    public KegRecipeBuilder inputFluid(FluidStack fluid) {
        this.inputFluid = fluid;
        return this;
    }

    public KegRecipeBuilder resultFluid(FluidStack fluid) {
        this.resultFluid = fluid;
        return this;
    }

    public KegRecipeBuilder resultItem(ItemLike item, int count) {
        this.resultItem = new ItemStack(item, count);
        return this;
    }

    public KegRecipeBuilder resultItem(ItemStack stack) {
        this.resultItem = stack;
        return this;
    }

    public KegRecipeBuilder addIngredient(TagKey<Item> tag) {
        return addIngredient(Ingredient.of(tag));
    }

    public KegRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(Ingredient.of(item), 1);
    }

    public KegRecipeBuilder addIngredient(ItemLike item, int quantity) {
        return addIngredient(Ingredient.of(item), quantity);
    }

    public KegRecipeBuilder addIngredient(Ingredient ingredient) {
        return addIngredient(ingredient, 1);
    }

    public KegRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredient);
        }
        return this;
    }

    public KegRecipeBuilder setRecipeBookTab(CaskRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public KegRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterion) {
        this.criteria.put(criterionName, criterion);
        return this;
    }

    public KegRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return resultItem.isEmpty() ? Items.AIR : resultItem.getItem();
    }

    public void build(RecipeOutput output, String name) {
        save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, name));
    }

    public void build(RecipeOutput output) {
        if (resultItem.isEmpty()) {
            throw new IllegalStateException("Keg recipes with no item result must be saved with an explicit name");
        }
        save(output, BuiltInRegistries.ITEM.getKey(resultItem.getItem()));
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "fermenting/" + id.getPath());
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        KegRecipe recipe = new KegRecipe(tab, ingredients, inputFluid, resultFluid, resultItem, fermentTime, experience);
        output.accept(recipeId, recipe, advancement.build(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/fermenting/" + id.getPath())));
    }
}