package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.Arrays;

public class FirstValidAction implements IAction {

    private final Entry[] entries;
    private final Component[] description;

    private FirstValidAction(Entry[] entries) {
        this.entries = entries;
        this.description = this.generateDescription(entries);
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

    @Override
    public Component[] getDescription() {
        return description;
    }

    @Override
    public int compareTo(@NotNull IDescriptionProvider o) {
        if (o instanceof FirstValidAction) {
            FirstValidAction fva = (FirstValidAction) o;
            Entry[] vals = fva.entries;
            if (entries.length > 0 && vals.length > 0) {
                return compareEntries(entries[0], vals[0]);
            }
            return entries.length - vals.length;
        }
        return 0;
    }

    private Component[] generateDescription(Entry[] entries) {
        return Arrays.stream(entries)
                .sorted(this::compareEntries)
                .flatMap(e -> Arrays.stream(e.getDescription()))
                .toArray(Component[]::new);
    }

    private int compareEntries(Entry o1, Entry o2) {
        ICardBoostCondition[] c1 = o1.conditions;
        ICardBoostCondition[] c2 = o2.conditions;
        if (c1.length > 0 && c2.length > 0) {
            return c1[0].compareTo(c2[0]);
        }
        return o1.action.compareTo(o2.action);
    }

    public static final class Serializer implements IActionSerializer<FirstValidAction> {

        @Override
        public FirstValidAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray valuesArr = GsonHelper.getAsJsonArray(data, "values");
            Entry[] entries = JsonHelper.resolveArray(valuesArr, Entry[]::new, element -> Entry.fromJson(element, opType));
            return new FirstValidAction(entries);
        }
    }

    public static final class Entry implements IDescriptionProvider {

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

        @Override
        public Component[] getDescription() {
            Component[] conditionsDesc = Arrays.stream(conditions)
                    .flatMap(cond -> Arrays.stream(cond.getDescription()))
                    .toArray(Component[]::new);
            Component[] actionDesc = action.getDescription();
            Component[] res = new Component[conditionsDesc.length + actionDesc.length];
            System.arraycopy(conditionsDesc, 0, res, 0, conditionsDesc.length);
            System.arraycopy(actionDesc, 0, res, conditionsDesc.length, actionDesc.length);
            return res;
        }

        static Entry fromJson(JsonElement element, OpType opType) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            JsonObject applyAction = GsonHelper.getAsJsonObject(object, "apply");
            JsonArray conditionsArray = GsonHelper.getAsJsonArray(object, "conditions", new JsonArray());
            IAction action = ActionType.fromJson(opType, applyAction);
            ICardBoostCondition[] conditions = JsonHelper.resolveArray(conditionsArray, ICardBoostCondition[]::new, CardBoostConditionType::fromJson);
            return new Entry(conditions, action);
        }
    }
}
