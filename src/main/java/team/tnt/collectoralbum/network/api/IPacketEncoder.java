package team.tnt.collectoralbum.network.api;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IPacketEncoder<T> extends BiConsumer<T, FriendlyByteBuf> {

    void encode(T data, FriendlyByteBuf buffer);

    @Override
    default void accept(T t, FriendlyByteBuf friendlyByteBuf) {
        this.encode(t, friendlyByteBuf);
    }
}
