package alabaster.hearthandharvest.common.events;

import alabaster.hearthandharvest.common.block.IHarvestable;
import alabaster.hearthandharvest.common.registry.HHModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = "hearthandharvest")
public class HoeEnchantmentEvents {

    private static final Map<Block, BlockState> TILL_MAP = Map.of(
            Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(),
            Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(),
            Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState(),
            Blocks.ROOTED_DIRT, Blocks.DIRT.defaultBlockState(),
            Blocks.DIRT_PATH, Blocks.DIRT.defaultBlockState()
    );

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        Player player = event.getEntity();
        ItemStack hoe = event.getItemStack();
        if (!(hoe.getItem() instanceof HoeItem)) return;

        Level level = (Level) event.getLevel();
        BlockPos center = event.getPos();
        BlockState centerState = level.getBlockState(center);

        int tillingLevel = getLevel(hoe, HHModEnchantments.TILLING, level);
        if (tillingLevel > 0
                && TILL_MAP.containsKey(centerState.getBlock())
                && level.getBlockState(center.above()).isAir()) {
            int r = tillingLevel;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    tryTill(level, center.offset(dx, 0, dz), player);
                }
            }
        }

        int harvestingLevel = getLevel(hoe, HHModEnchantments.HARVESTING, level);
        if (harvestingLevel > 0 && isFullyGrownCrop(centerState)) {
            int r = harvestingLevel;

            // BFS flood-fill through orthogonally connected harvestable blocks
            // bounded by a cube of radius r in all three axes.
            Set<BlockPos> visited = new HashSet<>();
            Deque<BlockPos> queue = new ArrayDeque<>();

            harvestCrop(level, center, centerState, player, hoe);
            visited.add(center);
            queue.add(center);

            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    if (visited.contains(neighbor)) continue;
                    visited.add(neighbor);
                    if (Math.abs(neighbor.getX() - center.getX()) > r
                            || Math.abs(neighbor.getY() - center.getY()) > r
                            || Math.abs(neighbor.getZ() - center.getZ()) > r) continue;
                    BlockState state = level.getBlockState(neighbor);
                    if (!isFullyGrownCrop(state)) continue;
                    harvestCrop(level, neighbor, state, player, hoe);
                    queue.add(neighbor);
                }
            }

            hoe.hurtAndBreak(1, player, LivingEntity.getSlotForHand(event.getHand()));
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static void tryTill(Level level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        BlockState result = TILL_MAP.get(state.getBlock());
        if (result == null || !level.getBlockState(pos.above()).isAir()) return;
        level.setBlock(pos, result, Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player));
    }

    private static void harvestCrop(Level level, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        if (state.getBlock() instanceof IHarvestable h) {
            h.harvestBlock(state, level, pos, player, tool);
        } else {
            Block.dropResources(state, level, pos, null, player, tool);
            level.setBlock(pos, state.getBlock().defaultBlockState(), Block.UPDATE_ALL);
        }
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player));
    }

    private static boolean isFullyGrownCrop(BlockState state) {
        if (state.getBlock() instanceof IHarvestable h) return h.isHarvestReady(state);
        return state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state);
    }

    private static int getLevel(ItemStack stack, ResourceKey<Enchantment> key, Level level) {
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(key)
                .map(h -> stack.getEnchantmentLevel(h))
                .orElse(0);
    }
}