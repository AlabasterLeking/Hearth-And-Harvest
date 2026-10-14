package alabaster.hearthandharvest.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class HoeEnchantment extends Enchantment {
    public static final EnchantmentCategory HOE = EnchantmentCategory.create("HEARTHANDHARVEST_HOE", item -> item instanceof HoeItem);

    public HoeEnchantment() {
        super(Rarity.UNCOMMON, HOE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 15 + (level - 1) * 9;
    }

    @Override
    public int getMaxCost(int level) {
        return 65 + (level - 1) * 9;
    }
}
