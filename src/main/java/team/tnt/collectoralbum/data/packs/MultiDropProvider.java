package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.tnt.collectoralbum.util.JsonHelper;

public class MultiDropProvider implements ICardDropProvider {

    private final ICardDropProvider[] nestedProviders;

    private MultiDropProvider(ICardDropProvider[] nestedProviders) {
        this.nestedProviders = nestedProviders;
    }

    @Override
    public void provideDrops(Player player, Level level) {
        for (ICardDropProvider provider : nestedProviders) {
            provider.provideDrops(player, level);
        }
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
