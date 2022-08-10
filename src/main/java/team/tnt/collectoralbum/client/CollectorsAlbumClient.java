package team.tnt.collectoralbum.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.client.screen.AlbumScreen;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.network.Networking;

public class CollectorsAlbumClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(CollectorsAlbumClient.class);

    @Override
    public void onInitializeClient() {
        Networking.registerClientReceivers();
        ScreenRegistry.register(MenuTypes.ALBUM, AlbumScreen::new);
    }
}
