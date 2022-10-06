package team.tnt.collectoralbum.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    public void init(FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ScreenManager.register(MenuTypes.ALBUM.get(), (AlbumMenu menu, PlayerInventory inventory, ITextComponent title) -> {
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
