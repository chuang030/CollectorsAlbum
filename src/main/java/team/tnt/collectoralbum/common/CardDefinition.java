package team.tnt.collectoralbum.common;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.common.item.CardCategory;

public record CardDefinition(ResourceLocation cardId, CardCategory category) {
}
