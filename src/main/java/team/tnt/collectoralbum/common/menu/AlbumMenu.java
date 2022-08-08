package team.tnt.collectoralbum.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.item.CardCategory;
import team.tnt.collectoralbum.common.item.ICard;

public class AlbumMenu extends AbstractContainerMenu {

    private final AlbumContainer container;
    @Nullable
    private final CardCategory category;

    public AlbumMenu(AlbumContainer container, Inventory playerInventory, int id) {
        this(container, playerInventory, id, null);
    }

    public AlbumMenu(AlbumContainer container, Inventory playerInventory, int id, @Nullable CardCategory category) {
        super(MenuTypes.ALBUM, id);
        this.container = container;
        this.category = category;

        SimpleContainer categoryContainer = container.forCategory(category);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int slotId = x + (y * 9) + 9;
                addSlot(new Slot(playerInventory, slotId, 73 + x * 18, 176 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, 73 + x * 18, 234));
        }
        if (category == null) return;
        for (int i = 0; i < 30; i++) {
            int slotX = i < 15 ? 31 + (i % 3) * 41 : 170 + ((i - 15) % 3) * 41;
            int slotY = 21 + ((i % 15) / 3) * 29;
            int cardNumber = container.getCategoryIndexOffset(category) + i;
            addSlot(new CardSlot(categoryContainer, i, slotX, slotY, cardNumber + 1));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean isTitle() {
        return category == null;
    }

    public int getCategoryIndex() {
        return this.category.ordinal() + 1;
    }

    public AlbumContainer getContainer() {
        return this.container;
    }

    public static class CardSlot extends Slot {

        private final int cardNumber;

        public CardSlot(Container container, int slotIndex, int slotX, int slotY, int cardNumber) {
            super(container, slotIndex, slotX, slotY);
            this.cardNumber = cardNumber;
        }

        public int getCardNumber() {
            return cardNumber;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (stack.getItem() instanceof ICard card) {
                return card.getCard().cardNumber() == this.cardNumber;
            }
            return false;
        }
    }
}
