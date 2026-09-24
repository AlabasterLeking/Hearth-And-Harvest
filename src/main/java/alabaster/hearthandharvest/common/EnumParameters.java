package alabaster.hearthandharvest.common;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class EnumParameters
{
    public static final EnumProxy<RecipeBookCategories> PROXY_FERMENTING_SEARCH = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_FERMENTING_MEALS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.PICKLED_CARROTS.get()))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_FERMENTING_DRINKS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.RED_GRAPE_WINE.get()))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_FERMENTING_MISC = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.CHEDDAR_CHEESE_WHEEL.get()))
    );

    public static final EnumProxy<RecipeBookCategories> PROXY_AGING_SEARCH = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_AGING_MEALS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.CHEDDAR_CHEESE_WHEEL.get()))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_AGING_DRINKS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.RED_GRAPE_WINE.get()))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_AGING_MISC = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(HHModItems.JERKY.get()), new ItemStack(HHModItems.PICKLED_CARROTS.get()))
    );
}