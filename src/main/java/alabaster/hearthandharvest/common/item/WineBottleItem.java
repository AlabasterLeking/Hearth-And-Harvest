package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.registry.HHModDataComponents;
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
import net.minecraft.util.Mth;
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

public class WineBottleItem extends Item implements AgeableItem {
    private static final int DRUNK_DURATION = 2400;
    private static final int MAX_DRUNK_AMPLIFIER = 4;
    private static final float DRUNK_REDUCTION_PER_VINTAGE = 0.2F;
    private static final int BAR_COLOR = 0x9C2A4A;

    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    private final Supplier<Fluid> fluid;
    private int glasses = 1;
    private boolean ageable;

    public WineBottleItem(Supplier<Fluid> fluid, Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties);
        this.fluid = fluid;
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = hasCustomTooltip;
    }

    public WineBottleItem glasses(int glasses) {
        this.glasses = Math.max(1, glasses);
        return this;
    }

    public WineBottleItem ageable() {
        this.ageable = true;
        return this;
    }

    public Fluid getFluid() {
        return this.fluid.get();
    }

    public int getMaxGlasses() {
        return glasses;
    }

    public int getGlasses(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(HHModDataComponents.SERVINGS.get(), glasses), 1, glasses);
    }

    @Override
    public boolean isAgeable() {
        return ageable;
    }

    @Override
    public boolean canAgeFurther(ItemStack stack) {
        return ageable && !isOpened(stack) && VintageHelper.getVintage(stack) < VintageHelper.MAX_VINTAGE;
    }

    public boolean isOpened(ItemStack stack) {
        return glasses > 1 && getGlasses(stack) < glasses;
    }

    private ItemStack withGlasses(ItemStack stack, int remaining) {
        if (remaining >= glasses) {
            stack.remove(HHModDataComponents.SERVINGS.get());
        } else {
            stack.set(HHModDataComponents.SERVINGS.get(), remaining);
        }
        return stack;
    }

    private ItemStack leftoverAfterGlass(ItemStack stack) {
        int remaining = getGlasses(stack) - 1;
        return remaining > 0
                ? withGlasses(stack.copyWithCount(1), remaining)
                : new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        FoodProperties food = stack.getFoodProperties(player);
        if (food != null) {
            if (player.canEat(food.canAlwaysEat())) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            return InteractionResultHolder.fail(stack);
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
        FoodProperties food = stack.getFoodProperties(consumer);
        Player player = consumer instanceof Player p ? p : null;

        if (food == null) {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }
            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return giveBack(stack, new ItemStack(Items.GLASS_BOTTLE), player);
        }

        int vintage = VintageHelper.getVintage(stack);
        if (!level.isClientSide) {
            applyDrunk(food, vintage, level, consumer);
        }
        consumer.eat(level, stack.copyWithCount(1), VintageHelper.scale(food, vintage, 0.0F, HHModEffects.DRUNK));

        if (player != null && player.getAbilities().instabuild) {
            return stack;
        }

        ItemStack leftover = leftoverAfterGlass(stack);
        stack.shrink(1);
        return giveBack(stack, leftover, player);
    }

    private static ItemStack giveBack(ItemStack stack, ItemStack leftover, Player player) {
        if (stack.isEmpty()) {
            return leftover;
        }
        if (player != null && !player.getAbilities().instabuild && !player.getInventory().add(leftover)) {
            player.drop(leftover, false);
        }
        return stack;
    }

    private static void applyDrunk(FoodProperties food, int vintage, Level level, LivingEntity consumer) {
        float drunkProbability = 0.0F;
        int baseAmplifier = 0;
        for (FoodProperties.PossibleEffect possible : food.effects()) {
            MobEffectInstance effect = possible.effect();
            if (effect.getEffect().value() == HHModEffects.DRUNK.value()) {
                drunkProbability = possible.probability();
                baseAmplifier = effect.getAmplifier();
                break;
            }
        }
        drunkProbability *= Math.max(0.0F, 1.0F - DRUNK_REDUCTION_PER_VINTAGE * vintage);
        if (drunkProbability <= 0.0F || level.random.nextFloat() >= drunkProbability) return;

        MobEffectInstance existing = consumer.getEffect(HHModEffects.DRUNK);
        int escalated = (existing != null ? existing.getAmplifier() : -1) + 1;
        int amplifier = Math.min(Math.max(escalated, baseAmplifier), MAX_DRUNK_AMPLIFIER);
        consumer.addEffect(new MobEffectInstance(HHModEffects.DRUNK, DRUNK_DURATION, amplifier, false, true));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return glasses > 1 ? leftoverAfterGlass(stack) : super.getCraftingRemainingItem(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isOpened(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getGlasses(stack) / glasses);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        VintageHelper.appendTooltip(stack, tooltip);
        if (glasses > 1) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.glasses", getGlasses(stack), glasses).withStyle(ChatFormatting.GRAY));
        }
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath());
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip::add, VintageHelper.durationFactor(stack), context.tickRate());
            }
        }
    }
}