package alabaster.hearthandharvest.data.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public record HHFinishedRecipe(ResourceLocation id, RecipeSerializer<?> serializer, Consumer<JsonObject> writer,
                               @Nullable Advancement.Builder advancement, @Nullable ResourceLocation advancementId) implements FinishedRecipe {

    public static HHFinishedRecipe of(ResourceLocation id, RecipeSerializer<?> serializer, Consumer<JsonObject> writer,
                                      Map<String, CriterionTriggerInstance> criteria) {
        if (criteria.isEmpty()) return new HHFinishedRecipe(id, serializer, writer, null, null);
        Advancement.Builder builder = Advancement.Builder.recipeAdvancement()
                .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);
        criteria.forEach(builder::addCriterion);
        return new HHFinishedRecipe(id, serializer, writer, builder, id.withPrefix("recipes/"));
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        writer.accept(json);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return serializer;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return advancement == null ? null : advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return advancementId;
    }

    public static JsonObject stack(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        CompoundTag tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            JsonObject components = new JsonObject();
            for (String key : tag.getAllKeys()) {
                JsonElement value = NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tag.get(key));
                components.add(key, value);
            }
            json.add("components", components);
        }
        return json;
    }

    public static JsonObject fluid(FluidStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", stack.getAmount());
        json.addProperty("id", BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
        return json;
    }
}
