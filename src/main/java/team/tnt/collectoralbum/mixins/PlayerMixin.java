package team.tnt.collectoralbum.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final private Inventory inventory;

    public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "dropEquipment",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;destroyVanishingCursedItems()V", shift = At.Shift.AFTER),
            cancellable = true
    )
    public void collectorsalbum$dropInventoryContents(CallbackInfo ci) {
        if (CollectorsAlbum.config.persistAlbumThroughDeath) {
            Player player = (Player) (Object) this;
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!(stack.getItem() instanceof IDeathPersistableItem persistableItem) || !persistableItem.shouldKeepItem(player, stack)) {
                    player.drop(stack, true, false);
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
            ci.cancel();
        }
    }
}
