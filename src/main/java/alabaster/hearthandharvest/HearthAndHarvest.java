package alabaster.hearthandharvest;

import alabaster.hearthandharvest.common.block.SaltBlock;
import alabaster.hearthandharvest.common.block.StompingBasinBlock;
import alabaster.hearthandharvest.common.entity.crow.CrowEntity;
import alabaster.hearthandharvest.common.entity.crow.CrowSpawnRules;
import alabaster.hearthandharvest.common.entity.goal.FoxEatBushBerriesGoal;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import alabaster.hearthandharvest.common.network.HHModNetworking;
import alabaster.hearthandharvest.common.crafting.VintageIngredient;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HearthAndHarvest.MODID)
public class HearthAndHarvest {
    public static final String MODID = "hearthandharvest";
    public static final Logger LOGGER = LogManager.getLogger();

    public HearthAndHarvest() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ForgeMod.enableMilkFluid();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::flowerSetup);
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(HearthAndHarvest::registerSpawnPlacements);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        HHModTriggers.init();
        HHModNetworking.register();
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
        HHModMenuTypes.MENUS.register(modEventBus);
        HHModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        HHModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        HHModStructurePieces.STRUCTURE_PIECES.register(modEventBus);
        HHModStructures.STRUCTURES.register(modEventBus);
        HHModStructures.PROCESSORS.register(modEventBus);
        HHModFeatures.FEATURES.register(modEventBus);
        HHModSounds.SOUNDS.register(modEventBus);
        HHModEnchantments.ENCHANTMENTS.register(modEventBus);
        HHModLootModifiers.LOOT_MODIFIERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new PigLitters());
        MinecraftForge.EVENT_BUS.register(new RabbitLitters());
        MinecraftForge.EVENT_BUS.addListener(VillageCrops::addVillageCrops);

        if (ModList.get().isLoaded("thirst")) {
            MinecraftForge.EVENT_BUS.register(ThirstWasTakenCompat.class);
        }

        if (ModList.get().isLoaded("everycomp")) {
            try {
                EveryCompatPlugin.register(MODID);
            } catch (Exception e) {
                LOGGER.debug("Every Compat registration skipped: {}", e.getMessage());
            }
        }

        if (ModList.get().isLoaded("create")) {
            MinecraftForge.EVENT_BUS.register(CreateCompat.class);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CrateBlockItem.registerDispenseBehavior(HHModItems.CRATE.get());
            SeedPouchItem.registerDispenseBehavior();
            SaltBlock.registerDispenseBehavior();
            VintageIngredient.register();
        });
    }

    private void flowerSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.YELLOW_MUM.get()), HHModBlocks.POTTED_YELLOW_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.ORANGE_MUM.get()), HHModBlocks.POTTED_ORANGE_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.RED_MUM.get()), HHModBlocks.POTTED_RED_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.BLUE_MUM.get()), HHModBlocks.POTTED_BLUE_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.LIGHT_BLUE_MUM.get()), HHModBlocks.POTTED_LIGHT_BLUE_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.PURPLE_MUM.get()), HHModBlocks.POTTED_PURPLE_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.PINK_MUM.get()), HHModBlocks.POTTED_PINK_MUM);
            pot.addPlant(ForgeRegistries.BLOCKS.getKey(HHModBlocks.WHITE_MUM.get()), HHModBlocks.POTTED_WHITE_MUM);
        });
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HHModEntities.CROW.get(), CrowEntity.createAttributes().build());
    }

    private static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                HHModEntities.CROW.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CrowSpawnRules::canSpawnCrow,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Mob mob) {
            mob.goalSelector.addGoal(1, new PungentEffectGoal(mob, 1.0D, 1.5D, 8.0D));
            mob.goalSelector.addGoal(1, new TemptingEffectGoal(mob, 1.0D, 1.25D, 8.0D));
        }
        if (event.getEntity() instanceof Fox fox) {
            fox.goalSelector.addGoal(10, new FoxEatBushBerriesGoal(fox, 1.2F, 12, 1));
        }
        if (event.getEntity() instanceof Chicken chicken && Config.CHICKENS_SEEK_NESTS.get()) {
            chicken.goalSelector.addGoal(2, new SeekNestGoal(chicken, 1.0D));
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Hearth and Harvest is starting");
        StompingBasinBlock.clearAirborneTracking();
    }
}