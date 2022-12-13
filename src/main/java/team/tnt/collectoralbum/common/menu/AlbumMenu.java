package team.tnt.collectoralbum.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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

public class AlbumMenu extends AbstractContainerMenu {

    private final AlbumContainer container;
    @Nullable
    private final ICardCategory category;

    public AlbumMenu(AlbumContainer container, Inventory playerInventory, int id) {
        this(container, playerInventory, id, null);
    }

    public AlbumMenu(AlbumContainer container, Inventory playerInventory, int id, @Nullable ICardCategory category) {
        super(MenuTypes.ALBUM.get(), id);
        this.container = container;
        this.category = category;

        SimpleContainer categoryContainer = container.forCategory(category);
        ICategorySlotDistributor slotDistributor = category != null ? category.getMenuSlotDistributor() : CardCategory.DISTRIBUTOR;
        ISlotAppender<Slot> playerSlotAppender = this::addSlot;
        slotDistributor.addPlayerSlots(playerSlotAppender, playerInventory);
        if (category == null) return;
        int offset = 1 + container.getCategoryIndexOffset(category);
        ISlotAppender<CardSlotDefinition> appender = definition -> addSlot(new CardSlot(categoryContainer, definition.slotIndex(), definition.slotX(), definition.slotY(), definition.cardNumber()));
        for (int i = 0; i < category.getCapacity(); i++) {
            slotDistributor.distributeSlot(appender, i, offset);
        }
        this.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
                categoryContainer.setChanged();
            }

            @Override
            public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
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
                                if (!(stack.getItem() instanceof ICard targetCard)) {
                                    return ItemStack.EMPTY;
                                }
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
    public boolean stillValid(Player player) {
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
        if (stack.getItem() instanceof ICard card) {
            return card.getCard().category().equals(this.category);
        }
        return false;
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
                int offset = CardCategoryIndexPool.getIndexOffset(card.getCard().category());
                return card.getCard().cardNumber() == (this.cardNumber - offset);
            }
            return false;
        }

        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}
