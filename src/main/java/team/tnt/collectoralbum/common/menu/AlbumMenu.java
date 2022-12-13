package team.tnt.collectoralbum.common.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import team.tnt.collectoralbum.api.CardSlotDefinition;
import team.tnt.collectoralbum.api.ICategorySlotDistributor;
import team.tnt.collectoralbum.api.ISlotAppender;
import team.tnt.collectoralbum.common.CardCategory;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.MenuTypes;
import team.tnt.collectoralbum.common.item.CardRarity;
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
        this.addSlotListener(new IContainerListener() {

            @Override
            public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {

            }

            @Override
            public void slotChanged(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
                categoryContainer.setChanged();
            }

            @Override
            public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {

            }
        });
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();
            if (category != null) {
                int categorySize = category.getCapacity();
                // inventory to album
                if (index >= 0 && index < 36) {
                    if (this.isValidCard(slotItem)) {
                        ICard card = (ICard) slotItem.getItem();
                        int targetSlotIndex = card.getCard().cardNumber() + 35;
                        Slot targetSlot = this.slots.get(targetSlotIndex);
                        if (slot != null) {
                            ItemStack stack = targetSlot.getItem();
                            if (stack.isEmpty()) {
                                if (!this.moveItemStackTo(slotItem, targetSlotIndex, targetSlotIndex + 1, false)) {
                                    return ItemStack.EMPTY;
                                }
                            } else {
                                if (!(stack.getItem() instanceof ICard)) {
                                    return ItemStack.EMPTY;
                                }
                                ICard targetCard = (ICard) stack.getItem();
                                CardRarity usedRarity = targetCard.getCardRarity();
                                CardRarity newRarity = card.getCardRarity();
                                if (newRarity.ordinal() <= usedRarity.ordinal()) {
                                    return ItemStack.EMPTY;
                                }
                                ItemStack temp = stack.copy();
                                targetSlot.set(itemStack);
                                slot.set(temp);
                            }
                        }
                    }
                } else if (index >= 36 && index < 36 + categorySize) {
                    if (!this.moveItemStackTo(slotItem, 0, 36, true)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (index >= 0 && index < 27) {
                    if (this.moveItemStackTo(slotItem, 27, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 && index < 36) {
                    if (this.moveItemStackTo(slotItem, 0, 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

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

    private boolean isValidCard(ItemStack stack) {
        if (stack.getItem() instanceof ICard) {
            ICard card = (ICard) stack.getItem();
            return card.getCard().category().equals(this.category);
        }
        return false;
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
