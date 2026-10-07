package alabaster.hearthandharvest.data.builder;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedHashMap;
import java.util.Map;

public class CaskRecipeBuilder implements RecipeBuilder
{
    private CaskRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Item result;
    private final ItemStack resultStack;
    private final int agingTime;
    private final float experience;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public CaskRecipeBuilder(ItemLike result, int count, int agingTime, float experience) {
        this(new ItemStack(result, count), agingTime, experience);
    }

    public CaskRecipeBuilder(ItemStack resultIn, int agingTime, float experience) {
        this.result = resultIn.getItem();
        this.resultStack = resultIn;
        this.agingTime = agingTime;
        this.experience = experience;
        this.tab = null;
    }

    public static CaskRecipeBuilder caskRecipe(ItemLike mainResult, int count, int agingTime, float experience) {
        return new CaskRecipeBuilder(mainResult, count, agingTime, experience);
    }

    public CaskRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(tagIn));
    }

    public CaskRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public CaskRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public CaskRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public CaskRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    @Override
    public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
        return this;
    }

    public CaskRecipeBuilder setRecipeBookTab(CaskRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public CaskRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
        this.criteria.put(criterionName, criterionTrigger);
        return this;
    }

    public CaskRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    public CaskRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
        return this;
    }

    public void build(RecipeOutput output) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
        save(output, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, location.getPath()));
    }

    public void build(RecipeOutput outputIn, String save) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(result);
        ResourceLocation id = save.indexOf(':') >= 0 ? ResourceLocation.parse(save) : ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, save);
        if (id.equals(resourcelocation)) {
            throw new IllegalStateException("Aging Recipe " + save + " should remove its 'save' argument");
        } else {
            save(outputIn, id);
        }
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation recipeId = id.withPrefix("aging/");
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        CaskRecipe recipe = new CaskRecipe(
                this.tab,
                this.ingredients,
                this.resultStack,
                this.experience,
                this.agingTime
        );
        output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/aging/")));
    }
}