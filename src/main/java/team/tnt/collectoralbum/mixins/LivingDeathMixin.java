package team.tnt.collectoralbum.mixins;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.common.MobDrops;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingDeathMixin {

    @Inject(method = "die", at = @At("TAIL"))
    private void collectorsalbum$onDeathInject(DamageSource cause, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.level.isClientSide && livingEntity instanceof Monster monster) {
            MobDrops drops = MobDrops.instance();
            Item item = drops.get();

            // drop testing start
            int runs = 1000000;
            Map<Item, Integer> countCache = new IdentityHashMap<>();
            for (int i = 0; i < runs; i++) {
                Item it = drops.get();
                int value = countCache.computeIfAbsent(it, k -> 0);
                countCache.put(it, value + 1);
            }
            System.out.println("Total: " + runs);
            countCache.entrySet().stream()
                    .sorted(Comparator.<Map.Entry<Item, Integer>>comparingInt(Map.Entry::getValue).reversed())
                    .forEach((e) -> {
                        ResourceLocation id = Registry.ITEM.getKey(e.getKey());
                        System.out.printf("%s: %d\n", id, e.getValue());
                    });
            // drop testing end

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
