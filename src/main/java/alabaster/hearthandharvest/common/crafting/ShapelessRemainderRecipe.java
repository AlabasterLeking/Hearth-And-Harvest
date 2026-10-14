package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;


public class ShapelessRemainderRecipe extends ShapelessRecipe {

    final ItemStack result;
    private final NonNullList<ItemStack> remainders;

    public ShapelessRemainderRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, NonNullList<ItemStack> remainders) {
        super(id, group, category, result, ingredients);
        this.result = result;
        this.remainders = remainders;
    }

    public NonNullList<ItemStack> getRemainderItems() {
        return remainders;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.getContainerSize(), ItemStack.EMPTY);
        int remainderIndex = 0;
        for (int i = 0; i < input.getContainerSize(); i++) {
            if (!input.getItem(i).isEmpty()) {
                if (remainderIndex < remainders.size()) {
                    remaining.set(i, remainders.get(remainderIndex).copy());
                }
                remainderIndex++;
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.SHAPELESS_REMAINDER.get();
    }

    public static class Serializer implements RecipeSerializer<ShapelessRemainderRecipe> {
        @Override
        public ShapelessRemainderRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            ItemStack result = RecipeJson.itemStack(json.get("result"));
            NonNullList<Ingredient> ingredients = RecipeJson.ingredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            NonNullList<ItemStack> remainders = NonNullList.create();
            for (var element : GsonHelper.getAsJsonArray(json, "remainders")) {
                boolean empty = element.isJsonObject() && element.getAsJsonObject().size() == 0;
                remainders.add(empty ? ItemStack.EMPTY : RecipeJson.itemStack(element));
            }
            return new ShapelessRemainderRecipe(id, group, category, result, ingredients, remainders);
        }

        @Override
        public ShapelessRemainderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String group = buf.readUtf();
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            ItemStack result = buf.readItem();
            NonNullList<Ingredient> ingredients = RecipeJson.readIngredients(buf);
            int size = buf.readVarInt();
            NonNullList<ItemStack> remainders = NonNullList.create();
            for (int i = 0; i < size; i++) remainders.add(buf.readItem());
            return new ShapelessRemainderRecipe(id, group, category, result, ingredients, remainders);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessRemainderRecipe recipe) {
            buf.writeUtf(recipe.getGroup());
            buf.writeEnum(recipe.category());
            buf.writeItem(recipe.result);
            RecipeJson.writeIngredients(buf, recipe.getIngredients());
            buf.writeVarInt(recipe.remainders.size());
            for (ItemStack stack : recipe.remainders) buf.writeItem(stack);
        }
    }
}
