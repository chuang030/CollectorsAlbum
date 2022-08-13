package team.tnt.collectoralbum.data.boosts;

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
}
