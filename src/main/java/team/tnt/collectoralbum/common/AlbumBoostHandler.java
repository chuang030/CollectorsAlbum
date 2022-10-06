package team.tnt.collectoralbum.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.data.boosts.*;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CollectorsAlbum.MODID)
public class AlbumBoostHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity entity = event.player;
        if (event.phase == TickEvent.Phase.START || entity.level.isClientSide)
            return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
        tickPlayer(serverPlayer);
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        if (player.level.getGameTime() % 50L != 0L) return;
        Container inventory = player.containerMenu;
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

    private static void applyBoosts(ItemStack album, ServerPlayerEntity player, AlbumCardBoostCollection ops) {
        AlbumStats stats = AlbumStats.createSimplifiedWithoutContainer(album);
        IBoostContext ctx = new ActiveBoostContext(player, stats);
        ops.processOp(OpType.ACTIVE, ctx);
    }
}
