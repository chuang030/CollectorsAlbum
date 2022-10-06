package team.tnt.collectoralbum.network.packet;

import net.minecraft.network.PacketBuffer;

public abstract class AbstractNetworkHandlePacket<T> extends AbstractNetworkPacket<T> {

    public abstract T instantiate();

    @Override
    public final void encode(PacketBuffer buffer) {
    }

    @Override
    public T decode(PacketBuffer buffer) {
        return this.instantiate();
    }
}
