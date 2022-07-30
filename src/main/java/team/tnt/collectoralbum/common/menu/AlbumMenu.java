package team.tnt.collectoralbum.common.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class AlbumMenu extends AbstractContainerMenu {

    public AlbumMenu(@Nullable MenuType<?> menuType, int id) {
        super(menuType, id);

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
