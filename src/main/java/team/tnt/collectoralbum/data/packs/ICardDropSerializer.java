package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface ICardDropSerializer<P extends ICardDropProvider> {

    P fromJson(JsonElement data) throws JsonParseException;
}
