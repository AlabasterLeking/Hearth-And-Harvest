package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class VintageHelper {
    public static final int MAX_VINTAGE = 3;

    private static final float DURATION_BONUS_PER_VINTAGE = 0.5F;

    private VintageHelper() {
    }

    public static int getVintage(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(HHModDataComponents.VINTAGE.get(), 0), 0, MAX_VINTAGE);
    }

    public static ItemStack withVintage(ItemStack stack, int vintage) {
        if (vintage <= 0) {
            stack.remove(HHModDataComponents.VINTAGE.get());
        } else {
            stack.set(HHModDataComponents.VINTAGE.get(), Math.min(vintage, MAX_VINTAGE));
        }
        return stack;
    }

    @Nullable
    public static String stageName(ItemStack stack) {
        return switch (getVintage(stack)) {
            case 1 -> "aged";
            case 2 -> "fine";
            case 3 -> "reserve";
            default -> null;
        };
    }

    @Nullable
    public static String overlayStage(String kind, ItemStack stack) {
        String stage = stageName(stack);
        if (stage == null && kind.equals("jar") && stack.getItem() instanceof AgeableItem ageable && ageable.isAgeable()) {
            return "fresh";
        }
        return stage;
    }

    @Nullable
    public static ResourceLocation overlayTexture(String kind, ItemStack stack) {
        String stage = overlayStage(kind, stack);
        return stage == null ? null : ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "textures/item/vintage/" + kind + "_" + stage + ".png");
    }

    public static float durationFactor(ItemStack stack) {
        return 1.0F + DURATION_BONUS_PER_VINTAGE * getVintage(stack);
    }

    public static ItemStack aged(ItemStack stack) {
        return withVintage(stack.copyWithCount(1), getVintage(stack) + 1);
    }

    public static FoodProperties scale(FoodProperties food, int vintage, float saturationBonusPerVintage, @Nullable Holder<MobEffect> excluded) {
        List<FoodProperties.PossibleEffect> effects = new ArrayList<>();
        for (FoodProperties.PossibleEffect possible : food.effects()) {
            MobEffectInstance base = possible.effect();
            if (excluded != null && base.getEffect().value() == excluded.value()) continue;

            int duration = base.getEffect().value().isInstantenous()
                    ? base.getDuration()
                    : Math.round(base.getDuration() * (1.0F + DURATION_BONUS_PER_VINTAGE * vintage));
            int amplifier = base.getAmplifier() + (vintage >= MAX_VINTAGE ? 1 : 0);
            MobEffectInstance scaled = new MobEffectInstance(base.getEffect(), duration, amplifier, base.isAmbient(), base.isVisible(), base.showIcon());
            effects.add(new FoodProperties.PossibleEffect(() -> new MobEffectInstance(scaled), possible.probability()));
        }
        float saturation = food.saturation() + food.nutrition() * 2.0F * saturationBonusPerVintage * vintage;
        return new FoodProperties(food.nutrition(), saturation, food.canAlwaysEat(), food.eatSeconds(), food.usingConvertsTo(), effects);
    }

    public static void appendTooltip(ItemStack stack, List<Component> tooltip) {
        int vintage = getVintage(stack);
        if (vintage > 0) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.vintage." + vintage).withStyle(ChatFormatting.GOLD));
        }
    }
}