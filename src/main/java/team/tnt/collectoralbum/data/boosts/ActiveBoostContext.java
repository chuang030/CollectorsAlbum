package team.tnt.collectoralbum.data.boosts;

import net.minecraft.world.entity.player.Player;
import team.tnt.collectoralbum.common.AlbumStats;

public class ActiveBoostContext extends SimpleBoostContext {

    public static final String STATS = "stats";

    public ActiveBoostContext(Player player, AlbumStats stats) {
        super(player);
        set(STATS, stats);
    }
}
