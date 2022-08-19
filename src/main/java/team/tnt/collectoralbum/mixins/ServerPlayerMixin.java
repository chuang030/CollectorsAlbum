package team.tnt.collectoralbum.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    public void collectorsalbum$restoreAlbumItem(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        ServerPlayer newPlayer = (ServerPlayer) (Object) this;
        GameRules rules = newPlayer.level.getGameRules();
        if (rules.getBoolean(GameRules.RULE_KEEPINVENTORY) || keepEverything || that.isSpectator()) {
            return;
        }
        Inventory oldInv = that.inventory;
        Inventory newInv = newPlayer.inventory;
        for (int i = 0; i < oldInv.getContainerSize(); i++) {
            ItemStack stack = oldInv.getItem(i);
            if (stack.getItem() instanceof IDeathPersistableItem && ((IDeathPersistableItem) stack.getItem()).shouldKeepItem(newPlayer, stack)) {
                newInv.setItem(i, stack);
            }
        }
    }
}
