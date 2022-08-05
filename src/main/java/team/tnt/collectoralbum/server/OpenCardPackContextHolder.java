package team.tnt.collectoralbum.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class OpenCardPackContextHolder {

    private static final Map<UUID, List<ItemStack>> PLAYER_DROPS_MAP = new HashMap<>();

    public static void store(ServerPlayer player, List<ItemStack> drops) {
        PLAYER_DROPS_MAP.put(player.getUUID(), drops);
    }

    public static Optional<List<ItemStack>> getContext(ServerPlayer player) {
        List<ItemStack> value = PLAYER_DROPS_MAP.remove(player.getUUID());
        return Optional.ofNullable(value);
    }
}
