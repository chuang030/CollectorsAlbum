package team.tnt.collectoralbum.mixins;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    public void collectorsalbum$restoreAlbumItem(ServerPlayerEntity that, boolean keepEverything, CallbackInfo ci) {
        ServerPlayerEntity newPlayer = (ServerPlayerEntity) (Object) this;
        GameRules rules = newPlayer.level.getGameRules();
        if (rules.getBoolean(GameRules.RULE_KEEPINVENTORY) || keepEverything || that.isSpectator()) {
            return;
        }
        PlayerInventory oldInv = that.inventory;
        PlayerInventory newInv = newPlayer.inventory;
        for (int i = 0; i < oldInv.getContainerSize(); i++) {
            ItemStack stack = oldInv.getItem(i);
            if (stack.getItem() instanceof IDeathPersistableItem && ((IDeathPersistableItem) stack.getItem()).shouldKeepItem(newPlayer, stack)) {
                newInv.setItem(i, stack);
            }
        }
    }
}
