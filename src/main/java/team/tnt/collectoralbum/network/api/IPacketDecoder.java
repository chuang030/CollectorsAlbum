package team.tnt.collectoralbum.network.api;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

@FunctionalInterface
public interface IPacketDecoder<T> extends Function<FriendlyByteBuf, T> {

    T decode(FriendlyByteBuf buffer);

    @Override
    default T apply(FriendlyByteBuf friendlyByteBuf) {
        return this.decode(friendlyByteBuf);
    }
}
