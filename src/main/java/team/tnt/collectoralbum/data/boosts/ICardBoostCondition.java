package team.tnt.collectoralbum.data.boosts;

public interface ICardBoostCondition {

    boolean isValid(IBoostContext context);
}
