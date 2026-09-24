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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class FermentingRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_TIME = 600;

    private final CaskRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients;
    private final FluidStack inputFluid;
    private final FluidStack resultFluid;
    private final ItemStack resultItem;
    private final int fermentTime;
    private final float experience;

    public FermentingRecipe(CaskRecipeBookTab tab, NonNullList<Ingredient> ingredients, FluidStack inputFluid, FluidStack resultFluid, ItemStack resultItem, int fermentTime, float experience) {
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

    public FluidStack getInputFluid() {
        return inputFluid;
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
        if (inputFluid.isEmpty()) return true;
        return FluidStack.isSameFluidSameComponents(available, inputFluid) && available.getAmount() >= inputFluid.getAmount();
    }

    public boolean matchesItems(List<ItemStack> available) {
        if (ingredients.isEmpty()) return true;

        int[] remaining = new int[available.size()];
        for (int i = 0; i < available.size(); i++) {
            remaining[i] = available.get(i).getCount();
        }
        return assign(0, available, remaining);
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
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return resultItem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HHModRecipeSerializers.FERMENTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HHModRecipeTypes.FERMENTING.get();
    }

    public static class Serializer implements RecipeSerializer<FermentingRecipe> {

        private static final MapCodec<FermentingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CaskRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab")
                                .xmap(optional -> optional.orElse(null), java.util.Optional::ofNullable)
                                .forGetter(FermentingRecipe::getRecipeBookTab),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .optionalFieldOf("ingredients", List.of())
                                .xmap(list -> {
                                    NonNullList<Ingredient> nnList = NonNullList.create();
                                    nnList.addAll(list);
                                    return nnList;
                                }, nnList -> nnList)
                                .forGetter(FermentingRecipe::getIngredients),
                        FluidStack.CODEC
                                .optionalFieldOf("input_fluid", FluidStack.EMPTY)
                                .forGetter(FermentingRecipe::getInputFluid),
                        FluidStack.CODEC
                                .optionalFieldOf("result_fluid", FluidStack.EMPTY)
                                .forGetter(FermentingRecipe::getResultFluid),
                        ItemStack.OPTIONAL_CODEC
                                .optionalFieldOf("result_item", ItemStack.EMPTY)
                                .forGetter(FermentingRecipe::getResultItem),
                        com.mojang.serialization.Codec.INT
                                .optionalFieldOf("ferment_time", DEFAULT_TIME)
                                .forGetter(FermentingRecipe::getFermentTime),
                        com.mojang.serialization.Codec.FLOAT
                                .optionalFieldOf("experience", 0.0F)
                                .forGetter(FermentingRecipe::getExperience)
                ).apply(instance, FermentingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FermentingRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<FermentingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FermentingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FermentingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            CaskRecipeBookTab tab = CaskRecipeBookTab.findByName(buf.readUtf());
            int count = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));

            FluidStack inputFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
            FluidStack resultFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack resultItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            int time = ByteBufCodecs.VAR_INT.decode(buf);
            float experience = buf.readFloat();
            return new FermentingRecipe(tab, ingredients, inputFluid, resultFluid, resultItem, time, experience);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, FermentingRecipe recipe) {
            buf.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.inputFluid);
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultFluid);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultItem);
            ByteBufCodecs.VAR_INT.encode(buf, recipe.fermentTime);
            buf.writeFloat(recipe.experience);
        }
    }
}