package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.entity.horseshoe.ThrownHorseshoe;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class HorseshoeItem extends Item {

    private static final int MIN_THROW_TICKS = 5;

    public HorseshoeItem(Properties properties) {
        super(properties);
    }

    // Right-click tamed horse to equip.
    // Sneak + right-click an already-shod horse to remove and return the horseshoe.
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof AbstractHorse horse)) return InteractionResult.PASS;
        if (!horse.isTamed()) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;
        boolean shod = !horse.getData(HHModAttachments.HORSESHOE_ITEM).isEmpty();
        if (shod && player.isCrouching()) {
            ItemStack shoe = horse.getData(HHModAttachments.HORSESHOE_ITEM).copy();
            horse.setData(HHModAttachments.HORSESHOE_ITEM, ItemStack.EMPTY);
            if (!player.getInventory().add(shoe)) player.drop(shoe, false);
            horse.level().playSound(null, horse, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.PLAYERS, 1.0f, 0.8f);
            return InteractionResult.SUCCESS;
        }
        if (!shod) {
            horse.setData(HHModAttachments.HORSESHOE_ITEM, stack.copyWithCount(1));
            if (!player.getAbilities().instabuild) stack.shrink(1);
            horse.level().playSound(null, horse, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.PLAYERS, 1.0f, 1.2f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        if (getUseDuration(stack, entity) - timeLeft < MIN_THROW_TICKS) return;
        if (level.isClientSide) return;
        ThrownHorseshoe thrown = new ThrownHorseshoe(level, player, stack);
        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 1.0f);
        level.addFreshEntity(thrown);
        level.playSound(null, thrown, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!player.getAbilities().instabuild) player.getInventory().removeItem(stack);
        player.awardStat(Stats.ITEM_USED.get(this));
    }
}