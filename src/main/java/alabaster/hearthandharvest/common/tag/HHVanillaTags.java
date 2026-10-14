package alabaster.hearthandharvest.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HHVanillaTags {
    public static final TagKey<Item> MEAT = tag("meat");
    public static final TagKey<Item> CHICKEN_FOOD = tag("chicken_food");
    public static final TagKey<Item> COW_FOOD = tag("cow_food");
    public static final TagKey<Item> GOAT_FOOD = tag("goat_food");
    public static final TagKey<Item> SHEEP_FOOD = tag("sheep_food");
    public static final TagKey<Item> PIG_FOOD = tag("pig_food");
    public static final TagKey<Item> WOLF_FOOD = tag("wolf_food");
    public static final TagKey<Item> CAT_FOOD = tag("cat_food");
    public static final TagKey<Item> HORSE_FOOD = tag("horse_food");
    public static final TagKey<Item> CAMEL_FOOD = tag("camel_food");
    public static final TagKey<Item> PARROT_POISONOUS_FOOD = tag("parrot_poisonous_food");
    public static final TagKey<Item> HORSE_TEMPT_ITEMS = tag("horse_tempt_items");
    public static final TagKey<Item> LLAMA_TEMPT_ITEMS = tag("llama_tempt_items");
    public static final TagKey<Item> LLAMA_FOOD = tag("llama_food");
    public static final TagKey<Item> PARROT_FOOD = tag("parrot_food");
    public static final TagKey<Item> FOX_FOOD = tag("fox_food");

    private static TagKey<Item> tag(String path) {
        return ItemTags.create(new ResourceLocation("minecraft", path));
    }
}
