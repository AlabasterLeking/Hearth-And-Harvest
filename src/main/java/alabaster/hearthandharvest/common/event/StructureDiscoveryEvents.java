package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class StructureDiscoveryEvents {
    private static final int CHECK_INTERVAL = 40;
    private static final ResourceKey<Structure> CORN_MAZE = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(HearthAndHarvest.MODID, "corn_maze"));
    private static final ResourceLocation CORN_MAZE_ADVANCEMENT = new ResourceLocation(HearthAndHarvest.MODID, "farming/a_maize_ing");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (player.tickCount % CHECK_INTERVAL != 0 || player.isSpectator()) return;
        if (hasAdvancement(player, CORN_MAZE_ADVANCEMENT)) return;

        ServerLevel level = player.serverLevel();
        Structure cornMaze = level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(CORN_MAZE);
        if (cornMaze == null) return;

        if (level.structureManager().getStructureWithPieceAt(player.blockPosition(), cornMaze).isValid()) {
            HHModTriggers.FOUND_CORN_MAZE.trigger(player);
        }
    }

    private static boolean hasAdvancement(ServerPlayer player, ResourceLocation id) {
        Advancement holder = player.server.getAdvancements().getAdvancement(id);
        return holder == null || player.getAdvancements().getOrStartProgress(holder).isDone();
    }
}