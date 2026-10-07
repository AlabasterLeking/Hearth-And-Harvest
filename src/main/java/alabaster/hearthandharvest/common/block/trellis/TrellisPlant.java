package alabaster.hearthandharvest.common.block.trellis;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;

public enum TrellisPlant implements StringRepresentable {
    NONE, VINE, ROSE, RED_GRAPE, GREEN_GRAPE, HOPS;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public boolean isGrape() {
        return this == RED_GRAPE || this == GREEN_GRAPE;
    }

    public boolean isFruiting() {
        return isGrape() || this == HOPS;
    }

    public boolean usesAge() {
        return isFruiting();
    }

    public Item getProduce() {
        return switch (this) {
            case RED_GRAPE -> HHModItems.RED_GRAPES.get();
            case GREEN_GRAPE -> HHModItems.GREEN_GRAPES.get();
            case HOPS -> HHModItems.HOPS.get();
            default -> Items.AIR;
        };
    }

    @Nullable
    public static TrellisPlant fruitingFrom(ItemStack stack) {
        for (TrellisPlant plant : values()) {
            if (plant.isFruiting() && stack.is(plant.getProduce())) return plant;
        }
        return null;
    }
}