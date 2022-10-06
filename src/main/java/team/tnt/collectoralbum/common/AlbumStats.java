package team.tnt.collectoralbum.common;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;
import team.tnt.collectoralbum.common.init.CardRegistry;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;

import java.util.*;

public class AlbumStats {

    private final int cardsCollected;
    private final int totalCards;
    private final Map<CardRarity, Integer> cardsByRarity = new EnumMap<>(CardRarity.class);
    private final Map<ICardCategory, List<ICard>> cardsByCategory;
    private final int points;

    public AlbumStats(AlbumContainer container) {
        this.cardsByCategory = new HashMap<>();
        int collected = 0;
        int pointCounter = 0;
        for (ICardCategory category : CardCategoryRegistry.getValues()) {
            Inventory categoryContainer = container.forCategory(category);
            for (int i = 0; i < categoryContainer.getContainerSize(); i++) {
                ItemStack stack = categoryContainer.getItem(i);
                if (stack.getItem() instanceof ICard) {
                    ICard card = (ICard) stack.getItem();
                    ++collected;
                    cardsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(card);
                    CardRarity rarity = card.getCardRarity();
                    increaseCounter(cardsByRarity, rarity);
                    pointCounter += rarity.getValue();
                }
            }
        }
        this.cardsCollected = collected;
        this.totalCards = CardRegistry.count();
        this.points = pointCounter;
    }

    private AlbumStats(int cards, int points, Map<ICardCategory, List<ICard>> byCategory) {
        this.cardsCollected = cards;
        this.totalCards = CardRegistry.count();
        this.points = points;
        this.cardsByCategory = byCategory;
    }

    public static AlbumStats createSimplifiedWithoutContainer(ItemStack album) {
        CompoundNBT tag = album.getOrCreateTag();
        CompoundNBT inventories = tag.getCompound("inventories");
        int cardCounter = 0;
        int pointCounter = 0;
        Map<ICardCategory, List<ICard>> byCategory = new HashMap<>();
        for (ICardCategory category : CardCategoryRegistry.getValues()) {
            ListNBT slots = inventories.getList(category.getId().toString(), Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < slots.size(); i++) {
                CompoundNBT slotDef = slots.getCompound(i);
                ItemStack item = ItemStack.of(slotDef.getCompound("itemStack"));
                if (item.getItem() instanceof ICard) {
                    ICard card = (ICard) item.getItem();
                    cardCounter++;
                    pointCounter += card.getCardRarity().getValue();
                    byCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(card);
                }
            }
        }
        return new AlbumStats(cardCounter, pointCounter, byCategory);
    }

    public int getCardsCollected() {
        return cardsCollected;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public Map<CardRarity, Integer> getCardsByRarity() {
        return cardsByRarity;
    }

    public Map<ICardCategory, List<ICard>> getCardsByCategory() {
        return cardsByCategory;
    }

    public int getPoints() {
        return points;
    }

    private <K> void increaseCounter(Map<K, Integer> map, K key) {
        int value = map.computeIfAbsent(key, k -> 0) + 1;
        map.put(key, value);
    }
}
