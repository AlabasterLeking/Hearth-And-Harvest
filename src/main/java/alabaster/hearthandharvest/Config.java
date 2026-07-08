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
    public static ModConfigSpec.BooleanValue STACK_WATER_BOTTLES;
    public static ModConfigSpec.BooleanValue GENERATE_CORN_MAZES;
    public static ModConfigSpec.BooleanValue DISABLE_BOTTLE_MILKING;
    public static ModConfigSpec.BooleanValue TRELLIS_PLACEMENT_PREVIEW;
    public static ModConfigSpec.DoubleValue SALTED_HUNGER_BONUS;
    public static ModConfigSpec.DoubleValue SALTED_SATURATION_PENALTY;
    public static ModConfigSpec.IntValue SALT_ANIMAL_RADIUS;
    public static ModConfigSpec.IntValue SALT_LICK_INTERVAL;
    public static ModConfigSpec.DoubleValue SALT_PLAYER_LICK_CHANCE;
    public static ModConfigSpec.IntValue TROUGH_ANIMAL_CAP;
    public static ModConfigSpec.BooleanValue PLAYER_POOP_ENABLED;
    public static ModConfigSpec.BooleanValue MANURE_FED_POOP_ENABLED;
    public static ModConfigSpec.BooleanValue MANURE_RANDOM_POOP_ENABLED;
    public static ModConfigSpec.IntValue MANURE_RANDOM_POOP_CHANCE;

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

        TRELLIS_PLACEMENT_PREVIEW = COMMON_BUILDER
                .comment("Whether a ghost preview of the trellis piece is shown before placing")
                .define("trellisPlacementPreview", true);

        TROUGH_ANIMAL_CAP = COMMON_BUILDER
                .comment("Max animals in a 10-block radius before the food trough stops triggering breeding.")
                .defineInRange("troughBreedingCap", 16, 1, 256);

        CROW_SPAWN_NUMBER_OF_CROPS = COMMON_BUILDER
                .comment("Amount of crops that need to be in an area for a crow to spawn nearby. Used alongside the crowSpawnRadius config to control crow spawning.\n" +
                        "Setting to 0 would prevent crow spawning")
                .defineInRange("crow.crowCropRequirement", 8, 0, 192);

        CROW_SPAWN_RADIUS = COMMON_BUILDER
                .comment("Radius that crows check for crops to be in when trying to spawn. Larger radius means higher changes of spawning.\n" +
                        "Setting to 0 would prevent crow spawning")
                .defineInRange("crow.crowSpawnRadius", 8, 0, 64);

        CROW_SCARE_RADIUS = COMMON_BUILDER
                .comment("Radius that players, villgers, and repelling blocks will be effective towards scaring wild crows.\n" +
                        "Setting to 0 would prevent crows from being scared")
                .defineInRange("crow.crowScareRadius", 6, 0, 64);

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

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void put(ModConfigSpec.Builder builder, String name) {
        ITEMS.put(name, builder.define(name, true));
    }

    private static boolean contains(String item) {
        return ITEMS.containsKey(item);
    }
}