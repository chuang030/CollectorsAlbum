package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface IActionSerializer<A extends IAction> {

    A fromJson(JsonObject data, OpType opType) throws JsonParseException;
}
