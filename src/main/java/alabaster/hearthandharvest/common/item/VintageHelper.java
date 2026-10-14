package alabaster.hearthandharvest.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffect;
import alabaster.hearthandharvest.common.registry.HHDataMaps;
import alabaster.hearthandharvest.common.data.VintageStyle;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public final class VintageHelper {
    public static final int MAX_VINTAGE = 3;
    public static final String[] STAGE_NAMES = {"aged", "fine", "reserve"};

    private static final float DURATION_BONUS_PER_VINTAGE = 0.5F;
    private static final int[] NUTRITION_BONUS = {0, 1, 2, 4};

    private VintageHelper() {
    }

    public static int getVintage(ItemStack stack) {
        return Mth.clamp(HHModDataComponents.VINTAGE.getOrDefault(stack, 0), 0, MAX_VINTAGE);
    }

    public static ItemStack withVintage(ItemStack stack, int vintage) {
        if (vintage <= 0) {
            HHModDataComponents.VINTAGE.remove(stack);
        } else {
            HHModDataComponents.VINTAGE.set(stack, Math.min(vintage, MAX_VINTAGE));
        }
        return stack;
    }

    @Nullable
    public static String stageName(ItemStack stack) {
        int vintage = getVintage(stack);
        return vintage >= 1 && vintage <= MAX_VINTAGE ? STAGE_NAMES[vintage - 1] : null;
    }

    @Nullable
    public static VintageStyle getStyle(ItemStack stack) {
        return HHDataMaps.getVintageStyle(stack.getItem());
    }

    public static boolean isAgeable(ItemStack stack) {
        VintageStyle style = getStyle(stack);
        return style != null && style.ageable();
    }

    public static boolean canAgeFurther(ItemStack stack) {
        return isAgeable(stack) && getVintage(stack) < MAX_VINTAGE;
    }

    @Nullable
    public static String overlayStage(ItemStack stack) {
        VintageStyle style = getStyle(stack);
        if (style == null || !style.hasOverlay()) return null;

        String stage = stageName(stack);
        if (stage == null && style.showFresh() && style.ageable()) {
            return "fresh";
        }
        return stage;
    }

    @Nullable
    public static ResourceLocation overlayModel(ItemStack stack) {
        VintageStyle style = getStyle(stack);
        if (style == null || !style.drawModel()) return null;

        String stage = overlayStage(stack);
        return stage == null ? null : style.modelFor(stage);
    }

    public static float durationFactor(ItemStack stack) {
        return 1.0F + DURATION_BONUS_PER_VINTAGE * getVintage(stack);
    }

    public static ItemStack aged(ItemStack stack) {
        return withVintage(stack.copyWithCount(1), getVintage(stack) + 1);
    }

    public static FoodProperties scale(FoodProperties food, int vintage, float saturationBonusPerVintage, @Nullable MobEffect excluded) {
        return build(food, food.getNutrition(), vintage, saturationBonusPerVintage, excluded);
    }

    private static FoodProperties build(FoodProperties food, int nutrition, int vintage, float saturationBonusPerVintage, @Nullable MobEffect excluded) {
        FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition);
        float absolute = food.getNutrition() * 2.0F * (food.getSaturationModifier() + saturationBonusPerVintage * vintage);
        builder.saturationMod(nutrition > 0 ? absolute / (nutrition * 2.0F) : food.getSaturationModifier());
        if (food.isMeat()) builder.meat();
        if (food.canAlwaysEat()) builder.alwaysEat();
        if (food.isFastFood()) builder.fast();
        for (Pair<MobEffectInstance, Float> possible : food.getEffects()) {
            MobEffectInstance base = possible.getFirst();
            if (base == null) continue;
            if (excluded != null && base.getEffect() == excluded) continue;
            int duration = base.getEffect().isInstantenous()
                    ? base.getDuration()
                    : Math.round(base.getDuration() * (1.0F + DURATION_BONUS_PER_VINTAGE * vintage));
            int amplifier = base.getAmplifier() + (vintage >= MAX_VINTAGE ? 1 : 0);
            MobEffectInstance scaled = new MobEffectInstance(base.getEffect(), duration, amplifier, base.isAmbient(), base.isVisible(), base.showIcon());
            builder.effect(() -> new MobEffectInstance(scaled), possible.getSecond());
        }
        return builder.build();
    }

    public static void applyFood(LivingEntity consumer, Level level, ItemStack stack, FoodProperties food) {
        if (consumer instanceof Player player) {
            player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }
        }
        if (!level.isClientSide) {
            for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                if (pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                    consumer.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
        }
        consumer.gameEvent(GameEvent.EAT);
    }

    public static int nutritionBonus(int vintage) {
        return NUTRITION_BONUS[Mth.clamp(vintage, 0, MAX_VINTAGE)];
    }

    public static FoodProperties scaleFood(FoodProperties food, int vintage, float saturationBonusPerVintage) {
        return build(food, food.getNutrition() + nutritionBonus(vintage), vintage, saturationBonusPerVintage, null);
    }

    public static Component vintageName(ItemStack stack, Component name) {
        int vintage = getVintage(stack);
        return vintage > 0 ? Component.translatable("item.hearthandharvest.vintage_name." + vintage, name) : name;
    }

    public static void appendTooltip(ItemStack stack, List<Component> tooltip) {
        int vintage = getVintage(stack);
        if (vintage > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.vintage." + vintage).withStyle(ChatFormatting.GOLD));
        }
    }
}