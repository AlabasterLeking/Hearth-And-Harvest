package alabaster.hearthandharvest.common.item;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import alabaster.hearthandharvest.common.block.CrateBlock;
import alabaster.hearthandharvest.common.block.entity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CrateBlockItem extends BlockItem {

    private static final int MAX_TOOLTIP_LINES = 5;

    public CrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static void registerDispenseBehavior(Item crateItem) {
        DispenserBlock.registerBehavior(
                crateItem,
                new OptionalDispenseItemBehavior() {
                    @Override
                    public ItemStack execute(BlockSource source, ItemStack stack) {
                        Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                        BlockPos target = source.getPos().relative(facing);
                        DirectionalPlaceContext ctx = new DirectionalPlaceContext(
                                source.getLevel(), target, facing, stack, facing.getOpposite());
                        InteractionResult result = ((CrateBlockItem) stack.getItem()).place(ctx);
                        setSuccess(result.consumesAction());
                        return stack;
                    }
                }
        );
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState existing = level.getBlockState(pos);

        boolean willBeDouble = existing.is(this.getBlock())
                && existing.getValue(CrateBlock.TYPE) != SlabType.DOUBLE;

        NonNullList<ItemStack> bottomSnapshot = null;
        NonNullList<ItemStack> topContents = null;

        if (willBeDouble && !level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof CrateBlockEntity existingCrate) {
                bottomSnapshot = NonNullList.withSize(CrateBlockEntity.SLOTS_PER_HALF, ItemStack.EMPTY);
                for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
                    bottomSnapshot.set(i, existingCrate.getItem(i).copy());
                }
            }

            topContents = NonNullList.withSize(CrateBlockEntity.SLOTS_PER_HALF, ItemStack.EMPTY);
            CompoundTag itemData = BlockItem.getBlockEntityData(context.getItemInHand());
            if (itemData != null) {
                ContainerHelper.loadAllItems(itemData, topContents);
            }
        }

        InteractionResult result = super.place(context);

        if (willBeDouble && result.consumesAction() && !level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof CrateBlockEntity newCrate) {
                for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
                    newCrate.setItem(CrateBlockEntity.SLOTS_PER_HALF + i,
                            topContents != null ? topContents.get(i) : ItemStack.EMPTY);
                }
                if (bottomSnapshot != null) {
                    for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
                        newCrate.setItem(i, bottomSnapshot.get(i));
                    }
                }
                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            }
        }

        return result;
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) return super.getName(stack);

        NonNullList<ItemStack> items = NonNullList.withSize(CrateBlockEntity.TOTAL_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(data, items);

        ItemStack first = ItemStack.EMPTY;
        ResourceLocation firstKey = null;
        for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
            ItemStack s = items.get(i);
            if (!s.isEmpty()) { first = s; firstKey = BuiltInRegistries.ITEM.getKey(s.getItem()); break; }
        }
        if (first.isEmpty()) return super.getName(stack);

        for (int i = 0; i < CrateBlockEntity.SLOTS_PER_HALF; i++) {
            ItemStack s = items.get(i);
            if (s.isEmpty()) continue;
            if (!BuiltInRegistries.ITEM.getKey(s.getItem()).equals(firstKey))
                return Component.translatable("block.hearthandharvest.crate.mixed");
        }

        return Component.translatable("block.hearthandharvest.crate.named", first.getHoverName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) return;

        NonNullList<ItemStack> items = NonNullList.withSize(CrateBlockEntity.TOTAL_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(data, items);

        LinkedHashMap<ResourceLocation, ItemStack> representatives = new LinkedHashMap<>();
        LinkedHashMap<ResourceLocation, Integer> grouped = new LinkedHashMap<>();
        for (ItemStack s : items) {
            if (s.isEmpty()) continue;
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(s.getItem());
            representatives.putIfAbsent(key, s);
            grouped.merge(key, s.getCount(), Integer::sum);
        }

        if (grouped.isEmpty()) return;

        List<Map.Entry<ResourceLocation, Integer>> entries = new ArrayList<>(grouped.entrySet());

        int shown = Math.min(entries.size(), MAX_TOOLTIP_LINES);
        for (int i = 0; i < shown; i++) {
            var e = entries.get(i);
            ItemStack rep = representatives.get(e.getKey());
            int count = e.getValue();
            Component line = Component.literal("  ").append(rep.getHoverName());
            if (count > 1) {
                line = ((MutableComponent) line).append(
                        Component.literal(" \u00d7 " + count)
                                .withStyle(style -> style.withColor(0xAAAAAA)));
            }
            tooltip.add(line);
        }

        int remaining = entries.size() - shown;
        if (remaining > 0) {
            tooltip.add(
                    Component.translatable("container.shulkerBox.more", remaining)
                            .withStyle(style -> style.withColor(0x888888))
            );
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return alabaster.hearthandharvest.client.renderer.CrateItemRenderer.getInstance();
            }
        });
    }
}
