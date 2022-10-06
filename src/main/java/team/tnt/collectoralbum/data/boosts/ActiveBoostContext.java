package team.tnt.collectoralbum.data.boosts;

import net.minecraft.entity.player.PlayerEntity;
import team.tnt.collectoralbum.common.AlbumStats;

public class ActiveBoostContext extends SimpleBoostContext {

    public static final String STATS = "stats";

    public ActiveBoostContext(PlayerEntity player, AlbumStats stats) {
        super(player);
        set(STATS, stats);
    }
}
