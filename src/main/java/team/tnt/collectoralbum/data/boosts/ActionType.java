package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import team.tnt.collectoralbum.common.init.ActionTypeRegistry;

import java.util.function.Predicate;

public final class ActionType<A extends IAction> {

    private final ResourceLocation id;
    private final Predicate<OpType> allowedOps;
    private final IActionSerializer<A> serializer;

    public ActionType(ResourceLocation id, Predicate<OpType> allowedOps, IActionSerializer<A> serializer) {
        this.id = id;
        this.allowedOps = allowedOps;
        this.serializer = serializer;
    }

    public ResourceLocation getId() {
        return id;
    }

    public static <A extends IAction> A fromJson(OpType type, JsonObject data) throws JsonParseException {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(data, "type"));
        ActionType<A> actionType = ActionTypeRegistry.get(id);
        if (actionType == null)
            throw new JsonSyntaxException("Unknown action type: " + id);
        if (!actionType.allowedOps.test(type))
            throw new JsonSyntaxException(String.format("Unsupported op type %s for %s action", type, actionType.id));
        return actionType.serializer.fromJson(data, type);
    }

    @SuppressWarnings("unchecked")
    public static <A extends IAction> void encode(A action, FriendlyByteBuf buffer) {
        ActionType<A> actionType = (ActionType<A>) action.getType();
        IActionSerializer<A> serializer = actionType.serializer;
        buffer.writeResourceLocation(actionType.id);
        serializer.networkEncode(action, buffer);
    }

    public static <A extends IAction> A decode(FriendlyByteBuf buffer) {
        ResourceLocation location = buffer.readResourceLocation();
        ActionType<A> actionType = ActionTypeRegistry.get(location);
        if (actionType == null) {
            return null;
        }
        IActionSerializer<A> serializer = actionType.serializer;
        return serializer.networkDecode(actionType, buffer);
    }
}
