package alabaster.hearthandharvest.integration.everycompat;

import alabaster.hearthandharvest.common.block.BottleRackBlock;
import alabaster.hearthandharvest.common.block.HalfCabinetBlock;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModCreativeTabs;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.PaletteStrategy;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.function.Consumer;

import static net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys.SLAB;
import static net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys.TRAPDOOR;

public class HHEveryCompatModule extends EveryCompatModule {

    private static final String HH = "hearthandharvest";

    private static final PaletteStrategy CABINET_PALETTE = PaletteStrategies.registerCached((blockType, manager) -> PaletteStrategies.makePaletteFromChild(
            blockType, manager, VanillaWoodChildKeys.PLANKS, null, p -> {
                p.reduceDown();
                if (p.size() < 9) {
                    while (p.size() <= 9) {
                        p.increaseInner();
                    }
                } else {
                    while (p.size() >= 9) {
                        p.reduce();
                    }
                }
            })
    );

    public final SimpleEntrySet<WoodType, HalfCabinetBlock> halfCabinets;
    public final SimpleEntrySet<WoodType, BottleRackBlock> bottleRacks;

    public HHEveryCompatModule(String modId) {
        super(modId, "hnh");

        halfCabinets = SimpleEntrySet.builder(
                        WoodType.class,
                        "half_cabinet",
                        () -> (HalfCabinetBlock) HHModBlocks.OAK_HALF_CABINET.get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new HalfCabinetBlock(BlockBehaviour.Properties.copy(w.planks))
                )
                .requiresChildren(TRAPDOOR, SLAB)
                .addTile(ModBlockEntityTypes.CABINET)
                .setTabKey(HHModCreativeTabs.BLOCKS_TAB_KEY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(new ResourceLocation("farmersdelight", "cabinets"), Registries.BLOCK, Registries.ITEM)
                .addTag(new ResourceLocation("farmersdelight", "cabinets/wooden"), Registries.BLOCK, Registries.ITEM)
                .addTexture(new ResourceLocation(HH, "block/oak_half_cabinet_side"), CABINET_PALETTE)
                .addTexture(new ResourceLocation(HH, "block/oak_half_cabinet_top"), CABINET_PALETTE)
                .addTextureM(new ResourceLocation(HH, "block/oak_cabinet_front"), EveryCompat.res("block/fd/oak_cabinet_front_m"), CABINET_PALETTE)
                .addTextureM(new ResourceLocation(HH, "block/oak_cabinet_front_open"), EveryCompat.res("block/fd/oak_cabinet_front_m"), CABINET_PALETTE)
                .addTexture(new ResourceLocation(HH, "block/oak_cabinet_side"), CABINET_PALETTE)
                .build();
        addEntry(halfCabinets);

        bottleRacks = SimpleEntrySet.builder(
                        WoodType.class,
                        "bottle_rack",
                        () -> (BottleRackBlock) HHModBlocks.OAK_BOTTLE_RACK.get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new BottleRackBlock(BlockBehaviour.Properties.copy(w.planks))
                )
                .addTile(HHModBlockEntities.BOTTLE_RACK)
                .setTabKey(HHModCreativeTabs.BLOCKS_TAB_KEY)
                .defaultRecipe()
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTexture(new ResourceLocation(HH, "block/oak_half_cabinet_side"), CABINET_PALETTE)
                .addTexture(new ResourceLocation(HH, "block/oak_half_cabinet_top"), CABINET_PALETTE)
                .addTexture(new ResourceLocation(HH, "block/oak_cabinet_side"), CABINET_PALETTE)
                .build();
        addEntry(bottleRacks);
    }

    @Override
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> halfCabinets.blocks.forEach((woodType, halfCabBlock) -> {
            ResourceLocation fdCabinetId = new ResourceLocation("everycomp", "fd/" + woodType.getAppendableId() + "_cabinet");
            Block fdCabinet = BuiltInRegistries.BLOCK.getOptional(fdCabinetId).orElse(null);
            if (fdCabinet == null) return;

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, halfCabBlock, 2)
                    .requires(fdCabinet)
                    .requires(fdCabinet)
                    .unlockedBy("has_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(fdCabinet))
                    .save(sink::addRecipeNoAdvancement, EveryCompat.res(shortenedId() + "/half_cabinet_from_cabinet/" + woodType.getAppendableId()));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, fdCabinet, 1)
                    .requires(halfCabBlock)
                    .requires(halfCabBlock)
                    .unlockedBy("has_half_cabinet", InventoryChangeTrigger.TriggerInstance.hasItems(halfCabBlock))
                    .save(sink::addRecipeNoAdvancement, EveryCompat.res(shortenedId() + "/cabinet_from_halves/" + woodType.getAppendableId()));
        }));
    }
}
