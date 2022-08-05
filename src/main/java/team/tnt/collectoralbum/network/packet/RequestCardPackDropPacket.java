package team.tnt.collectoralbum.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;
import team.tnt.collectoralbum.network.api.IServerPacket;
import team.tnt.collectoralbum.network.packet.data.EmptyPacketData;
import team.tnt.collectoralbum.server.OpenCardPackContextHolder;
import team.tnt.collectoralbum.util.PlayerHelper;

import java.util.List;
import java.util.Optional;

public class RequestCardPackDropPacket implements IServerPacket<EmptyPacketData> {

    private static final ResourceLocation PACKET_ID = Networking.generateUniquePacketId(RequestCardPackDropPacket.class);

    @Override
    public ResourceLocation getPacketId() {
        return PACKET_ID;
    }

    @Override
    public EmptyPacketData getPacketData() {
        return EmptyPacketData.EMPTY;
    }

    @Override
    public IPacketEncoder<EmptyPacketData> getEncoder() {
        return EmptyPacketData.EMPTY;
    }

    @Override
    public IPacketDecoder<EmptyPacketData> getDecoder() {
        return EmptyPacketData.EMPTY;
    }

    @Override
    public void handleServersidePacket(MinecraftServer server, ServerPlayer player, ServerPacketListener listener, EmptyPacketData packetData, PacketSender dispatcher) {
        Optional<List<ItemStack>> optional = OpenCardPackContextHolder.getContext(player);
        optional.ifPresent(list -> {
            for (ItemStack stack : list) {
                PlayerHelper.giveItem(player, stack);
            }
        });
    }
}
