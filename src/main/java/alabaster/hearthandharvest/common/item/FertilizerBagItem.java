package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.IHarvestable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FertilizerBagItem extends Item {

    public FertilizerBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        boolean validTarget = block instanceof SaplingBlock
                || block instanceof StemBlock
                || block instanceof CropBlock
                || block instanceof IHarvestable
                || block instanceof BonemealableBlock;

        if (!validTarget) return InteractionResult.PASS;

        if (level.isClientSide) {
            BoneMealItem.addGrowthParticles(level, pos, 15);
            level.playSound(player, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(true);
        }

        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

        boolean used = false;

        if (block instanceof SaplingBlock sapling) {
            if (state.hasProperty(BlockStateProperties.STAGE)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.STAGE, 1), Block.UPDATE_ALL);
            }
            sapling.performBonemeal(serverLevel, serverLevel.random, pos, level.getBlockState(pos));
            used = true;
        } else if (block instanceof StemBlock) {
            level.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, 7), Block.UPDATE_ALL);
            for (int i = 0; i < 16; i++) {
                BlockState current = level.getBlockState(pos);
                if (!(current.getBlock() instanceof StemBlock)) break;
                current.randomTick(serverLevel, pos, serverLevel.random);
            }
            used = true;
        } else if (block instanceof CropBlock crop) {
            try {
                java.lang.reflect.Method getAgeProp = CropBlock.class.getDeclaredMethod("getAgeProperty");
                getAgeProp.setAccessible(true);
                net.minecraft.world.level.block.state.properties.IntegerProperty ageProp =
                        (net.minecraft.world.level.block.state.properties.IntegerProperty) getAgeProp.invoke(crop);
                level.setBlock(pos, state.setValue(ageProp, crop.getMaxAge()), Block.UPDATE_ALL);
            } catch (Exception e) {
                if (state.hasProperty(BlockStateProperties.AGE_7))
                    level.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, 7), Block.UPDATE_ALL);
            }
            used = true;
        } else if (block instanceof IHarvestable harvestable) {
            if (harvestable.isHarvestReady(state)) {
                harvestable.harvestBlock(state, level, pos, player, stack);
            } else if (block instanceof BonemealableBlock bonemealable
                    && bonemealable.isValidBonemealTarget(level, pos, state)) {
                for (int i = 0; i < 16; i++) {
                    BlockState current = level.getBlockState(pos);
                    if (harvestable.isHarvestReady(current)) break;
                    bonemealable.performBonemeal(serverLevel, serverLevel.random, pos, current);
                }
            }
            used = true;
        } else if (block instanceof BonemealableBlock bonemealable) {
            if (bonemealable.isValidBonemealTarget(level, pos, state)) {
                bonemealable.performBonemeal(serverLevel, serverLevel.random, pos, state);
                used = true;
            }
        }

        if (used) {
            if (player != null && !player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }
            return InteractionResult.sidedSuccess(false);
        }

        return InteractionResult.PASS;
    }
}