package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;

public interface ICardBoostConditionSerializer<C extends ICardBoostCondition> {

    C fromJson(JsonElement element) throws JsonParseException;

    void networkEncode(C condition, PacketBuffer buffer);

    C networkDecode(CardBoostConditionType<C> type, PacketBuffer buffer);
}
