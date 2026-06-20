package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.List;

public class SugarCubesItem extends ConsumableItem {
    private static final int BOOST_DURATION = 3600; // 3 minutes

    public SugarCubesItem(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof AbstractHorse horse) {
            if (!player.level().isClientSide()) {
                horse.addEffect(new MobEffectInstance(HHModEffects.HORSE_BOOST, BOOST_DURATION));
                horse.level().playSound(null, horse.getX(), horse.getY(), horse.getZ(),
                        SoundEvents.HORSE_EAT, SoundSource.NEUTRAL, 1.0f, 1.0f);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.hearthandharvest.sugar_cubes.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}