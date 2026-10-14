package alabaster.hearthandharvest.common.event;

import java.util.UUID;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class HorseshoeEventHandler {

    public static final UUID SPEED_MODIFIER_ID = UUID.fromString("9e7c1f20-3b4a-4d8e-b6f2-1a5c0d9e7f33");

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (horse.level().isClientSide()) return;

        AttributeInstance speedAttr = horse.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean shod = !HHModAttachments.HORSESHOE_ITEM.get(horse).isEmpty();

        if (!shod) {
            if (speedAttr != null && speedAttr.getModifier(SPEED_MODIFIER_ID) != null)
                speedAttr.removeModifier(SPEED_MODIFIER_ID);
            return;
        }

        if (!horse.hasEffect(MobEffects.FIRE_RESISTANCE))
            horse.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, false, false));

        if (speedAttr != null) {
            BlockState below = horse.level().getBlockState(horse.blockPosition().below());
            float factor = below.getBlock().getSpeedFactor();
            if (factor < 1.0f && horse.onGround()) {
                speedAttr.removeModifier(SPEED_MODIFIER_ID);
                speedAttr.addTransientModifier(new AttributeModifier(
                        SPEED_MODIFIER_ID,
                        "Horseshoe speed",
                        (1.0f / factor) - 1.0f,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
            } else {
                if (speedAttr.getModifier(SPEED_MODIFIER_ID) != null)
                    speedAttr.removeModifier(SPEED_MODIFIER_ID);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        ItemStack shoe = HHModAttachments.HORSESHOE_ITEM.get(horse);
        if (shoe.isEmpty()) return;
        if (horse.level().isClientSide()) return;
        horse.spawnAtLocation(shoe.copy());
        HHModAttachments.HORSESHOE_ITEM.set(horse, ItemStack.EMPTY);
    }
}