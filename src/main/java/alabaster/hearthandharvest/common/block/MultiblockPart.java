package alabaster.hearthandharvest.common.block;

import net.minecraft.util.StringRepresentable;

public enum MultiblockPart implements StringRepresentable {
    NONE("none"),
    CONTROLLER("controller"),
    MEMBER("member");

    private final String name;

    MultiblockPart(String name) { this.name = name; }

    @Override
    public String getSerializedName() { return name; }

    public static MultiblockPart byName(String name) {
        for (MultiblockPart part : values()) {
            if (part.name.equals(name)) return part;
        }
        return NONE;
    }
}