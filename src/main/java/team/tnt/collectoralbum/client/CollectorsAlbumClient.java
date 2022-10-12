package team.tnt.collectoralbum.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import team.tnt.collectoralbum.api.IAlbumScreenFactory;
import team.tnt.collectoralbum.client.screen.AlbumScreen;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public final class CollectorsAlbumClient {

    private static final CollectorsAlbumClient INSTANCE = new CollectorsAlbumClient();

    public static CollectorsAlbumClient getClient() {
        return INSTANCE;
    }

    public void synchInit(ParallelDispatchEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypes.ALBUM.get(), (AlbumMenu menu, Inventory inventory, Component title) -> {
                ICardCategory category = menu.getCategory();
                if (category == null) {
                    return new AlbumScreen(menu, inventory, title);
                }
                IAlbumScreenFactory factory = category.getAlbumScreenFactory();
                return factory.createAlbumScreen(menu, inventory, title);
            });
        });
    }
}