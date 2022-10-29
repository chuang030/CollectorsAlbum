package team.tnt.collectoralbum.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.api.IClientPacket;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;
import team.tnt.collectoralbum.network.packet.data.EmptyPacketData;

public class SendAlbumBoostsPacket implements IClientPacket<EmptyPacketData> {

    private static final ResourceLocation PACKET_ID = Networking.generateUniquePacketId(SendAlbumBoostsPacket.class);

    @Override
    public ResourceLocation getPacketId() {
        return PACKET_ID;
    }

    @Override
    public EmptyPacketData getPacketData() {
        return EmptyPacketData.EMPTY;
    }

    @Override
    public IPacketEncoder<EmptyPacketData> getEncoder() {
        return (data, buffer) -> CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoosts()
                .ifPresent(boosts -> boosts.encode(buffer));
    }

    @Override
    public IPacketDecoder<EmptyPacketData> getDecoder() {
        return buffer -> {
            CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoosts()
                    .ifPresent(boosts -> boosts.decode(buffer));
            return EmptyPacketData.EMPTY;
        };
    }

    @Override
    public void handleClientsidePacket(Minecraft client, ClientPacketListener listener, EmptyPacketData packetData, PacketSender dispatcher) {
    }
}
