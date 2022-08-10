package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class MultiDropProvider implements ICardDropProvider {

    private final ICardDropProvider[] nestedProviders;

    private MultiDropProvider(ICardDropProvider[] nestedProviders) {
        this.nestedProviders = nestedProviders;
    }

    @Override
    public List<ItemStack> provideDrops() {
        List<ItemStack> list = new ArrayList<>();
        for (ICardDropProvider provider : nestedProviders) {
            list.addAll(provider.provideDrops());
        }
        return list;
    }

    public static final class Serializer implements ICardDropSerializer<MultiDropProvider> {

        @Override
        public MultiDropProvider fromJson(JsonElement data) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(data);
            JsonArray array = GsonHelper.getAsJsonArray(object, "providers");
            ICardDropProvider[] providers = JsonHelper.resolveArray(array, ICardDropProvider[]::new, CardDropProviderType::fromJson);
            return new MultiDropProvider(providers);
        }
    }
}
