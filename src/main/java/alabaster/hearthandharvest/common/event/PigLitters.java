package alabaster.hearthandharvest.common.event;

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
        Level world = event.getParentA().level();
        RandomSource random = event.getParentA().getRandom();
        int extraCount = 1 + random.nextInt(3); // 1–3 extras; vanilla spawns 1, total = 2–4

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