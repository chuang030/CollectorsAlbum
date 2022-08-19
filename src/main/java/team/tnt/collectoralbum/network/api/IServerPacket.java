package team.tnt.collectoralbum.network.api;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface IServerPacket<T> extends IPacket<T> {

    void handleServersidePacket(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, T packetData, PacketSender dispatcher);
}
