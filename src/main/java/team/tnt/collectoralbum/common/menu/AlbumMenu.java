package team.tnt.collectoralbum.common.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.common.init.MenuTypes;

public class AlbumMenu extends AbstractContainerMenu {

    public AlbumMenu(int id) {
        super(MenuTypes.ALBUM, id);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
