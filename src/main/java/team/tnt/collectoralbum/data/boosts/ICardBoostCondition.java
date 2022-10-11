package team.tnt.collectoralbum.data.boosts;

public interface ICardBoostCondition extends IDescriptionProvider {

    boolean isValid(IBoostContext context);
}
