package team.tnt.collectoralbum.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.*;

public class OpenCardPackContextHolder {

    private static final Map<UUID, List<ItemStack>> PLAYER_DROPS_MAP = new HashMap<>();

    public static void store(ServerPlayerEntity player, List<ItemStack> drops) {
        PLAYER_DROPS_MAP.put(player.getUUID(), drops);
    }

    public static Optional<List<ItemStack>> getContextAndClear(ServerPlayerEntity player) {
        List<ItemStack> value = PLAYER_DROPS_MAP.remove(player.getUUID());
        return Optional.ofNullable(value);
    }
}
