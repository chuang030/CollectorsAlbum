package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import team.tnt.collectoralbum.util.JsonHelper;
import team.tnt.collectoralbum.util.math.WeightedRandom;

import java.util.List;

public class WeightedDropProvider implements ICardDropProvider {

    private final WeightedRandom<Entry> randomProvider;

    private WeightedDropProvider(Entry[] entries) {
        this.randomProvider = WeightedRandom.create(entries);
    }

    @Override
    public List<ItemStack> provideDrops() {
        Entry providerEntry = randomProvider.get();
        ICardDropProvider provider = providerEntry.provider();
        return provider.provideDrops();
    }

    private static class Entry implements WeightedRandom.IWeighted {

        private final int value;
        private final ICardDropProvider provider;

        public Entry(int value, ICardDropProvider provider) {
            this.value = value;
            this.provider = provider;
        }

        public ICardDropProvider provider() {
            return provider;
        }

        @Override
        public int getWeight() {
            return value;
        }

        static Entry fromJson(JsonElement element) throws JsonParseException {
            JsonObject data = JsonHelper.asObject(element);
            int weight = JSONUtils.getAsInt(data, "weight", 1);
            JsonObject provider = JSONUtils.getAsJsonObject(data, "provider");
            ICardDropProvider cardDropProvider = CardDropProviderType.fromJson(provider);
            return new Entry(weight, cardDropProvider);
        }
    }

    public static final class Serializer implements ICardDropSerializer<WeightedDropProvider> {

        @Override
        public WeightedDropProvider fromJson(JsonElement data) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(data);
            JsonArray array = JSONUtils.getAsJsonArray(object, "providers");
            Entry[] entries = JsonHelper.resolveArray(array, Entry[]::new, Entry::fromJson);
            return new WeightedDropProvider(entries);
        }
    }
}
