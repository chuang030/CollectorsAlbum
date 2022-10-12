package team.tnt.collectoralbum.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.data.boosts.*;

import java.util.Optional;

public class AlbumBoostHandler {

    public void onServerTick(MinecraftServer server) {
        PlayerList playerList = server.getPlayerList();
        for (ServerPlayer player : playerList.getPlayers()) {
            tickPlayer(player);
        }
    }

    private void tickPlayer(ServerPlayer player) {
        if (player.level.getGameTime() % 50L != 0L) return;
        AbstractContainerMenu inventory = player.containerMenu;
        Optional<AlbumCardBoostCollection> data = CollectorsAlbum.ALBUM_CARD_BOOST_MANAGER.getBoosts();
        data.ifPresent(ops -> {
            boolean hasAlbum = false;
            for (ItemStack stack : inventory.getItems()) {
                if (stack.getItem() == ItemRegistry.ALBUM.get()) {
                    applyBoosts(stack, player, ops);
                    hasAlbum = true;
                    break;
                }
            }
            if (!hasAlbum) {
                IBoostContext callCtx = new SimpleBoostContext(player);
                ops.processOp(OpType.CLEANUP, callCtx);
            }
        });
    }

    private void applyBoosts(ItemStack album, ServerPlayer player, AlbumCardBoostCollection ops) {
        AlbumStats stats = AlbumStats.createSimplifiedWithoutContainer(album);
        IBoostContext ctx = new ActiveBoostContext(player, stats);
        ops.processOp(OpType.ACTIVE, ctx);
    }
}
