package team.tnt.collectoralbum.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractNetworkHandlePacket<T> extends AbstractNetworkPacket<T> {

    public abstract T instantiate();

    @Override
    public final void encode(FriendlyByteBuf buffer) {
    }

    @Override
    public T decode(FriendlyByteBuf buffer) {
        return this.instantiate();
    }
}
