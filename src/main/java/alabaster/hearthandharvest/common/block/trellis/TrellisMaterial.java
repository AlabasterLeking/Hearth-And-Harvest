package alabaster.hearthandharvest.common.block.trellis;

import net.minecraft.util.StringRepresentable;

public enum TrellisMaterial implements StringRepresentable {
    STICK("trellis", "trellis", "trellis_flat"),
    BAMBOO("bamboo_trellis", "bamboo_trellis", "bamboo_trellis_flat"),
    STRIPPED_BAMBOO("stripped_bamboo_trellis", "stripped_bamboo_trellis", "stripped_bamboo_trellis_flat");

    public final String id;
    public final String woodTexture;
    public final String flatTexture;

    TrellisMaterial(String id, String woodTexture, String flatTexture) {
        this.id = id;
        this.woodTexture = woodTexture;
        this.flatTexture = flatTexture;
    }

    @Override
    public String getSerializedName() {
        return id;
    }
}
