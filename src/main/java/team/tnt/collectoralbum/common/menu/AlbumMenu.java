package team.tnt.collectoralbum.common.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import team.tnt.collectoralbum.api.CardSlotDefinition;
import team.tnt.collectoralbum.api.ICategorySlotDistributor;
import team.tnt.collectoralbum.api.ISlotAppender;
import team.tnt.collectoralbum.common.CardCategory;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.item.ICard;

public class AlbumMenu extends Container {

    private final AlbumContainer container;
    private final ICardCategory category;

    public AlbumMenu(AlbumContainer container, PlayerInventory playerInventory, int id) {
        this(container, playerInventory, id, null);
    }

    public AlbumMenu(AlbumContainer container, PlayerInventory playerInventory, int id, ICardCategory category) {
        super(MenuTypes.ALBUM.get(), id);
        this.container = container;
        this.category = category;

        Inventory categoryContainer = container.forCategory(category);
        ICategorySlotDistributor slotDistributor = category != null ? category.getMenuSlotDistributor() : CardCategory.DISTRIBUTOR;
        ISlotAppender<Slot> playerSlotAppender = this::addSlot;
        slotDistributor.addPlayerSlots(playerSlotAppender, playerInventory);
        if (category == null) return;
        int offset = 1 + container.getCategoryIndexOffset(category);
        ISlotAppender<CardSlotDefinition> appender = definition -> addSlot(new CardSlot(categoryContainer, definition.slotIndex(), definition.slotX(), definition.slotY(), definition.cardNumber()));
        for (int i = 0; i < category.getCapacity(); i++) {
            slotDistributor.distributeSlot(appender, i, offset);
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public boolean isTitle() {
        return category == null;
    }

    public int getCategoryIndex() {
        return this.category.getIndex() + 1;
    }

    public AlbumContainer getContainer() {
        return this.container;
    }

    public ICardCategory getCategory() {
        return category;
    }

    public static class CardSlot extends Slot {

        private final int cardNumber;

        public CardSlot(IInventory container, int slotIndex, int slotX, int slotY, int cardNumber) {
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
            if (stack.getItem() instanceof ICard) {
                ICard card = (ICard) stack.getItem();
                int offset = CardCategoryIndexPool.getIndexOffset(card.getCard().category());
                return card.getCard().cardNumber() == (this.cardNumber - offset);
            }
            return false;
        }
    }
}
