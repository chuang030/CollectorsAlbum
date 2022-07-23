package team.tnt.collectoralbum;

import net.fabricmc.api.ModInitializer;
import team.tnt.collectoralbum.common.init.ItemRegistry;

public class CollectorsAlbum implements ModInitializer {

	public static final String MODID = "collectorsalbum";

	@Override
	public void onInitialize() {
		ItemRegistry.registerItems();
	}
}
