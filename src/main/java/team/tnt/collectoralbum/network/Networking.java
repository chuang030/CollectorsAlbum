package team.tnt.collectoralbum.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.network.api.IPacket;
import team.tnt.collectoralbum.network.packet.OpenCardScreenPacket;
import team.tnt.collectoralbum.network.packet.RequestAlbumPagePacket;
import team.tnt.collectoralbum.network.packet.RequestCardPackDropPacket;
import team.tnt.collectoralbum.network.packet.SendAlbumBoostsPacket;

import java.lang.reflect.InvocationTargetException;

public class Networking {

    private static final String NETWORK_PROTOCOL_VERSION = "collectorsalbum-v1";
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CollectorsAlbum.MODID, "network"),
            () -> NETWORK_PROTOCOL_VERSION,
            NETWORK_PROTOCOL_VERSION::equals,
            NETWORK_PROTOCOL_VERSION::equals
    );
    private static byte packetId;

    // Packet dispatching
    // --------------------------------------------------------------------------

    public static void dispatchServerPacket(IPacket<?> packet) {
        NETWORK_CHANNEL.sendToServer(packet);
    }

    public static void dispatchClientPacket(ServerPlayerEntity serverPlayerRef, IPacket<?> packet) {
        NETWORK_CHANNEL.sendTo(packet, serverPlayerRef.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    // Packet registration
    // --------------------------------------------------------------------------

    public static void registerPackets() {
        registerPacket(OpenCardScreenPacket.class);
        registerPacket(RequestAlbumPagePacket.class);
        registerPacket(RequestCardPackDropPacket.class);
        registerPacket(SendAlbumBoostsPacket.class);
    }

    // Internal
    // --------------------------------------------------------------------------

    private static <T extends IPacket<T>> void registerPacket(Class<T> packetClass) {
        T instance;
        try {
            instance = packetClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        NETWORK_CHANNEL.registerMessage(packetId++, packetClass, IPacket::encode, instance::decode, IPacket::handle);
    }
}
