package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.entity.goal.StayNearSaltGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

@Mod.EventBusSubscriber(modid = "hearthandharvest", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SaltBlockEvents {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal) {
            animal.goalSelector.addGoal(4, new StayNearSaltGoal(animal));
        }
    }
}