package team.tnt.collectoralbum.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import team.tnt.collectoralbum.CollectorsAlbum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendBoostsDescriptionPacket extends AbstractNetworkPacket<SendBoostsDescriptionPacket> {

    private final List<Component> list;

    public SendBoostsDescriptionPacket() {
        this.list = Collections.emptyList();
    }

    public SendBoostsDescriptionPacket(Component[] components) {
        this.list = Arrays.asList(components);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(list.size());
        list.forEach(buffer::writeComponent);
    }

    @Override
    public SendBoostsDescriptionPacket decode(FriendlyByteBuf buffer) {
        int i = buffer.readInt();
        Component[] components = new Component[i];
        for (int j = 0; j < i; j++) {
            components[j] = buffer.readComponent();
        }
        return new SendBoostsDescriptionPacket(components);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.loadDescriptionFromList(list);
    }
}
