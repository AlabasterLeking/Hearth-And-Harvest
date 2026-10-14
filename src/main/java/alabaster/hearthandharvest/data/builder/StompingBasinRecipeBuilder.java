package alabaster.hearthandharvest.data.builder;

import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import com.google.gson.JsonArray;
import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class StompingBasinRecipeBuilder implements RecipeBuilder {

    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final FluidStack resultFluid;
    private final ItemStack  resultItem;
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();

    private StompingBasinRecipeBuilder(FluidStack resultFluid, ItemStack resultItem) {
        this.resultFluid = resultFluid;
        this.resultItem  = resultItem;
    }

    public static StompingBasinRecipeBuilder stomping(FluidStack resultFluid) {
        return new StompingBasinRecipeBuilder(resultFluid, ItemStack.EMPTY);
    }

    public static StompingBasinRecipeBuilder stomping(ItemLike resultItem) {
        return stomping(resultItem, 1);
    }

    public static StompingBasinRecipeBuilder stomping(ItemLike resultItem, int count) {
        return new StompingBasinRecipeBuilder(FluidStack.EMPTY, new ItemStack(resultItem, count));
    }

    public static StompingBasinRecipeBuilder stomping(FluidStack resultFluid, ItemLike resultItem) {
        return stomping(resultFluid, resultItem, 1);
    }

    public static StompingBasinRecipeBuilder stomping(FluidStack resultFluid, ItemLike resultItem, int count) {
        return new StompingBasinRecipeBuilder(resultFluid, new ItemStack(resultItem, count));
    }

    // ── Ingredient helpers ────────────────────────────────────────────────────

    public StompingBasinRecipeBuilder addIngredient(TagKey<Item> tag, int count) {
        Ingredient ingredient = Ingredient.of(tag);
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public StompingBasinRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(item, 1);
    }

    public StompingBasinRecipeBuilder addIngredient(ItemLike item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addIngredient(Ingredient.of(item));
        }
        return this;
    }

    public StompingBasinRecipeBuilder addIngredient(Ingredient ingredient) {
        return addIngredient(ingredient, 1);
    }

    public StompingBasinRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            ingredients.add(ingredient);
        }
        return this;
    }

    public StompingBasinRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.criteria.put("has_any_ingredient",
                InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        if (!resultItem.isEmpty()) return resultItem.getItem();
        if (!resultFluid.isEmpty()) return resultFluid.getFluid().getBucket();
        throw new IllegalStateException("StompingBasinRecipe has no output");
    }

    public void build(RecipeOutput output) {
        ResourceLocation location = !resultItem.isEmpty()
                ? BuiltInRegistries.ITEM.getKey(resultItem.getItem())
                : BuiltInRegistries.FLUID.getKey(resultFluid.getFluid());
        save(output, new ResourceLocation(HearthAndHarvest.MODID, location.getPath()));
    }

    public void build(RecipeOutput output, String path) {
        save(output, new ResourceLocation(HearthAndHarvest.MODID, path));
    }

    public void build(RecipeOutput output, ResourceLocation id) {
        save(output, id);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Stomping Basin recipe " + id + " has no ingredients");
        }
        if (resultFluid.isEmpty() && resultItem.isEmpty()) {
            throw new IllegalStateException("Stomping Basin recipe " + id + " has no outputs");
        }
        ResourceLocation recipeId = id.withPrefix("stomping/");
        output.accept(HHFinishedRecipe.of(recipeId, HHModRecipeSerializers.STOMPING.get(), json -> {
            JsonArray array = new JsonArray();
            ingredients.forEach(ingredient -> array.add(ingredient.toJson()));
            json.add("ingredients", array);
            if (!resultFluid.isEmpty()) json.add("result_fluid", HHFinishedRecipe.fluid(resultFluid));
            if (!resultItem.isEmpty()) json.add("result_item", HHFinishedRecipe.stack(resultItem));
        }, criteria));
    }
}