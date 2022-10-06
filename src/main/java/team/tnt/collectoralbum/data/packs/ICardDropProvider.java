package team.tnt.collectoralbum.data.packs;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ICardDropProvider {

    List<ItemStack> provideDrops();
}
