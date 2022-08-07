package team.tnt.collectoralbum.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.item.CardCategory;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class MenuTypes {

    public static final MenuType<AlbumMenu> ALBUM = new MenuType<>((i, inventory) -> new AlbumMenu(new AlbumContainer(ItemStack.EMPTY), inventory, i, CardCategory.ARMOR));

    public static void registerMenus() {
        registerMenuType("album", ALBUM);
    }

    private static void registerMenuType(String id, MenuType<?> type) {
        Registry.register(Registry.MENU, new ResourceLocation(CollectorsAlbum.MODID, id), type);
    }
}
