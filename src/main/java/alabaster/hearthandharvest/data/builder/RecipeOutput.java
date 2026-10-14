package alabaster.hearthandharvest.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class RecipeOutput implements Consumer<FinishedRecipe> {
    private final Consumer<FinishedRecipe> consumer;
    private final ICondition[] conditions;

    public RecipeOutput(Consumer<FinishedRecipe> consumer) {
        this(consumer, new ICondition[0]);
    }

    private RecipeOutput(Consumer<FinishedRecipe> consumer, ICondition[] conditions) {
        this.consumer = consumer;
        this.conditions = conditions;
    }

    public RecipeOutput withConditions(ICondition... conditions) {
        return new RecipeOutput(consumer, conditions);
    }

    @Override
    public void accept(FinishedRecipe recipe) {
        consumer.accept(conditions.length == 0 ? recipe : new Conditional(recipe, conditions));
    }

    private record Conditional(FinishedRecipe delegate, ICondition[] conditions) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            delegate.serializeRecipeData(json);
            JsonArray array = new JsonArray();
            for (ICondition condition : conditions) array.add(CraftingHelper.serialize(condition));
            json.add("conditions", array);
        }

        @Override
        public ResourceLocation getId() {
            return delegate.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return delegate.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return delegate.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return delegate.getAdvancementId();
        }
    }
}
