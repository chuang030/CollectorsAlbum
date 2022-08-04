package team.tnt.collectoralbum.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.client.CollectorsAlbumClient;
import team.tnt.collectoralbum.network.api.*;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;

public class Networking {

    // Packet dispatching
    // --------------------------------------------------------------------------

    public static <T> void dispatchServerPacket(IServerPacket<T> serverPacket) {
        dispatch(serverPacket, ClientPlayNetworking::send);
    }

    public static <T> void dispatchClientPacket(ServerPlayer serverPlayerRef, IClientPacket<T> clientPacket) {
        dispatch(clientPacket, (packetId, buffer) -> ServerPlayNetworking.send(serverPlayerRef, packetId, buffer));
    }

    // Packet registration
    // --------------------------------------------------------------------------

    @Environment(EnvType.CLIENT)
    public static void registerClientReceivers() {}

    public static void registerServerReceivers() {}

    // Internal
    // --------------------------------------------------------------------------

    private static <T> void dispatch(IPacket<T> packet, BiConsumer<ResourceLocation, FriendlyByteBuf> dispatcher) {
        ResourceLocation packetId = packet.getPacketId();
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        IPacketEncoder<T> encoder = packet.getEncoder();
        T data = packet.getPacketData();
        encoder.encode(data, buffer);
        dispatcher.accept(packetId, buffer);
    }

    @Environment(EnvType.CLIENT)
    private static <T> void registerServer2ClientReceiver(Class<? extends IClientPacket<T>> clientPacketClass) {
        try {
            IClientPacket<T> packet = clientPacketClass.getDeclaredConstructor().newInstance();
            ResourceLocation packetId = packet.getPacketId();
            ClientPlayNetworking.registerGlobalReceiver(packetId, (client, handler, buffer, responseDispatcher) -> {
                IPacketDecoder<T> decoder = packet.getDecoder();
                T packetData = decoder.decode(buffer);
                client.execute(() -> packet.handleClientsidePacket(client, handler, packetData, responseDispatcher));
            });
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            CollectorsAlbumClient.LOGGER.fatal("Couldn't instantiate new client packet from class {}, make sure it declares public default constructor", clientPacketClass.getSimpleName());
            throw new RuntimeException(exc);
        }
    }

    private static <T> void registerClient2ServerReceiver(Class<? extends IServerPacket<T>> serverPacketClass) {
        try {
            IServerPacket<T> packet = serverPacketClass.getDeclaredConstructor().newInstance();
            ResourceLocation packetId = packet.getPacketId();
            ServerPlayNetworking.registerGlobalReceiver(packetId, (server, player, handler, buffer, responseDispatcher) -> {
                IPacketDecoder<T> decoder = packet.getDecoder();
                T packetData = decoder.decode(buffer);
                server.execute(() -> packet.handleServersidePacket(server, player, handler, packetData, responseDispatcher));
            });
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            CollectorsAlbum.LOGGER.fatal("Couldn't instantiate new server packet from class {}, make sure it declares public default constructor", serverPacketClass.getSimpleName());
            throw new RuntimeException(exc);
        }
    }
}
