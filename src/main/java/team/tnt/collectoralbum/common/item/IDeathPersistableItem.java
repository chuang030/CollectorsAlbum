package team.tnt.collectoralbum.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IDeathPersistableItem {

    boolean shouldKeepItem(PlayerEntity player, ItemStack stack);
}
