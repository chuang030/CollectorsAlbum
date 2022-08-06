package team.tnt.collectoralbum.common.item;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class AlbumItem extends Item {

    public AlbumItem() {
        super(new Properties().tab(CollectorsAlbum.TAB).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider((i, inventory, player1) -> new AlbumMenu(i), TextComponent.EMPTY));
        }
        return InteractionResultHolder.pass(itemStack);
    }
}
