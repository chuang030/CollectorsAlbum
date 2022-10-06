package team.tnt.collectoralbum.data.packs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardPackLootManager extends JsonReloadListener {

    private static final Logger LOGGER = LogManager.getLogger(CardPackLootManager.class);

    private static final Gson GSON = new GsonBuilder().create();
    private final Map<ResourceLocation, ICardDropProvider> providerMap = new HashMap<>();

    public CardPackLootManager() {
        super(GSON, "card_packs");
    }

    public Optional<ICardDropProvider> getProvider(ResourceLocation identifier) {
        return Optional.ofNullable(providerMap.get(identifier));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager resourceManager, IProfiler profiler) {
        LOGGER.info("Loading card pack drops");
        providerMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            try {
                ResourceLocation path = entry.getKey();
                JsonElement data = entry.getValue();
                ICardDropProvider provider = CardDropProviderType.fromJson(data);
                providerMap.put(path, provider);
            } catch (JsonParseException e) {
                LOGGER.error("Error loading card pack provider with id {}, error {}", entry.getKey(), e);
            }
        }
        LOGGER.info("Loaded {} card pack drops", providerMap.size());
    }
}
