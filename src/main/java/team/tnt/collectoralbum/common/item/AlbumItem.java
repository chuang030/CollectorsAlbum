package team.tnt.collectoralbum.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.menu.AlbumMenu;
import team.tnt.collectoralbum.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class AlbumItem extends Item implements IDeathPersistableItem {

    private static final Component SHOW_BOOSTS = new TranslatableComponent("text.collectorsalbum.album.boost.show").withStyle(ChatFormatting.GRAY);

    public AlbumItem() {
        super(new Properties().tab(CollectorsAlbum.TAB).stacksTo(1));
    }

    @Override
    public boolean shouldKeepItem(Player player, ItemStack stack) {
        return ModConfig.INSTANCE.persistAlbumThroughDeath.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            NetworkHooks.openGui(serverPlayer, new SimpleMenuProvider((id, inv, owner) -> new AlbumMenu(new AlbumContainer(itemStack), inv, id), TextComponent.EMPTY), buffer -> {
                buffer.writeItem(itemStack);
                buffer.writeInt(0);
            });
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (Screen.hasControlDown()) {
            Component[] text = CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoostsDescription();
            if (text == null) return;
            tooltipComponents.addAll(Arrays.asList(text));
        } else {
            tooltipComponents.add(SHOW_BOOSTS);
        }
    }
}
