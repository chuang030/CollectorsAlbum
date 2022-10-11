package team.tnt.collectoralbum.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.api.IClientPacket;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendBoostsDescriptionPacket implements IClientPacket<List<Component>> {

    private static final ResourceLocation PACKET_ID = Networking.generateUniquePacketId(SendBoostsDescriptionPacket.class);

    private final List<Component> list;

    public SendBoostsDescriptionPacket() {
        this.list = Collections.emptyList();
    }

    public SendBoostsDescriptionPacket(Component[] components) {
        this.list = Arrays.asList(components);
    }

    @Override
    public ResourceLocation getPacketId() {
        return PACKET_ID;
    }

    @Override
    public List<Component> getPacketData() {
        return list;
    }

    @Override
    public IPacketEncoder<List<Component>> getEncoder() {
        return (data, buffer) -> {
            buffer.writeInt(data.size());
            data.forEach(buffer::writeComponent);
        };
    }

    @Override
    public IPacketDecoder<List<Component>> getDecoder() {
        return buffer -> {
            int i = buffer.readInt();
            List<Component> res = new ArrayList<>(i);
            for (int j = 0; j < i; j++) {
                res.add(buffer.readComponent());
            }
            return res;
        };
    }

    @Override
    public void handleClientsidePacket(Minecraft client, ClientPacketListener listener, List<Component> packetData, PacketSender dispatcher) {
        CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.loadDescriptionFromList(packetData);
    }
}
