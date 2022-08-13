package team.tnt.collectoralbum.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public final class JsonHelper {

    public static <T> T resolveNullable(JsonObject data, String key, Function<JsonElement, T> typeResolver) {
        if (!data.has(key)) {
            return null;
        }
        JsonElement res = data.get(key);
        if (res.isJsonNull()) {
            return null;
        }
        return typeResolver.apply(res);
    }

    public static JsonObject asObject(JsonElement in) {
        if (in.isJsonObject()) {
            return in.getAsJsonObject();
        }
        throw new JsonSyntaxException(in.getClass().getSimpleName() + " is not a JsonObject!");
    }

    public static <R> R[] resolveArray(JsonArray in, Function<Integer, R[]> arrayFactory, Function<JsonElement, R> resolver) {
        R[] result = arrayFactory.apply(in.size());
        int i = 0;
        for (JsonElement element : in) {
            R r = resolver.apply(element);
            result[i++] = r;
        }
        return result;
    }

    public static <R> R[] resolveRegistryObjectsFromIdArray(JsonArray array, Registry<R> registry, Function<Integer, R[]> arrayFactory) {
        return resolveRegistryObjectsFromIdArray(array, registry, arrayFactory, null);
    }

    public static <R> R[] resolveRegistryObjectsFromIdArray(JsonArray array, Registry<R> registry, Function<Integer, R[]> arrayFactory, R nullType) {
        R[] result = arrayFactory.apply(array.size());
        int i = 0;
        for (JsonElement element : array) {
            ResourceLocation id = new ResourceLocation(element.getAsString());
            R r = registry.get(id);
            if (r == nullType) {
                throw new JsonSyntaxException("Unknown id: " + id);
            }
            result[i++] = r;
        }
        return result;
    }

    private JsonHelper() {
    }
}
