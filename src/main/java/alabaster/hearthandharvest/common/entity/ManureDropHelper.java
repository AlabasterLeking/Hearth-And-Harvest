package alabaster.hearthandharvest.common.entity;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;

public final class ManureDropHelper {

    private ManureDropHelper() {}

    public static boolean canPoop(LivingEntity entity) {
        return !entity.isBaby()
                && !entity.hasCustomName()
                && !entity.getType().is(HHModTags.DOES_NOT_POOP);
    }

    // Called when an animal is fed. Schedules a poop 30–60 seconds later.
    public static void schedulePoop(Animal animal) {
        if (animal.level().isClientSide) return;
        if (!Config.MANURE_FED_POOP_ENABLED.get()) return;
        if (!canPoop(animal)) return;
        int delay = 600 + animal.getRandom().nextInt(601);
        animal.setData(HHModAttachments.MANURE_POOP_TIMER.get(), delay);
    }

    public static void dropPoop(LivingEntity entity) {
        entity.spawnAtLocation(HHModItems.MANURE.get());
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                HHModSounds.FART.get(), SoundSource.NEUTRAL,
                0.5f, 0.5f + entity.getRandom().nextFloat() * 1.5f);
    }
}