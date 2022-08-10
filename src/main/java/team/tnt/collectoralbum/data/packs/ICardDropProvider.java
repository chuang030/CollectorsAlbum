package team.tnt.collectoralbum.data.packs;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ICardDropProvider {

    List<ItemStack> provideDrops();
}
