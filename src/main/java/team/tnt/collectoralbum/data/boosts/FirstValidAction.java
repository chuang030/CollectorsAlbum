package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import team.tnt.collectoralbum.util.JsonHelper;

public class FirstValidAction implements IAction {

    private final Entry[] entries;

    private FirstValidAction(Entry[] entries) {
        this.entries = entries;
    }

    @Override
    public void apply(IBoostContext context) {
        for (Entry entry : entries) {
            if (entry.accepts(context)) {
                entry.apply(context);
                break;
            }
        }
    }

    public static final class Serializer implements IActionSerializer<FirstValidAction> {

        @Override
        public FirstValidAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray valuesArr = JSONUtils.getAsJsonArray(data, "values");
            Entry[] entries = JsonHelper.resolveArray(valuesArr, Entry[]::new, element -> Entry.fromJson(element, opType));
            return new FirstValidAction(entries);
        }
    }

    public static final class Entry {

        private final ICardBoostCondition[] conditions;
        private final IAction action;

        public Entry(ICardBoostCondition[] conditions, IAction action) {
            this.conditions = conditions;
            this.action = action;
        }

        public boolean accepts(IBoostContext ctx) {
            for (ICardBoostCondition condition : conditions) {
                if (!condition.isValid(ctx)) {
                    return false;
                }
            }
            return true;
        }

        public void apply(IBoostContext ctx) {
            action.apply(ctx);
        }

        static Entry fromJson(JsonElement element, OpType opType) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            JsonObject applyAction = JSONUtils.getAsJsonObject(object, "apply");
            JsonArray conditionsArray = JSONUtils.getAsJsonArray(object, "conditions", new JsonArray());
            IAction action = ActionType.fromJson(opType, applyAction);
            ICardBoostCondition[] conditions = JsonHelper.resolveArray(conditionsArray, ICardBoostCondition[]::new, CardBoostConditionType::fromJson);
            return new Entry(conditions, action);
        }
    }
}
