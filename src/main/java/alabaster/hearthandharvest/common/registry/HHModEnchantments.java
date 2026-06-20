package alabaster.hearthandharvest.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class HHModEnchantments {
    public static final ResourceKey<Enchantment> TILLING =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("hearthandharvest", "tilling"));
    public static final ResourceKey<Enchantment> HARVESTING =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("hearthandharvest", "harvesting"));
}