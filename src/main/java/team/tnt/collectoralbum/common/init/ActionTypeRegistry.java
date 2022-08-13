package team.tnt.collectoralbum.common.init;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.data.boosts.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class ActionTypeRegistry {

    private static final Map<ResourceLocation, ActionType<?>> REGISTRY = new HashMap<>();

    public static final ActionType<ClearEffectsAction> CLEAR_EFFECTS = internalRegister("clear_effects", OpType.any(), new ClearEffectsAction.Serializer());
    public static final ActionType<GiveEffectsAction> GIVE_EFFECTS = internalRegister("give_effects", OpType.specific(OpType.ACTIVE), new GiveEffectsAction.Serializer());
    public static final ActionType<FirstValidAction> FIRST_VALID = internalRegister("first_valid", OpType.any(), new FirstValidAction.Serializer());

    public static void register(ActionType<?> type) {
        REGISTRY.put(type.getId(), type);
    }

    @SuppressWarnings("unchecked")
    public static <A extends IAction> ActionType<A> get(ResourceLocation id) {
        return (ActionType<A>) REGISTRY.get(id);
    }

    private static <A extends IAction> ActionType<A> internalRegister(String id, Predicate<OpType> filter, IActionSerializer<A> serializer) {
        ActionType<A> type = new ActionType<>(new ResourceLocation(CollectorsAlbum.MODID, id), filter, serializer);
        register(type);
        return type;
    }
}
