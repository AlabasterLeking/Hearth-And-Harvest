package alabaster.hearthandharvest.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import javax.annotation.Nullable;
import java.util.List;

public class CheeseSliceItem extends ConsumableItem {
    private static final float SATURATION_BONUS_PER_VINTAGE = 0.1F;

    public CheeseSliceItem(Properties properties) {
        super(properties);
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        FoodProperties food = super.getFoodProperties(stack, entity);
        int vintage = VintageHelper.getVintage(stack);
        return food == null || vintage <= 0 ? food : VintageHelper.scaleFood(food, vintage, SATURATION_BONUS_PER_VINTAGE);
    }

    @Override
    public Component getName(ItemStack stack) {
        return VintageHelper.vintageName(stack, super.getName(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        VintageHelper.appendTooltip(stack, tooltip);
        super.appendHoverText(stack, context, tooltip, flag);
    }
}