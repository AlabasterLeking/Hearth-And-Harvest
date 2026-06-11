package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.entity.ManureDropHelper;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModParticleTypes;
import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = "hearthandharvest", bus = EventBusSubscriber.Bus.GAME)
public class ManureEvents {

    // Player hand-feeds an animal. Fires before the interaction resolves, so we use
    // isFood() + !isInLove() as a proxy for "this will set love mode".
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
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Animal self)) return;
        if (self.level().isClientSide) return;
        if (!ManureDropHelper.canPoop(self)) return;

        if (Config.MANURE_FED_POOP_ENABLED.get()) {
            int timer = self.getData(HHModAttachments.MANURE_POOP_TIMER.get());
            if (timer > 0) {
                self.setData(HHModAttachments.MANURE_POOP_TIMER.get(), timer - 1);
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
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living)) return;
        if (living.level().isClientSide) return;
        int flyTicks = living.getData(HHModAttachments.MANURE_FLY_TICKS.get());
        if (flyTicks <= 0) return;
        living.setData(HHModAttachments.MANURE_FLY_TICKS.get(), flyTicks - 1);
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