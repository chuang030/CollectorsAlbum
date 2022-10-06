package team.tnt.collectoralbum.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class PlayerHelper {

    public static void giveItem(PlayerEntity player, ItemStack itemStack) {
        if (!player.addItem(itemStack)) {
            ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), itemStack.copy());
            entity.setNoPickUpDelay();
            player.level.addFreshEntity(entity);
        }
    }

    private PlayerHelper() {
    }
}
