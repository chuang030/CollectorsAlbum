package team.tnt.collectoralbum.client;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.network.Networking;

public class CollectorsAlbumClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(CollectorsAlbumClient.class);

    @Override
    public void onInitializeClient() {
        Networking.registerClientReceivers();
    }
}
