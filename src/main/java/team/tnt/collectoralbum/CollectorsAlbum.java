package team.tnt.collectoralbum;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.data.packs.CardPackLootManager;

public class CollectorsAlbum implements ModInitializer {

    public static final String MODID = "collectorsalbum";

    public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
            new ResourceLocation(MODID, "tab"),
            () -> new ItemStack(ItemRegistry.ALBUM)
    );

    public static final CardPackLootManager CARD_PACK_MANAGER = new CardPackLootManager();

    @Override
    public void onInitialize() {
        ItemRegistry.registerItems();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(CARD_PACK_MANAGER);
    }
}
