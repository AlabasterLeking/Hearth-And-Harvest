package alabaster.hearthandharvest.common.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidInput {
    @Nullable
    private final Fluid fluid;
    @Nullable
    private final TagKey<Fluid> tag;
    private final int amount;

    private FluidInput(@Nullable Fluid fluid, @Nullable TagKey<Fluid> tag, int amount) {
        this.fluid = fluid;
        this.tag = tag;
        this.amount = amount;
    }

    public static FluidInput of(FluidStack stack) {
        return new FluidInput(stack.getFluid(), null, stack.getAmount());
    }

    public int amount() {
        return amount;
    }

    public FluidInput ingredient() {
        return this;
    }

    public boolean test(FluidStack stack) {
        if (stack.isEmpty()) return false;
        Fluid candidate = stack.getFluid();
        boolean matches = tag != null ? candidate.is(tag) : candidate.isSame(fluid);
        return matches && stack.getAmount() >= amount;
    }

    public boolean testFluid(FluidStack stack) {
        if (stack.isEmpty()) return false;
        return tag != null ? stack.getFluid().is(tag) : stack.getFluid().isSame(fluid);
    }

    public FluidStack[] getFluids() {
        List<FluidStack> stacks = new ArrayList<>();
        if (tag != null) {
            BuiltInRegistries.FLUID.getTagOrEmpty(tag).forEach(holder -> {
                if (holder.value().isSource(holder.value().defaultFluidState())) stacks.add(new FluidStack(holder.value(), amount));
            });
        } else if (fluid != null) {
            stacks.add(new FluidStack(fluid, amount));
        }
        return stacks.toArray(FluidStack[]::new);
    }

    public static FluidInput fromJson(JsonObject json) {
        int amount = GsonHelper.getAsInt(json, "amount", 1000);
        if (json.has("tag")) {
            return new FluidInput(null, TagKey.create(Registries.FLUID, new ResourceLocation(GsonHelper.getAsString(json, "tag"))), amount);
        }
        String id = json.has("fluid") ? GsonHelper.getAsString(json, "fluid") : GsonHelper.getAsString(json, "id");
        Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(id));
        if (fluid == Fluids.EMPTY) throw new JsonParseException("Unknown fluid '" + id + "'");
        return new FluidInput(fluid, null, amount);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        if (tag != null) json.addProperty("tag", tag.location().toString());
        else json.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
        return json;
    }

    public static FluidInput of(TagKey<Fluid> tag, int amount) {
        return new FluidInput(null, tag, amount);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeBoolean(tag != null);
        if (tag != null) buf.writeResourceLocation(tag.location());
        else buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(fluid));
        buf.writeVarInt(amount);
    }

    public static FluidInput fromNetwork(FriendlyByteBuf buf) {
        boolean isTag = buf.readBoolean();
        ResourceLocation id = buf.readResourceLocation();
        int amount = buf.readVarInt();
        return isTag
                ? new FluidInput(null, TagKey.create(Registries.FLUID, id), amount)
                : new FluidInput(BuiltInRegistries.FLUID.get(id), null, amount);
    }
}
