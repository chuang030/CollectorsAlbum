package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import team.tnt.collectoralbum.common.init.ActionTypeRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.Arrays;

public class FirstValidAction implements IAction {

    private final Entry[] entries;
    private final ITextComponent[] description;

    private FirstValidAction(Entry[] entries) {
        this.entries = entries;
        this.description = this.generateDescription(entries);
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypeRegistry.FIRST_VALID;
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
    public ITextComponent[] getDescription() {
        return description;
    }

    @Override
    public int compareTo(IDescriptionProvider o) {
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

    private ITextComponent[] generateDescription(Entry[] entries) {
        return Arrays.stream(entries)
                .sorted(this::compareEntries)
                .flatMap(e -> Arrays.stream(e.getDescription()))
                .toArray(ITextComponent[]::new);
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
            JsonArray valuesArr = JSONUtils.getAsJsonArray(data, "values");
            Entry[] entries = JsonHelper.resolveArray(valuesArr, Entry[]::new, element -> Entry.fromJson(element, opType));
            return new FirstValidAction(entries);
        }

        @Override
        public void networkEncode(FirstValidAction action, PacketBuffer buffer) {
            buffer.writeInt(action.entries.length);
            for (Entry entry : action.entries) {
                entry.encode(buffer);
            }
        }

        @Override
        public FirstValidAction networkDecode(ActionType<FirstValidAction> type, PacketBuffer buffer) {
            int i = buffer.readInt();
            Entry[] entries = new Entry[i];
            for (int j = 0; j < i; j++) {
                entries[j] = Entry.decode(buffer);
            }
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
        public ITextComponent[] getDescription() {
            ITextComponent[] conditionsDesc = Arrays.stream(conditions)
                    .flatMap(cond -> Arrays.stream(cond.getDescription()))
                    .toArray(ITextComponent[]::new);
            ITextComponent[] actionDesc = action.getDescription();
            ITextComponent[] res = new ITextComponent[conditionsDesc.length + actionDesc.length];
            System.arraycopy(conditionsDesc, 0, res, 0, conditionsDesc.length);
            System.arraycopy(actionDesc, 0, res, conditionsDesc.length, actionDesc.length);
            return res;
        }

        static Entry fromJson(JsonElement element, OpType opType) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            JsonObject applyAction = JSONUtils.getAsJsonObject(object, "apply");
            JsonArray conditionsArray = JSONUtils.getAsJsonArray(object, "conditions", new JsonArray());
            IAction action = ActionType.fromJson(opType, applyAction);
            ICardBoostCondition[] conditions = JsonHelper.resolveArray(conditionsArray, ICardBoostCondition[]::new, CardBoostConditionType::fromJson);
            return new Entry(conditions, action);
        }

        void encode(PacketBuffer buffer) {
            ActionType.encode(action, buffer);
            buffer.writeInt(conditions.length);
            for (ICardBoostCondition condition : conditions) {
                CardBoostConditionType.encode(condition, buffer);
            }
        }

        static Entry decode(PacketBuffer buffer) {
            IAction action = ActionType.decode(buffer);
            int count = buffer.readInt();
            ICardBoostCondition[] cardBoostConditions = new ICardBoostCondition[count];
            for (int i = 0; i < count; i++) {
                cardBoostConditions[i] = CardBoostConditionType.decode(buffer);
            }
            return new Entry(cardBoostConditions, action);
        }
    }
}
