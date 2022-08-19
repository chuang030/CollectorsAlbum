package team.tnt.collectoralbum.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.common.menu.AlbumMenu;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.api.IPacketDecoder;
import team.tnt.collectoralbum.network.api.IPacketEncoder;
import team.tnt.collectoralbum.network.api.IServerPacket;

public class RequestAlbumPagePacket implements IServerPacket<RequestAlbumPagePacket.AlbumPacketData> {

    private static final ResourceLocation PACKET_ID = Networking.generateUniquePacketId(RequestAlbumPagePacket.class);
    private final AlbumPacketData data;

    public RequestAlbumPagePacket() {
        this(null);
    }

    public RequestAlbumPagePacket(ICardCategory category) {
        this.data = new AlbumPacketData(category);
    }

    @Override
    public ResourceLocation getPacketId() {
        return PACKET_ID;
    }

    @Override
    public AlbumPacketData getPacketData() {
        return data;
    }

    @Override
    public IPacketEncoder<AlbumPacketData> getEncoder() {
        return (packetData, buffer) -> {
            ICardCategory category = packetData.category;
            buffer.writeBoolean(category != null);
            if (category != null) {
                buffer.writeResourceLocation(category.getId());
            }
        };
    }

    @Override
    public IPacketDecoder<AlbumPacketData> getDecoder() {
        return (buffer) -> {
            boolean flag = buffer.readBoolean();
            ICardCategory category = null;
            if (flag) {
                category = CardCategoryRegistry.getByKey(buffer.readResourceLocation());
            }
            return new AlbumPacketData(category);
        };
    }

    @Override
    public void handleServersidePacket(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, AlbumPacketData packetData, PacketSender dispatcher) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() != ItemRegistry.ALBUM) {
            return;
        }
        AlbumContainer container = new AlbumContainer(stack);
        ICardCategory category = packetData.category;
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeItem(stack);
                buf.writeBoolean(category != null);
                if (category != null) {
                    buf.writeResourceLocation(category.getId());
                }
            }

            @Override
            public Component getDisplayName() {
                return TextComponent.EMPTY;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new AlbumMenu(container, inventory, i, packetData.category);
            }
        });
    }

    static class AlbumPacketData {

        private final ICardCategory category;

        AlbumPacketData(ICardCategory category) {
            this.category = category;
        }
    }
}
