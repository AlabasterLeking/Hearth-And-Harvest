package alabaster.hearthandharvest.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HorseBoostEffect extends MobEffect {
    public HorseBoostEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF5C542);
        addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "76bd2dcd-456b-350b-a0c1-cdfd87c48aa9",
                0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.JUMP_STRENGTH,
                "fce8017f-8ffa-33c8-9c5d-b83f441d6907",
                0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}