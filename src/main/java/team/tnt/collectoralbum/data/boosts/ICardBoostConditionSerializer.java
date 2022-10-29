package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;

public interface ICardBoostConditionSerializer<C extends ICardBoostCondition> {

    C fromJson(JsonElement element) throws JsonParseException;

    void networkEncode(C condition, FriendlyByteBuf buffer);

    C networkDecode(CardBoostConditionType<C> type, FriendlyByteBuf buffer);
}
