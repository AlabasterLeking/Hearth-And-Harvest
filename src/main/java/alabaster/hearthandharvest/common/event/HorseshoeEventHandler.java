package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class HorseshoeEventHandler {

    public static final ResourceLocation SPEED_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "horseshoe_speed");

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (horse.level().isClientSide()) return;

        AttributeInstance speedAttr = horse.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean shod = !horse.getData(HHModAttachments.HORSESHOE_ITEM).isEmpty();

        if (!shod) {
            if (speedAttr != null && speedAttr.hasModifier(SPEED_MODIFIER_ID))
                speedAttr.removeModifier(SPEED_MODIFIER_ID);
            return;
        }

        if (!horse.hasEffect(MobEffects.FIRE_RESISTANCE))
            horse.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, false, false));

        if (speedAttr != null) {
            BlockState below = horse.level().getBlockState(horse.blockPosition().below());
            float factor = below.getBlock().getSpeedFactor();
            if (factor < 1.0f && horse.onGround()) {
                speedAttr.addOrUpdateTransientModifier(new AttributeModifier(
                        SPEED_MODIFIER_ID,
                        (1.0f / factor) - 1.0f,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            } else {
                if (speedAttr.hasModifier(SPEED_MODIFIER_ID))
                    speedAttr.removeModifier(SPEED_MODIFIER_ID);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        ItemStack shoe = horse.getData(HHModAttachments.HORSESHOE_ITEM);
        if (shoe.isEmpty()) return;
        if (horse.level().isClientSide()) return;
        horse.spawnAtLocation(shoe.copy());
        horse.setData(HHModAttachments.HORSESHOE_ITEM, ItemStack.EMPTY);
    }
}