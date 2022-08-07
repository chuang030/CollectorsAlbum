package team.tnt.collectoralbum.common.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.item.CardCategory;

public class AlbumMenu extends AbstractContainerMenu {

    private final AlbumContainer container;
    private final CardCategory category;

    public AlbumMenu(AlbumContainer container, Inventory playerInventory, int id, CardCategory category) {
        super(MenuTypes.ALBUM, id);
        this.container = container;
        this.category = category;

        SimpleContainer albumContainer = container.forCategory(category);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
