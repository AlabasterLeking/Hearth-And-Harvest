package alabaster.hearthandharvest.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PinnedEffect extends MobEffect {

    public PinnedEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B4513);
        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "d6b9ad5c-bff3-3bd3-b5ef-6b12da63e6ce",
                -1.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                "310c7f89-032a-370a-8fcc-7b792bbadde8",
                -1.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}