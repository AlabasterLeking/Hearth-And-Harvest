package alabaster.hearthandharvest.common.item;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import java.util.UUID;
import alabaster.hearthandharvest.common.entity.pitchfork.ThrownPitchfork;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import alabaster.hearthandharvest.common.tag.HHModTags;

public class PitchforkItem extends Item {

    private static final int MIN_THROW_TICKS = 10;

    public PitchforkItem(Properties properties) {
        super(properties);
    }

    private static final UUID DAMAGE_ID = BASE_ATTACK_DAMAGE_UUID;
    private static final UUID SPEED_ID = BASE_ATTACK_SPEED_UUID;
    private Multimap<Attribute, AttributeModifier> attributes;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) return super.getDefaultAttributeModifiers(slot);
        if (attributes == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(DAMAGE_ID, "Weapon modifier", 4.0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(SPEED_ID, "Weapon modifier", -2.6, AttributeModifier.Operation.ADDITION));
            attributes = builder.build();
        }
        return attributes;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 14;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, broken -> broken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
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
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        int chargedTicks = getUseDuration(stack) - timeLeft;
        if (chargedTicks < MIN_THROW_TICKS) return;
        if (level.isClientSide) return;

        stack.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(player.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        ThrownPitchfork thrown = new ThrownPitchfork(level, player, stack);
        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.5f, 1.0f);
        level.addFreshEntity(thrown);
        level.playSound(null, thrown, HHModSounds.PITCHFORK_THROW.get(), SoundSource.PLAYERS, 1.0f, .75f);
        if (!player.getAbilities().instabuild) {
            player.getInventory().removeItem(stack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(HHModTags.MINEABLE_WITH_PITCHFORK)) return Float.MAX_VALUE;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.LOYALTY) return true;
        if (enchantment.category == EnchantmentCategory.WEAPON || enchantment.category == EnchantmentCategory.BREAKABLE) return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return alabaster.hearthandharvest.client.renderer.PitchforkItemRenderer.getInstance();
            }
        });
    }
}
