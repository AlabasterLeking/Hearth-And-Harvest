package alabaster.hearthandharvest.common.crafting;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.item.AgeableItem;
import alabaster.hearthandharvest.common.item.VintageHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

public class VintageIngredient extends AbstractIngredient {
    public static final ResourceLocation ID = new ResourceLocation(HearthAndHarvest.MODID, "vintage");
    public static final Serializer SERIALIZER = new Serializer();

    private final Ingredient base;
    private final int vintage;
    private ItemStack[] cachedItems;

    public VintageIngredient(Ingredient base, int vintage) {
        super(Stream.empty());
        this.base = base;
        this.vintage = vintage;
    }

    public VintageIngredient(Item item, int vintage) {
        this(Ingredient.of(item), vintage);
    }

    public static void register() {
        CraftingHelper.register(ID, SERIALIZER);
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || !base.test(stack) || VintageHelper.getVintage(stack) != vintage) return false;
        return !(stack.getItem() instanceof AgeableItem ageable) || ageable.canAgeFurther(stack);
    }

    @Override
    public ItemStack[] getItems() {
        if (cachedItems == null) {
            cachedItems = Arrays.stream(base.getItems()).map(stack -> VintageHelper.withVintage(stack.copy(), vintage)).toArray(ItemStack[]::new);
        }
        return cachedItems;
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", ID.toString());
        json.add("base", base.toJson());
        json.addProperty("vintage", vintage);
        return json;
    }

    public Ingredient toVanilla() {
        return this;
    }

    public static class Serializer implements IIngredientSerializer<VintageIngredient> {
        @Override
        public VintageIngredient parse(FriendlyByteBuf buffer) {
            return new VintageIngredient(Ingredient.fromNetwork(buffer), buffer.readVarInt());
        }

        @Override
        public VintageIngredient parse(JsonObject json) {
            return new VintageIngredient(CraftingHelper.getIngredient(json.get("base"), false), GsonHelper.getAsInt(json, "vintage"));
        }

        @Override
        public void write(FriendlyByteBuf buffer, VintageIngredient ingredient) {
            ingredient.base.toNetwork(buffer);
            buffer.writeVarInt(ingredient.vintage);
        }
    }
}
