package alabaster.hearthandharvest.common.migration;

import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class LegacyCrates {
    public static final Map<String, Supplier<ItemStack>> CONTENTS = new LinkedHashMap<>();

    static {
        CONTENTS.put("milk_crate", () -> new ItemStack(ModItems.MILK_BOTTLE.get()));
        CONTENTS.put("goat_milk_crate", () -> new ItemStack(HHModItems.GOAT_MILK_BOTTLE.get()));
        CONTENTS.put("water_crate", () -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
        CONTENTS.put("egg_crate", () -> new ItemStack(Items.EGG));
        CONTENTS.put("turtle_egg_crate", () -> new ItemStack(Items.TURTLE_EGG));
        CONTENTS.put("honey_crate", () -> new ItemStack(Items.HONEY_BOTTLE));
        CONTENTS.put("syrup_crate", () -> new ItemStack(HHModItems.SYRUP_BOTTLE.get()));
        CONTENTS.put("mead_crate", () -> new ItemStack(HHModItems.MEAD.get()));
        CONTENTS.put("blueberry_wine_crate", () -> new ItemStack(HHModItems.BLUEBERRY_WINE.get()));
        CONTENTS.put("cherry_wine_crate", () -> new ItemStack(HHModItems.CHERRY_WINE.get()));
        CONTENTS.put("green_grape_wine_crate", () -> new ItemStack(HHModItems.GREEN_GRAPE_WINE.get()));
        CONTENTS.put("raspberry_wine_crate", () -> new ItemStack(HHModItems.RASPBERRY_WINE.get()));
        CONTENTS.put("red_grape_wine_crate", () -> new ItemStack(HHModItems.RED_GRAPE_WINE.get()));
        CONTENTS.put("sweet_berry_wine_crate", () -> new ItemStack(HHModItems.SWEET_BERRY_WINE.get()));
    }

    private LegacyCrates() {
    }

    public static CompoundTag contentsTag(Supplier<ItemStack> content, int count) {
        NonNullList<ItemStack> items = NonNullList.withSize(CrateBlockEntity.TOTAL_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(count, CrateBlockEntity.TOTAL_SLOTS); i++) {
            items.set(i, content.get());
        }
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items);
        return tag;
    }

    public static ItemStack filledCrateItem(Supplier<ItemStack> content, int count) {
        ItemStack crate = new ItemStack(HHModItems.CRATE.get(), count);
        BlockItem.setBlockEntityData(crate, HHModBlockEntities.CRATE.get(), contentsTag(content, CrateBlockEntity.SLOTS_PER_HALF));
        return crate;
    }
}
