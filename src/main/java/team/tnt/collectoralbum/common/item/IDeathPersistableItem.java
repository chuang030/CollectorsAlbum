package team.tnt.collectoralbum.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IDeathPersistableItem {

    boolean shouldKeepItem(Player player, ItemStack stack);
}
