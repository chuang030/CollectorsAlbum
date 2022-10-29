package team.tnt.collectoralbum.mixins;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.packet.SendAlbumBoostsPacket;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void collectorsalbum_onPlayerLogIn(NetworkManager manager, ServerPlayerEntity player, CallbackInfo ci) {
        Networking.dispatchClientPacket(player, new SendAlbumBoostsPacket());
    }
}
