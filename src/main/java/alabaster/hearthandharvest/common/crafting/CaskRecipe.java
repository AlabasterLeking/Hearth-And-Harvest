package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class CaskRecipe implements Recipe<RecipeWrapper>
{
    public static final int INPUT_SLOTS = 4;

    private final ResourceLocation id;
    private final CaskRecipeBookTab tab;
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final float experience;
    private final int cookTime;

    public CaskRecipe(ResourceLocation id, @Nullable CaskRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, float experience, int cookTime) {
        this.id = id;
        this.tab = tab;
        this.inputItems = inputItems;
        this.output = output;
        this.experience = experience;
        this.cookTime = cookTime;
    }

    @Nullable
    public CaskRecipeBookTab getRecipeBookTab() {
        return this.tab;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputItems;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < INPUT_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.inputItems.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.AGING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HHModRecipeTypes.AGING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(HHModItems.CASK.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaskRecipe that = (CaskRecipe) o;

        if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
        if (getCookTime() != that.getCookTime()) return false;
        if (tab != that.tab) return false;
        if (!inputItems.equals(that.inputItems)) return false;
        if (!output.equals(that.output)) return false;
        else return true;
    }

    @Override
    public int hashCode() {
        int result = getGroup().hashCode();
        result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
        result = 31 * result + inputItems.hashCode();
        result = 31 * result + output.hashCode();
        result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
        result = 31 * result + getCookTime();
        return result;
    }

    public static class Serializer implements RecipeSerializer<CaskRecipe>
    {
        @Override
        public CaskRecipe fromJson(ResourceLocation id, JsonObject json) {
            CaskRecipeBookTab tab = CaskRecipeBookTab.findByName(GsonHelper.getAsString(json, "recipe_book_tab", ""));
            NonNullList<Ingredient> inputs = RecipeJson.ingredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (inputs.isEmpty()) throw new JsonParseException("No ingredients for aging recipe");
            if (inputs.size() > INPUT_SLOTS) throw new JsonParseException("Too many ingredients for aging recipe! The max is " + INPUT_SLOTS);
            ItemStack output = RecipeJson.itemStack(json.get("result"));
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int cookTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            return new CaskRecipe(id, tab, inputs, output, experience, cookTime);
        }

        @Override
        public CaskRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            CaskRecipeBookTab tabIn = CaskRecipeBookTab.findByName(buffer.readUtf());
            NonNullList<Ingredient> inputItemsIn = RecipeJson.readIngredients(buffer);
            ItemStack outputIn = buffer.readItem();
            float experienceIn = buffer.readFloat();
            int cookTimeIn = buffer.readVarInt();
            return new CaskRecipe(id, tabIn, inputItemsIn, outputIn, experienceIn, cookTimeIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CaskRecipe recipe) {
            buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            RecipeJson.writeIngredients(buffer, recipe.inputItems);
            buffer.writeItem(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
