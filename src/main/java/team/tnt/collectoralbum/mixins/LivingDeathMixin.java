package team.tnt.collectoralbum.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.common.MobDrops;

@Mixin(LivingEntity.class)
public abstract class LivingDeathMixin {

    @Inject(method = "die", at = @At("TAIL"))
    private void collectorsalbum$onDeathInject(DamageSource cause, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.level.isClientSide && livingEntity instanceof MonsterEntity) {
            MonsterEntity monster = (MonsterEntity) livingEntity;
            MobDrops drops = MobDrops.instance();
            Item item = drops.get();
            if (item == Items.AIR) {
                return;
            }
            ItemStack stack = new ItemStack(item);
            ItemEntity itemEntity = new ItemEntity(monster.level, monster.getX(), monster.getY(), monster.getZ(), stack);
            itemEntity.setDefaultPickUpDelay();
            monster.level.addFreshEntity(itemEntity);
        }
    }
}
