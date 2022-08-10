package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.packet.RequestCardPackDropPacket;

@Testable
public class TestPacketNames {

    @Test
    public void testPacketNameGen() {
        Assertions.assertEquals("collectorsalbum:request_card_pack_drop_packet", Networking.generateUniquePacketId(RequestCardPackDropPacket.class).toString());
    }
}
