package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.entity.JugBlockEntity;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JugBlockItem extends BlockItem {

    public static final int JUG_CAPACITY = 8000;

    private static final String LEGACY_TANK_KEY = "FluidTank";

    public JugBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static FluidStack getFluid(ItemStack stack, @Nullable HolderLookup.Provider registries) {
        SimpleFluidContent content = stack.get(HHModDataComponents.JUG_FLUID.get());
        if (content != null) return content.copy();
        if (registries == null) return FluidStack.EMPTY;
        return readLegacyFluid(stack, registries);
    }

    public static void setFluid(ItemStack stack, FluidStack fluid) {
        if (fluid.isEmpty()) {
            stack.remove(HHModDataComponents.JUG_FLUID.get());
        } else {
            stack.set(HHModDataComponents.JUG_FLUID.get(), SimpleFluidContent.copyOf(fluid));
        }
        stripLegacyTank(stack);
    }

    public static FluidTank readTank(ItemStack stack, @Nullable HolderLookup.Provider registries) {
        FluidTank tank = new FluidTank(JUG_CAPACITY);
        tank.setFluid(getFluid(stack, registries));
        return tank;
    }

    public static void writeTank(ItemStack stack, FluidTank tank) {
        setFluid(stack, tank.getFluid());
    }

    private static FluidStack readLegacyFluid(ItemStack stack, HolderLookup.Provider registries) {
        CompoundTag tankTag = legacyTankTag(stack.get(DataComponents.CUSTOM_DATA));
        if (tankTag == null) tankTag = legacyTankTag(stack.get(DataComponents.BLOCK_ENTITY_DATA));
        if (tankTag == null) return FluidStack.EMPTY;

        FluidTank tank = new FluidTank(JUG_CAPACITY);
        tank.readFromNBT(registries, tankTag);
        return tank.getFluid();
    }

    @Nullable
    private static CompoundTag legacyTankTag(@Nullable CustomData data) {
        if (data == null || data.isEmpty()) return null;
        CompoundTag tag = data.copyTag();
        return tag.contains(LEGACY_TANK_KEY, Tag.TAG_COMPOUND) ? tag.getCompound(LEGACY_TANK_KEY) : null;
    }

    private static void stripLegacyTank(ItemStack stack) {
        stripLegacyTank(stack, DataComponents.CUSTOM_DATA);
        stripLegacyTank(stack, DataComponents.BLOCK_ENTITY_DATA);
    }

    private static void stripLegacyTank(ItemStack stack, net.minecraft.core.component.DataComponentType<CustomData> type) {
        CustomData data = stack.get(type);
        if (data == null || data.isEmpty()) return;
        CompoundTag tag = data.copyTag();
        if (!tag.contains(LEGACY_TANK_KEY)) return;
        tag.remove(LEGACY_TANK_KEY);
        if (tag.isEmpty()) stack.remove(type);
        else stack.set(type, CustomData.of(tag));
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidStack carried = getFluid(context.getItemInHand(), level.registryAccess());

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
        FluidTank tank = readTank(singleJug, level.registryAccess());
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
        return !pickup.pickupBlock(player, level, pos, state).isEmpty();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        IFluidHandler fluidHandler = level.getCapability(
                Capabilities.FluidHandler.BLOCK, pos, context.getClickedFace());
        if (fluidHandler == null) return super.useOn(context);

        if (!player.mayUseItemAt(pos, context.getClickedFace(), stack)) return InteractionResult.FAIL;

        ItemStack singleJug = stack.copyWithCount(1);
        FluidTank jugTank = readTank(singleJug, level.registryAccess());

        FluidStack jugFluid = jugTank.getFluid().copy();
        if (!jugFluid.isEmpty()) {
            int simFill = fluidHandler.fill(jugFluid, IFluidHandler.FluidAction.SIMULATE);
            if (simFill > 0) {
                if (!level.isClientSide) {
                    fluidHandler.fill(jugFluid.copyWithAmount(simFill), IFluidHandler.FluidAction.EXECUTE);
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

        FluidStack simDrain = fluidHandler.drain(inSource.copyWithAmount(space), IFluidHandler.FluidAction.SIMULATE);
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
            FluidStack actualDrain = fluidHandler.drain(inSource.copyWithAmount(simFill), IFluidHandler.FluidAction.EXECUTE);
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        FluidStack fs = getFluid(stack, ctx.registries());

        if (fs.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.jug.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(fs.getHoverName().copy().withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal(fs.getAmount() + " mB").withStyle(ChatFormatting.GRAY));
    }
}