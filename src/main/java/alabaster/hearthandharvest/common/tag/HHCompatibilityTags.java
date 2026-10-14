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

    // Farmer's Delight
    public static final String FARMERS_DELIGHT = "farmersdelight";
    public static final TagKey<Item> FARMERS_DELIGHT_ITEM_CABINETS_WOODEN = externalItemTag(FARMERS_DELIGHT, "cabinets/wooden");
    public static final TagKey<Item> FARMERS_DELIGHT_ITEM_KNIVES = externalItemTag(FARMERS_DELIGHT, "tools/knives");
    public static final TagKey<Item> FARMERS_DELIGHT_ITEM_PIES = externalItemTag(FARMERS_DELIGHT, "pies");
    public static final TagKey<Item> FARMERS_DELIGHT_ITEM_WILD_CROPS = externalItemTag(FARMERS_DELIGHT, "wild_crops");
    public static final TagKey<Block> FARMERS_DELIGHT_CABINETS_WOODEN = externalBlockTag(FARMERS_DELIGHT, "cabinets/wooden");
    public static final TagKey<Block> FARMERS_DELIGHT_CAMPFIRE_SIGNAL_SMOKE = externalBlockTag(FARMERS_DELIGHT, "campfire_signal_smoke");
    public static final TagKey<Block> FARMERS_DELIGHT_COMPOST_ACTIVATORS = externalBlockTag(FARMERS_DELIGHT, "compost_activators");
    public static final TagKey<Block> FARMERS_DELIGHT_MINEABLE_WITH_KNIFE = externalBlockTag(FARMERS_DELIGHT, "mineable/knife");
    public static final TagKey<Block> FARMERS_DELIGHT_PIES = externalBlockTag(FARMERS_DELIGHT, "pies");
    public static final TagKey<Block> FARMERS_DELIGHT_STRAW_BLOCKS = externalBlockTag(FARMERS_DELIGHT, "straw_blocks");
    public static final TagKey<Block> FARMERS_DELIGHT_WILD_CROPS = externalBlockTag(FARMERS_DELIGHT, "wild_crops");

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return ItemTags.create(new ResourceLocation(modId, path));
    }

    private static TagKey<Block> externalBlockTag(String modId, String path) {
        return BlockTags.create(new ResourceLocation(modId, path));
    }
}