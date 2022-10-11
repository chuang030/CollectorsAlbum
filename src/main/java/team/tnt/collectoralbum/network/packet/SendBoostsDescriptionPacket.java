package team.tnt.collectoralbum.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import team.tnt.collectoralbum.CollectorsAlbum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendBoostsDescriptionPacket extends AbstractNetworkPacket<SendBoostsDescriptionPacket> {

    private final List<ITextComponent> list;

    public SendBoostsDescriptionPacket() {
        this.list = Collections.emptyList();
    }

    public SendBoostsDescriptionPacket(ITextComponent[] components) {
        this.list = Arrays.asList(components);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(list.size());
        list.forEach(buffer::writeComponent);
    }

    @Override
    public SendBoostsDescriptionPacket decode(PacketBuffer buffer) {
        int i = buffer.readInt();
        ITextComponent[] components = new ITextComponent[i];
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
