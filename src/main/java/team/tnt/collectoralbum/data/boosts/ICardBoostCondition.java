package team.tnt.collectoralbum.data.boosts;

public interface ICardBoostCondition extends IDescriptionProvider {

    CardBoostConditionType<?> getType();

    boolean isValid(IBoostContext context);
}
