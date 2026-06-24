package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, HearthAndHarvest.MODID);

    // Crow
    public static final Supplier<SoundEvent> CROW_SQUAWK = SOUNDS.register("entities.crow.squawking",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.crow.squawking")));

    public static final Supplier<SoundEvent> CROW_HURT = SOUNDS.register("entities.crow.hurt",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.crow.hurt")));

    public static final Supplier<SoundEvent> CROW_EAT = SOUNDS.register("entities.crow.eat",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.crow.eat")));

    public static final Supplier<SoundEvent> CROW_STEP = SOUNDS.register("entities.crow.step",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.crow.step")));

    // Wine Drink
    public static final Supplier<SoundEvent> WINE_DRINK = SOUNDS.register("items.wine.drink",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.wine.drink")));

    // Bottle Place
    public static final Supplier<SoundEvent> BOTTLE_INSERT = SOUNDS.register("blocks.bottle.insert",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "blocks.bottle.insert")));

    public static final Supplier<SoundEvent> BOTTLE_REMOVE = SOUNDS.register("blocks.bottle.remove",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "blocks.bottle.remove")));

    public static final Supplier<SoundEvent> STOMPING_BASIN_STOMP = SOUNDS.register("blocks.stomping_basin.stomp",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "blocks.stomping_basin.stomp")));

    // Fart
    public static final Supplier<SoundEvent> FART = SOUNDS.register("entities.fart",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.fart")));

    // Lick
    public static final Supplier<SoundEvent> LICK = SOUNDS.register("entities.lick",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "entities.lick")));

    // Pitchfork
    public static final Supplier<SoundEvent> PITCHFORK_THROW = SOUNDS.register("items.pitchfork.throw",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.pitchfork.throw")));
    public static final Supplier<SoundEvent> PITCHFORK_HIT = SOUNDS.register("items.pitchfork.hit",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.pitchfork.hit")));

    // Cleaver
    public static final Supplier<SoundEvent> CLEAVER_THROW = SOUNDS.register("items.cleaver.throw",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.cleaver.throw")));
    public static final Supplier<SoundEvent> CLEAVER_HIT = SOUNDS.register("items.cleaver.hit",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.cleaver.hit")));

    // Horseshoe
    public static final Supplier<SoundEvent> HORSESHOE_THROW = SOUNDS.register("items.horseshoe.throw",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.horseshoe.throw")));
    public static final Supplier<SoundEvent> HORSESHOE_HIT = SOUNDS.register("items.horseshoe.hit",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "items.horseshoe.hit")));

    // Misc
    public static final Supplier<SoundEvent> SALT_LAMP_SWITCH = SOUNDS.register("blocks.salt_lamp.switch",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "blocks.salt_lamp.switch")));
}
