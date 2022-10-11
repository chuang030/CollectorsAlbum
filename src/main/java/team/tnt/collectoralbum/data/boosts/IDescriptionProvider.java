package team.tnt.collectoralbum.data.boosts;

import net.minecraft.util.text.ITextComponent;

public interface IDescriptionProvider extends Comparable<IDescriptionProvider> {

    ITextComponent[] getDescription();

    @Override
    default int compareTo(IDescriptionProvider o) {
        return 0;
    }
}
