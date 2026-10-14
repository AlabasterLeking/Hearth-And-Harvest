package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.enchantment.HoeEnchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HHModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, HearthAndHarvest.MODID);

    public static final RegistryObject<Enchantment> TILLING = ENCHANTMENTS.register("tilling", HoeEnchantment::new);
    public static final RegistryObject<Enchantment> HARVESTING = ENCHANTMENTS.register("harvesting", HoeEnchantment::new);
}
