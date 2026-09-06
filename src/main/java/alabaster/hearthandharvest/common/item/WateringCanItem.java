package alabaster.hearthandharvest.common.item;

import java.util.List;
import javax.annotation.Nullable;

import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WateringCanItem extends Item {

    // Maximum water and bone meal charges.
    private static final int MAX_WATER = 16;
    private static final int MAX_BONEMEAL = 16;

    public WateringCanItem(Properties properties) {
        super(properties.component(HHModDataComponents.WATER_LEVEL, 0).component(HHModDataComponents.BONEMEAL_LEVEL, 0)
                .component(HHModDataComponents.FERTILIZER_ITEM, Items.AIR).stacksTo(1));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getWaterCharge(stack) > 0 || getBoneMealCharge(stack) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x0437F2;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int totalCharge = getWaterCharge(stack) + getBoneMealCharge(stack);
        return Math.round(13.0F * ((float) totalCharge / (MAX_WATER + MAX_BONEMEAL)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack canStack = player.getItemInHand(hand);

        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack offhandStack = player.getOffhandItem();

            if (offhandStack.is(HHModTags.BONEMEAL_SUBSTITUTES)) {
                Item offhandItem = offhandStack.getItem();
                int currentBonemeal = getBoneMealCharge(canStack);
                Item loadedItem = getFertilizerItem(canStack);
                // Once charged, the can only accepts more of the same item until it empties out.
                boolean typeMatches = currentBonemeal == 0 || loadedItem == offhandItem;

                if (currentBonemeal < MAX_BONEMEAL) {
                    if (typeMatches) {
                        if (!level.isClientSide()) {
                            offhandStack.shrink(1);
                            setBoneMealCharge(canStack, currentBonemeal + 1);
                            setFertilizerItem(canStack, offhandItem);
                            level.playSound(null, player.blockPosition(), SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 0.8F, 1.0F);
                            if (level instanceof ServerLevel serverLevel) {
                                double x = player.getX();
                                double y = player.getY() + 0.8;
                                double z = player.getZ();
                                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 10, 0.5, 0.4, 0.5, 0.05);
                            }
                        }
                        return InteractionResultHolder.sidedSuccess(canStack, level.isClientSide());
                    } else if (!level.isClientSide()) {
                        player.displayClientMessage(
                                Component.translatable("tooltip.hearthandharvest.watering_can.wrong_fertilizer",
                                        loadedItem.getDefaultInstance().getHoverName()),
                                true
                        );
                    }
                }
            }

            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState targetState = level.getBlockState(pos);

                if (targetState.getBlock() == Blocks.WATER) {
                    if (getWaterCharge(canStack) < MAX_WATER) {
                        if (!level.isClientSide()) {
                            setWaterCharge(canStack, MAX_WATER);
                            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        return InteractionResultHolder.sidedSuccess(canStack, level.isClientSide());
                    }
                }

                // Extinguish fire or any LIT block
                if (isExtinguishable(targetState)) {
                    if (getWaterCharge(canStack) > 0) {
                        if (!level.isClientSide()) {
                            if (targetState.getBlock() == Blocks.FIRE) {
                                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                            } else {
                                level.setBlock(pos, targetState.setValue(CampfireBlock.LIT, false), 3);
                            }
                            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
                            consumeWater(canStack);
                        }
                        return InteractionResultHolder.sidedSuccess(canStack, level.isClientSide());
                    }
                }

                // Apply bonemeal to crop, if applicable
                if (targetState.getBlock() instanceof BonemealableBlock) {
                    int water = getWaterCharge(canStack);
                    int bonemeal = getBoneMealCharge(canStack);
                    if (water > 0 && bonemeal > 0) {
                        if (level.isClientSide()) {
                            return InteractionResultHolder.sidedSuccess(canStack, true);
                        }
                        boolean applied = applyBonemeal(level, pos, targetState, player);
                        if (applied) {
                            consumeBoth(canStack);
                            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            if (level instanceof ServerLevel serverLevel) {
                                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.25, 0.3, 0.25, 0.05);
                            }
                            return InteractionResultHolder.sidedSuccess(canStack, false);
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.pass(canStack);
    }

    private boolean isExtinguishable(BlockState state) {
        // Check for blocks with the LIT property
        if (state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT)) {
            return true; // Campfires or any block with LIT property
        }
        if (state.getBlock() == Blocks.FIRE) {
            return true; // Fire block
        }
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack canStack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);

        if (!level.isClientSide() && player != null) {
            if (state.getBlock() instanceof BonemealableBlock) {
                int water = getWaterCharge(canStack);
                int bonemeal = getBoneMealCharge(canStack);
                if (water > 0 && bonemeal > 0) {
                    boolean applied = applyBonemeal(level, pos, state, player);
                    if (applied) {
                        consumeBoth(canStack);
                        level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.25, 0.3, 0.25, 0.05);
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    private void consumeWater(ItemStack stack) {
        int water = stack.getOrDefault(HHModDataComponents.WATER_LEVEL, 0);
        stack.update(HHModDataComponents.WATER_LEVEL, 0, oldValue -> water - 1);
    }

    private void consumeBoth(ItemStack stack) {
        int water = stack.getOrDefault(HHModDataComponents.WATER_LEVEL, 0);
        int boneMeal = stack.getOrDefault(HHModDataComponents.BONEMEAL_LEVEL, 0);
        int newBoneMeal = boneMeal - 1;
        stack.update(HHModDataComponents.WATER_LEVEL, 0, oldValue -> water - 1);
        stack.update(HHModDataComponents.BONEMEAL_LEVEL, 0, oldValue -> newBoneMeal);
        if (newBoneMeal <= 0) {
            stack.set(HHModDataComponents.FERTILIZER_ITEM, Items.AIR);
        }
    }

    private int getWaterCharge(ItemStack stack) {
        return stack.getOrDefault(HHModDataComponents.WATER_LEVEL, 0);
    }

    private void setWaterCharge(ItemStack stack, int value) {
        stack.set(HHModDataComponents.WATER_LEVEL, value);
    }

    private int getBoneMealCharge(ItemStack stack) {
        return stack.getOrDefault(HHModDataComponents.BONEMEAL_LEVEL, 0);
    }

    private void setBoneMealCharge(ItemStack stack, int value) {
        stack.set(HHModDataComponents.BONEMEAL_LEVEL, value);
    }

    private Item getFertilizerItem(ItemStack stack) {
        return stack.getOrDefault(HHModDataComponents.FERTILIZER_ITEM, Items.AIR);
    }

    private void setFertilizerItem(ItemStack stack, Item item) {
        stack.set(HHModDataComponents.FERTILIZER_ITEM, item);
    }

    private boolean applyBonemeal(Level level, BlockPos centerPos, BlockState state, @Nullable Player player) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        boolean appliedAny = false;
        int radius = 1; // 3x3 area around center

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                mutablePos.set(centerPos.getX() + dx, centerPos.getY(), centerPos.getZ() + dz);
                BlockState targetState = level.getBlockState(mutablePos);
                Block block = targetState.getBlock();

                if (block instanceof BonemealableBlock growable) {
                    if (growable.isValidBonemealTarget(level, mutablePos, targetState)) {
                        if (growable.isBonemealSuccess(serverLevel, level.random, mutablePos, targetState)) {
                            growable.performBonemeal(serverLevel, level.random, mutablePos, targetState);
                            serverLevel.levelEvent(2005, mutablePos, 0);
                            appliedAny = true;
                        }
                    }
                }
            }
        }

        return appliedAny;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int waterLevel = getWaterCharge(stack);
        int boneMealLevel = getBoneMealCharge(stack);
        Item fertilizerItem = getFertilizerItem(stack);

        Component fertilizerLabel = fertilizerItem == Items.AIR
                ? Component.translatable("tooltip.hearthandharvest.watering_can.bone_meal")
                : Component.translatable("tooltip.hearthandharvest.watering_can.fertilizer",
                fertilizerItem.getDefaultInstance().getHoverName());

        tooltipComponents.add(
                Component.translatable("tooltip.hearthandharvest.watering_can.water")
                        .append(Component.literal(": " + waterLevel + " / " + MAX_WATER))
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC)
        );
        tooltipComponents.add(
                fertilizerLabel.copy()
                        .append(Component.literal(": " + boneMealLevel + " / " + MAX_BONEMEAL))
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
        );

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}