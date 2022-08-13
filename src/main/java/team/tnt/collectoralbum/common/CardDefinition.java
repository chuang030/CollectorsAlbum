package team.tnt.collectoralbum.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

public record CardDefinition(ResourceLocation cardId, ICardCategory category, int cardNumber) {

    private static final Int2ObjectMap<CardDefinition> CARD_BY_ID = new Int2ObjectOpenHashMap<>();

    public CardDefinition {
        if (cardNumber > category.getCapacity()) {
            throw new IndexOutOfBoundsException("Category card index overflow! Got " + cardNumber + ", capacity: " + category.getCapacity() + " for card " + cardId);
        }
        int idValue = CardCategoryIndexPool.getIndexOffset(category) + cardNumber;
        if (CARD_BY_ID.put(idValue, this) != null) {
            throw new IllegalStateException("Duplicate card number " + cardNumber);
        }
    }

    public static CardDefinition getCardByNumericId(int id) {
        return CARD_BY_ID.get(id);
    }
}
