package alabaster.hearthandharvest.common.event;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.MobEffectEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class EffectEvents {

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        // Clarity
        MobEffectInstance incoming = event.getEffectInstance();

        if ((incoming.getEffect() == MobEffects.BLINDNESS || incoming.getEffect() == MobEffects.DARKNESS)
                && entity.hasEffect(HHModEffects.CLARITY.get())) {
            event.setResult(Event.Result.DENY);
            return;
        }

        // Drunk
        if (incoming.getEffect() == HHModEffects.DRUNK.get()) return;
        if (incoming.getEffect().getCategory() != MobEffectCategory.BENEFICIAL) return;

        MobEffectInstance drunkInstance = entity.getActiveEffects().stream()
                .filter(e -> e.getEffect() == HHModEffects.DRUNK.get())
                .findFirst()
                .orElse(null);

        if (drunkInstance != null && drunkInstance.getAmplifier() >= 4) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == HHModEffects.CLARITY.get()) {
            event.getEntity().removeEffect(MobEffects.BLINDNESS);
            event.getEntity().removeEffect(MobEffects.DARKNESS);
        }
    }

    @SubscribeEvent
    public static void onPinnedJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.hasEffect(HHModEffects.PINNED.get())) return;
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }
}
