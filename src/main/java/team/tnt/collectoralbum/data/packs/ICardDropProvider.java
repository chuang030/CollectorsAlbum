package team.tnt.collectoralbum.data.packs;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ICardDropProvider {

    void provideDrops(Player player, Level level);
}
