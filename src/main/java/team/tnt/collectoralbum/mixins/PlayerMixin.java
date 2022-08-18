package team.tnt.collectoralbum.mixins;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(Player.class)
public class PlayerMixin {

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
    public void collectorsalbum$dropInventoryContents(Inventory inventory) {
        Player player = (Player) (Object) this;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!(stack.getItem() instanceof IDeathPersistableItem persistableItem) || !persistableItem.shouldKeepItem(player, stack)) {
                if (!stack.isEmpty()) {
                    player.drop(stack, true, false);
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }
}
