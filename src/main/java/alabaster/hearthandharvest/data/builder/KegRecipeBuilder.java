package alabaster.hearthandharvest.data.builder;

import alabaster.hearthandharvest.common.crafting.FluidInput;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import com.google.gson.JsonArray;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class KegRecipeBuilder implements RecipeBuilder {
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    private FluidInput inputFluid;
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
        this.inputFluid = FluidInput.of(fluid);
        return this;
    }

    public KegRecipeBuilder inputFluid(TagKey<Fluid> tag, int amount) {
        this.inputFluid = FluidInput.of(tag, amount);
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

    public KegRecipeBuilder addIngredient(TagKey<Item> tag, int quantity) {
        return addIngredient(Ingredient.of(tag), quantity);
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
    public KegRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterion) {
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
        save(output, new ResourceLocation(HearthAndHarvest.MODID, name));
    }

    public void build(RecipeOutput output) {
        if (resultItem.isEmpty()) {
            throw new IllegalStateException("Keg recipes with no item result must be saved with an explicit name");
        }
        save(output, BuiltInRegistries.ITEM.getKey(resultItem.getItem()));
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        ResourceLocation recipeId = new ResourceLocation(id.getNamespace(), "fermenting/" + id.getPath());
        output.accept(HHFinishedRecipe.of(recipeId, HHModRecipeSerializers.FERMENTING.get(), json -> {
            if (tab != null) json.addProperty("recipe_book_tab", tab.toString());
            JsonArray array = new JsonArray();
            ingredients.forEach(ingredient -> array.add(ingredient.toJson()));
            json.add("ingredients", array);
            if (inputFluid != null) json.add("input_fluid", inputFluid.toJson());
            if (!resultFluid.isEmpty()) json.add("result_fluid", HHFinishedRecipe.fluid(resultFluid));
            if (!resultItem.isEmpty()) json.add("result_item", HHFinishedRecipe.stack(resultItem));
            if (fermentTime != KegRecipe.DEFAULT_TIME) json.addProperty("ferment_time", fermentTime);
            json.addProperty("experience", experience);
        }, criteria));
    }
}
