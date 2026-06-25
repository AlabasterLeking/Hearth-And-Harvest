package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.item.component.SeedPouchContents;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class SeedPouchItem extends Item {
    public SeedPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack pouchStack = context.getItemInHand();
        SeedPouchContents contents = pouchStack.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());

        if (player != null && player.isShiftKeyDown()) {
            if (contents == null) return InteractionResult.PASS;
            if (!level.isClientSide()) cycleRadius(pouchStack, contents, player);
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos = context.getClickedPos();
        if (!level.getBlockState(clickedPos).is(Tags.Blocks.VILLAGER_FARMLANDS) || context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }

        if (contents == null || contents.count() == 0) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        Item seedItem = contents.seedType();
        if (!(seedItem instanceof BlockItem blockItem)) return InteractionResult.PASS;

        int radius = contents.plantRadius();
        int seedsLeft = contents.count();

        outer:
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (seedsLeft == 0) break outer;
                BlockPos farmPos = clickedPos.offset(dx, 0, dz);
                BlockPos plantPos = farmPos.above();
                if (!level.getBlockState(farmPos).is(Tags.Blocks.VILLAGER_FARMLANDS)) continue;
                if (!level.isEmptyBlock(plantPos)) continue;
                ItemStack seedStack = new ItemStack(seedItem, 1);
                BlockHitResult hitResult = new BlockHitResult(Vec3.atBottomCenterOf(plantPos), Direction.UP, farmPos, false);
                UseOnContext seedCtx = new UseOnContext(level, player, context.getHand(), seedStack, hitResult);
                if (blockItem.place(new BlockPlaceContext(seedCtx)).consumesAction()) seedsLeft--;
            }
        }

        if (seedsLeft == contents.count()) return InteractionResult.PASS;
        if (seedsLeft == 0) pouchStack.remove(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        else pouchStack.set(HHModDataComponents.SEED_POUCH_CONTENTS.get(), contents.withCount(seedsLeft));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) return InteractionResultHolder.pass(stack);
        SeedPouchContents contents = stack.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        if (contents == null) return InteractionResultHolder.pass(stack);
        if (!level.isClientSide()) cycleRadius(stack, contents, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pouch, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;
        SeedPouchContents contents = pouch.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        ItemStack slotItem = slot.getItem();

        if (slotItem.isEmpty()) {
            if (contents == null || contents.count() == 0) return false;
            int toExtract = Math.min(contents.count(), new ItemStack(contents.seedType()).getMaxStackSize());
            slot.set(new ItemStack(contents.seedType(), toExtract));
            updatePouch(pouch, contents.seedType(), contents.count() - toExtract);
            playRemoveSound(player);
            return true;
        } else if (canAdd(pouch, slotItem)) {
            int current = contents != null ? contents.count() : 0;
            int available = SeedPouchContents.MAX_COUNT - current;
            if (available <= 0) return false;
            int toAdd = Math.min(available, slotItem.getCount());
            Item seedItem = slotItem.getItem();
            slotItem.shrink(toAdd);
            slot.set(slotItem.isEmpty() ? ItemStack.EMPTY : slotItem);
            updatePouch(pouch, seedItem, current + toAdd);
            playInsertSound(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        SeedPouchContents contents = pouch.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());

        if (other.isEmpty()) {
            if (contents == null || contents.count() == 0) return false;
            int toExtract = Math.min(contents.count(), new ItemStack(contents.seedType()).getMaxStackSize());
            access.set(new ItemStack(contents.seedType(), toExtract));
            updatePouch(pouch, contents.seedType(), contents.count() - toExtract);
            playRemoveSound(player);
            return true;
        } else if (canAdd(pouch, other)) {
            int current = contents != null ? contents.count() : 0;
            int available = SeedPouchContents.MAX_COUNT - current;
            if (available <= 0) return false;
            int toAdd = Math.min(available, other.getCount());
            Item seedItem = other.getItem();
            updatePouch(pouch, seedItem, current + toAdd);
            other.shrink(toAdd);
            access.set(other.isEmpty() ? ItemStack.EMPTY : other);
            playInsertSound(player);
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        SeedPouchContents contents = stack.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        if (contents != null && contents.count() > 0) {
            components.add(Component.translatable("tooltip.hearthandharvest.seed_pouch.contents",
                    Component.translatable(contents.seedType().getDescriptionId()), contents.count()).withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("tooltip.hearthandharvest.seed_pouch.area" + ": " + SeedPouchContents.AREA_NAMES[contents.plantRadius()]).withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("tooltip.hearthandharvest.seed_pouch.empty").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, components, flag);
    }

    public static void registerDispenseBehavior() {
        DispenserBlock.registerBehavior(HHModItems.SEED_POUCH.get(), new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                SeedPouchContents contents = stack.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
                if (contents == null || contents.count() == 0) return stack;

                Item seedItem = contents.seedType();
                if (!(seedItem instanceof BlockItem)) return stack;

                ServerLevel level = source.level();
                Direction facing = source.state().getValue(DispenserBlock.FACING);
                int radius = contents.plantRadius();
                int seedsLeft = contents.count();

                for (BlockPos candidate : buildGrid(source.pos(), facing, radius)) {
                    if (seedsLeft == 0) break;

                    BlockPos plantPos;
                    if (level.getBlockState(candidate).is(Tags.Blocks.VILLAGER_FARMLANDS)) {
                        plantPos = candidate.above();
                    } else if (level.getBlockState(candidate.below()).is(Tags.Blocks.VILLAGER_FARMLANDS)) {
                        plantPos = candidate;
                    } else {
                        continue;
                    }

                    if (!level.isEmptyBlock(plantPos)) continue;
                    BlockState cropState = ((BlockItem) seedItem).getBlock().defaultBlockState();
                    if (!cropState.canSurvive(level, plantPos)) continue;
                    level.setBlock(plantPos, cropState, Block.UPDATE_ALL);
                    seedsLeft--;
                }

                if (seedsLeft == contents.count()) return stack;
                if (seedsLeft == 0) stack.remove(HHModDataComponents.SEED_POUCH_CONTENTS.get());
                else stack.set(HHModDataComponents.SEED_POUCH_CONTENTS.get(), contents.withCount(seedsLeft));
                return stack;
            }
        });
    }

    private static List<BlockPos> buildGrid(BlockPos dispenserPos, Direction facing, int radius) {
        List<BlockPos> positions = new ArrayList<>();
        if (facing.getAxis() == Direction.Axis.Y) {
            for (int a = -radius; a <= radius; a++) {
                for (int b = -radius; b <= radius; b++) {
                    positions.add(dispenserPos.relative(facing).relative(Direction.NORTH, a).relative(Direction.EAST, b));
                }
            }
        } else {
            Direction perp = facing.getClockWise();
            for (int depth = 1; depth <= 2 * radius + 1; depth++) {
                for (int side = -radius; side <= radius; side++) {
                    positions.add(dispenserPos.relative(facing, depth).relative(perp, side));
                }
            }
        }
        return positions;
    }

    private void cycleRadius(ItemStack stack, SeedPouchContents contents, Player player) {
        int next = (contents.plantRadius() + 1) % SeedPouchContents.RADII.length;
        stack.set(HHModDataComponents.SEED_POUCH_CONTENTS.get(), contents.withRadius(next));
        player.displayClientMessage(
                Component.translatable("tooltip.hearthandharvest.seed_pouch.planting_area" + ": ")
                        .append(Component.literal(SeedPouchContents.AREA_NAMES[next]).withStyle(ChatFormatting.YELLOW)),
                true
        );
    }

    private boolean canAdd(ItemStack pouch, ItemStack stack) {
        if (stack.isEmpty()) return false;
        SeedPouchContents contents = pouch.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        if (contents != null && contents.count() > 0) return stack.getItem() == contents.seedType();
        return stack.is(Tags.Items.SEEDS);
    }

    private void updatePouch(ItemStack pouch, Item seedType, int newCount) {
        SeedPouchContents current = pouch.get(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        int radius = current != null ? current.plantRadius() : 0;
        if (newCount <= 0) pouch.remove(HHModDataComponents.SEED_POUCH_CONTENTS.get());
        else pouch.set(HHModDataComponents.SEED_POUCH_CONTENTS.get(), new SeedPouchContents(seedType, newCount, radius));
    }

    private void playInsertSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + player.level().getRandom().nextFloat() * 0.4f);
    }

    private void playRemoveSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + player.level().getRandom().nextFloat() * 0.4f);
    }
}