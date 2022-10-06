package team.tnt.collectoralbum.common.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public final class MenuTypes {

    public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, CollectorsAlbum.MODID);

    public static final RegistryObject<ContainerType<AlbumMenu>> ALBUM = REGISTRY.register("album", () -> IForgeContainerType.create((windowId, inv, data) -> {
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
