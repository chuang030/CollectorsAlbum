package team.tnt.collectoralbum.network.api;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface IServerPacket<T> extends IPacket<T> {

    void handleServersidePacket(MinecraftServer server, ServerPlayer player, ServerPacketListener listener, T packetData, PacketSender dispatcher);
}
