package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;

import alabaster.hearthandharvest.common.crafting.FermentingRecipe;
import alabaster.hearthandharvest.common.crafting.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, HearthAndHarvest.MODID);

    public static final Supplier<RecipeSerializer<?>> AGING =
            RECIPE_SERIALIZERS.register("aging", CaskRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> STOMPING =
            RECIPE_SERIALIZERS.register("stomping", StompingBasinRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<?>> FERMENTING =
            RECIPE_SERIALIZERS.register("fermenting", FermentingRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> BOTTLE_CRATE =
            RECIPE_SERIALIZERS.register("bottle_crate", BottleCrateRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> SHAPELESS_REMAINDER =
            RECIPE_SERIALIZERS.register("shapeless_remainder", ShapelessRemainderRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> SALTING =
            RECIPE_SERIALIZERS.register("salting", SaltingRecipe.Serializer::new);

}