package team.tnt.collectoralbum.data.boosts;

import java.util.function.Predicate;

public enum OpType {

    CLEANUP,
    ACTIVE;

    public static Predicate<OpType> any() {
        return type -> true;
    }

    public static Predicate<OpType> specific(OpType targetType) {
        return type -> type == targetType;
    }
}
