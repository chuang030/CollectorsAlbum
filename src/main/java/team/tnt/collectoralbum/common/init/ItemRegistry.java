package team.tnt.collectoralbum.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import team.tnt.collectoralbum.CollectorsAlbum;

public final class ItemRegistry {

	public static void registerItems() {

	}

	private static void registerItem(String localId, Item itemInstance) {
		Registry.register(Registry.ITEM, new ResourceLocation(CollectorsAlbum.MODID, localId), itemInstance);
	}
}
