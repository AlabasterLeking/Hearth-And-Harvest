package alabaster.hearthandharvest.common.block.trellis;

import net.minecraft.util.StringRepresentable;

public enum TrellisShape implements StringRepresentable {
    MIDDLE_EW, MIDDLE_NS,
    SIDE_NORTH, SIDE_SOUTH, SIDE_EAST, SIDE_WEST,
    FLAT, TOP;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
