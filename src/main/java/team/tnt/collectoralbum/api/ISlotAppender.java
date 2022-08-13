package team.tnt.collectoralbum.api;

@FunctionalInterface
public interface ISlotAppender<T> {

    void appendSlot(T data);
}
