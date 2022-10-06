package team.tnt.collectoralbum.api;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import team.tnt.collectoralbum.client.screen.AlbumScreen;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

@FunctionalInterface
public interface IAlbumScreenFactory {

    IAlbumScreenFactory DEFAULT = AlbumScreen::new;

    AlbumScreen createAlbumScreen(AlbumMenu menu, PlayerInventory inventory, ITextComponent component);
}
