package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.entity.JugBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;

public class JugBlockItem extends BlockItem {

    public static final int JUG_CAPACITY = 8000;

    public JugBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();

        CompoundTag fluidTag = findFluidTag(stack);

        InteractionResult result = super.place(context);

        if (result.consumesAction() && !context.getLevel().isClientSide) {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();

            if (level.getBlockEntity(pos) instanceof JugBlockEntity jug) {
                if (fluidTag != null) {
                    jug.getFluidTank().readFromNBT(level.registryAccess(), fluidTag);
                    jug.setChanged();
                    jug.syncToClient();
                }
            }
        }

        return result;
    }

    private CompoundTag findFluidTag(ItemStack stack) {
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty()) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("FluidTank", Tag.TAG_COMPOUND)) {
                return tag.getCompound("FluidTank");
            }
        }

        var beData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (beData != null && !beData.isEmpty()) {
            CompoundTag tag = beData.copyTag();
            if (tag.contains("FluidTank", Tag.TAG_COMPOUND)) {
                return tag.getCompound("FluidTank");
            }
        }

        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult fluidHit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (fluidHit.getType() != HitResult.Type.MISS) {
            BlockPos pos = fluidHit.getBlockPos();
            FluidState fluidState = level.getFluidState(pos);

            if (fluidState.isSource()) {
                FluidStack incoming = new FluidStack(fluidState.getType(), 1000);
                FluidTank tank = readTank(stack, level);

                if (tank.fill(incoming, IFluidHandler.FluidAction.SIMULATE) == 1000) {
                    if (!level.isClientSide) {
                        tank.fill(incoming, IFluidHandler.FluidAction.EXECUTE);
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        writeTank(stack, tank, level);
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
                } else {
                    if (level.isClientSide) {
                        player.displayClientMessage(
                                Component.translatable("tooltip.hearthandharvest.jug.full")
                                        .withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResultHolder.fail(stack);
                }
            }
        }

        return super.use(level, player, hand);
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

        if (fluidHandler != null) {
            // Work on a single jug, not the whole stack
            ItemStack singleJug = stack.copyWithCount(1);
            FluidTank jugTank = readTank(singleJug, level);
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

            FluidStack drainRequest = new FluidStack(inSource.getFluid(), space);
            FluidStack simDrain = fluidHandler.drain(drainRequest, IFluidHandler.FluidAction.SIMULATE);

            if (!simDrain.isEmpty()) {
                int simFill = jugTank.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);

                if (simFill > 0) {
                    if (!level.isClientSide) {
                        FluidStack toDrain = new FluidStack(inSource.getFluid(), simFill);
                        FluidStack actualDrain = fluidHandler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                        jugTank.fill(actualDrain, IFluidHandler.FluidAction.EXECUTE);
                        writeTank(singleJug, jugTank, level);
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            player.setItemInHand(context.getHand(), singleJug);
                        } else {
                            if (!player.addItem(singleJug)) {
                                player.drop(singleJug, false);
                            }
                        }

                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    if (level.isClientSide) {
                        player.displayClientMessage(
                                Component.translatable("tooltip.hearthandharvest.jug.wrong_fluid")
                                        .withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.PASS;
        }
        return super.useOn(context);
    }

    private FluidTank readTank(ItemStack stack, Level level) {
        FluidTank tank = new FluidTank(JUG_CAPACITY);
        var data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || data.isEmpty()) return tank;

        CompoundTag tag = data.copyTag();
        if (!tag.contains("FluidTank", Tag.TAG_COMPOUND)) return tank;

        tank.readFromNBT(level.registryAccess(), tag.getCompound("FluidTank"));
        return tank;
    }

    private void writeTank(ItemStack stack, FluidTank tank, Level level) {
        var existing = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag = (existing != null && !existing.isEmpty())
                ? existing.copyTag()
                : new CompoundTag();

        CompoundTag tankTag = new CompoundTag();
        tank.writeToNBT(level.registryAccess(), tankTag);
        tag.put("FluidTank", tankTag);

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static FluidTank readTankStatic(ItemStack stack) {
        FluidTank tank = new FluidTank(JUG_CAPACITY);
        var data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || data.isEmpty()) return tank;
        CompoundTag tag = data.copyTag();
        if (!tag.contains("FluidTank", Tag.TAG_COMPOUND)) return tank;

        CompoundTag fluidTag = tag.getCompound("FluidTank");
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        HolderLookup.Provider registries = server != null
                ? server.registryAccess()
                : RegistryAccess.EMPTY;

        tank.readFromNBT(registries, fluidTag);
        return tank;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        HolderLookup.Provider lookup = ctx.registries();
        if (lookup == null) return;

        FluidStack fs = readFluidForTooltip(stack, lookup);

        if (fs.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.hearthandharvest.jug.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(fs.getHoverName().copy().withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal(fs.getAmount() + " mB").withStyle(ChatFormatting.GRAY));
    }

    private FluidStack readFluidForTooltip(ItemStack stack, HolderLookup.Provider lookup) {
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty()) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("FluidTank", Tag.TAG_COMPOUND)) {
                FluidTank tank = new FluidTank(JUG_CAPACITY);
                tank.readFromNBT(lookup, tag.getCompound("FluidTank"));
                if (!tank.getFluid().isEmpty()) return tank.getFluid();
            }
        }

        var beData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (beData != null && !beData.isEmpty()) {
            CompoundTag tag = beData.copyTag();
            if (tag.contains("FluidTank", Tag.TAG_COMPOUND)) {
                FluidTank tank = new FluidTank(JUG_CAPACITY);
                tank.readFromNBT(lookup, tag.getCompound("FluidTank"));
                return tank.getFluid();
            }
        }

        return FluidStack.EMPTY;
    }
}