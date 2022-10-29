package team.tnt.collectoralbum.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.api.IAlbumScreenFactory;
import team.tnt.collectoralbum.client.screen.AlbumScreen;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.menu.AlbumMenu;
import team.tnt.collectoralbum.data.boosts.OpType;
import team.tnt.collectoralbum.network.Networking;

public class CollectorsAlbumClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(CollectorsAlbumClient.class);
    private int timer;
    private static int albumPageIndex;
    private static int pageCount;

    @Override
    public void onInitializeClient() {
        Networking.registerClientReceivers();
        ScreenRegistry.register(MenuTypes.ALBUM, (AlbumMenu menu, Inventory inventory, Component title) -> {
            ICardCategory category = menu.getCategory();
            if (category == null) {
                return new AlbumScreen(menu, inventory, title);
            }
            IAlbumScreenFactory factory = category.getAlbumScreenFactory();
            return factory.createAlbumScreen(menu, inventory, title);
        });
        ClientTickEvents.START_WORLD_TICK.register(this::handleClientTick);
    }

    public void handleClientTick(Level level) {
        ++timer;
        CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoosts()
                .ifPresent(collection -> {
                    pageCount = collection.getActionsCount(OpType.ACTIVE);
                    albumPageIndex = (timer / 60) % pageCount;
                });
    }

    public static int getPageCount() {
        return pageCount;
    }

    public static int getAlbumPageIndex() {
        return albumPageIndex;
    }
}
