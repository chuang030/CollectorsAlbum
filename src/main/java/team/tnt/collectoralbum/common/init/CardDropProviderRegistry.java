package team.tnt.collectoralbum.common.init;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.data.packs.*;

import java.util.HashMap;
import java.util.Map;

public final class CardDropProviderRegistry {

    private static final Map<ResourceLocation, CardDropProviderType<?>> REGISTRY = new HashMap<>();

    public static final CardDropProviderType<ItemCardProvider> ITEM = register("item", new ItemCardProvider.Serializer());
    public static final CardDropProviderType<TagCardProvider> TAG = register("tag", new TagCardProvider.Serializer());
    public static final CardDropProviderType<MultiDropProvider> MULTI = register("multiple", new MultiDropProvider.Serializer());
    public static final CardDropProviderType<WeightedDropProvider> WEIGHTED = register("weighted", new WeightedDropProvider.Serializer());
    public static final CardDropProviderType<RepeatedDropProvider> REPEATED = register("repeat", new RepeatedDropProvider.Serializer());

    public static <P extends ICardDropProvider> CardDropProviderType<P> registerProviderType(CardDropProviderType<P> providerType) {
        REGISTRY.put(providerType.identifier(), providerType);
        return providerType;
    }

    @SuppressWarnings("unchecked")
    public static <P extends ICardDropProvider> CardDropProviderType<P> findInRegistry(ResourceLocation identifier) {
        return (CardDropProviderType<P>) REGISTRY.get(identifier);
    }

    private static <P extends ICardDropProvider> CardDropProviderType<P> register(String id, ICardDropSerializer<P> serializer) {
        return registerProviderType(new CardDropProviderType<>(new ResourceLocation(CollectorsAlbum.MODID, id), serializer));
    }
}
