package team.tnt.collectoralbum.network.api;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket<T> {

    void encode(PacketBuffer buffer);

    T decode(PacketBuffer buffer);

    void handle(Supplier<NetworkEvent.Context> supplier);
}
