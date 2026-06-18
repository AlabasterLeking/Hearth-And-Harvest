package alabaster.hearthandharvest.common.effect;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;

public class PinnedEffect extends MobEffect {

    public PinnedEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B4513);
        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "pinned_slowness"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }
}