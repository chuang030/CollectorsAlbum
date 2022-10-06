package team.tnt.collectoralbum.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    public void collectorsalbum$dropInventoryContents(PlayerInventory inventory) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!(stack.getItem() instanceof IDeathPersistableItem) || !((IDeathPersistableItem) stack.getItem()).shouldKeepItem(player, stack)) {
                if (!stack.isEmpty()) {
                    player.drop(stack, true, false);
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }
}
