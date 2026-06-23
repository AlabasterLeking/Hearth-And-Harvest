package alabaster.hearthandharvest.client.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.gui.CaskGUI;
import alabaster.hearthandharvest.client.particle.DrippingSapParticle;
import alabaster.hearthandharvest.client.particle.FliesParticle;
import alabaster.hearthandharvest.client.recipebook.RecipeCategories;
import alabaster.hearthandharvest.client.renderer.*;
import alabaster.hearthandharvest.common.entity.crow.CrowModel;
import alabaster.hearthandharvest.common.entity.crow.CrowRenderer;
import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisPlant;
import alabaster.hearthandharvest.common.entity.crow.CrowOnShoulderLayer;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchforkModel;
import alabaster.hearthandharvest.common.item.component.SeedPouchContents;
import alabaster.hearthandharvest.common.network.PlayerPoopPacket;
import alabaster.hearthandharvest.common.registry.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("unused")
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return -1;
            if (state.getValue(TrellisBlock.PLANT) != TrellisPlant.VINE) return -1;
            if (level != null && pos != null) {
                return BiomeColors.getAverageFoliageColor(level, pos);
            }
            return FoliageColor.getDefaultColor();
        }, HHModBlocks.TRELLIS.get());
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        // Bottle Racks
        ResourceManager rm = Minecraft.getInstance().getResourceManager();
        rm.listResources("models/bottle_rack", path -> path.getPath().endsWith(".json"))
                .keySet()
                .forEach(resourceLocation -> {
                    String path = resourceLocation.getPath();
                    String modelPath = path.substring("models/".length(), path.length() - ".json".length());
                    event.register(ModelResourceLocation.standalone(
                            ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), modelPath)
                    ));
                });

        // Big Stomping Basin Models
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_nw")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_ne")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_sw")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/big_stomping_basin_se")));

        // Pitchfork
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "item/pitchfork_throwing")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "item/2d_pitchfork")));
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                HHModBlockEntities.BOTTLE_RACK.get(),
                BottleRackRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.CRATE.get(),
                CrateRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.STOMPING_BASIN.get(),
                StompingBasinRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.JAR.get(),
                JarRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.TROUGH.get(),
                TroughRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.TREE_TAPPER.get(),
                TreeTapperRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.BASIN.get(),
                BasinRenderer::new
        );
        event.registerBlockEntityRenderer(
                HHModBlockEntities.SPRINKLER.get(),
                SprinklerRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                HHModParticleTypes.DRIPPING_SAP.get(),
                DrippingSapParticle.Provider::new
        );
        event.registerSpriteSet(
                HHModParticleTypes.FLIES.get(),
                FliesParticle.Provider::new
        );
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (var skinModel : PlayerSkin.Model.values()) {
            PlayerRenderer renderer = event.getSkin(skinModel);
            if (renderer != null) {
                renderer.addLayer(new CrowOnShoulderLayer(renderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return CrateItemRenderer.getInstance();
                    }
                },
                HHModItems.CRATE.get()
        );

        event.registerItem(
                new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return PitchforkItemRenderer.getInstance();
                    }
                },
                HHModItems.PITCHFORK.get()
        );

        registerFluidTextures(event, "cooking_oil", HHModFluids.COOKING_OIL.type().get());
        registerFluidTextures(event, "sap", HHModFluids.SAP.type().get());
        registerFluidTextures(event, "syrup", HHModFluids.SYRUP.type().get());
        registerFluidTextures(event, "apple_cider", HHModFluids.APPLE_CIDER.type().get());
        registerFluidTextures(event, "hard_cider", HHModFluids.HARD_CIDER.type().get());
        registerFluidTextures(event, "root_beer", HHModFluids.ROOT_BEER.type().get());
        registerFluidTextures(event, "mead", HHModFluids.MEAD.type().get());
        registerFluidTextures(event, "moonshine", HHModFluids.MOONSHINE.type().get());
        registerFluidTextures(event, "blueberry_juice", HHModFluids.BLUEBERRY_JUICE.type().get());
        registerFluidTextures(event, "cherry_juice", HHModFluids.CHERRY_JUICE.type().get());
        registerFluidTextures(event, "green_grape_juice",HHModFluids.GREEN_GRAPE_JUICE.type().get());
        registerFluidTextures(event, "raspberry_juice", HHModFluids.RASPBERRY_JUICE.type().get());
        registerFluidTextures(event, "red_grape_juice", HHModFluids.RED_GRAPE_JUICE.type().get());
        registerFluidTextures(event, "sweet_berry_juice",HHModFluids.SWEET_BERRY_JUICE.type().get());
        registerFluidTextures(event, "melon_juice", HHModFluids.MELON_JUICE.type().get());
        registerFluidTextures(event, "glow_berry_juice", HHModFluids.GLOW_BERRY_JUICE.type().get());
        registerFluidTextures(event, "blueberry_wine", HHModFluids.BLUEBERRY_WINE.type().get());
        registerFluidTextures(event, "cherry_wine", HHModFluids.CHERRY_WINE.type().get());
        registerFluidTextures(event, "green_grape_wine", HHModFluids.GREEN_GRAPE_WINE.type().get());
        registerFluidTextures(event, "raspberry_wine", HHModFluids.RASPBERRY_WINE.type().get());
        registerFluidTextures(event, "red_grape_wine", HHModFluids.RED_GRAPE_WINE.type().get());
        registerFluidTextures(event, "sweet_berry_wine", HHModFluids.SWEET_BERRY_WINE.type().get());
        registerFluidTextures(event, "glow_berry_wine", HHModFluids.GLOW_BERRY_WINE.type().get());
        registerFluidTextures(event, "melon_wine", HHModFluids.MELON_WINE.type().get());
    }

    private static void registerFluidTextures(RegisterClientExtensionsEvent event, String name, FluidType type) {
        ResourceLocation still   = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/" + name + "_still");
        ResourceLocation flowing = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "block/" + name + "_flow");

        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override public ResourceLocation getStillTexture()   { return still; }
            @Override public ResourceLocation getFlowingTexture() { return flowing; }
        }, type);
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(HHModItems.SEED_POUCH.get(), (guiGraphics, font, stack, xOffset, yOffset) -> {
            SeedPouchContents contents = stack.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
            if (contents == null || contents.count() == 0) return false;

            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(xOffset + 8, yOffset + 8, 200);
            pose.scale(0.5f, 0.5f, 1.0f);
            guiGraphics.renderItem(new ItemStack(contents.seedType()), 0, 0);
            pose.popPose();
            return true;
        });
    }

    private static int cooldownTicks = 0;

    public static void startCooldown() {
        cooldownTicks = 300;
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(HHKeyBindings.POOP);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (HHKeyBindings.POOP.consumeClick()) {
            PacketDistributor.sendToServer(new PlayerPoopPacket());
        }
        if (cooldownTicks <= 0) return;
        cooldownTicks--;
        if (cooldownTicks % 4 != 0) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) return;
        for (int i = 0; i < 2; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 1.5;
            double oy = level.random.nextDouble() * player.getBbHeight();
            double oz = (level.random.nextDouble() - 0.5) * 1.5;
            level.addParticle(HHModParticleTypes.FLIES.get(),
                    player.getX() + ox,
                    player.getY() + oy,
                    player.getZ() + oz,
                    0, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(HHModEntities.CROW.get(), CrowRenderer::new);
            EntityRenderers.register(HHModEntities.MANURE_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_PITCHFORK.get(), ThrownPitchforkRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_CLEAVER.get(), ThrownCleaverRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_HORSESHOE.get(), ThrownHorseshoeRenderer::new);
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrowModel.LAYER_LOCATION, CrowModel::createBodyLayer);
        event.registerLayerDefinition(ThrownPitchforkModel.LAYER_LOCATION, ThrownPitchforkModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(HHModMenuTypes.CASK_MENU.get(), CaskGUI::new);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        RecipeCategories.init(event);
    }
}