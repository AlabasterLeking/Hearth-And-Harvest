package alabaster.hearthandharvest.common.network;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.client.event.ClientEventHandler;
import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "hearthandharvest", bus = EventBusSubscriber.Bus.MOD)
public class HHModNetworking {

    private static final int POOP_COOLDOWN_TICKS = 300; // 15 seconds

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar handler = event.registrar("1");

        handler.playToServer(
                PlayerPoopPacket.TYPE,
                PlayerPoopPacket.STREAM_CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> {
                    if (!Config.PLAYER_POOP_ENABLED.get()) return;
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    long now = player.level().getGameTime();
                    if (now - player.getData(HHModAttachments.PLAYER_LAST_POOP_TIME.get()) < POOP_COOLDOWN_TICKS) return;
                    FoodData food = player.getFoodData();
                    if (food.getFoodLevel() < 1) return;
                    food.setFoodLevel(food.getFoodLevel() - 1);
                    player.setData(HHModAttachments.PLAYER_LAST_POOP_TIME.get(), now);
                    player.spawnAtLocation(HHModItems.MANURE.get());
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            HHModSounds.FART.get(), SoundSource.PLAYERS,
                            0.7f, 0.8f + player.getRandom().nextFloat() * 0.4f);
                    PacketDistributor.sendToPlayer(player, new PlayerPoopCooldownPacket());
                })
        );

        handler.playToClient(
                PlayerPoopCooldownPacket.TYPE,
                PlayerPoopCooldownPacket.STREAM_CODEC,
                (packet, ctx) -> ctx.enqueueWork(ClientEventHandler::startCooldown)
        );
    }
}