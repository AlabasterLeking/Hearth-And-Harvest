package alabaster.hearthandharvest;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    public static ModConfigSpec COMMON_CONFIG;
    private static final Map<String, ModConfigSpec.BooleanValue> ITEMS = new HashMap<>();

    public static ModConfigSpec.DoubleValue TREE_TAPPER_BASE_CHANCE;
    public static ModConfigSpec.IntValue CROW_SPAWN_NUMBER_OF_CROPS;
    public static ModConfigSpec.IntValue CROW_SPAWN_RADIUS;
    public static ModConfigSpec.IntValue CROW_SCARE_RADIUS;
    public static ModConfigSpec.BooleanValue CROW_SPAWN_NEAR_NESTS;
    public static ModConfigSpec.BooleanValue CHICKENS_SEEK_NESTS;
    public static ModConfigSpec.BooleanValue GENERATE_NESTS;
    public static ModConfigSpec.BooleanValue CROW_STEAL_SHINY_ITEMS;
    public static ModConfigSpec.BooleanValue CROW_FETCH_ITEMS;
    public static ModConfigSpec.BooleanValue CROW_FETCH_THROWN_ITEMS;
    public static ModConfigSpec.BooleanValue CROW_LEAVE_SHOULDER_TO_FETCH;
    public static ModConfigSpec.BooleanValue CROW_EAT_DROPPED_FOOD;
    public static ModConfigSpec.BooleanValue CROW_TEMPTING;
    public static ModConfigSpec.BooleanValue CROW_FLOCK_ALARM;
    public static ModConfigSpec.BooleanValue CROW_EAT_CROPS;
    public static ModConfigSpec.BooleanValue STACK_WATER_BOTTLES;
    public static ModConfigSpec.BooleanValue GENERATE_CORN_MAZES;
    public static ModConfigSpec.BooleanValue GENERATE_LILLIPUT_LANE;
    public static ModConfigSpec.BooleanValue DISABLE_BOTTLE_MILKING;
    public static ModConfigSpec.BooleanValue TRELLIS_PLACEMENT_PREVIEW;
    public static ModConfigSpec.BooleanValue GRAPE_REQUIRE_FARMLAND;
    public static ModConfigSpec.DoubleValue SALTED_HUNGER_BONUS;
    public static ModConfigSpec.DoubleValue SALTED_SATURATION_PENALTY;
    public static ModConfigSpec.IntValue SALT_ANIMAL_RADIUS;
    public static ModConfigSpec.IntValue SALT_LICK_INTERVAL;
    public static ModConfigSpec.DoubleValue SALT_PLAYER_LICK_CHANCE;
    public static ModConfigSpec.BooleanValue SALT_CAVES_ENABLED;
    public static ModConfigSpec.IntValue SALT_CAVE_RARITY;
    public static ModConfigSpec.IntValue SALT_CAVE_MIN_Y;
    public static ModConfigSpec.IntValue SALT_CAVE_MAX_Y;
    public static ModConfigSpec.IntValue TROUGH_ANIMAL_CAP;
    public static ModConfigSpec.BooleanValue PLAYER_POOP_ENABLED;
    public static ModConfigSpec.BooleanValue MANURE_FED_POOP_ENABLED;
    public static ModConfigSpec.BooleanValue MANURE_RANDOM_POOP_ENABLED;
    public static ModConfigSpec.IntValue MANURE_RANDOM_POOP_CHANCE;
    public static ModConfigSpec.BooleanValue DISABLE_PIG_LITTERS;
    public static ModConfigSpec.BooleanValue DISABLE_RABBIT_LITTERS;
    public static ModConfigSpec.BooleanValue DISABLE_CHICKEN_PLUCKING;
    public static ModConfigSpec.BooleanValue CHICKEN_GLIDING;

    public Config() {
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        TREE_TAPPER_BASE_CHANCE = COMMON_BUILDER
                .comment("Chance (0.0 - 1.0) per check interval (~2 seconds) for a Tree Tapper to collect sap when on a tappable block.\n"
                        + "Higher values make sap fill faster.")
                .defineInRange("treeTapperBaseChance", 0.5D, 0.0D, 1.0D);

        STACK_WATER_BOTTLES = COMMON_BUILDER
                .comment("Whether water bottles should stack up to 16")
                .define("stackWaterBottles", true);

        DISABLE_BOTTLE_MILKING = COMMON_BUILDER
                .comment("Disables milking cows and goats with glass bottles. \n" +
                        "Be aware that setting this can cause goat milk bottles to be unobtainable unless handled otherwise")
                .define("disableBottleMilking", false);

        GENERATE_CORN_MAZES = COMMON_BUILDER
                .comment("Whether corn mazes should spawn in the world")
                .define("generateCornMazes", true);

        GENERATE_LILLIPUT_LANE = COMMON_BUILDER
                .comment("Whether Lilliput Lane should spawn in the world")
                .define("generateLilliputLane", true);

        TRELLIS_PLACEMENT_PREVIEW = COMMON_BUILDER
                .comment("Whether a ghost preview of the trellis piece is shown before placing")
                .define("trellisPlacementPreview", true);

        GRAPE_REQUIRE_FARMLAND = COMMON_BUILDER
                .comment("Whether grapes on a trellis require farmland beneath the base of the column to grow and spread.\n" +
                        "Set to false to let grapes grow anywhere, like the Vine and Rose Bush trellis plants.")
                .define("grapeRequireFarmland", true);

        TROUGH_ANIMAL_CAP = COMMON_BUILDER
                .comment("Max animals in a 10-block radius before the food trough stops triggering breeding.")
                .defineInRange("troughBreedingCap", 16, 1, 256);

        CROW_SPAWN_NUMBER_OF_CROPS = COMMON_BUILDER
                .comment("Amount of crops that need to be in an area for a crow to spawn nearby. Used alongside the crowSpawnRadius config to control crow spawning.\n" +
                        "Setting to 0 disables crop-based crow spawning. Crows can still spawn near naturally generated nests unless crow.spawnNearNests is false")
                .defineInRange("crow.crowCropRequirement", 8, 0, 192);

        CROW_SPAWN_RADIUS = COMMON_BUILDER
                .comment("Radius that crows check for crops to be in when trying to spawn. Larger radius means higher changes of spawning.\n" +
                        "Setting to 0 would prevent crow spawning")
                .defineInRange("crow.crowSpawnRadius", 8, 0, 64);

        CROW_SCARE_RADIUS = COMMON_BUILDER
                .comment("Radius that players, villgers, and repelling blocks will be effective towards scaring wild crows.\n" +
                        "Setting to 0 would prevent crows from being scared")
                .defineInRange("crow.crowScareRadius", 6, 0, 64);

        CROW_SPAWN_NEAR_NESTS = COMMON_BUILDER
                .comment("Whether crows can spawn near naturally generated nests without needing nearby crops")
                .define("crow.spawnNearNests", true);

        CHICKENS_SEEK_NESTS = COMMON_BUILDER
                .comment("Whether chickens walk to a nest before laying an egg. Disable if another mod already adds nest-seeking behavior")
                .define("nests.chickensSeekNests", true);

        GENERATE_NESTS = COMMON_BUILDER
                .comment("Whether nests generate naturally in the world")
                .define("nests.generateNests", true);

        CROW_STEAL_SHINY_ITEMS = COMMON_BUILDER
                .comment("Whether wild crows snatch shiny items off the ground and carry them to nests")
                .define("crow.stealShinyItems", true);

        CROW_FETCH_ITEMS = COMMON_BUILDER
                .comment("Whether tamed crows pick up nearby items and bring them to their owner")
                .define("crow.fetchItems", true);

        CROW_FETCH_THROWN_ITEMS = COMMON_BUILDER
                .comment("Whether tamed crows also fetch items their owner threw away")
                .define("crow.fetchThrownItems", true);

        CROW_LEAVE_SHOULDER_TO_FETCH = COMMON_BUILDER
                .comment("Whether tamed crows hop off their owner's shoulder to fetch nearby items")
                .define("crow.leaveShoulderToFetch", true);

        CROW_EAT_DROPPED_FOOD = COMMON_BUILDER
                .comment("Whether wild crows eat crow food dropped on the ground, with a chance to be tamed if a player threw it")
                .define("crow.eatDroppedFood", true);

        CROW_TEMPTING = COMMON_BUILDER
                .comment("Whether wild crows are drawn to players holding crow food and slowly learn to trust them")
                .define("crow.tempting", true);

        CROW_FLOCK_ALARM = COMMON_BUILDER
                .comment("Whether hurting a wild crow makes nearby crows flee from the attacker")
                .define("crow.flockAlarm", true);

        CROW_EAT_CROPS = COMMON_BUILDER
                .comment("Whether wild crows peck at and damage crops. Also requires the mobGriefing gamerule")
                .define("crow.eatCrops", true);

        SALTED_HUNGER_BONUS = COMMON_BUILDER
                .comment("Multiplier applied to a food's nutrition value to determine bonus hunger granted when eating salted food.\n" +
                        "For example, 0.2 means a food restoring 5 hunger gets +1 bonus hunger (20% of 5, minimum 1 for foods with less than 5 hunger).")
                .defineInRange("salt.saltedHungerBonus", 0.2D, 0.0D, 1.0D);

        SALTED_SATURATION_PENALTY = COMMON_BUILDER
                .comment("Fraction of the saturation granted by a food that is removed when eating salted food.\n" +
                        "For example, 0.1 means 10% of the saturation normally given is taken away.")
                .defineInRange("salt.saltedSaturationPenalty", 0.1D, 0.0D, 1.0D);

        SALT_ANIMAL_RADIUS = COMMON_BUILDER
                .comment("Radius in blocks that animals are kept within when near a salt block.")
                .defineInRange("salt.saltAnimalRadius", 12, 1, 64);

        SALT_LICK_INTERVAL = COMMON_BUILDER
                .comment("Ticks between each animal licking a nearby salt block. 9600 = 8 minutes.")
                .defineInRange("salt.saltLickInterval", 9600, 200, 72000);

        SALT_PLAYER_LICK_CHANCE = COMMON_BUILDER
                .comment("Chance (0.0–1.0) that a player's right-click lick degrades the salt block.")
                .defineInRange("salt.saltPlayerLickChance", 0.05D, 0.0D, 1.0D);

        SALT_CAVES_ENABLED = COMMON_BUILDER
                .comment("Whether salt caves generate underground in biomes tagged #hearthandharvest:has_salt_caves")
                .define("salt.generateSaltCaves", true);

        SALT_CAVE_RARITY = COMMON_BUILDER
                .comment("Salt caves attempt to generate on average once every this many chunks in eligible biomes")
                .defineInRange("salt.saltCaveRarity", 24, 1, 1000);

        SALT_CAVE_MIN_Y = COMMON_BUILDER
                .comment("Lowest Y level a salt cave can be centered on. Caves always stay clear of bedrock")
                .defineInRange("salt.saltCaveMinY", -40, -64, 320);

        SALT_CAVE_MAX_Y = COMMON_BUILDER
                .comment("Highest Y level a salt cave can be centered on")
                .defineInRange("salt.saltCaveMaxY", 30, -64, 320);

        PLAYER_POOP_ENABLED = COMMON_BUILDER
                .comment("Whether players are able to poop using the poop keybind")
                .define("manure.playerPoopEnabled", true);

        MANURE_FED_POOP_ENABLED = COMMON_BUILDER
                .comment("Drop manure after being fed")
                .define("manure.fedPoopEnabled", true);

        MANURE_RANDOM_POOP_ENABLED = COMMON_BUILDER
                .comment("Drop manure randomly over time")
                .define("manure.randomPoopEnabled", false);

        MANURE_RANDOM_POOP_CHANCE  = COMMON_BUILDER
                .comment("1-in-N chance per second for random drop")
                .defineInRange("manure.randomPoopChance", 300, 1, 10000);

        DISABLE_PIG_LITTERS = COMMON_BUILDER
                .comment("Disables the extra baby pigs spawned when pigs breed")
                .define("breeding.disablePigLitters", false);

        DISABLE_RABBIT_LITTERS = COMMON_BUILDER
                .comment("Disables the extra baby rabbits spawned when rabbits breed")
                .define("breeding.disableRabbitLitters", false);

        DISABLE_CHICKEN_PLUCKING = COMMON_BUILDER
                .comment("Disables shift-right-click plucking feathers from chickens")
                .define("breeding.disableChickenPlucking", false);

        CHICKEN_GLIDING = COMMON_BUILDER
                .comment("Whether players can pick up a chicken with an empty hand and glide while holding it overhead. Sneak to put it down")
                .define("breeding.chickenGliding", true);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void put(ModConfigSpec.Builder builder, String name) {
        ITEMS.put(name, builder.define(name, true));
    }

    private static boolean contains(String item) {
        return ITEMS.containsKey(item);
    }
}