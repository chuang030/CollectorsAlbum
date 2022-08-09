package team.tnt.collectoralbum.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.function.Function;

public final class JsonHelper {

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

    private JsonHelper() {
    }
}
