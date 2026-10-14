package alabaster.hearthandharvest.common.item;

import net.minecraft.world.item.ItemStack;

public interface AgeableItem {
    boolean canAgeFurther(ItemStack stack);
}