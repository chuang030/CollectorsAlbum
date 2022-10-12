package team.tnt.collectoralbum.common.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public final class MenuTypes {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, CollectorsAlbum.MODID);

    public static final RegistryObject<MenuType<AlbumMenu>> ALBUM = REGISTRY.register("album", () -> IForgeMenuType.create((windowId, inv, data) -> {
        ItemStack stack = data.readItem();
        boolean flag = data.readBoolean();
        ICardCategory category = null;
        if (flag) {
            category = CardCategoryRegistry.getByKey(data.readResourceLocation());
        }
        AlbumContainer container = new AlbumContainer(stack);
        return new AlbumMenu(container, inv, windowId, category);
    }));
}
