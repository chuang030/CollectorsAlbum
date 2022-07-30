package team.tnt.collectoralbum.common.handler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class AlbumScreenHandler extends ExtendedScreenHandlerType<AlbumMenu> {

    public AlbumScreenHandler(ExtendedFactory<AlbumMenu> factory) {
        super(factory);
    }
}
