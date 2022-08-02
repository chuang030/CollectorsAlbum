package team.tnt.collectoralbum.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class PlayerHelper {

    public static void giveItem(Player player, ItemStack itemStack) {
        if (!player.addItem(itemStack)) {
            ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), itemStack.copy());
            entity.setNoPickUpDelay();
            player.level.addFreshEntity(entity);
        }
    }

    private PlayerHelper() {}
}
