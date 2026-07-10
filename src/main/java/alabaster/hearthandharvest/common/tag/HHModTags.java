package alabaster.hearthandharvest.common.tag;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class HHModTags {

    // Blocks that can produce sap
    public static final TagKey<Block> TAPPABLE = modBlockTag("tappable");

    public static final TagKey<Item> CLEAVERS = modItemTag("cleavers");
    public static final TagKey<Item> JAMS = modItemTag("jelly");
    public static final TagKey<Item> BOTTLES = modItemTag("bottles");
    public static final TagKey<Item> TALL_BOTTLES = modItemTag("tall_bottles");
    public static final TagKey<Item> SHORT_BOTTLES = modItemTag("short_bottles");
    public static final TagKey<Item> CRATEABLE_ITEMS = modItemTag("crateable_items");
    public static final TagKey<Item> CHEESE_SLICES = modItemTag("cheese_slices");
    public static final TagKey<Item> TRELLIS_PLANTABLE = modItemTag("trellis_plantable");
    public static final TagKey<Item> CROW_FOOD = modItemTag("crow_food");
    public static final TagKey<Item> CROW_TEMPT_ITEMS = modItemTag("crow_tempt_items");
    public static final TagKey<Item> CROW_SHINY_ITEMS = modItemTag("crow_shiny_items");

    public static final TagKey<Block> CROW_EDIBLE_CROPS = modBlockTag("crow_edible_crops");
    public static final TagKey<Block> REPELS_CROWS = modBlockTag("repels_crows");
    public static final TagKey<Block> SALT_BLOCKS = modBlockTag("salt_blocks");
    public static final TagKey<Block> RIGHT_CLICK_HARVESTABLE = modBlockTag("right_click_harvestable");
    public static final TagKey<Block> MINEABLE_WITH_PITCHFORK = modBlockTag("mineable_with_pitchfork");

    public static final TagKey<Biome> HAS_CROWS = modBiomeTag("has_crows");

    public static final TagKey<EntityType<?>> CAN_BE_BUTCHERED = modEntityTag("can_be_butchered");
    public static final TagKey<EntityType<?>> DOES_NOT_POOP = modEntityTag("does_not_poop");
    public static final TagKey<EntityType<?>> CAN_POOP = modEntityTag("can_poop");

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path));
    }

    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path));
    }

    private static TagKey<Biome> modBiomeTag(String path) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path)
        );
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path));
    }
}