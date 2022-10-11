package team.tnt.collectoralbum.data.boosts;

public interface IAction extends IDescriptionProvider {

    void apply(IBoostContext context);
}
