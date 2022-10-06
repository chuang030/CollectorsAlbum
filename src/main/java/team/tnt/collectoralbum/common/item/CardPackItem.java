package team.tnt.collectoralbum.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.SoundRegistry;
import team.tnt.collectoralbum.data.packs.ICardDropProvider;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.packet.OpenCardScreenPacket;
import team.tnt.collectoralbum.server.OpenCardPackContextHolder;

import java.util.List;
import java.util.Optional;

public class CardPackItem extends Item {

    private final ResourceLocation dropsProviderPath;

    public CardPackItem(ResourceLocation dropsProviderPath) {
        super(new Properties().tab(CollectorsAlbum.TAB));
        this.dropsProviderPath = dropsProviderPath;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            Optional<ICardDropProvider> optional = CollectorsAlbum.CARD_PACK_MANAGER.getProvider(dropsProviderPath);
            optional.ifPresent(provider -> {
                List<ItemStack> itemStacks = provider.provideDrops();
                OpenCardPackContextHolder.store(player, itemStacks);
                Networking.dispatchClientPacket(player, new OpenCardScreenPacket(itemStacks));
            });
            CooldownTracker cooldowns = player.getCooldowns();
            cooldowns.addCooldown(stack.getItem(), 10);
        } else {
            livingEntity.playSound(SoundRegistry.OPEN.get(), 0.8f, 1.0f);
        }
        return stack;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        CooldownTracker cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(stack.getItem())) {
            return ActionResult.pass(stack);
        }
        player.startUsingItem(usedHand);
        return ActionResult.consume(stack);
    }
}
