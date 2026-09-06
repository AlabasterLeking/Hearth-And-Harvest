package alabaster.hearthandharvest.integration.everycompat;

import alabaster.hearthandharvest.common.block.BottleRackBlock;
import alabaster.hearthandharvest.common.block.HalfCabinetBlock;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModCreativeTabs;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.every_compat.modules.farmersdelight.FarmersDelightModule;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.tags.BlockTags;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys.SLAB;
import static net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys.TRAPDOOR;

public class HHEveryCompatModule extends EveryCompatModule {

    private static final String HH = "hearthandharvest";

    public final SimpleEntrySet<WoodType, HalfCabinetBlock> halfCabinets;
    public final SimpleEntrySet<WoodType, BottleRackBlock> bottleRacks;

    public HHEveryCompatModule(String modId) {
        super(modId, "hnh");

        Supplier<CreativeModeTab> tab = getTab(HHModCreativeTabs.BLOCKS_TAB_KEY);

        halfCabinets = SimpleEntrySet.builder(
                        WoodType.class,
                        "half_cabinet",
                        () -> (HalfCabinetBlock) HHModBlocks.OAK_HALF_CABINET.get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new HalfCabinetBlock(BlockBehaviour.Properties.ofFullCopy(w.planks))
                )
                .requiresChildren(TRAPDOOR, SLAB)
                .addTile(ModBlockEntityTypes.CABINET)
                .setTab(tab)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.CABINETS, Registries.BLOCK, Registries.ITEM)
                .addTag(ModTags.Blocks.CABINETS_WOODEN, Registries.BLOCK, Registries.ITEM)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_half_cabinet_side"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_half_cabinet_top"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTextureM(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_cabinet_front"),
                        EveryCompat.res("block/fd/oak_cabinet_front_m"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTextureM(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_cabinet_front_open"),
                        EveryCompat.res("block/fd/oak_cabinet_front_m"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_cabinet_side"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .build();
        addEntry(halfCabinets);

        bottleRacks = SimpleEntrySet.builder(
                        WoodType.class,
                        "bottle_rack",
                        () -> (BottleRackBlock) HHModBlocks.OAK_BOTTLE_RACK.get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new BottleRackBlock(BlockBehaviour.Properties.ofFullCopy(w.planks))
                )
                .addTile(HHModBlockEntities.BOTTLE_RACK)
                .setTab(tab)
                .defaultRecipe()
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_half_cabinet_side"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_half_cabinet_top"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(HH, "block/oak_cabinet_side"),
                        FarmersDelightModule.CUSTOM_PALETTE)
                .build();
        addEntry(bottleRacks);
    }

    @Override
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            halfCabinets.blocks.forEach((woodType, halfCabBlock) -> {
                ResourceLocation fdCabinetId = ResourceLocation.fromNamespaceAndPath(
                        "everycomp", "fd/" + woodType.getAppendableId() + "_cabinet");

                var fdCabinetOpt = BuiltInRegistries.BLOCK.getOptional(fdCabinetId);
                if (fdCabinetOpt.isEmpty()) return;
                Block fdCabinet = fdCabinetOpt.get();

                sink.addRecipe(new RecipeHolder<>(
                        EveryCompat.res(shortenedId() + "/half_cabinet_from_cabinet/" + woodType.getAppendableId()),
                        new ShapelessRecipe("", CraftingBookCategory.MISC,
                                new ItemStack(halfCabBlock, 2),
                                NonNullList.of(Ingredient.EMPTY,
                                        Ingredient.of(fdCabinet),
                                        Ingredient.of(fdCabinet)))
                ));

                sink.addRecipe(new RecipeHolder<>(
                        EveryCompat.res(shortenedId() + "/cabinet_from_halves/" + woodType.getAppendableId()),
                        new ShapelessRecipe("", CraftingBookCategory.MISC,
                                new ItemStack(fdCabinet, 1),
                                NonNullList.of(Ingredient.EMPTY,
                                        Ingredient.of(halfCabBlock),
                                        Ingredient.of(halfCabBlock)))
                ));
            });
        });
    }
}