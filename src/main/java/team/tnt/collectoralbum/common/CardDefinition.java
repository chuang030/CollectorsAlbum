package team.tnt.collectoralbum.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

public record CardDefinition(ResourceLocation cardId, ICardCategory category, int cardNumber) {

    private static final Int2ObjectMap<CardDefinition> CARD_BY_ID = new Int2ObjectOpenHashMap<>();

    public CardDefinition {
        if (CARD_BY_ID.put(cardNumber, this) != null) {
            throw new IllegalStateException("Duplicate card number " + cardNumber);
        }
    }

    public static CardDefinition getCardByNumericId(int id) {
        return CARD_BY_ID.get(id);
    }
}
