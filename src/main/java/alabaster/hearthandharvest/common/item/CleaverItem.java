package alabaster.hearthandharvest.common.item;

import javax.annotation.Nullable;
import alabaster.hearthandharvest.common.entity.cleaver.ThrownCleaver;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModEnchantments;

import java.util.List;

public class CleaverItem extends KnifeItem {

    private static final int MIN_THROW_TICKS = 5;

    public CleaverItem(Tier tier, Properties properties) {
        super(tier, 2.0F, -3.0F, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.hearthandharvest.cleaver.butchering").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == ModEnchantments.BACKSTABBING.get()) return false;
        if (enchantment == Enchantments.LOYALTY) return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack remainder = stack.copy();
        remainder.setCount(1);
        if (remainder.isDamageableItem()) {
            remainder.setDamageValue(remainder.getDamageValue() + 1);
            if (remainder.getDamageValue() >= remainder.getMaxDamage()) return ItemStack.EMPTY;
        }
        return remainder;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) return InteractionResultHolder.fail(stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        if (getUseDuration(stack) - timeLeft < MIN_THROW_TICKS) return;
        if (level.isClientSide) return;
        EquipmentSlot slot = player.getOffhandItem() == stack ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        stack.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(slot));
        ThrownCleaver thrown = new ThrownCleaver(level, player, stack);
        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 1.0f);
        level.addFreshEntity(thrown);
        level.playSound(null, thrown, HHModSounds.CLEAVER_THROW.get(), SoundSource.PLAYERS, 1.0f, 1.2f);
        if (!player.getAbilities().instabuild) player.getInventory().removeItem(stack);
        player.awardStat(Stats.ITEM_USED.get(this));
    }
}