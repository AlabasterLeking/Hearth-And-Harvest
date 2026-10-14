package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BottleCrateRecipe extends CustomRecipe {
    private final Ingredient input;
    private final ItemStack result;

    public BottleCrateRecipe(ResourceLocation id, CraftingBookCategory category, Ingredient input, ItemStack result) {
        super(id, category);
        this.input = input;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (container.getContainerSize() != 9) return false;

        for (int index = 0; index < container.getContainerSize(); ++index) {
            ItemStack stack = container.getItem(index);
            if (stack.isEmpty() || !input.test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return new ItemStack(result.getItem());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        return remainders;
    }

    public int getWidth() { return 3; }
    public int getHeight() { return 3; }

    @Override public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(9, input);
        return list;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.BOTTLE_CRATE.get();
    }

    @Override
    public boolean isSpecial() {
        return false; // allow recipe book discovery
    }

    // --- Serializer ---

    public static class Serializer implements RecipeSerializer<BottleCrateRecipe> {
        @Override
        public BottleCrateRecipe fromJson(ResourceLocation id, JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new BottleCrateRecipe(id, category, Ingredient.fromJson(json.get("input"), false), RecipeJson.itemStack(json.get("result")));
        }

        @Override
        public BottleCrateRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return new BottleCrateRecipe(id, category, input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BottleCrateRecipe recipe) {
            buf.writeEnum(recipe.category());
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.result);
        }
    }
}
