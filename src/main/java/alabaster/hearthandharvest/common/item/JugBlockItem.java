package alabaster.hearthandharvest.common.item;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import alabaster.hearthandharvest.common.block.entity.JugBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JugBlockItem extends BlockItem {

    public static final int JUG_CAPACITY = 8000;

    private static final String LEGACY_TANK_KEY = "FluidTank";

    public JugBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static FluidStack getFluid(ItemStack stack) {
        FluidStack content = HHModDataComponents.JUG_FLUID.get(stack);
        if (content != null) return content.copy();
        return readLegacyFluid(stack);
    }

    public static void setFluid(ItemStack stack, FluidStack fluid) {
        if (fluid.isEmpty()) {
            HHModDataComponents.JUG_FLUID.remove(stack);
        } else {
            HHModDataComponents.JUG_FLUID.set(stack, fluid.copy());
        }
        stripLegacyTank(stack);
    }

    public static FluidTank readTank(ItemStack stack) {
        FluidTank tank = new FluidTank(JUG_CAPACITY);
        tank.setFluid(getFluid(stack));
        return tank;
    }

    public static void writeTank(ItemStack stack, FluidTank tank) {
        setFluid(stack, tank.getFluid());
    }

    private static FluidStack readLegacyFluid(ItemStack stack) {
        CompoundTag tankTag = legacyTankTag(stack.getTag());
        if (tankTag == null) tankTag = legacyTankTag(BlockItem.getBlockEntityData(stack));
        if (tankTag == null) return FluidStack.EMPTY;

        FluidTank tank = new FluidTank(JUG_CAPACITY);
        tank.readFromNBT(tankTag);
        return tank.getFluid();
    }

    @Nullable
    private static CompoundTag legacyTankTag(@Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) return null;
        return tag.contains(LEGACY_TANK_KEY, Tag.TAG_COMPOUND) ? tag.getCompound(LEGACY_TANK_KEY) : null;
    }

    private static void stripLegacyTank(ItemStack stack) {
        CompoundTag root = stack.getTag();
        if (root != null) root.remove(LEGACY_TANK_KEY);
        CompoundTag blockEntity = stack.getTagElement(BLOCK_ENTITY_TAG);
        if (blockEntity != null) {
            blockEntity.remove(LEGACY_TANK_KEY);
            if (blockEntity.isEmpty() && root != null) root.remove(BLOCK_ENTITY_TAG);
        }
        if (root != null && root.isEmpty()) stack.setTag(null);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidStack carried = getFluid(context.getItemInHand());

        InteractionResult result = super.place(context);

        if (result.consumesAction() && !level.isClientSide && !carried.isEmpty()) {
            if (level.getBlockEntity(context.getClickedPos()) instanceof JugBlockEntity jug) {
                jug.getFluidTank().setFluid(carried);
                jug.setChanged();
                jug.syncToClient();
            }
        }

        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult fluidHit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (fluidHit.getType() != HitResult.Type.BLOCK) return super.use(level, player, hand);

        BlockPos pos = fluidHit.getBlockPos();
        FluidState fluidState = level.getFluidState(pos);
        if (!fluidState.isSource()) return super.use(level, player, hand);

        if (!player.mayUseItemAt(pos, fluidHit.getDirection(), stack)) {
            return InteractionResultHolder.fail(stack);
        }

        ItemStack singleJug = stack.copyWithCount(1);
        FluidTank tank = readTank(singleJug);
        FluidStack incoming = new FluidStack(fluidState.getType(), FluidType.BUCKET_VOLUME);

        if (tank.fill(incoming, IFluidHandler.FluidAction.SIMULATE) != FluidType.BUCKET_VOLUME) {
            if (level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("tooltip.hearthandharvest.jug.full")
                                .withStyle(ChatFormatting.RED), true);
            }
            return InteractionResultHolder.fail(stack);
        }

        if (level.isClientSide) return InteractionResultHolder.sidedSuccess(stack, true);

        if (!pickUpSource(player, level, pos)) return InteractionResultHolder.fail(stack);
        tank.fill(incoming, IFluidHandler.FluidAction.EXECUTE);
        writeTank(singleJug, tank);
        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResultHolder.sidedSuccess(swapForFilled(player, stack, singleJug), false);
    }

    private static ItemStack swapForFilled(Player player, ItemStack held, ItemStack modified) {
        if (held.getCount() == 1) return modified;
        held.shrink(1);
        if (!player.addItem(modified)) player.drop(modified, false);
        return held;
    }

    private static boolean pickUpSource(Player player, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BucketPickup pickup)) return false;
        return !pickup.pickupBlock(level, pos, state).isEmpty();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        BlockEntity targetEntity = level.getBlockEntity(pos);
        IFluidHandler fluidHandler = targetEntity == null ? null
                : targetEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, context.getClickedFace()).resolve().orElse(null);
        if (fluidHandler == null) return super.useOn(context);

        if (!player.mayUseItemAt(pos, context.getClickedFace(), stack)) return InteractionResult.FAIL;

        ItemStack singleJug = stack.copyWithCount(1);
        FluidTank jugTank = readTank(singleJug);

        FluidStack jugFluid = jugTank.getFluid().copy();
        if (!jugFluid.isEmpty()) {
            int simFill = fluidHandler.fill(jugFluid, IFluidHandler.FluidAction.SIMULATE);
            if (simFill > 0) {
                if (!level.isClientSide) {
                    fluidHandler.fill(new FluidStack(jugFluid, simFill), IFluidHandler.FluidAction.EXECUTE);
                    jugTank.drain(simFill, IFluidHandler.FluidAction.EXECUTE);
                    writeTank(singleJug, jugTank);
                    giveBack(player, context.getHand(), stack, singleJug);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        int space = JUG_CAPACITY - jugTank.getFluidAmount();
        if (space <= 0) {
            if (level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("tooltip.hearthandharvest.jug.full")
                                .withStyle(ChatFormatting.RED), true);
            }
            return InteractionResult.FAIL;
        }

        if (fluidHandler.getTanks() == 0) return InteractionResult.PASS;
        FluidStack inSource = fluidHandler.getFluidInTank(0);
        if (inSource.isEmpty()) return InteractionResult.PASS;

        FluidStack simDrain = fluidHandler.drain(new FluidStack(inSource, space), IFluidHandler.FluidAction.SIMULATE);
        if (simDrain.isEmpty()) return InteractionResult.PASS;

        int simFill = jugTank.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
        if (simFill <= 0) {
            if (level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("tooltip.hearthandharvest.jug.wrong_fluid")
                                .withStyle(ChatFormatting.RED), true);
            }
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            FluidStack actualDrain = fluidHandler.drain(new FluidStack(inSource, simFill), IFluidHandler.FluidAction.EXECUTE);
            jugTank.fill(actualDrain, IFluidHandler.FluidAction.EXECUTE);
            writeTank(singleJug, jugTank);
            giveBack(player, context.getHand(), stack, singleJug);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void giveBack(Player player, InteractionHand hand, ItemStack held, ItemStack modified) {
        held.shrink(1);
        if (held.isEmpty()) {
            player.setItemInHand(hand, modified);
        } else if (!player.addItem(modified)) {
            player.drop(modified, false);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        FluidStack fs = getFluid(stack);

        if (fs.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.jug.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(fs.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal(fs.getAmount() + " mB").withStyle(ChatFormatting.GRAY));
    }
}