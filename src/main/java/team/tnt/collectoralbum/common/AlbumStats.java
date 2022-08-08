package team.tnt.collectoralbum.common;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.CardRegistry;
import team.tnt.collectoralbum.common.item.CardCategory;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;

import java.util.EnumMap;
import java.util.Map;

public class AlbumStats {

    private final int cardsCollected;
    private final int totalCards;
    private final Map<CardRarity, Integer> cardsByRarity = new EnumMap<>(CardRarity.class);
    private final Map<CardCategory, Integer> cardsByCategory = new EnumMap<>(CardCategory.class);
    private final int points;

    public AlbumStats(AlbumContainer container) {
        int collected = 0;
        int pointCounter = 0;
        for (CardCategory category : CardCategory.values()) {
            SimpleContainer categoryContainer = container.forCategory(category);
            for (int i = 0; i < categoryContainer.getContainerSize(); i++) {
                ItemStack stack = categoryContainer.getItem(i);
                if (stack.getItem() instanceof ICard card) {
                    ++collected;
                    increaseCounter(cardsByCategory, category);
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

    public int getCardsCollected() {
        return cardsCollected;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public Map<CardRarity, Integer> getCardsByRarity() {
        return cardsByRarity;
    }

    public Map<CardCategory, Integer> getCardsByCategory() {
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
