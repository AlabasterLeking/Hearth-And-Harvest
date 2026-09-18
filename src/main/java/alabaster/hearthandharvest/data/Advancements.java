package alabaster.hearthandharvest.data;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Advancements extends AdvancementProvider {

    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {
        private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/block/spruce_planks.png");

        private static final List<Supplier<Item>> MUMS = List.of(
                HHModItems.YELLOW_MUM,
                HHModItems.ORANGE_MUM,
                HHModItems.RED_MUM,
                HHModItems.BLUE_MUM,
                HHModItems.LIGHT_BLUE_MUM,
                HHModItems.PURPLE_MUM,
                HHModItems.PINK_MUM,
                HHModItems.WHITE_MUM);

        private static final List<Supplier<Item>> CRATES = List.of(
                HHModItems.BLUEBERRY_CRATE,
                HHModItems.CHERRY_CRATE,
                HHModItems.RASPBERRY_CRATE,
                HHModItems.RED_GRAPE_CRATE,
                HHModItems.GREEN_GRAPE_CRATE,
                HHModItems.PEANUT_CRATE,
                HHModItems.CORN_CRATE,
                HHModItems.APPLE_CRATE,
                HHModItems.GOLDEN_APPLE_CRATE,
                HHModItems.GOLDEN_CARROT_CRATE,
                HHModItems.GLISTERING_MELON_CRATE,
                HHModItems.POISONOUS_POTATO_CRATE,
                HHModItems.ROTTEN_TOMATO_CRATE,
                HHModItems.GLOW_BERRY_CRATE,
                HHModItems.SWEET_BERRY_CRATE,
                HHModItems.BROWN_MUSHROOM_CRATE,
                HHModItems.RED_MUSHROOM_CRATE,
                HHModItems.CRIMSON_FUNGUS_CRATE,
                HHModItems.WARPED_FUNGUS_CRATE);

        private static final List<Supplier<Item>> JUICES = List.of(
                HHModItems.BLUEBERRY_JUICE,
                HHModItems.CHERRY_JUICE,
                HHModItems.RASPBERRY_JUICE,
                HHModItems.RED_GRAPE_JUICE,
                HHModItems.GREEN_GRAPE_JUICE,
                HHModItems.SWEET_BERRY_JUICE,
                HHModItems.GLOW_BERRY_JUICE);

        private static final List<Supplier<Item>> WINES = List.of(
                HHModItems.BLUEBERRY_WINE,
                HHModItems.CHERRY_WINE,
                HHModItems.RASPBERRY_WINE,
                HHModItems.RED_GRAPE_WINE,
                HHModItems.GREEN_GRAPE_WINE,
                HHModItems.SWEET_BERRY_WINE,
                HHModItems.GLOW_BERRY_WINE,
                HHModItems.MELON_WINE);

        private static final List<Supplier<Item>> SPIRITS = List.of(
                HHModItems.MEAD,
                HHModItems.HARD_CIDER,
                HHModItems.ROOT_BEER,
                HHModItems.MOONSHINE);

        private static final List<Supplier<Item>> CHEESE_WHEELS = List.of(
                HHModItems.CHEDDAR_CHEESE_WHEEL,
                HHModItems.GOAT_CHEESE_WHEEL);

        private static final List<Supplier<Item>> PICKLES = List.of(
                HHModItems.PICKLED_BEETROOTS,
                HHModItems.PICKLED_CABBAGE,
                HHModItems.PICKLED_CARROTS,
                HHModItems.PICKLED_ONIONS,
                HHModItems.PICKLED_POTATOES);

        private static final List<Supplier<Item>> SPREADS = List.of(
                HHModItems.BLUEBERRY_JAM,
                HHModItems.CHERRY_JAM,
                HHModItems.RASPBERRY_JAM,
                HHModItems.GRAPE_JAM,
                HHModItems.APPLE_JAM,
                HHModItems.SWEET_BERRY_JAM,
                HHModItems.GLOW_BERRY_JAM,
                HHModItems.MELON_JAM,
                HHModItems.PEANUT_BUTTER);

        private static final List<Supplier<Item>> SWEETS = List.of(
                HHModItems.CHOCOLATE_BAR,
                HHModItems.BLUEBERRY_MUFFIN,
                HHModItems.RASPBERRY_JAM,
                HHModItems.CARAMEL,
                HHModItems.COTTON_CANDY,
                HHModItems.CANDY_CORN,
                HHModItems.CARAMEL_APPLE);

        private static final List<Supplier<Item>> PIES = List.of(
                HHModItems.BLUEBERRY_PIE,
                HHModItems.RASPBERRY_PIE,
                HHModItems.GRAPE_PIE,
                HHModItems.PEANUT_BUTTER_PIE,
                HHModItems.CHICKEN_POT_PIE);

        private Consumer<AdvancementHolder> saver;
        private ExistingFileHelper fileHelper;

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            this.saver = saver;
            this.fileHelper = existingFileHelper;

            AdvancementHolder root = task(null, "main/root", HHModItems.BLUEBERRIES.get(), PlayerTrigger.TriggerInstance.tick());

            farming(root);
            crows(root);
            brewing(root);
            animals(root);
            salt(root);
            sweets(root);
            tools(root);
        }

        private void farming(AdvancementHolder root) {
            AdvancementHolder shucks = task(root, "farming/shucks", HHModItems.CORN.get(), has(HHModItems.CORN));
            task(shucks, "farming/pop_goes_the_kernel", HHModItems.POPCORN.get(), has(HHModItems.POPCORN));
            goal(shucks, "farming/a_maize_ing", HHModItems.CORN_HUSK.get(), trigger(HHModTriggers.FOUND_CORN_MAZE));

            AdvancementHolder mulch = task(shucks, "farming/mulch_obliged", HHModItems.MULCH.get(), placed(HHModBlocks.MULCH));
            task(mulch, "farming/green_thumb", HHModItems.FERTILIZER_BAG.get(), trigger(HHModTriggers.FERTILIZER_GREW_CROP));
            AdvancementHolder sprinkler = task(mulch, "farming/let_it_rain", HHModItems.SPRINKLER.get(), placed(HHModBlocks.SPRINKLER));
            task(sprinkler, "farming/fire_brigade", Items.WATER_BUCKET, trigger(HHModTriggers.SPRINKLER_EXTINGUISHED));

            AdvancementHolder uprooted = task(shucks, "farming/uprooted", Items.IRON_HOE, trigger(HHModTriggers.HOE_AREA_WORK));
            goal(uprooted, "farming/hats_off", HHModItems.FARMERS_HAT.get(), trigger(HHModTriggers.FARMERS_HAT_WORN_OUT));

            AdvancementHolder trellis = task(root, "farming/climbing_the_walls", HHModItems.TRELLIS.get(),
                    has(HHModItems.TRELLIS, HHModItems.BAMBOO_TRELLIS, HHModItems.STRIPPED_BAMBOO_TRELLIS));
            task(trellis, "farming/grape_expectations", HHModItems.RED_GRAPES.get(), has(HHModItems.RED_GRAPES, HHModItems.GREEN_GRAPES));
            challenge(trellis, "farming/floriculture", HHModItems.RED_MUM.get(), MUMS);
        }

        private void crows(AdvancementHolder root) {
            AdvancementHolder scarecrow = task(root, "farming/scare_tactics", HHModItems.SCARECROW.get(), placed(HHModBlocks.SCARECROW));

            AdvancementHolder robbery = task(scarecrow, "crows/daylight_robbery", Items.GOLD_INGOT, trigger(HHModTriggers.CROW_STOLE_ITEM));
            task(robbery, "crows/stash_found", Items.GOLD_NUGGET, trigger(HHModTriggers.CROW_STASH_FOUND));

            hidden(scarecrow, "crows/murder_scene", HHModItems.CROW_FEATHER.get(), trigger(HHModTriggers.CROW_FLOCK_ALARM));
            goal(scarecrow, "crows/trust_fall", HHModItems.CORN_KERNELS.get(), trigger(HHModTriggers.CROW_FULL_TRUST));

            AdvancementHolder tamed = goal(scarecrow, "crows/caw_panion", HHModItems.CORN_KERNELS.get(),
                    TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(HHModEntities.CROW.get())));
            goal(tamed, "crows/special_delivery", HHModItems.CROW_FEATHER.get(), trigger(HHModTriggers.CROW_DELIVERED_ITEM));
        }

        private void brewing(AdvancementHolder root) {
            AdvancementHolder stomp = task(root, "kitchen/stomp_the_yard", HHModItems.STOMPING_BASIN.get(), trigger(HHModTriggers.STOMPED_RECIPE));
            goal(stomp, "kitchen/big_stomper", HHModItems.STOMPING_BASIN.get(), trigger(HHModTriggers.BIG_STOMP));

            AdvancementHolder cask = task(stomp, "kitchen/patience_is_a_virtue", HHModItems.CASK.get(), trigger(HHModTriggers.CASK_AGED));
            task(cask, "kitchen/say_cheese", HHModItems.CHEDDAR_CHEESE_WHEEL.get(), has(CHEESE_WHEELS));

            AdvancementHolder juice = task(stomp, "kitchen/fresh_squeezed", HHModItems.RED_GRAPE_JUICE.get(), has(JUICES));
            challenge(juice, "kitchen/juice_bar", HHModItems.GREEN_GRAPE_JUICE.get(), JUICES);

            AdvancementHolder wine = save("kitchen/aged_to_perfection", display(juice, "kitchen/aged_to_perfection", HHModItems.RED_GRAPE_WINE.get(), AdvancementType.GOAL, false)
                    .addCriterion("wine", has(WINES))
                    .requirements(AdvancementRequirements.Strategy.AND));
            task(wine, "kitchen/one_too_many", HHModItems.MOONSHINE.get(), effect(HHModEffects.DRUNK));
            task(wine, "tools/seeing_clearly", Items.SPYGLASS, effect(HHModEffects.CLARITY));
            task(wine, "kitchen/well_stocked", HHModItems.OAK_BOTTLE_RACK.get(), trigger(HHModTriggers.FILLED_BOTTLE_RACK));
            challenge(wine, "kitchen/sommelier", HHModItems.GREEN_GRAPE_WINE.get(), WINES);
            challenge(wine, "kitchen/master_distiller", HHModItems.MEAD.get(), SPIRITS);
        }

        private void animals(AdvancementHolder root) {
            AdvancementHolder plucky = task(root, "animals/plucky", Items.FEATHER, trigger(HHModTriggers.PLUCKED_CHICKEN));
            task(plucky, "animals/the_goat", HHModItems.GOAT_MILK_BOTTLE.get(), trigger(HHModTriggers.MILKED_GOAT));
            goal(plucky, "animals/fowl_flight", Items.FEATHER, trigger(HHModTriggers.CHICKEN_GLIDE));
            goal(plucky, "animals/pied_piper", Items.WHEAT, trigger(HHModTriggers.TEMPTING_CROWD));
            goal(plucky, "animals/pig_pile", Items.PORKCHOP, trigger(HHModTriggers.BIG_PIG_LITTER));
            goal(plucky, "animals/bunny_boom", Items.RABBIT_FOOT, trigger(HHModTriggers.BIG_RABBIT_LITTER));

            AdvancementHolder shod = task(plucky, "animals/well_shod", HHModItems.HORSESHOE.get(),
                    PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(HHModItems.HORSESHOE.get()), Optional.empty()));
            task(shod, "animals/sugar_rush", HHModItems.SUGAR_CUBES.get(), trigger(HHModTriggers.FED_SUGAR_CUBES));
            hidden(shod, "animals/horseshoe_toss", HHModItems.HORSESHOE.get(), trigger(HHModTriggers.HORSESHOE_RINGER));

            AdvancementHolder manure = task(plucky, "animals/number_two", HHModItems.MANURE.get(), has(HHModItems.MANURE));
            task(manure, "animals/brick_by_stinky_brick", HHModItems.MANURE_BRICK.get(), has(HHModItems.MANURE_BRICK));
            task(manure, "animals/wet_blanket", Items.ROTTEN_FLESH, trigger(HHModTriggers.PUNGENT_SCARED));
            hidden(manure, "animals/stink_bomb", HHModItems.MANURE.get(), trigger(HHModTriggers.MANURE_HIT));
            hidden(manure, "animals/nature_calls", HHModItems.MANURE.get(), trigger(HHModTriggers.PLAYER_POOPED));
        }

        private void salt(AdvancementHolder root) {
            AdvancementHolder salt = task(root, "kitchen/salt_of_the_earth", HHModItems.SALT.get(), has(HHModItems.SALT));
            task(salt, "kitchen/mood_lighting", HHModItems.SALT_LAMP.get(), placed(HHModBlocks.SALT_LAMP));
            goal(salt, "kitchen/a_lick_too_far", HHModItems.HEAVILY_LICKED_SALT_BLOCK.get(), trigger(HHModTriggers.LICKED_SALT_AWAY));
            task(salt, "kitchen/worth_your_salt", HHModItems.SALT.get(), trigger(HHModTriggers.ATE_SALTED_FOOD));

            AdvancementHolder pickle = task(salt, "kitchen/in_a_pickle", HHModItems.PICKLED_CARROTS.get(), has(PICKLES));
            challenge(pickle, "kitchen/well_preserved", HHModItems.SWEET_BERRY_JAM.get(), Stream.concat(PICKLES.stream(), SPREADS.stream()).toList());
        }

        private void sweets(AdvancementHolder root) {
            AdvancementHolder tapper = task(root, "kitchen/sap_happens", HHModItems.TREE_TAPPER.get(), placed(HHModBlocks.TREE_TAPPER));

            AdvancementHolder syrup = goal(tapper, "kitchen/sugaring_off", HHModItems.SYRUP_BOTTLE.get(), trigger(HHModTriggers.BOTTLED_SYRUP));
            collection(syrup, "kitchen/sweet_tooth", HHModItems.COTTON_CANDY.get(), AdvancementType.GOAL, SWEETS);
            challenge(syrup, "kitchen/pie_chart", HHModItems.BLUEBERRY_PIE.get(), PIES);

            AdvancementHolder roasted = task(tapper, "kitchen/roasting_pro", HHModItems.ROASTED_MARSHMALLOW_STICK.get(), has(HHModItems.ROASTED_MARSHMALLOW_STICK));
            task(roasted, "kitchen/just_one_smore", HHModItems.SMORE.get(), ConsumeItemTrigger.TriggerInstance.usedItem(HHModItems.SMORE.get()));
            hidden(roasted, "kitchen/well_done", HHModItems.CHARRED_MARSHMALLOW_STICK.get(), has(HHModItems.CHARRED_MARSHMALLOW_STICK));
        }

        private void tools(AdvancementHolder root) {
            AdvancementHolder cleaver = task(root, "tools/clever_girl", HHModItems.IRON_CLEAVER.get(), trigger(HHModTriggers.CLEAVER_KILL));
            AdvancementHolder pitchfork = task(cleaver, "tools/pitch_perfect", HHModItems.PITCHFORK.get(), trigger(HHModTriggers.PITCHFORK_KILL));
            hidden(pitchfork, "tools/pinned_down", HHModItems.PITCHFORK.get(), effect(HHModEffects.PINNED));
        }

        private AdvancementHolder task(@Nullable AdvancementHolder parent, String path, ItemLike icon, Criterion<?> criterion) {
            return single(parent, path, icon, AdvancementType.TASK, false, criterion);
        }

        private AdvancementHolder goal(AdvancementHolder parent, String path, ItemLike icon, Criterion<?> criterion) {
            return single(parent, path, icon, AdvancementType.GOAL, false, criterion);
        }

        private AdvancementHolder hidden(AdvancementHolder parent, String path, ItemLike icon, Criterion<?> criterion) {
            return single(parent, path, icon, AdvancementType.TASK, true, criterion);
        }

        private AdvancementHolder challenge(AdvancementHolder parent, String path, ItemLike icon, List<Supplier<Item>> items) {
            return collection(parent, path, icon, AdvancementType.CHALLENGE, items);
        }

        private AdvancementHolder single(@Nullable AdvancementHolder parent, String path, ItemLike icon, AdvancementType type, boolean hidden, Criterion<?> criterion) {
            return save(path, display(parent, path, icon, type, hidden).addCriterion(name(path), criterion));
        }

        private AdvancementHolder collection(AdvancementHolder parent, String path, ItemLike icon, AdvancementType type, List<Supplier<Item>> items) {
            Advancement.Builder builder = display(parent, path, icon, type, false).requirements(AdvancementRequirements.Strategy.AND);
            for (Supplier<Item> item : items) {
                builder.addCriterion(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item.get()));
            }
            return save(path, builder);
        }

        private AdvancementHolder save(String path, Advancement.Builder builder) {
            return builder.save(saver, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, path), fileHelper);
        }

        private static Advancement.Builder display(@Nullable AdvancementHolder parent, String path, ItemLike icon, AdvancementType type, boolean hidden) {
            String key = "advancement." + HearthAndHarvest.MODID + "." + name(path);
            boolean isRoot = parent == null;
            Advancement.Builder builder = Advancement.Builder.advancement().display(
                    icon,
                    Component.translatable(key),
                    Component.translatable(key + ".desc"),
                    isRoot ? BACKGROUND : null,
                    type,
                    !isRoot,
                    !isRoot && type != AdvancementType.TASK,
                    hidden);
            if (!isRoot) builder.parent(parent);
            return builder;
        }

        private static String name(String path) {
            return path.substring(path.lastIndexOf('/') + 1);
        }

        @SafeVarargs
        private static Criterion<InventoryChangeTrigger.TriggerInstance> has(Supplier<? extends ItemLike>... items) {
            return has(List.of(items));
        }

        private static Criterion<InventoryChangeTrigger.TriggerInstance> has(List<? extends Supplier<? extends ItemLike>> items) {
            ItemLike[] resolved = items.stream().map(Supplier::get).toArray(ItemLike[]::new);
            return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(resolved));
        }

        private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placed(Supplier<Block> block) {
            return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block.get());
        }

        private static Criterion<EffectsChangedTrigger.TriggerInstance> effect(Holder<MobEffect> effect) {
            return EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(effect));
        }

        private static Criterion<HHSimpleTrigger.TriggerInstance> trigger(DeferredHolder<CriterionTrigger<?>, HHSimpleTrigger> trigger) {
            return HHModTriggers.criterion(trigger);
        }
    }
}