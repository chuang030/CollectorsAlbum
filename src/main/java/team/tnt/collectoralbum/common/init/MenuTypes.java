package team.tnt.collectoralbum.common.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class MenuTypes {

    public static final ExtendedScreenHandlerType<AlbumMenu> ALBUM = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        ItemStack stack = buf.readItem();
        boolean flag = buf.readBoolean();
        ICardCategory category = null;
        if (flag) {
            category = CardCategoryRegistry.getByKey(buf.readResourceLocation());
        }
        AlbumContainer container = new AlbumContainer(stack);
        return new AlbumMenu(container, inventory, syncId, category);
    });

    public static void registerMenus() {
        registerMenuType("album", ALBUM);
    }

    private static void registerMenuType(String id, ExtendedScreenHandlerType<?> extendedScreenHandlerType) {
        Registry.register(Registry.MENU, new ResourceLocation(CollectorsAlbum.MODID, id), extendedScreenHandlerType);
    }
}
