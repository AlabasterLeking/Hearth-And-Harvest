package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.function.Supplier;

public class WineBottleItem extends Item {

    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;

    private final Supplier<Fluid> fluid;

    public WineBottleItem(Supplier<Fluid> fluid, Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties);
        this.fluid = fluid;
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = hasCustomTooltip;
    }

    public Fluid getFluid() {
        return this.fluid.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        FoodProperties food = stack.getFoodProperties(player);
        if (food != null) {
            if (player.canEat(food.canAlwaysEat())) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return HHModSounds.WINE_DRINK.get();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack containerItem = new ItemStack(Items.GLASS_BOTTLE);
        FoodProperties food = stack.getFoodProperties(consumer);
        Player player = consumer instanceof Player p ? p : null;

        if (!level.isClientSide && food != null) {
            float drunkProbability = 0.0F;
            for (FoodProperties.PossibleEffect possible : food.effects()) {
                if (possible.effect().getEffect().value() == HHModEffects.DRUNK.value()) {
                    drunkProbability = possible.probability();
                    break;
                }
            }
            if (drunkProbability > 0.0F && level.random.nextFloat() < drunkProbability) {
                int currentAmplifier = -1;
                MobEffectInstance existing = consumer.getEffect(HHModEffects.DRUNK);
                if (existing != null) currentAmplifier = existing.getAmplifier();
                int newAmplifier = Math.min(currentAmplifier + 1, 4);
                consumer.addEffect(new MobEffectInstance(HHModEffects.DRUNK, 2400, newAmplifier, false, true));
            }
        }

        if (food != null) {
            super.finishUsingItem(stack, level, consumer);
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }
            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        if (stack.isEmpty()) {
            return containerItem;
        } else {
            if (player != null && !player.getAbilities().instabuild && !player.getInventory().add(containerItem)) {
                player.drop(containerItem, false);
            }
            return stack;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath());
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
            }
        }
    }
}