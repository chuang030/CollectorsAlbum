package team.tnt.collectoralbum.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import team.tnt.collectoralbum.client.screen.AlbumScreen;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

@FunctionalInterface
public interface IAlbumScreenFactory {

    IAlbumScreenFactory DEFAULT = AlbumScreen::new;

    AlbumScreen createAlbumScreen(AlbumMenu menu, Inventory inventory, Component component);
}
