package team.tnt.collectoralbum.data.boosts;

public interface IBoostContext {

    <T> T get(String paramName, Class<T> tClass);
}
