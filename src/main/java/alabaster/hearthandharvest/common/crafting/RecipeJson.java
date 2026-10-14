package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;

public final class RecipeJson {
    private RecipeJson() {
    }

    public static NonNullList<Ingredient> ingredients(JsonArray array) {
        NonNullList<Ingredient> list = NonNullList.create();
        for (JsonElement element : array) {
            Ingredient ingredient = CraftingHelper.getIngredient(element, false);
            if (!ingredient.isEmpty()) list.add(ingredient);
        }
        return list;
    }

    public static ItemStack itemStack(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return new ItemStack(item(element.getAsString()));
        }
        JsonObject json = element.getAsJsonObject();
        String id = json.has("id") ? GsonHelper.getAsString(json, "id") : GsonHelper.getAsString(json, "item");
        ItemStack stack = new ItemStack(item(id), GsonHelper.getAsInt(json, "count", 1));
        if (json.has("components")) {
            CompoundTag tag = stack.getOrCreateTag();
            for (var entry : GsonHelper.getAsJsonObject(json, "components").entrySet()) {
                Tag converted = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, entry.getValue());
                tag.put(entry.getKey(), converted);
            }
        }
        if (json.has("nbt")) {
            stack.getOrCreateTag().merge(CraftingHelper.getNBT(json.get("nbt")));
        }
        return stack;
    }

    public static FluidStack fluidStack(JsonObject json) {
        String id = json.has("id") ? GsonHelper.getAsString(json, "id") : GsonHelper.getAsString(json, "fluid");
        Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(id));
        if (fluid == Fluids.EMPTY) throw new JsonParseException("Unknown fluid '" + id + "'");
        int amount = json.has("amount") ? GsonHelper.getAsInt(json, "amount") : GsonHelper.getAsInt(json, "count", 1000);
        return new FluidStack(fluid, amount);
    }

    public static Item item(String id) {
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(id));
        if (item == Items.AIR && !"minecraft:air".equals(id)) throw new JsonParseException("Unknown item '" + id + "'");
        return item;
    }

    public static void writeIngredients(FriendlyByteBuf buf, NonNullList<Ingredient> ingredients) {
        buf.writeVarInt(ingredients.size());
        for (Ingredient ingredient : ingredients) ingredient.toNetwork(buf);
    }

    public static NonNullList<Ingredient> readIngredients(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        NonNullList<Ingredient> list = NonNullList.withSize(size, Ingredient.EMPTY);
        list.replaceAll(ignored -> Ingredient.fromNetwork(buf));
        return list;
    }
}
