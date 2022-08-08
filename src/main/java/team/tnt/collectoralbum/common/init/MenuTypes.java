package team.tnt.collectoralbum.common.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.item.CardCategory;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class MenuTypes {

    public static final ExtendedScreenHandlerType<AlbumMenu> ALBUM = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        ItemStack stack = buf.readItem();
        int id = buf.readInt();
        AlbumContainer container = new AlbumContainer(stack);
        CardCategory category = id == 0 ? null : CardCategory.values()[id - 1];
        return new AlbumMenu(container, inventory, syncId, category);
    });

    public static void registerMenus() {
        registerMenuType("album", ALBUM);
    }

    private static void registerMenuType(String id, ExtendedScreenHandlerType<?> extendedScreenHandlerType) {
        Registry.register(Registry.MENU, new ResourceLocation(CollectorsAlbum.MODID, id), extendedScreenHandlerType);
    }
}
