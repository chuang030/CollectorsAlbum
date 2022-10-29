package team.tnt.collectoralbum.data.boosts;

public interface IAction extends IDescriptionProvider {

    ActionType<?> getType();

    void apply(IBoostContext context);
}
