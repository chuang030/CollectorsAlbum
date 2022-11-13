package team.tnt.collectoralbum;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.common.AlbumBoostHandler;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.init.SoundRegistry;
import team.tnt.collectoralbum.config.ModConfig;
import team.tnt.collectoralbum.data.boosts.AlbumCardBoostManager;
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
    public static final AlbumCardBoostManager ALBUM_CARD_BOOST_MANAGER = new AlbumCardBoostManager();

    // event handlers
    private final AlbumBoostHandler boostHandler = new AlbumBoostHandler();

    @Override
    public void onInitialize() {
        // config
        config = Configuration.registerConfig(ModConfig.class, ConfigFormats.yaml()).getConfigInstance();
        // registries
        ItemRegistry.registerItems();
        SoundRegistry.registerSounds();
        MenuTypes.registerMenus();
        // datapacks
        ResourceManagerHelper resourceManagerHelper = ResourceManagerHelper.get(PackType.SERVER_DATA);
        resourceManagerHelper.registerReloadListener(CARD_PACK_MANAGER);
        resourceManagerHelper.registerReloadListener(ALBUM_CARD_BOOST_MANAGER);
        // network
        Networking.registerServerReceivers();
        // callbacks
        ServerTickEvents.END_SERVER_TICK.register(boostHandler::onServerTick);
    }
}
