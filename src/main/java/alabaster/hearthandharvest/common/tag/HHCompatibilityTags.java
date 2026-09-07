package alabaster.hearthandharvest.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class HHCompatibilityTags {

    // Serene Seasons
    public static final String SERENE_SEASONS = "sereneseasons";
    public static final TagKey<Block> SERENE_SEASONS_UNBREAKABLE_INFERTILE_CROPS = externalBlockTag(SERENE_SEASONS, "unbreakable_infertile_crops");

    // Create
    public static final String CREATE = "create";
    public static final TagKey<Item> CREATE_UPRIGHT_ON_BELT = externalItemTag(CREATE, "upright_on_belt");
    public static final TagKey<Block> CREATE_BRITTLE = externalBlockTag(CREATE, "brittle");

    // Supplementaries
    public static final String SUPPLEMENTARIES = "supplementaries";
    public static final TagKey<Item> SUPPLEMENTARIES_JAR_COOKIES = externalItemTag(SUPPLEMENTARIES, "jar_cookies");

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modId, path));
    }

    private static TagKey<Block> externalBlockTag(String modId, String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(modId, path));
    }
}