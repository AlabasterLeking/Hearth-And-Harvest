package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.JarBlock;
import alabaster.hearthandharvest.common.block.entity.JarBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class JarBlockItem extends BlockItem implements AgeableItem {
    public static final int DEFAULT_SERVINGS = 4;
    private static final int BAR_COLOR = 0xE8A33D;

    private final Block displayBlock;
    private List<Holder<MobEffect>> cures = List.of();
    private int maxServings = DEFAULT_SERVINGS;
    private boolean ageable;
    private static final float SATURATION_BONUS_PER_VINTAGE = 0.1F;

    public JarBlockItem(Block placedBlock, Block displayBlock, Properties properties) {
        super(placedBlock, properties);
        this.displayBlock = displayBlock;
    }

    @SafeVarargs
    public final JarBlockItem cures(Holder<MobEffect>... effects) {
        this.cures = List.of(effects);
        return this;
    }

    public JarBlockItem ageable() {
        this.ageable = true;
        return this;
    }

    @Override
    public boolean isAgeable() {
        return ageable;
    }

    @Override
    public boolean canAgeFurther(ItemStack stack) {
        return ageable && !isOpened(stack) && VintageHelper.getVintage(stack) < VintageHelper.MAX_VINTAGE;
    }

    public JarBlockItem servings(int servings) {
        this.maxServings = Math.max(1, servings);
        return this;
    }

    public Block getDisplayBlock() {
        return displayBlock;
    }

    public int getMaxServings() {
        return maxServings;
    }

    public int getServings(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(HHModDataComponents.SERVINGS.get(), maxServings), 1, maxServings);
    }

    private boolean isEdible(ItemStack stack) {
        return stack.getFoodProperties(null) != null;
    }

    private boolean isOpened(ItemStack stack) {
        return isEdible(stack) && getServings(stack) < maxServings;
    }

    private ItemStack withServings(ItemStack stack, int servings) {
        if (servings >= maxServings) {
            stack.remove(HHModDataComponents.SERVINGS.get());
        } else {
            stack.set(HHModDataComponents.SERVINGS.get(), servings);
        }
        return stack;
    }

    private ItemStack leftoverAfterServing(ItemStack stack) {
        int remaining = getServings(stack) - 1;
        return remaining > 0
                ? withServings(stack.copyWithCount(1), remaining)
                : new ItemStack(HHModItems.JAR.get());
    }

    @Override
    public String getDescriptionId() {
        return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof JarBlockEntity be) {
                for (int i = 0; i < JarBlock.SLOTS.length; i++) {
                    if (state.getValue(JarBlock.SLOTS[i])) {
                        be.setSlot(i, stack);
                        be.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (existing.getBlock() == this.getBlock()) {
            return InteractionResult.PASS;
        }

        return super.place(ctx);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        FoodProperties food = stack.getFoodProperties(entity);
        if (food == null) {
            return super.finishUsingItem(stack, level, entity);
        }

        entity.eat(level, stack.copyWithCount(1), VintageHelper.scale(food, VintageHelper.getVintage(stack), SATURATION_BONUS_PER_VINTAGE, null));
        if (!level.isClientSide) {
            for (Holder<MobEffect> effect : cures) {
                entity.removeEffect(effect);
            }
        }

        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }

        ItemStack leftover = leftoverAfterServing(stack);
        stack.shrink(1);
        if (stack.isEmpty()) {
            return leftover;
        }
        if (entity instanceof Player player && !player.getInventory().add(leftover)) {
            player.drop(leftover, false);
        }
        return stack;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return isEdible(stack) ? leftoverAfterServing(stack) : super.getCraftingRemainingItem(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isOpened(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getServings(stack) / maxServings);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        VintageHelper.appendTooltip(stack, tooltip);
        if (!isEdible(stack)) return;

        tooltip.add(Component.translatable("tooltip.hearthandharvest.servings", getServings(stack), maxServings)
                .withStyle(ChatFormatting.GRAY));

        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            TextUtils.addFoodEffectTooltip(stack, tooltip::add, VintageHelper.durationFactor(stack), context.tickRate());
            if (!cures.isEmpty()) {
                MutableComponent names = Component.empty();
                for (int i = 0; i < cures.size(); i++) {
                    if (i > 0) names.append(", ");
                    names.append(Component.translatable(cures.get(i).value().getDescriptionId()));
                }
                tooltip.add(Component.translatable("tooltip.hearthandharvest.cures", names).withStyle(ChatFormatting.BLUE));
            }
        }
    }
}