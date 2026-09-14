package alabaster.hearthandharvest.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class CleaverItem extends KnifeItem {

    public CleaverItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack remainder = stack.copy();
        remainder.setCount(1);
        if (remainder.isDamageableItem()) {
            remainder.setDamageValue(remainder.getDamageValue() + 1);
            if (remainder.getDamageValue() >= remainder.getMaxDamage()) {
                return ItemStack.EMPTY;
            }
        }
        return remainder;
    }
}