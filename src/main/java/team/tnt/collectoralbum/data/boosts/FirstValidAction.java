package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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
                break;
            }
        }
    }

    public static final class Serializer implements IActionSerializer<FirstValidAction> {

        @Override
        public FirstValidAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray valuesArr = GsonHelper.getAsJsonArray(data, "values");
            Entry[] entries = JsonHelper.resolveArray(valuesArr, Entry[]::new, element -> Entry.fromJson(element, opType));
            return new FirstValidAction(entries);
        }
    }

    public static final class Entry {

        private final IEntryCondition[] conditions;
        private final IAction action;

        public Entry(IEntryCondition[] conditions, IAction action) {
            this.conditions = conditions;
            this.action = action;
        }

        public boolean accepts(IBoostContext ctx) {
            for (IEntryCondition condition : conditions) {
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
            JsonObject applyAction = GsonHelper.getAsJsonObject(object, "apply");
            JsonArray conditionsArray = GsonHelper.getAsJsonArray(object, "conditions", new JsonArray());
            IAction action = ActionType.fromJson(opType, applyAction);
            // TODO conditions
            return new Entry(new IEntryCondition[0], action);
        }
    }

    @FunctionalInterface
    public interface IEntryCondition {

        boolean isValid(IBoostContext context);
    }

    public static final class EntryConditionType<C extends IEntryCondition> {

        private final ResourceLocation identifier;
        private final IEntryConditionSerializer<C> serializer;

        public EntryConditionType(ResourceLocation identifier, IEntryConditionSerializer<C> serializer) {
            this.identifier = identifier;
            this.serializer = serializer;
        }
    }

    @FunctionalInterface
    public interface IEntryConditionSerializer<C extends IEntryCondition> {

        C fromJson(JsonElement data) throws JsonParseException;
    }
}
