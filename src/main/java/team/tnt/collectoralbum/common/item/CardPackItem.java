package team.tnt.collectoralbum.common.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.data.packs.ICardDropProvider;

import java.util.Optional;

public class CardPackItem extends Item {

    private final ResourceLocation dropsProviderPath;

    public CardPackItem(ResourceLocation dropsProviderPath) {
        super(new Properties().tab(CollectorsAlbum.TAB));
        this.dropsProviderPath = dropsProviderPath;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            Optional<ICardDropProvider> optional = CollectorsAlbum.CARD_PACK_MANAGER.getProvider(dropsProviderPath);
            optional.ifPresent(provider -> provider.provideDrops(player, level));
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.consume(stack);
    }
}
