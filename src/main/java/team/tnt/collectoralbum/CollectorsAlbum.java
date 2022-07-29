package team.tnt.collectoralbum;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.init.ItemRegistry;

public class CollectorsAlbum implements ModInitializer {

	public static final String MODID = "collectorsalbum";

	public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
			new ResourceLocation(MODID, "tab"),
			() -> new ItemStack(ItemRegistry.ALBUM)
	);

	@Override
	public void onInitialize() {
		ItemRegistry.registerItems();
	}
}
