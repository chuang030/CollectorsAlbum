package team.tnt.collectoralbum;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.init.SoundRegistry;
import team.tnt.collectoralbum.config.ModConfig;
import team.tnt.collectoralbum.data.packs.CardPackLootManager;
import team.tnt.collectoralbum.network.Networking;

public class CollectorsAlbum implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(CollectorsAlbum.class);
    public static final String MODID = "collectorsalbum";

    public static ModConfig config;

    public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
            new ResourceLocation(MODID, "tab"),
            () -> new ItemStack(ItemRegistry.ALBUM)
    );

    public static final CardPackLootManager CARD_PACK_MANAGER = new CardPackLootManager();

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        ItemRegistry.registerItems();
        SoundRegistry.registerSounds();
        MenuTypes.registerMenus();
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(CARD_PACK_MANAGER);
        Networking.registerServerReceivers();
    }
}
