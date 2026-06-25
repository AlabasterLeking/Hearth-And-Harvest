package alabaster.hearthandharvest.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UniversalFeedItem extends Item {
    public UniversalFeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Animal animal) {
            if (!animal.isBaby() && animal.canFallInLove()) {
                animal.setInLove(player);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.hearthandharvest.universal_feed").withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}