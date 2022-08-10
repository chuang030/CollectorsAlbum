package team.tnt.collectoralbum.network.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.client.screen.CardOpenScreen;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.api.IClientPacket;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;

import java.util.ArrayList;
import java.util.List;

public class OpenCardScreenPacket implements IClientPacket<OpenCardScreenPacket.Data> {

    private static final ResourceLocation PACKET_ID = Networking.generateUniquePacketId(OpenCardScreenPacket.class);

    private final Data data;

    public OpenCardScreenPacket() {
        data = null;
    }

    public OpenCardScreenPacket(List<ItemStack> drops) {
        this.data = new Data(drops);
    }

    @Override
    public ResourceLocation getPacketId() {
        return PACKET_ID;
    }

    @Override
    public Data getPacketData() {
        return data;
    }

    @Override
    public IPacketEncoder<Data> getEncoder() {
        return (packetData, buffer) -> {
            List<ItemStack> list = packetData.drops();
            buffer.writeInt(list.size());
            for (ItemStack stack : list) {
                buffer.writeItem(stack);
            }
        };
    }

    @Override
    public IPacketDecoder<Data> getDecoder() {
        return buffer -> {
            int count = buffer.readInt();
            List<ItemStack> list = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                ItemStack stack = buffer.readItem();
                list.add(stack);
            }
            return new Data(list);
        };
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleClientsidePacket(Minecraft client, ClientPacketListener listener, Data packetData, PacketSender dispatcher) {
        CardOpenScreen screen = new CardOpenScreen(packetData.drops());
        client.setScreen(screen);
    }

    record Data(List<ItemStack> drops) {
    }
}
