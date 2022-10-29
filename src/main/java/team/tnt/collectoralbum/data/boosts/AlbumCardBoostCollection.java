package team.tnt.collectoralbum.data.boosts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.*;

public final class AlbumCardBoostCollection {

    private final Map<OpType, IAction[]> byOps = new EnumMap<>(OpType.class);

    public AlbumCardBoostCollection(IAction[] cleanUpActions, IAction[] activeActions) {
        byOps.put(OpType.CLEANUP, cleanUpActions);
        byOps.put(OpType.ACTIVE, activeActions);
    }

    public void processOp(OpType type, IBoostContext ctx) {
        Optional.ofNullable(byOps.get(type))
                .stream()
                .flatMap(Arrays::stream)
                .forEach(action -> action.apply(ctx));
    }

    public int getActionsCount(OpType type) {
        IAction[] actions = byOps.get(type);
        return actions != null ? actions.length : 0;
    }

    public Component[] getPagedDescription(int index) {
        IAction[] actions = byOps.get(OpType.ACTIVE);
        if (actions == null || actions.length == 0 || index >= actions.length) {
            return new Component[0];
        }
        List<IAction> sortedActions = Arrays.asList(actions);
        sortedActions.sort(IDescriptionProvider::compareTo);
        IAction action = sortedActions.get(index);
        return action.getDescription();
    }

    public void encode(FriendlyByteBuf buffer) {
        IAction[] actions = byOps.get(OpType.ACTIVE);
        int length = actions != null ? actions.length : 0;
        buffer.writeInt(length);
        if (length > 0) {
            for (IAction action : actions) {
                ActionType.encode(action, buffer);
            }
        }
    }

    public void decode(FriendlyByteBuf buffer) {
        int count = buffer.readInt();
        IAction[] actions = new IAction[count];
        for (int i = 0; i < count; i++) {
            actions[i] = ActionType.decode(buffer);
        }
        this.byOps.put(OpType.ACTIVE, actions);
    }
}
