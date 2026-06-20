package alabaster.hearthandharvest.common.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HorseBoostEffect extends MobEffect {
    public HorseBoostEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF5C542);
        addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath("hearthandharvest", "sugar_cubes_speed"),
                0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.JUMP_STRENGTH,
                ResourceLocation.fromNamespaceAndPath("hearthandharvest", "sugar_cubes_jump"),
                0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}