package alabaster.hearthandharvest.common.item;

import net.minecraft.world.item.ItemStack;

public interface AgeableItem {
    boolean isAgeable();

    boolean canAgeFurther(ItemStack stack);
}