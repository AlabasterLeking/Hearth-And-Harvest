package alabaster.hearthandharvest.common.network;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.event.ClientEventHandler;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHDataMaps;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class HHModNetworking {
    private static final String PROTOCOL = "1";
    private static final int POOP_COOLDOWN_TICKS = 300; // 15 seconds

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HearthAndHarvest.MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, PlayerPoopPacket.class, (msg, buf) -> {}, buf -> new PlayerPoopPacket(),
                HHModNetworking::handlePoop, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, PlayerPoopCooldownPacket.class, (msg, buf) -> {}, buf -> new PlayerPoopCooldownPacket(),
                HHModNetworking::handlePoopCooldown, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, DataMapSyncPacket.class, DataMapSyncPacket::encode, DataMapSyncPacket::decode,
                HHModNetworking::handleDataMapSync, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAll(Object packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    private static void handlePoop(PlayerPoopPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !Config.PLAYER_POOP_ENABLED.get()) return;
            long now = player.level().getGameTime();
            if (now - HHModAttachments.PLAYER_LAST_POOP_TIME.get(player) < POOP_COOLDOWN_TICKS) return;
            FoodData food = player.getFoodData();
            if (food.getFoodLevel() < 1) return;
            food.setFoodLevel(food.getFoodLevel() - 1);
            HHModAttachments.PLAYER_LAST_POOP_TIME.set(player, now);
            player.spawnAtLocation(HHModItems.MANURE.get());
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    HHModSounds.FART.get(), SoundSource.PLAYERS,
                    0.7f, 0.8f + player.getRandom().nextFloat() * 0.4f);
            sendToPlayer(player, new PlayerPoopCooldownPacket());
            HHSimpleTrigger.trigger(HHModTriggers.PLAYER_POOPED, player);
        });
        ctx.get().setPacketHandled(true);
    }

    private static void handlePoopCooldown(PlayerPoopCooldownPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEventHandler::startCooldown));
        ctx.get().setPacketHandled(true);
    }

    private static void handleDataMapSync(DataMapSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> HHDataMaps.applySynced(packet.entries()));
        ctx.get().setPacketHandled(true);
    }

    public record PlayerPoopPacket() {
    }

    public record PlayerPoopCooldownPacket() {
    }

    public record DataMapSyncPacket(Map<ResourceLocation, String> entries) {
        void encode(FriendlyByteBuf buf) {
            buf.writeMap(entries, FriendlyByteBuf::writeResourceLocation, (b, s) -> b.writeUtf(s, Integer.MAX_VALUE / 4));
        }

        static DataMapSyncPacket decode(FriendlyByteBuf buf) {
            return new DataMapSyncPacket(buf.readMap(HashMap::new, FriendlyByteBuf::readResourceLocation, b -> b.readUtf(Integer.MAX_VALUE / 4)));
        }
    }
}
