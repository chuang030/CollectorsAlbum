package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import team.tnt.collectoralbum.common.init.CardDropProviderRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

public class CardDropProviderType<P extends ICardDropProvider> {

    private final ResourceLocation identifier;
    private final ICardDropSerializer<P> serializer;

    public CardDropProviderType(ResourceLocation identifier, ICardDropSerializer<P> serializer) {
        this.identifier = identifier;
        this.serializer = serializer;
    }

    public ResourceLocation identifier() {
        return identifier;
    }

    public ICardDropSerializer<P> serializer() {
        return serializer;
    }

    public static <P extends ICardDropProvider> P fromJson(JsonElement entry) throws JsonParseException {
        JsonObject object = JsonHelper.asObject(entry);
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "type"));
        CardDropProviderType<P> type = CardDropProviderRegistry.findInRegistry(id);
        if (type == null) throw new JsonSyntaxException("Unknown card provider: " + id);
        ICardDropSerializer<P> serializer = type.serializer();
        return serializer.fromJson(object);
    }
}
