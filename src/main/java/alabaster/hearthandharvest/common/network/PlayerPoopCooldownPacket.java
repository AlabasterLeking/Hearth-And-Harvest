package alabaster.hearthandharvest.common.network;

import alabaster.hearthandharvest.HearthAndHarvest;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerPoopCooldownPacket() implements CustomPacketPayload {

    public static final Type<PlayerPoopCooldownPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "player_poop_cooldown")
    );
    public static final StreamCodec<ByteBuf, PlayerPoopCooldownPacket> STREAM_CODEC =
            StreamCodec.unit(new PlayerPoopCooldownPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}