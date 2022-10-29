package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;

public interface IActionSerializer<A extends IAction> {

    A fromJson(JsonObject data, OpType opType) throws JsonParseException;

    void networkEncode(A action, PacketBuffer buffer);

    A networkDecode(ActionType<A> type, PacketBuffer buffer);
}
