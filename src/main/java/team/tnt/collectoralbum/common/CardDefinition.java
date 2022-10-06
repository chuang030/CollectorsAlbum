package team.tnt.collectoralbum.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;

public final class CardDefinition {

    private static final Int2ObjectMap<CardDefinition> CARD_BY_ID = new Int2ObjectOpenHashMap<>();

    private final ResourceLocation cardId;
    private final ICardCategory category;
    private final int cardNumber;

    public CardDefinition(ResourceLocation cardId, ICardCategory category, int cardNumber) {
        this.cardId = cardId;
        this.category = category;
        this.cardNumber = cardNumber;
        if (cardNumber > category.getCapacity()) {
            throw new IndexOutOfBoundsException("Category card index overflow! Got " + cardNumber + ", capacity: " + category.getCapacity() + " for card " + cardId);
        }
        int idValue = CardCategoryIndexPool.getIndexOffset(category) + cardNumber;
        if (CARD_BY_ID.put(idValue, this) != null) {
            throw new IllegalStateException("Duplicate card number " + cardNumber);
        }
    }

    public ResourceLocation cardId() {
        return cardId;
    }

    public ICardCategory category() {
        return category;
    }

    public int cardNumber() {
        return cardNumber;
    }

    public static CardDefinition getCardByNumericId(int id) {
        return CARD_BY_ID.get(id);
    }
}
