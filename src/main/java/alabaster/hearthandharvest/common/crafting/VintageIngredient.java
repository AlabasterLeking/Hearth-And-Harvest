package alabaster.hearthandharvest.common.crafting;

import alabaster.hearthandharvest.common.item.AgeableItem;
import alabaster.hearthandharvest.common.item.VintageHelper;
import alabaster.hearthandharvest.common.registry.HHModIngredients;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.stream.Stream;

public class VintageIngredient implements ICustomIngredient {
    public static final MapCodec<VintageIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("base").forGetter(ingredient -> ingredient.base),
                    Codec.INT.fieldOf("vintage").forGetter(ingredient -> ingredient.vintage)
            ).apply(instance, VintageIngredient::new)
    );

    private final Ingredient base;
    private final int vintage;

    public VintageIngredient(Ingredient base, int vintage) {
        this.base = base;
        this.vintage = vintage;
    }

    public VintageIngredient(Item item, int vintage) {
        this(Ingredient.of(item), vintage);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!base.test(stack) || VintageHelper.getVintage(stack) != vintage) return false;
        return !(stack.getItem() instanceof AgeableItem ageable) || ageable.canAgeFurther(stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Stream.of(base.getItems()).map(stack -> VintageHelper.withVintage(stack.copy(), vintage));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return HHModIngredients.VINTAGE.get();
    }

    public Ingredient toVanilla() {
        return new Ingredient(this);
    }
}