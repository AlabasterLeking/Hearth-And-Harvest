package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.ManureDropHelper;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModParticleTypes;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = "hearthandharvest", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManureEvents {

    // Player hand-feeds an animal. Fires before the interaction resolves, so we use
    // isFood() + !isInLove() as a proxy for "this will set love mode". Stays Animal-only:
    // love mode / isFood() don't exist for arbitrary mobs, so this can't generalize to CAN_POOP.
    @SubscribeEvent
    public static void onPlayerFeedAnimal(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        Player player = event.getEntity();
        ItemStack item = player.getItemInHand(event.getHand());
        if (!animal.isInLove() && animal.isFood(item)) {
            ManureDropHelper.schedulePoop(animal);
        }
    }

    // Fed-poop countdown and random chance. Fires every tick per entity.
    // Eligible entities are Animals (default) or anything explicitly tagged CAN_POOP.
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity self = event.getEntity();
        if (self.level().isClientSide) return;
        if (!(self instanceof Animal) && !self.getType().is(HHModTags.CAN_POOP)) return;
        if (!ManureDropHelper.canPoop(self)) return;

        if (Config.MANURE_FED_POOP_ENABLED.get()) {
            int timer = HHModAttachments.MANURE_POOP_TIMER.get(self);
            if (timer > 0) {
                HHModAttachments.MANURE_POOP_TIMER.set(self, timer - 1);
                if (timer == 1) ManureDropHelper.dropPoop(self);
            }
        }

        if (Config.MANURE_RANDOM_POOP_ENABLED.get()
                && self.tickCount % 20 == 0
                && self.getRandom().nextInt(Config.MANURE_RANDOM_POOP_CHANCE.get()) == 0) {
            ManureDropHelper.dropPoop(self);
        }
    }

    // Ticks down MANURE_FLY_TICKS on any living entity hit by a manure projectile
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide) return;
        int flyTicks = HHModAttachments.MANURE_FLY_TICKS.get(living);
        if (flyTicks <= 0) return;
        HHModAttachments.MANURE_FLY_TICKS.set(living, flyTicks - 1);
        if (flyTicks % 4 != 0) return;
        if (!(living.level() instanceof ServerLevel serverLevel)) return;
        serverLevel.sendParticles(HHModParticleTypes.FLIES.get(),
                living.getX(),
                living.getY() + living.getBbHeight() * 0.5,
                living.getZ(),
                2,
                living.getBbWidth() * 0.5,
                living.getBbHeight() * 0.4,
                living.getBbWidth() * 0.5,
                0.02);
    }
}