package team.tnt.collectoralbum.network.packet.data;

import net.minecraft.network.FriendlyByteBuf;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;

public final class EmptyPacketData implements IPacketEncoder<EmptyPacketData>, IPacketDecoder<EmptyPacketData> {

    public static final EmptyPacketData EMPTY = new EmptyPacketData();

    private EmptyPacketData() {
    }

    @Override
    public void encode(EmptyPacketData data, FriendlyByteBuf buffer) {
    }

    @Override
    public EmptyPacketData decode(FriendlyByteBuf buffer) {
        return EMPTY;
    }
}
