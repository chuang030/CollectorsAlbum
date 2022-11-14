package team.tnt.collectoralbum.common.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.client.CollectorsAlbumClient;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

import java.util.Arrays;
import java.util.List;

public class AlbumItem extends Item implements IDeathPersistableItem {

    private static final ITextComponent SHOW_BOOSTS = new TranslationTextComponent("text.collectorsalbum.album.boost.show").withStyle(TextFormatting.GRAY);
    private static final String PAGE_INFO_TRANSLATION_KEY = "text.collectorsalbum.album.boost.paging";

    public AlbumItem() {
        super(new Properties().tab(CollectorsAlbum.TAB).stacksTo(1));
    }

    @Override
    public boolean shouldKeepItem(PlayerEntity player, ItemStack stack) {
        return CollectorsAlbum.config.persistAlbumThroughDeath;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayer, new SimpleNamedContainerProvider((id, inv, owner) -> new AlbumMenu(new AlbumContainer(itemStack), inv, id), StringTextComponent.EMPTY), buffer -> {
                buffer.writeItem(itemStack);
                buffer.writeBoolean(false);
            });
        }
        return ActionResult.sidedSuccess(itemStack, level.isClientSide());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World level, List<ITextComponent> tooltipComponents, ITooltipFlag isAdvanced) {
        if (Screen.hasControlDown()) {
            CollectorsAlbumClient client = CollectorsAlbumClient.getClient();
            int pageIndex = client.getAlbumPageIndex();
            int pageSize = client.getPageCount();
            ITextComponent[] pagedText = CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoosts()
                    .map(coll -> coll.getPagedDescription(pageIndex))
                    .orElse(new ITextComponent[0]);
            if (pagedText == null) return;
            tooltipComponents.addAll(Arrays.asList(pagedText));
            tooltipComponents.add(new TranslationTextComponent(PAGE_INFO_TRANSLATION_KEY, pageIndex + 1, pageSize).withStyle(TextFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(SHOW_BOOSTS);
        }
    }
}
