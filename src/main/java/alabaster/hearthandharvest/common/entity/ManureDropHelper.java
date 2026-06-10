package alabaster.hearthandharvest.common.entity;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;

public final class ManureDropHelper {

    public static final TagKey<EntityType<?>> NO_POOP_TAG =
            TagKey.create(Registries.ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath("hearthandharvest", "no_poop"));

    private ManureDropHelper() {}

    public static boolean canPoop(Animal animal) {
        return !animal.isBaby()
                && !animal.hasCustomName()
                && !animal.getType().is(NO_POOP_TAG);
    }

    // Called when an animal is fed. Schedules a poop 30–60 seconds later.
    public static void schedulePoop(Animal animal) {
        if (animal.level().isClientSide) return;
        if (!Config.MANURE_FED_POOP_ENABLED.get()) return;
        if (!canPoop(animal)) return;
        int delay = 600 + animal.getRandom().nextInt(601);
        animal.setData(HHModAttachments.MANURE_POOP_TIMER.get(), delay);
    }

    public static void dropPoop(Animal animal) {
        animal.spawnAtLocation(HHModItems.MANURE.get());
        animal.level().playSound(null, animal.getX(), animal.getY(), animal.getZ(),
                HHModSounds.FART.get(), SoundSource.NEUTRAL,
                0.5f, 0.5f + animal.getRandom().nextFloat() * 1.5f);
    }
}