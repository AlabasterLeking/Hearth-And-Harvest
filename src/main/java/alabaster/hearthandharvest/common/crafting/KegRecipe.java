package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import alabaster.hearthandharvest.common.fluid.HHFluidHandling;
import alabaster.hearthandharvest.common.registry.HHDataMaps;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KegRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_TIME = 600;

    private final ResourceLocation id;
    private final CaskRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients;
    @Nullable
    private final FluidInput inputFluid;
    private final FluidStack resultFluid;
    private final ItemStack resultItem;
    private final int fermentTime;
    private final float experience;

    public KegRecipe(ResourceLocation id, CaskRecipeBookTab tab, NonNullList<Ingredient> ingredients, @Nullable FluidInput inputFluid, FluidStack resultFluid, ItemStack resultItem, int fermentTime, float experience) {
        this.id = id;
        this.tab = tab;
        this.ingredients = ingredients;
        this.inputFluid = inputFluid;
        this.resultFluid = resultFluid;
        this.resultItem = resultItem;
        this.fermentTime = fermentTime;
        this.experience = experience;
    }

    @Nullable
    public CaskRecipeBookTab getRecipeBookTab() {
        return tab;
    }

    @Nullable
    public FluidInput getInputFluid() {
        return inputFluid;
    }

    public int getInputAmount() {
        return inputFluid == null ? 0 : inputFluid.amount();
    }

    public List<FluidStack> getInputFluidStacks() {
        return inputFluid == null ? List.of() : List.of(inputFluid.getFluids());
    }

    public boolean acceptsFluid(FluidStack stack) {
        return inputFluid != null && !stack.isEmpty() && inputFluid.testFluid(stack);
    }

    public List<SlotIngredient> getSlotIngredients() {
        List<SlotIngredient> grouped = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            int index = -1;
            for (int i = 0; i < grouped.size(); i++) {
                if (sameIngredient(grouped.get(i).ingredient(), ingredient)) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                grouped.add(new SlotIngredient(ingredient, 1));
            } else {
                grouped.set(index, new SlotIngredient(ingredient, grouped.get(index).count() + 1));
            }
        }
        return grouped;
    }

    private static boolean sameIngredient(Ingredient a, Ingredient b) {
        return a == b || a.toJson().equals(b.toJson());
    }

    public FluidStack getResultFluid() {
        return resultFluid;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    public int getFermentTime() {
        return fermentTime;
    }

    public float getExperience() {
        return experience;
    }

    public boolean matchesFluid(FluidStack available) {
        if (inputFluid == null) return true;
        return inputFluid.test(available);
    }

    public boolean matchesItems(List<ItemStack> available) {
        return getItemUsage(available) != null;
    }

    @Nullable
    public int[] getItemUsage(List<ItemStack> available) {
        int[] remaining = new int[available.size()];
        for (int i = 0; i < available.size(); i++) {
            remaining[i] = available.get(i).getCount();
        }
        if (!assign(0, available, remaining)) return null;

        int[] used = new int[available.size()];
        for (int i = 0; i < available.size(); i++) {
            used[i] = available.get(i).getCount() - remaining[i];
        }
        return used;
    }

    private boolean assign(int index, List<ItemStack> available, int[] remaining) {
        if (index >= ingredients.size()) return true;

        Ingredient ingredient = ingredients.get(index);
        for (int slot = 0; slot < available.size(); slot++) {
            if (remaining[slot] <= 0 || !ingredient.test(available.get(slot))) continue;

            remaining[slot]--;
            if (assign(index + 1, available, remaining)) return true;
            remaining[slot]++;
        }
        return false;
    }

    @Override
    public boolean matches(RecipeWrapper wrapper, Level level) {
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
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getDisplayResult();
    }

    public ItemStack getDisplayResult() {
        if (!resultFluid.isEmpty()) {
            Item bottle = HHDataMaps.getBottleForFluid(resultFluid.getFluid());
            if (bottle != null) return new ItemStack(bottle);
        }
        return resultItem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        Ingredient container = fluidContainerIngredient();
        if (container.isEmpty()) return ingredients;
        NonNullList<Ingredient> withContainer = NonNullList.create();
        withContainer.addAll(ingredients);
        withContainer.add(container);
        return withContainer;
    }

    public NonNullList<Ingredient> getItemIngredients() {
        return ingredients;
    }

    private Ingredient fluidContainerIngredient() {
        if (inputFluid == null) return Ingredient.EMPTY;
        List<ItemStack> containers = new ArrayList<>();
        for (FluidStack fluid : inputFluid.getFluids()) {
            ItemStack container = containerFor(fluid.getFluid());
            if (!container.isEmpty()) containers.add(container);
            Item bucket = fluid.getFluid().getBucket();
            if (bucket != Items.AIR && !container.is(bucket)) containers.add(new ItemStack(bucket));
        }
        return containers.isEmpty() ? Ingredient.EMPTY : Ingredient.of(containers.stream());
    }

    public static ItemStack containerFor(Fluid fluid) {
        if (fluid.isSame(Fluids.WATER)) return HHFluidHandling.waterBottle();
        Item bottle = HHDataMaps.getBottleForFluid(fluid);
        if (bottle != null) return new ItemStack(bottle);
        Item bucket = fluid.getBucket();
        return bucket == Items.AIR ? ItemStack.EMPTY : new ItemStack(bucket);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.FERMENTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HHModRecipeTypes.FERMENTING.get();
    }

    public record SlotIngredient(Ingredient ingredient, int count) {
    }

    public static class Serializer implements RecipeSerializer<KegRecipe> {
        @Override
        public KegRecipe fromJson(ResourceLocation id, JsonObject json) {
            CaskRecipeBookTab tab = CaskRecipeBookTab.findByName(GsonHelper.getAsString(json, "recipe_book_tab", ""));
            NonNullList<Ingredient> ingredients = json.has("ingredients") ? RecipeJson.ingredients(GsonHelper.getAsJsonArray(json, "ingredients")) : NonNullList.create();
            FluidInput input = json.has("input_fluid") ? FluidInput.fromJson(GsonHelper.getAsJsonObject(json, "input_fluid")) : null;
            FluidStack resultFluid = json.has("result_fluid") ? RecipeJson.fluidStack(GsonHelper.getAsJsonObject(json, "result_fluid")) : FluidStack.EMPTY;
            ItemStack resultItem = json.has("result_item") ? RecipeJson.itemStack(json.get("result_item")) : ItemStack.EMPTY;
            int time = GsonHelper.getAsInt(json, "ferment_time", DEFAULT_TIME);
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            return new KegRecipe(id, tab, ingredients, input, resultFluid, resultItem, time, experience);
        }

        @Override
        public KegRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CaskRecipeBookTab tab = CaskRecipeBookTab.findByName(buf.readUtf());
            NonNullList<Ingredient> ingredients = RecipeJson.readIngredients(buf);
            FluidInput inputFluid = buf.readBoolean() ? FluidInput.fromNetwork(buf) : null;
            FluidStack resultFluid = FluidStack.readFromPacket(buf);
            ItemStack resultItem = buf.readItem();
            int time = buf.readVarInt();
            float experience = buf.readFloat();
            return new KegRecipe(id, tab, ingredients, inputFluid, resultFluid, resultItem, time, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, KegRecipe recipe) {
            buf.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            RecipeJson.writeIngredients(buf, recipe.ingredients);
            buf.writeBoolean(recipe.inputFluid != null);
            if (recipe.inputFluid != null) recipe.inputFluid.toNetwork(buf);
            recipe.resultFluid.writeToPacket(buf);
            buf.writeItem(recipe.resultItem);
            buf.writeVarInt(recipe.fermentTime);
            buf.writeFloat(recipe.experience);
        }
    }
}