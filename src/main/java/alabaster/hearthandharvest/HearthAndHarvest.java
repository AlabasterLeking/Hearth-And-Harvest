package alabaster.hearthandharvest;

import alabaster.hearthandharvest.common.block.SaltBlock;
import alabaster.hearthandharvest.common.block.StompingBasinBlock;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.entity.crow.CrowSpawnRules;
import alabaster.hearthandharvest.common.entity.goal.PungentEffectGoal;
import alabaster.hearthandharvest.common.entity.goal.SeekNestGoal;
import alabaster.hearthandharvest.common.entity.goal.TemptingEffectGoal;
import alabaster.hearthandharvest.common.event.PigLitters;
import alabaster.hearthandharvest.common.event.RabbitLitters;
import alabaster.hearthandharvest.common.item.CrateBlockItem;
import alabaster.hearthandharvest.common.item.SeedPouchItem;
import alabaster.hearthandharvest.common.registry.*;
import alabaster.hearthandharvest.common.worldgen.VillageCrops;
import alabaster.hearthandharvest.integration.CreateCompat;
import alabaster.hearthandharvest.integration.ThirstWasTakenCompat;
import alabaster.hearthandharvest.integration.everycompat.EveryCompatPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HearthAndHarvest.MODID)
public class HearthAndHarvest {
    public static final String MODID = "hearthandharvest";
    public static final Logger LOGGER = LogManager.getLogger();

    public HearthAndHarvest(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::flowerSetup);
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(HearthAndHarvest::registerSpawnPlacements);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        HHModBlocks.BLOCKS.register(modEventBus);
        HHModItems.ITEMS.register(modEventBus);
        HHModEffects.EFFECTS.register(modEventBus);
        HHModEntities.ENTITY_TYPES.register(modEventBus);
        HHModParticleTypes.PARTICLE_TYPES.register(modEventBus);
        HHModPotions.POTIONS.register(modEventBus);
        HHModFluids.FLUIDS.register(modEventBus);
        HHModFluids.FLUID_TYPES.register(modEventBus);
        HHModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        HHModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        HHModDataComponents.DATA_COMPONENTS.register(modEventBus);
        HHModMenuTypes.MENUS.register(modEventBus);
        HHModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        HHModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        HHModStructurePieces.STRUCTURE_PIECES.register(modEventBus);
        HHModStructures.STRUCTURES.register(modEventBus);
        HHModFeatures.FEATURES.register(modEventBus);
        HHModSounds.SOUNDS.register(modEventBus);
        HHModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(new PigLitters());
        NeoForge.EVENT_BUS.register(new RabbitLitters());
        NeoForge.EVENT_BUS.addListener(VillageCrops::addVillageCrops);

        if (ModList.get().isLoaded("thirst")) {
            NeoForge.EVENT_BUS.register(ThirstWasTakenCompat.class);
        }

        if (ModList.get().isLoaded("everycomp")) {
            try {
                EveryCompatPlugin.register(MODID);
            } catch (Exception e) {
                LOGGER.debug("Every Compat registration skipped: {}", e.getMessage());
            }
        }

        if (ModList.get().isLoaded("create")) {
            NeoForge.EVENT_BUS.register(CreateCompat.class);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CrateBlockItem.registerDispenseBehavior(HHModItems.CRATE.get());
            SeedPouchItem.registerDispenseBehavior();
            SaltBlock.registerDispenseBehavior();
        });
    }

    private void flowerSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.YELLOW_MUM.get()), HHModBlocks.POTTED_YELLOW_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.ORANGE_MUM.get()), HHModBlocks.POTTED_ORANGE_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.RED_MUM.get()), HHModBlocks.POTTED_RED_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.BLUE_MUM.get()), HHModBlocks.POTTED_BLUE_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.LIGHT_BLUE_MUM.get()), HHModBlocks.POTTED_LIGHT_BLUE_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.PURPLE_MUM.get()), HHModBlocks.POTTED_PURPLE_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.PINK_MUM.get()), HHModBlocks.POTTED_PINK_MUM);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(HHModBlocks.WHITE_MUM.get()), HHModBlocks.POTTED_WHITE_MUM);
        });
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HHModEntities.CROW.get(), CrowEntity.createAttributes().build());
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                HHModEntities.CROW.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CrowSpawnRules::canSpawnCrow,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            mob.goalSelector.addGoal(1, new PungentEffectGoal(mob, 1.0D, 1.5D, 8.0D));
            mob.goalSelector.addGoal(1, new TemptingEffectGoal(mob, 1.0D, 1.25D, 8.0D));
        }
        if (event.getEntity() instanceof Chicken chicken) {
            chicken.goalSelector.addGoal(2, new SeekNestGoal(chicken, 1.0D));
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Hearth and Harvest is starting");
        StompingBasinBlock.clearAirborneTracking();
    }
}