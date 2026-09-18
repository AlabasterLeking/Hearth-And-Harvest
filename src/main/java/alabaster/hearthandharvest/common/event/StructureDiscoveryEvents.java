package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class StructureDiscoveryEvents {
    private static final int CHECK_INTERVAL = 40;
    private static final ResourceKey<Structure> CORN_MAZE = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "corn_maze"));
    private static final ResourceLocation CORN_MAZE_ADVANCEMENT = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "farming/a_maize_ing");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % CHECK_INTERVAL != 0 || player.isSpectator()) return;
        if (hasAdvancement(player, CORN_MAZE_ADVANCEMENT)) return;

        ServerLevel level = player.serverLevel();
        Structure cornMaze = level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(CORN_MAZE);
        if (cornMaze == null) return;

        if (level.structureManager().getStructureWithPieceAt(player.blockPosition(), cornMaze).isValid()) {
            HHModTriggers.FOUND_CORN_MAZE.get().trigger(player);
        }
    }

    private static boolean hasAdvancement(ServerPlayer player, ResourceLocation id) {
        AdvancementHolder holder = player.server.getAdvancements().get(id);
        return holder == null || player.getAdvancements().getOrStartProgress(holder).isDone();
    }
}