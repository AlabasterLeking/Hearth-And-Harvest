package alabaster.hearthandharvest.data.builder;

import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import com.google.gson.JsonArray;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import alabaster.hearthandharvest.data.builder.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShapelessRemainderRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final NonNullList<ItemStack> remainders  = NonNullList.create();
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    private @Nullable String group;

    private ShapelessRemainderRecipeBuilder(RecipeCategory category, ItemStack result) {
        this.category = category;
        this.result   = result;
    }

    public static ShapelessRemainderRecipeBuilder shapeless(RecipeCategory category, Item result, int count) {
        return new ShapelessRemainderRecipeBuilder(category, new ItemStack(result, count));
    }

    public static ShapelessRemainderRecipeBuilder shapeless(RecipeCategory category, Item result) {
        return shapeless(category, result, 1);
    }

    public ShapelessRemainderRecipeBuilder requires(Ingredient ingredient) {
        return requires(ingredient, ItemStack.EMPTY);
    }

    public ShapelessRemainderRecipeBuilder requires(TagKey<Item> tag) {
        return requires(Ingredient.of(tag), ItemStack.EMPTY);
    }

    public ShapelessRemainderRecipeBuilder requires(Item item) {
        return requires(Ingredient.of(item), ItemStack.EMPTY);
    }

    public ShapelessRemainderRecipeBuilder requires(Ingredient ingredient, ItemStack remainder) {
        ingredients.add(ingredient);
        remainders.add(remainder);
        return this;
    }

    public ShapelessRemainderRecipeBuilder requires(Ingredient ingredient, Item remainder) {
        return requires(ingredient, new ItemStack(remainder));
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        output.accept(HHFinishedRecipe.of(id, HHModRecipeSerializers.SHAPELESS_REMAINDER.get(), json -> {
            if (group != null && !group.isEmpty()) json.addProperty("group", group);
            json.addProperty("category", "misc");
            JsonArray array = new JsonArray();
            ingredients.forEach(ingredient -> array.add(ingredient.toJson()));
            json.add("ingredients", array);
            JsonArray leftovers = new JsonArray();
            remainders.forEach(stack -> leftovers.add(stack.isEmpty() ? new com.google.gson.JsonObject() : HHFinishedRecipe.stack(stack)));
            json.add("remainders", leftovers);
            json.add("result", HHFinishedRecipe.stack(result));
        }, criteria));
    }
}