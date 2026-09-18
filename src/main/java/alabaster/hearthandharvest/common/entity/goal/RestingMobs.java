package alabaster.hearthandharvest.common.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.camel.Camel;

public final class RestingMobs {
    private RestingMobs() {
    }

    public static boolean isResting(Mob mob) {
        if (mob.isPassenger() || mob.isSleeping()) return true;
        if (mob instanceof TamableAnimal tamable && (tamable.isOrderedToSit() || tamable.isInSittingPose())) return true;
        if (mob instanceof Fox fox && fox.isSitting()) return true;
        if (mob instanceof Cat cat && cat.isLying()) return true;
        if (mob instanceof Panda panda && panda.isSitting()) return true;
        return mob instanceof Camel camel && camel.isCamelSitting();
    }
}