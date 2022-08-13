package team.tnt.collectoralbum.common.init;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.data.boosts.*;

import java.util.HashMap;
import java.util.Map;

public final class CardBoostConditionRegistry {

    private static final Map<ResourceLocation, CardBoostConditionType<?>> REGISTRY = new HashMap<>();

    public static final CardBoostConditionType<CardsCondition> CARDS = internalRegister("cards", new CardsCondition.Serializer());
    public static final CardBoostConditionType<PointsCondition> POINTS = internalRegister("points", new PointsCondition.Serializer());

    public static void register(CardBoostConditionType<?> conditionType) {
        REGISTRY.put(conditionType.getId(), conditionType);
    }

    @SuppressWarnings("unchecked")
    public static <C extends ICardBoostCondition> CardBoostConditionType<C> lookup(ResourceLocation id) {
        return (CardBoostConditionType<C>) REGISTRY.get(id);
    }

    private static <C extends ICardBoostCondition> CardBoostConditionType<C> internalRegister(String id, ICardBoostConditionSerializer<C> serializer) {
        CardBoostConditionType<C> type = new CardBoostConditionType<>(new ResourceLocation(CollectorsAlbum.MODID, id), serializer);
        register(type);
        return type;
    }
}
