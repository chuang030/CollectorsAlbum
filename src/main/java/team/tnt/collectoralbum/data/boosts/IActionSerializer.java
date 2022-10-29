package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;

public interface IActionSerializer<A extends IAction> {

    A fromJson(JsonObject data, OpType opType) throws JsonParseException;

    void networkEncode(A action, FriendlyByteBuf buffer);

    A networkDecode(ActionType<A> type, FriendlyByteBuf buffer);
}
