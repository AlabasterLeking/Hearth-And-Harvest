package alabaster.hearthandharvest.common.fluid;

import alabaster.hearthandharvest.common.registry.HHDataMaps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public final class HHFluidHandling {

    public static final int MB_BOTTLE = 250;
    public static final int MB_BUCKET = 1000;

    private static final Pair<FluidStack, ItemStack> NO_EMPTYING = Pair.of(FluidStack.EMPTY, ItemStack.EMPTY);
    private static final Pair<Integer, ItemStack> NO_FILLING = Pair.of(0, ItemStack.EMPTY);

    private HHFluidHandling() {}

    public static boolean isWaterBottle(ItemStack stack) {
        if (!stack.is(Items.POTION)) return false;
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.is(Potions.WATER);
    }

    public static ItemStack waterBottle() {
        return PotionContents.createItemStack(Items.POTION, Potions.WATER);
    }

    public static int comparatorOutput(@Nullable IFluidHandler tank) {
        if (tank == null || tank.getTanks() == 0) return 0;
        int amount = tank.getFluidInTank(0).getAmount();
        if (amount <= 0) return 0;
        int capacity = tank.getTankCapacity(0);
        if (capacity <= 0) return 0;
        return Mth.floor(((float) amount / capacity) * 14.0F) + 1;
    }

    private static int spaceIn(IFluidHandler tank) {
        if (tank.getTanks() == 0) return 0;
        return tank.getTankCapacity(0) - tank.getFluidInTank(0).getAmount();
    }

    public static Pair<FluidStack, ItemStack> testEmptying(ItemStack input, IFluidHandler tank) {
        if (input.isEmpty() || tank.getTanks() == 0) return NO_EMPTYING;

        if (isWaterBottle(input)) {
            FluidStack water = new FluidStack(Fluids.WATER, MB_BOTTLE);
            if (tank.fill(water, IFluidHandler.FluidAction.SIMULATE) == MB_BOTTLE) {
                return Pair.of(water, new ItemStack(Items.GLASS_BOTTLE));
            }
            return NO_EMPTYING;
        }

        Fluid bottled = HHDataMaps.getFluidForBottle(input.getItem());
        if (bottled != null) {
            FluidStack contents = new FluidStack(bottled, HHDataMaps.BOTTLE_VOLUME);
            if (tank.fill(contents, IFluidHandler.FluidAction.SIMULATE) == HHDataMaps.BOTTLE_VOLUME) {
                return Pair.of(contents, new ItemStack(Items.GLASS_BOTTLE));
            }
            return NO_EMPTYING;
        }

        IFluidHandlerItem handler = input.copyWithCount(1).getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null) return NO_EMPTYING;

        FluidStack transfer = FluidUtil.tryFluidTransfer(tank, handler, spaceIn(tank), false);
        if (transfer.isEmpty()) return NO_EMPTYING;

        handler.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
        return Pair.of(transfer, handler.getContainer());
    }

    public static Pair<Integer, ItemStack> testFilling(ItemStack input, IFluidHandler tank) {
        if (input.isEmpty() || tank.getTanks() == 0) return NO_FILLING;

        FluidStack tankFluid = tank.getFluidInTank(0);
        if (tankFluid.isEmpty()) return NO_FILLING;

        if (input.is(Items.GLASS_BOTTLE)) {
            if (tankFluid.getFluid().isSame(Fluids.WATER) && tankFluid.getAmount() >= MB_BOTTLE) {
                return Pair.of(MB_BOTTLE, waterBottle());
            }
            Item bottle = HHDataMaps.getBottleForFluid(tankFluid.getFluid());
            if (bottle != null && tankFluid.getAmount() >= HHDataMaps.BOTTLE_VOLUME) {
                return Pair.of(HHDataMaps.BOTTLE_VOLUME, new ItemStack(bottle));
            }
            return NO_FILLING;
        }

        IFluidHandlerItem handler = input.copyWithCount(1).getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null) return NO_FILLING;

        FluidStack transfer = FluidUtil.tryFluidTransfer(handler, tank, tankFluid.getAmount(), false);
        if (transfer.isEmpty()) return NO_FILLING;

        handler.fill(transfer, IFluidHandler.FluidAction.EXECUTE);
        return Pair.of(transfer.getAmount(), handler.getContainer());
    }

    public static void giveResult(Player player, InteractionHand hand, ItemStack held, ItemStack result) {
        if (player.getAbilities().instabuild) return;
        if (result.isEmpty()) return;

        if (held.getCount() == 1) {
            player.setItemInHand(hand, result);
            return;
        }

        held.shrink(1);
        ItemStack remainder = ItemHandlerHelper.insertItemStacked(new InvWrapper(player.getInventory()), result, false);
        if (!remainder.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, remainder);
        }
    }

    public static void playFillSound(Level level, BlockPos pos, FluidType fluid, ItemLike item) {
        if (item.asItem() == Items.GLASS_BOTTLE) {
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
        SoundEvent sound = fluid.getSound(SoundActions.BUCKET_FILL);
        level.playSound(null, pos, sound != null ? sound : SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static void playEmptySound(Level level, BlockPos pos, FluidType fluid, ItemLike item) {
        if (item.asItem() == Items.GLASS_BOTTLE || isBottleLike(item)) {
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
        SoundEvent sound = fluid.getSound(SoundActions.BUCKET_EMPTY);
        level.playSound(null, pos, sound != null ? sound : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static boolean isBottleLike(ItemLike item) {
        return item.asItem() == Items.POTION || HHDataMaps.getFluidForBottle(item.asItem()) != null;
    }

    public static ItemInteractionResult useOnTank(Level level, BlockPos pos, Player player, InteractionHand hand,
                                                  IFluidHandler tank, @Nullable Runnable onChanged) {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || tank == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        Pair<FluidStack, ItemStack> emptying = testEmptying(held, tank);
        FluidStack incoming = emptying.getFirst();
        if (!incoming.isEmpty()) {
            tank.fill(incoming, IFluidHandler.FluidAction.EXECUTE);
            playEmptySound(level, pos, incoming.getFluidType(), held.getItem());
            giveResult(player, hand, held, emptying.getSecond());
            if (onChanged != null) onChanged.run();
            return ItemInteractionResult.CONSUME;
        }

        FluidType heldType = tank.getFluidInTank(0).getFluidType();
        Pair<Integer, ItemStack> filling = testFilling(held, tank);
        if (filling.getFirst() > 0) {
            tank.drain(filling.getFirst(), IFluidHandler.FluidAction.EXECUTE);
            playFillSound(level, pos, heldType, held.getItem());
            giveResult(player, hand, held, filling.getSecond());
            if (onChanged != null) onChanged.run();
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}