package team.tnt.collectoralbum.common.init;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.CardDefinition;
import team.tnt.collectoralbum.common.item.CardCategory;

import java.util.HashMap;
import java.util.Map;

public final class CardRegistry {

	private static final Map<ResourceLocation, CardDefinition> CARD_DEFINITION_MAP = new HashMap<>();

	// TODO card initialization

	public static void registerCards() {

	}

	private static CardDefinition createAndRegisterCard(String id, CardCategory category) {
		return createAndRegisterCard(new ResourceLocation(CollectorsAlbum.MODID, id), category);
	}

	public static CardDefinition createAndRegisterCard(ResourceLocation cardId, CardCategory category) {
		CardDefinition definition = new CardDefinition(cardId, category);
		registerCard(definition);
		return definition;
	}

	public static void registerCard(CardDefinition definition) {
		ResourceLocation cardId = definition.cardId();
		if (CARD_DEFINITION_MAP.put(cardId, definition) != null) {
			throw new IllegalStateException("Duplicate card entry found: " + cardId);
		}
	}
}
