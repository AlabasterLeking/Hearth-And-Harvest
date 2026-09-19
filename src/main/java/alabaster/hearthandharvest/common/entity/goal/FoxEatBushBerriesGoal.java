package alabaster.hearthandharvest.common.entity.goal;

import alabaster.hearthandharvest.common.block.BerryBush;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;

public class FoxEatBushBerriesGoal extends MoveToBlockGoal {
    private static final int WAIT_TICKS = 40;
    private static final int RIPE_AGE = 2;

    private final Fox fox;
    private int ticksWaited;

    public FoxEatBushBerriesGoal(Fox fox, double speedModifier, int searchRange, int verticalSearchRange) {
        super(fox, speedModifier, searchRange, verticalSearchRange);
        this.fox = fox;
    }

    @Override
    public double acceptedDistance() {
        return 2.0D;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 100 == 0;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof BerryBush && state.getValue(SweetBerryBushBlock.AGE) >= RIPE_AGE;
    }

    @Override
    public boolean canUse() {
        return !fox.isSleeping() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !fox.isSleeping() && super.canContinueToUse();
    }

    @Override
    public void start() {
        ticksWaited = 0;
        super.start();
    }

    @Override
    public void tick() {
        if (isReachedTarget()) {
            if (ticksWaited >= WAIT_TICKS) {
                pickBerries();
            } else {
                ticksWaited++;
            }
        }

        super.tick();
    }

    private void pickBerries() {
        Level level = fox.level();
        if (!EventHooks.canEntityGrief(level, fox)) return;

        BlockState state = level.getBlockState(blockPos);
        if (!(state.getBlock() instanceof BerryBush bush)) return;

        int age = state.getValue(SweetBerryBushBlock.AGE);
        int count = 1 + level.random.nextInt(2) + (age == 3 ? 1 : 0);

        if (fox.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            fox.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(bush.getBerry()));
            count--;
        }
        if (count > 0) {
            Block.popResource(level, blockPos, new ItemStack(bush.getBerry(), count));
        }

        fox.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
        BlockState picked = state.setValue(SweetBerryBushBlock.AGE, 1);
        level.setBlock(blockPos, picked, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(fox, picked));
        ticksWaited = 0;
    }
}