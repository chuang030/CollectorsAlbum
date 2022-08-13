package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface ICardBoostConditionSerializer<C extends ICardBoostCondition> {

    C fromJson(JsonElement element) throws JsonParseException;
}
