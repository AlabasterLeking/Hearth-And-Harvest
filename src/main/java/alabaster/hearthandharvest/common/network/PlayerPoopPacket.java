package alabaster.hearthandharvest.common.network;

import alabaster.hearthandharvest.HearthAndHarvest;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerPoopPacket() implements CustomPacketPayload {

    public static final Type<PlayerPoopPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "player_poop")
    );
    public static final StreamCodec<ByteBuf, PlayerPoopPacket> STREAM_CODEC =
            StreamCodec.unit(new PlayerPoopPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}