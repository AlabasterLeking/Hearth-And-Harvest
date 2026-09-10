package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class HHEntityTags extends EntityTypeTagsProvider
{
    public HHEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HearthAndHarvest.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(HHModTags.CAN_BE_BUTCHERED).add(
                EntityType.CHICKEN,
                EntityType.COW,
                EntityType.PIG,
                EntityType.SHEEP,
                EntityType.RABBIT
        );

        this.tag(HHModTags.DOES_NOT_POOP).add(
                EntityType.CHICKEN,
                HHModEntities.CROW.get()
        );

        this.tag(HHModTags.SCARY_FOR_CROW).add(
                EntityType.PLAYER,
                EntityType.VILLAGER
        );
    }
}
