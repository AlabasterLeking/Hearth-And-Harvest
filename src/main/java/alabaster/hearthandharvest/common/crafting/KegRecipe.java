package alabaster.hearthandharvest.common.crafting;

import alabaster.hearthandharvest.client.recipebook.CaskRecipeBookTab;
import alabaster.hearthandharvest.common.registry.HHModRecipeSerializers;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KegRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_TIME = 600;

    private final CaskRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients;
    @Nullable
    private final SizedFluidIngredient inputFluid;
    private final FluidStack resultFluid;
    private final ItemStack resultItem;
    private final int fermentTime;
    private final float experience;

    public KegRecipe(CaskRecipeBookTab tab, NonNullList<Ingredient> ingredients, @Nullable SizedFluidIngredient inputFluid, FluidStack resultFluid, ItemStack resultItem, int fermentTime, float experience) {
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
    public SizedFluidIngredient getInputFluid() {
        return inputFluid;
    }

    public int getInputAmount() {
        return inputFluid == null ? 0 : inputFluid.amount();
    }

    public List<FluidStack> getInputFluidStacks() {
        return inputFluid == null ? List.of() : List.of(inputFluid.getFluids());
    }

    public boolean acceptsFluid(FluidStack stack) {
        return inputFluid != null && !stack.isEmpty() && inputFluid.ingredient().test(stack);
    }

    public List<SlotIngredient> getSlotIngredients() {
        List<SlotIngredient> grouped = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            int index = -1;
            for (int i = 0; i < grouped.size(); i++) {
                if (grouped.get(i).ingredient().equals(ingredient)) {
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
    public ItemStack assemble(RecipeWrapper wrapper, HolderLookup.Provider registries) {
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
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

        private static final Codec<SizedFluidIngredient> INPUT_FLUID_CODEC = Codec.withAlternative(
                SizedFluidIngredient.FLAT_CODEC,
                FluidStack.CODEC.xmap(SizedFluidIngredient::of, ingredient -> ingredient.getFluids()[0]));

        private static final MapCodec<KegRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CaskRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab")
                                .xmap(optional -> optional.orElse(null), java.util.Optional::ofNullable)
                                .forGetter(KegRecipe::getRecipeBookTab),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .optionalFieldOf("ingredients", List.of())
                                .xmap(list -> {
                                    NonNullList<Ingredient> nnList = NonNullList.create();
                                    nnList.addAll(list);
                                    return nnList;
                                }, nnList -> nnList)
                                .forGetter(KegRecipe::getItemIngredients),
                        INPUT_FLUID_CODEC
                                .optionalFieldOf("input_fluid")
                                .forGetter(recipe -> Optional.ofNullable(recipe.getInputFluid())),
                        FluidStack.CODEC
                                .optionalFieldOf("result_fluid", FluidStack.EMPTY)
                                .forGetter(KegRecipe::getResultFluid),
                        ItemStack.OPTIONAL_CODEC
                                .optionalFieldOf("result_item", ItemStack.EMPTY)
                                .forGetter(KegRecipe::getResultItem),
                        com.mojang.serialization.Codec.INT
                                .optionalFieldOf("ferment_time", DEFAULT_TIME)
                                .forGetter(KegRecipe::getFermentTime),
                        com.mojang.serialization.Codec.FLOAT
                                .optionalFieldOf("experience", 0.0F)
                                .forGetter(KegRecipe::getExperience)
                ).apply(instance, (tab, ingredients, input, resultFluid, resultItem, time, experience) ->
                        new KegRecipe(tab, ingredients, input.orElse(null), resultFluid, resultItem, time, experience))
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, KegRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<KegRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, KegRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static KegRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            CaskRecipeBookTab tab = CaskRecipeBookTab.findByName(buf.readUtf());
            int count = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));

            SizedFluidIngredient inputFluid = buf.readBoolean() ? SizedFluidIngredient.STREAM_CODEC.decode(buf) : null;
            FluidStack resultFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack resultItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            int time = ByteBufCodecs.VAR_INT.decode(buf);
            float experience = buf.readFloat();
            return new KegRecipe(tab, ingredients, inputFluid, resultFluid, resultItem, time, experience);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, KegRecipe recipe) {
            buf.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            buf.writeBoolean(recipe.inputFluid != null);
            if (recipe.inputFluid != null) {
                SizedFluidIngredient.STREAM_CODEC.encode(buf, recipe.inputFluid);
            }
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultFluid);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultItem);
            ByteBufCodecs.VAR_INT.encode(buf, recipe.fermentTime);
            buf.writeFloat(recipe.experience);
        }
    }
}