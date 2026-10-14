package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import alabaster.hearthandharvest.common.block.entity.StompingBasinBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;


import javax.annotation.Nullable;

public class StompingBasinRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final FluidStack resultFluid;
    private final ItemStack  resultItem;

    public StompingBasinRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack resultFluid, ItemStack resultItem) {
        this.id = id;
        this.ingredients = ingredients;
        this.resultFluid = resultFluid;
        this.resultItem  = resultItem;
    }

    @Override
    public boolean matches(RecipeWrapper wrapper, Level level) {
        return findSlotAssignment(wrapper) != null;
    }

    @Nullable
    public int[] findSlotAssignment(RecipeWrapper wrapper) {
        if (ingredients.isEmpty()) return null;

        int slots = Math.min(wrapper.getContainerSize(), StompingBasinBlockEntity.ITEM_SLOTS);
        ItemStack[] stacks = new ItemStack[slots];
        int[] remaining = new int[slots];
        for (int slot = 0; slot < slots; slot++) {
            stacks[slot] = wrapper.getItem(slot);
            remaining[slot] = stacks[slot].getCount();
        }

        int[] assignment = new int[ingredients.size()];
        return assign(0, stacks, remaining, assignment) ? assignment : null;
    }

    private boolean assign(int index, ItemStack[] stacks, int[] remaining, int[] assignment) {
        if (index >= ingredients.size()) return true;

        Ingredient ingredient = ingredients.get(index);
        for (int slot = 0; slot < stacks.length; slot++) {
            if (remaining[slot] <= 0 || !ingredient.test(stacks[slot])) continue;

            remaining[slot]--;
            assignment[index] = slot;
            if (assign(index + 1, stacks, remaining, assignment)) return true;
            remaining[slot]++;
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack assemble(RecipeWrapper wrapper, RegistryAccess registryAccess) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return resultItem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.STOMPING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HHModRecipeTypes.STOMPING.get();
    }

    public FluidStack getResultFluid() {
        return resultFluid;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    // Serializer
    public static class Serializer implements RecipeSerializer<StompingBasinRecipe> {
        public Serializer() {}

        @Override
        public StompingBasinRecipe fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> ingredients = RecipeJson.ingredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            FluidStack fluid = json.has("result_fluid") ? RecipeJson.fluidStack(GsonHelper.getAsJsonObject(json, "result_fluid")) : FluidStack.EMPTY;
            ItemStack item = json.has("result_item") ? RecipeJson.itemStack(json.get("result_item")) : ItemStack.EMPTY;
            return new StompingBasinRecipe(id, ingredients, fluid, item);
        }

        @Override
        public StompingBasinRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> ingredients = RecipeJson.readIngredients(buf);
            FluidStack fluid = FluidStack.readFromPacket(buf);
            ItemStack item = buf.readItem();
            return new StompingBasinRecipe(id, ingredients, fluid, item);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, StompingBasinRecipe recipe) {
            RecipeJson.writeIngredients(buf, recipe.ingredients);
            recipe.resultFluid.writeToPacket(buf);
            buf.writeItem(recipe.resultItem);
        }
    }
}
