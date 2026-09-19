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
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
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
        TREE_TAPPER_BASE_CHANCE = range("treeTapperBaseChance", 0.5D, 0.0D, 1.0D, "Chance (0.0 - 1.0) per check interval (~2 seconds) for a Tree Tapper to collect sap when on a tappable block.\n" + "Higher values make sap fill faster.");

        STACK_WATER_BOTTLES = flag("stackWaterBottles", true, "Whether water bottles should stack up to 16");

        DISABLE_BOTTLE_MILKING = flag("disableBottleMilking", false, "Disables milking cows and goats with glass bottles. \n" + "Be aware that setting this can cause goat milk bottles to be unobtainable unless handled otherwise");

        GENERATE_CORN_MAZES = flag("generateCornMazes", true, "Whether corn mazes should spawn in the world");

        GENERATE_LILLIPUT_LANE = flag("generateLilliputLane", true, "Whether Lilliput Lane should spawn in the world");

        TRELLIS_PLACEMENT_PREVIEW = flag("trellisPlacementPreview", true, "Whether a ghost preview of the trellis piece is shown before placing");

        GRAPE_REQUIRE_FARMLAND = flag("grapeRequireFarmland", true, "Whether grapes on a trellis require farmland beneath the base of the column to grow and spread.\n" + "Set to false to let grapes grow anywhere, like the Vine and Rose Bush trellis plants.");

        TROUGH_ANIMAL_CAP = range("troughBreedingCap", 16, 1, 256, "Max animals in a 10-block radius before the food trough stops triggering breeding.");

        CROW_SPAWN_NUMBER_OF_CROPS = range("crow.crowCropRequirement", 8, 0, 192, "Amount of crops that need to be in an area for a crow to spawn nearby. Used alongside the crowSpawnRadius config to control crow spawning.\n" + "Setting to 0 disables crop-based crow spawning. Crows can still spawn near naturally generated nests unless crow.spawnNearNests is false");

        CROW_SPAWN_RADIUS = range("crow.crowSpawnRadius", 8, 0, 64, "Radius that crows check for crops to be in when trying to spawn. Larger radius means higher changes of spawning.\n" + "Setting to 0 would prevent crow spawning");

        CROW_SCARE_RADIUS = range("crow.crowScareRadius", 6, 0, 64, "Radius that players, villgers, and repelling blocks will be effective towards scaring wild crows.\n" + "Setting to 0 would prevent crows from being scared");

        CROW_SPAWN_NEAR_NESTS = flag("crow.spawnNearNests", true, "Whether crows can spawn near naturally generated nests without needing nearby crops");

        CHICKENS_SEEK_NESTS = flag("nests.chickensSeekNests", true, "Whether chickens walk to a nest before laying an egg. Disable if another mod already adds nest-seeking behavior");

        GENERATE_NESTS = flag("nests.generateNests", true, "Whether nests generate naturally in the world");

        CROW_STEAL_SHINY_ITEMS = flag("crow.stealShinyItems", true, "Whether wild crows snatch shiny items off the ground and carry them to nests");

        CROW_FETCH_ITEMS = flag("crow.fetchItems", true, "Whether tamed crows pick up nearby items and bring them to their owner");

        CROW_FETCH_THROWN_ITEMS = flag("crow.fetchThrownItems", true, "Whether tamed crows also fetch items their owner threw away");

        CROW_LEAVE_SHOULDER_TO_FETCH = flag("crow.leaveShoulderToFetch", true, "Whether tamed crows hop off their owner's shoulder to fetch nearby items");

        CROW_EAT_DROPPED_FOOD = flag("crow.eatDroppedFood", true, "Whether wild crows eat crow food dropped on the ground, with a chance to be tamed if a player threw it");

        CROW_TEMPTING = flag("crow.tempting", true, "Whether wild crows are drawn to players holding crow food and slowly learn to trust them");

        CROW_FLOCK_ALARM = flag("crow.flockAlarm", true, "Whether hurting a wild crow makes nearby crows flee from the attacker");

        CROW_EAT_CROPS = flag("crow.eatCrops", true, "Whether wild crows peck at and damage crops. Also requires the mobGriefing gamerule");

        SALTED_HUNGER_BONUS = range("salt.saltedHungerBonus", 0.2D, 0.0D, 1.0D, "Multiplier applied to a food's nutrition value to determine bonus hunger granted when eating salted food.\n" + "For example, 0.2 means a food restoring 5 hunger gets +1 bonus hunger (20% of 5, minimum 1 for foods with less than 5 hunger).");

        SALTED_SATURATION_PENALTY = range("salt.saltedSaturationPenalty", 0.1D, 0.0D, 1.0D, "Fraction of the saturation granted by a food that is removed when eating salted food.\n" + "For example, 0.1 means 10% of the saturation normally given is taken away.");

        SALT_ANIMAL_RADIUS = range("salt.saltAnimalRadius", 12, 1, 64, "Radius in blocks that animals are kept within when near a salt block.");

        SALT_LICK_INTERVAL = range("salt.saltLickInterval", 9600, 200, 72000, "Ticks between each animal licking a nearby salt block. 9600 = 8 minutes.");

        SALT_PLAYER_LICK_CHANCE = range("salt.saltPlayerLickChance", 0.05D, 0.0D, 1.0D, "Chance (0.0–1.0) that a player's right-click lick degrades the salt block.");

        SALT_CAVES_ENABLED = flag("salt.generateSaltCaves", true, "Whether salt caves generate underground in biomes tagged #hearthandharvest:has_salt_caves");

        SALT_CAVE_RARITY = range("salt.saltCaveRarity", 24, 1, 1000, "Salt caves attempt to generate on average once every this many chunks in eligible biomes");

        SALT_CAVE_MIN_Y = range("salt.saltCaveMinY", -40, -64, 320, "Lowest Y level a salt cave can be centered on. Caves always stay clear of bedrock");

        SALT_CAVE_MAX_Y = range("salt.saltCaveMaxY", 30, -64, 320, "Highest Y level a salt cave can be centered on");

        PLAYER_POOP_ENABLED = flag("manure.playerPoopEnabled", true, "Whether players are able to poop using the poop keybind");

        MANURE_FED_POOP_ENABLED = flag("manure.fedPoopEnabled", true, "Drop manure after being fed");

        MANURE_RANDOM_POOP_ENABLED = flag("manure.randomPoopEnabled", false, "Drop manure randomly over time");

        MANURE_RANDOM_POOP_CHANCE = range("manure.randomPoopChance", 300, 1, 10000, "1-in-N chance per second for random drop");

        DISABLE_PIG_LITTERS = flag("breeding.disablePigLitters", false, "Disables the extra baby pigs spawned when pigs breed");

        DISABLE_RABBIT_LITTERS = flag("breeding.disableRabbitLitters", false, "Disables the extra baby rabbits spawned when rabbits breed");

        DISABLE_CHICKEN_PLUCKING = flag("breeding.disableChickenPlucking", false, "Disables shift-right-click plucking feathers from chickens");

        CHICKEN_GLIDING = flag("breeding.chickenGliding", true, "Whether players can pick up a chicken with an empty hand and glide while holding it overhead. Sneak to put it down");

        COMMON_CONFIG = BUILDER.build();
    }

    private static ModConfigSpec.BooleanValue flag(String key, boolean defaultValue, String comment) {
        return BUILDER.comment(comment).define(key, defaultValue);
    }

    private static ModConfigSpec.IntValue range(String key, int defaultValue, int min, int max, String comment) {
        return BUILDER.comment(comment).defineInRange(key, defaultValue, min, max);
    }

    private static ModConfigSpec.DoubleValue range(String key, double defaultValue, double min, double max, String comment) {
        return BUILDER.comment(comment).defineInRange(key, defaultValue, min, max);
    }

    private static void put(ModConfigSpec.Builder builder, String name) {
        ITEMS.put(name, builder.define(name, true));
    }

    private static boolean contains(String item) {
        return ITEMS.containsKey(item);
    }
}