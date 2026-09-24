package alabaster.hearthandharvest.common.effect;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class DrunkEffect extends MobEffect {

    public DrunkEffect() {
        super(MobEffectCategory.HARMFUL, 0x7d4b2c);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "effect.drunk_courage_damage"),
                0.5D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "effect.drunk_courage_knockback"),
                0.05D, AttributeModifier.Operation.ADD_VALUE);
    }

    /*
     * Amplifier levels:
     * 0 (Drunk I)   - Slight attack damage and knockback resistance (scales with level)
     * 1 (Drunk II)  - Very mild saturation drain
     * 2 (Drunk III) - + Slowness I, Mild saturation drain
     * 3 (Drunk IV)  - + Slowness II, Weakness I, Stumbling, Moderate saturation drain
     * 4 (Drunk V)   - + Slowness II, Weakness I, Blindness, Stumbling, Saturation drain, Random hotbar switching,
     * Random item dropping, No new beneficial effects can be obtained
     */

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel)) return true;

        int duration = 80;

        if (amplifier >= 1) {
            drainHunger(entity, amplifier);
        }
        if (amplifier >= 2) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 0, true, false));
        }
        if (amplifier >= 3) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, true, false));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,          duration, 0, true, false));
            applyStumble(entity);
        }
        if (amplifier >= 4) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, true, false));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,          duration, 0, true, false));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,         duration, 0, true, false));
            applyStumble(entity);
            randomHotbarSwitch(entity);
            randomItemDrop(entity);
        }

        return true;
    }

    private void drainHunger(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return;

        // Only ever drains saturation — food level is never touched
        float saturationDrain = switch (amplifier) {
            case 1 -> 0.1f;
            case 2 -> 0.2f;
            case 3 -> 0.4f;
            default -> 0.8f;
        };

        float newSaturation = Math.max(0, player.getFoodData().getSaturationLevel() - saturationDrain);
        player.getFoodData().setSaturation(newSaturation);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return tickCount % 20 == 0;
    }

    private void randomHotbarSwitch(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        RandomSource rand = entity.level().getRandom();

        // 1-in-5 chance to switch hotbar slot
        if (rand.nextInt(5) != 0) return;

        int newSlot = rand.nextInt(9);
        player.getInventory().selected = newSlot;
    }

    private void randomItemDrop(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        RandomSource rand = entity.level().getRandom();

        // 1-in-8 chance to drop held item
        if (rand.nextInt(8) != 0) return;

        player.drop(player.getMainHandItem().split(1), false);
    }

    private void applyStumble(LivingEntity entity) {
        RandomSource rand = entity.level().getRandom();

        if (rand.nextInt(2) != 0) return;

        int stumbleType = rand.nextInt(6);

        switch (stumbleType) {
            case 0, 1 -> {
                // Lurch sideways relative to facing direction
                double yaw = Math.toRadians(entity.getYRot());
                double side = rand.nextBoolean() ? 1 : -1;
                double force = 0.3 + rand.nextDouble() * 0.3;
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                        Math.cos(yaw) * side * force,
                        0.1,
                        Math.sin(yaw) * side * force
                ));
            }
            case 2, 3 -> {
                // Stagger forward or backward
                double yaw = Math.toRadians(entity.getYRot());
                double direction = rand.nextBoolean() ? 1 : -1;
                double force = 0.25 + rand.nextDouble() * 0.25;
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                        -Math.sin(yaw) * direction * force,
                        0.12,
                        Math.cos(yaw) * direction * force
                ));
            }
            case 4 -> {
                // Heavy trip — strong random direction with upward launch
                double angle = rand.nextDouble() * Math.PI * 2;
                double force = 0.45 + rand.nextDouble() * 0.2;
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                        Math.sin(angle) * force,
                        0.28,
                        Math.cos(angle) * force
                ));
            }
            case 5 -> {
                // Spin stumble — sharp rotation snap + sideways lurch
                entity.setYRot(entity.getYRot() + (rand.nextBoolean() ? 45 : -45) * (0.5f + rand.nextFloat()));
                double angle = rand.nextDouble() * Math.PI * 2;
                double force = 0.3 + rand.nextDouble() * 0.15;
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                        Math.sin(angle) * force,
                        0.1,
                        Math.cos(angle) * force
                ));
            }
        }

        entity.hurtMarked = true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }
}