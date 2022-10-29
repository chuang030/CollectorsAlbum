package team.tnt.collectoralbum.data.boosts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

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

    public Component[] getDescription() {
        return Optional.ofNullable(byOps.get(OpType.ACTIVE))
                .stream()
                .flatMap(Arrays::stream)
                .sorted(IDescriptionProvider::compareTo)
                .map(IDescriptionProvider::getDescription)
                .filter(components -> components.length > 0)
                .flatMap(Arrays::stream)
                .toArray(Component[]::new);
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
