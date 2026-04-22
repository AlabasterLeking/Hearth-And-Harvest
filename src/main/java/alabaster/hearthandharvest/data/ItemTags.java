package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, HearthAndHarvest.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.registerModTags();
    }

    private void registerModTags() {
        tag(HHModTags.CLEAVERS)
                .add(HHModItems.FLINT_CLEAVER.get())
                .add(HHModItems.IRON_CLEAVER.get())
                .add(HHModItems.DIAMOND_CLEAVER.get())
                .add(HHModItems.GOLDEN_CLEAVER.get())
                .add(HHModItems.NETHERITE_CLEAVER.get());

        tag(ModTags.Items.KNIVES)
                .addTag(HHModTags.CLEAVERS);

        tag(HHModTags.JAMS).add(HHModItems.GRAPE_JAM.get(), HHModItems.RASPBERRY_JAM.get(), HHModItems.BLUEBERRY_JAM.get(), HHModItems.APPLE_JAM.get(), HHModItems.GLOW_BERRY_JAM.get(), HHModItems.SWEET_BERRY_JAM.get(), HHModItems.MELON_JAM.get());
        tag(CommonTags.Items.MILK_BOTTLE).add(HHModItems.GOAT_MILK_BOTTLE.get());
        tag(HHModTags.CHEESE_SLICES).add(HHModItems.CHEDDAR_CHEESE_SLICE.get(), HHModItems.GOAT_CHEESE_SLICE.get());
        tag(CommonTags.Items.BERRIES).add(HHModItems.BLUEBERRIES.get()).add(HHModItems.CHERRY.get()).add(HHModItems.RASPBERRY.get()).add(HHModItems.RED_GRAPES.get()).add(HHModItems.GREEN_GRAPES.get());
        tag(Tags.Items.SEEDS).add(HHModItems.COTTON_SEEDS.get()).add(HHModItems.SUNFLOWER_SEEDS.get());
        tag(HHModTags.WINE_BOTTLES).add(HHModItems.BLUEBERRY_WINE.get(), HHModItems.CHERRY_WINE.get(), HHModItems.GREEN_GRAPE_WINE.get(), HHModItems.MEAD.get(), HHModItems.RASPBERRY_WINE.get(), HHModItems.RED_GRAPE_WINE.get(), HHModItems.SWEET_BERRY_WINE.get());

        tag(ModTags.Items.CABINETS_WOODEN)
                .add(HHModItems.OAK_HALF_CABINET.get())
                .add(HHModItems.SPRUCE_HALF_CABINET.get())
                .add(HHModItems.BIRCH_HALF_CABINET.get())
                .add(HHModItems.JUNGLE_HALF_CABINET.get())
                .add(HHModItems.ACACIA_HALF_CABINET.get())
                .add(HHModItems.DARK_OAK_HALF_CABINET.get())
                .add(HHModItems.MANGROVE_HALF_CABINET.get())
                .add(HHModItems.CHERRY_HALF_CABINET.get())
                .add(HHModItems.BAMBOO_HALF_CABINET.get())
                .add(HHModItems.CRIMSON_HALF_CABINET.get())
                .add(HHModItems.WARPED_HALF_CABINET.get());
    }
}