package alabaster.hearthandharvest.data;

import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraft.world.level.biome.Biomes;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class HHBiomeTags extends BiomeTagsProvider {
    public HHBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HearthAndHarvest.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
    }

    protected void registerModTags() {
        tag(HHModTags.HAS_CROWS)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST);

        tag(HHModTags.HAS_CORN_MAZE)
                .addTag(Tags.Biomes.IS_PLAINS);

        tag(HHModTags.HAS_LILLIPUT_LANE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .addTag(BiomeTags.IS_TAIGA);

        tag(HHModTags.HAS_SALT_CAVES)
                .addTag(Tags.Biomes.IS_DRY_OVERWORLD)
                .addTag(Tags.Biomes.IS_DESERT)
                .addTag(BiomeTags.IS_BADLANDS)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_BEACH);
    }
}