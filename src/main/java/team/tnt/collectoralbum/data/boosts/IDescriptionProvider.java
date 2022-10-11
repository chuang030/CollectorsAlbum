package team.tnt.collectoralbum.data.boosts;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface IDescriptionProvider extends Comparable<IDescriptionProvider> {

    Component[] getDescription();

    @Override
    default int compareTo(@NotNull IDescriptionProvider o) {
        return 0;
    }
}
