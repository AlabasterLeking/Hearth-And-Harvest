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

    public Config() {
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        TREE_TAPPER_BASE_CHANCE = COMMON_BUILDER
                .comment("Base chance (0.0 - 1.0) per random tick for a Tree Tapper to increase sap when on a tappable block.\n"
                        + "Higher values make sap fill faster.")
                .defineInRange("treeTapperBaseChance", 0.5D, 0.0D, 1.0D);

        CROW_SPAWN_NUMBER_OF_CROPS = COMMON_BUILDER
                .comment("Amount of crops that need to be in an area for a crow to spawn nearby. Used alongside the crowSpawnRadius config to control crow spawning.\n" +
                        "Setting to 0 would prevent crow spawning")
                .defineInRange("crowCropRequirement", 8, 0, 192);

        CROW_SPAWN_RADIUS = COMMON_BUILDER
                .comment("Radius that crows check for crops to be in when trying to spawn. Larger radius means higher changes of spawning.\n" +
                        "Setting to 0 would prevent crow spawning")
                .defineInRange("crowSpawnRadius", 8, 0, 64);

        CROW_SCARE_RADIUS = COMMON_BUILDER
                .comment("Radius that players, villgers, and repelling blocks will be effective towards scaring wild crows.\n" +
                        "Setting to 0 would prevent crows from being scared")
                .defineInRange("crowScareRadius", 6, 0, 64);

        STACK_WATER_BOTTLES = COMMON_BUILDER
                .comment("Whether water bottles should stack up to 16")
                .define("stackWaterBottles", true);

        GENERATE_CORN_MAZES = COMMON_BUILDER
                .comment("Whether corn mazes should spawn in the world")
                .define("generateCornMazes", true);

        DISABLE_BOTTLE_MILKING = COMMON_BUILDER
                .comment("Disables milking cows and goats with glass bottles. \n" +
                        "Be aware that setting this can cause goat milk bottles to be unobtainable unless handled otherwise")
                .define("disableBottleMilking", false);

        TRELLIS_PLACEMENT_PREVIEW = COMMON_BUILDER
                .comment("Whether a ghost preview of the trellis piece is shown before placing")
                .define("trellisPlacementPreview", true);

        SALTED_HUNGER_BONUS = COMMON_BUILDER
                .comment("Multiplier applied to a food's nutrition value to determine bonus hunger granted when eating salted food.\n" +
                        "For example, 0.2 means a food restoring 5 hunger gets +1 bonus hunger (20% of 5, minimum 1 for foods with less than 5 hunger).")
                .defineInRange("saltedHungerBonus", 0.2D, 0.0D, 1.0D);

        SALTED_SATURATION_PENALTY = COMMON_BUILDER
                .comment("Fraction of the saturation granted by a food that is removed when eating salted food.\n" +
                        "For example, 0.1 means 10% of the saturation normally given is taken away.")
                .defineInRange("saltedSaturationPenalty", 0.1D, 0.0D, 1.0D);

        SALT_ANIMAL_RADIUS = COMMON_BUILDER
                .comment("Radius in blocks that animals are kept within when near a salt block.")
                .defineInRange("saltAnimalRadius", 12, 1, 64);

        SALT_LICK_INTERVAL = COMMON_BUILDER
                .comment("Ticks between each animal licking a nearby salt block. 9600 = 8 minutes.")
                .defineInRange("saltLickInterval", 9600, 200, 72000);

        SALT_PLAYER_LICK_CHANCE = COMMON_BUILDER
                .comment("Chance (0.0–1.0) that a player's right-click lick degrades the salt block.")
                .defineInRange("saltPlayerLickChance", 0.05D, 0.0D, 1.0D);

        TROUGH_ANIMAL_CAP = COMMON_BUILDER
                .comment("Max animals in a 10-block radius before the food trough stops triggering breeding.")
                .defineInRange("troughBreedingCap", 16, 1, 256);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void put(ModConfigSpec.Builder builder, String name) {
        ITEMS.put(name, builder.define(name, true));
    }

    private static boolean contains(String item) {
        return ITEMS.containsKey(item);
    }
}