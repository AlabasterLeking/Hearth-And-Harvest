package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SaltingRecipe implements CraftingRecipe {

    private final ResourceLocation id;
    private final CraftingBookCategory category;
    private final Ingredient saltIngredient;

    public SaltingRecipe(ResourceLocation id, CraftingBookCategory category, Ingredient saltIngredient) {
        this.id = id;
        this.category = category;
        this.saltIngredient = saltIngredient;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    @Override
    public boolean matches(CraftingContainer input, Level level) {
        List<ItemStack> nonEmpty = new ArrayList<>();
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) nonEmpty.add(stack);
        }
        if (nonEmpty.size() != 2) return false;

        boolean hasSalt = false;
        boolean hasFood = false;
        for (ItemStack stack : nonEmpty) {
            if (saltIngredient.test(stack)) {
                hasSalt = true;
            } else if (stack.isEdible()
                    && !HHModDataComponents.SALTED.has(stack)) {
                hasFood = true;
            }
        }
        return hasSalt && hasFood;
    }

    @Override
    public ItemStack assemble(CraftingContainer input, RegistryAccess registryAccess) {
        ItemStack foodStack = ItemStack.EMPTY;
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && !saltIngredient.test(stack) && stack.isEdible()) {
                foodStack = stack;
                break;
            }
        }
        if (foodStack.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = foodStack.copyWithCount(1);
        HHModDataComponents.SALTED.set(result, true);
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && saltIngredient.test(stack) && stack.hasCraftingRemainingItem()) {
                remaining.set(i, stack.getCraftingRemainingItem());
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.SALTING.get();
    }

    public static class Serializer implements RecipeSerializer<SaltingRecipe> {
        @Override
        public SaltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new SaltingRecipe(id, category, Ingredient.fromJson(json.get("salt"), false));
        }

        @Override
        public SaltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new SaltingRecipe(id, buf.readEnum(CraftingBookCategory.class), Ingredient.fromNetwork(buf));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SaltingRecipe recipe) {
            buf.writeEnum(recipe.category);
            recipe.saltIngredient.toNetwork(buf);
        }
    }
}
