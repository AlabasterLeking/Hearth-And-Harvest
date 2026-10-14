package alabaster.hearthandharvest.client.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.event.TickEvent;
import alabaster.hearthandharvest.common.network.HHModNetworking;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.gui.CaskGUI;
import alabaster.hearthandharvest.client.gui.KegGUI;
import alabaster.hearthandharvest.client.particle.DrippingSapParticle;
import alabaster.hearthandharvest.client.particle.FeatherParticle;
import alabaster.hearthandharvest.client.particle.FliesParticle;
import alabaster.hearthandharvest.client.recipebook.RecipeCategories;
import alabaster.hearthandharvest.client.renderer.*;
import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisPlant;
import alabaster.hearthandharvest.common.entity.crow.CrowModel;
import alabaster.hearthandharvest.common.entity.crow.CrowOnShoulderLayer;
import alabaster.hearthandharvest.common.entity.crow.CrowRenderer;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchforkModel;
import alabaster.hearthandharvest.common.item.VintageHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import alabaster.hearthandharvest.common.item.component.SeedPouchContents;
import alabaster.hearthandharvest.common.registry.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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

        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return -1;
            if (level != null && pos != null) {
                return BiomeColors.getAverageGrassColor(level, pos);
            }
            return GrassColor.getDefaultColor();
        }, HHModBlocks.WILD_COTTON.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? (0xFF000000 | GrassColor.getDefaultColor()) : -1,
                HHModItems.WILD_COTTON.get());
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        // Bottle Racks
        ResourceManager rm = Minecraft.getInstance().getResourceManager();
        rm.listResources("models/display", path -> path.getPath().endsWith(".json") && !path.getPath().contains("/template_"))
                .keySet()
                .forEach(resourceLocation -> {
                    String path = resourceLocation.getPath();
                    String modelPath = path.substring("models/".length(), path.length() - ".json".length());
                    event.register((new ResourceLocation(resourceLocation.getNamespace(), modelPath)
                    ));
                });

        // Big Stomping Basin Models
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "block/big_stomping_basin_nw")));
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "block/big_stomping_basin_ne")));
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "block/big_stomping_basin_sw")));
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "block/big_stomping_basin_se")));

        // Pitchfork
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "item/pitchfork_throwing")));
        event.register((new ResourceLocation(HearthAndHarvest.MODID, "item/2d_pitchfork")));
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                HHModBlockEntities.NEST.get(),
                NestRenderer::new
        );
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
        event.registerSpriteSet(
                HHModParticleTypes.FEATHER.get(),
                FeatherParticle.Provider::new
        );
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new CrowOnShoulderLayer(renderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(HHModItems.SEED_POUCH.get(), (guiGraphics, font, stack, xOffset, yOffset) -> {
            SeedPouchContents contents = HHModDataComponents.SEED_POUCH_CONTENTS.get(stack);
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
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(HHModEntities.CROW.get(), CrowRenderer::new);
            EntityRenderers.register(HHModEntities.MANURE_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_PITCHFORK.get(), ThrownPitchforkRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_CLEAVER.get(), ThrownCleaverRenderer::new);
            EntityRenderers.register(HHModEntities.THROWN_HORSESHOE.get(), ThrownHorseshoeRenderer::new);
            MenuScreens.register(HHModMenuTypes.CASK_MENU.get(), CaskGUI::new);
            MenuScreens.register(HHModMenuTypes.KEG_MENU.get(), KegGUI::new);
            ResourceLocation vintage = new ResourceLocation(HearthAndHarvest.MODID, "vintage");
            HHModItems.ITEMS.getEntries().forEach(item ->
                    ItemProperties.register(item.get(), vintage, (stack, level, entity, seed) -> VintageHelper.getVintage(stack)));
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrowModel.LAYER_LOCATION, CrowModel::createBodyLayer);
        event.registerLayerDefinition(ThrownPitchforkModel.LAYER_LOCATION, ThrownPitchforkModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        RecipeCategories.init(event);
    }

    @Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            boolean pressed = false;
            while (HHKeyBindings.POOP.consumeClick()) {
                pressed = true;
            }
            if (pressed && cooldownTicks <= 0) {
                HHModNetworking.sendToServer(new HHModNetworking.PlayerPoopPacket());
                cooldownTicks = 300; // optimistic client-side cooldown; server confirmation just reaffirms this
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
    }
}
