package team.tnt.collectoralbum.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.item.IDeathPersistableItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final public PlayerInventory inventory;

    public PlayerMixin(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }

    @Inject(
            method = "dropEquipment",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;destroyVanishingCursedItems()V", shift = At.Shift.AFTER),
            cancellable = true
    )
    public void collectorsalbum$dropInventoryContents(CallbackInfo ci) {
        if (CollectorsAlbum.config.persistAlbumThroughDeath) {
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
            ci.cancel();
        }
    }
}
