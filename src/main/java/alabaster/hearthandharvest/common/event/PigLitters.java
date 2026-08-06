package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

public class PigLitters {

    @SubscribeEvent
    public void onPigBreed(BabyEntitySpawnEvent event) {
        if (!(event.getParentA() instanceof Pig) || !(event.getParentB() instanceof Pig)) return;
        if (Config.DISABLE_PIG_LITTERS.get()) return;
        Level world = event.getParentA().level();
        RandomSource random = event.getParentA().getRandom();
        int extraCount = 1 + random.nextInt(3);

        for (int i = 0; i < extraCount; i++) {
            Pig babyPig = EntityType.PIG.create(world);
            if (babyPig != null) {
                babyPig.setPos(event.getParentA().getX(), event.getParentA().getY(), event.getParentA().getZ());
                babyPig.setBaby(true);
                world.addFreshEntity(babyPig);
            }
        }
    }
}