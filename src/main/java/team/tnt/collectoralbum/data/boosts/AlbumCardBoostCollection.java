package team.tnt.collectoralbum.data.boosts;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class AlbumCardBoostCollection {

    private final Map<OpType, IAction[]> byOps = new EnumMap<>(OpType.class);

    public AlbumCardBoostCollection(IAction[] cleanUpActions, IAction[] activeActions) {
        byOps.put(OpType.CLEANUP, cleanUpActions);
        byOps.put(OpType.ACTIVE, activeActions);
    }

    public void processOp(OpType type, IBoostContext ctx) {
        IAction[] actions = byOps.get(type);
        if (actions != null) {
            for (IAction action : actions) {
                action.apply(ctx);
            }
        }
    }

    public int getActionsCount(OpType type) {
        IAction[] actions = byOps.get(type);
        return actions != null ? actions.length : 0;
    }

    public ITextComponent[] getPagedDescription(int index) {
        IAction[] actions = byOps.get(OpType.ACTIVE);
        if (actions == null || actions.length == 0 || index >= actions.length) {
            return new ITextComponent[0];
        }
        List<IAction> sortedActions = Arrays.asList(actions);
        sortedActions.sort(IDescriptionProvider::compareTo);
        IAction action = sortedActions.get(index);
        return action.getDescription();
    }

    public void encode(PacketBuffer buffer) {
        IAction[] actions = byOps.get(OpType.ACTIVE);
        int length = actions != null ? actions.length : 0;
        buffer.writeInt(length);
        if (length > 0) {
            for (IAction action : actions) {
                ActionType.encode(action, buffer);
            }
        }
    }

    public void decode(PacketBuffer buffer) {
        int count = buffer.readInt();
        IAction[] actions = new IAction[count];
        for (int i = 0; i < count; i++) {
            actions[i] = ActionType.decode(buffer);
        }
        this.byOps.put(OpType.ACTIVE, actions);
    }
}
