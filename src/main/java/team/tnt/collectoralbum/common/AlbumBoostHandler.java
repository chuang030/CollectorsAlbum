package team.tnt.collectoralbum.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.container.AlbumContainer;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.common.item.CardCategory;

import java.util.Map;
import java.util.function.Consumer;

public class AlbumBoostHandler {

    public void onServerTick(MinecraftServer server) {
        PlayerList playerList = server.getPlayerList();
        for (ServerPlayer player : playerList.getPlayers()) {
            tickPlayer(player);
        }
    }

    private void tickPlayer(ServerPlayer player) {
        AbstractContainerMenu inventory = player.containerMenu;
        for (ItemStack stack : inventory.getItems()) {
            if (stack.getItem() == ItemRegistry.ALBUM) {
                applyBoosts(stack, player);
                break;
            }
        }
    }

    private void applyBoosts(ItemStack album, ServerPlayer player) {
        AlbumContainer container = new AlbumContainer(album);
        AlbumStats stats = container.getStats();
        int points = stats.getPoints();
        int healthBoostModifier = -1;
        if (points >= 900) {
            healthBoostModifier = 9;
        } else if (points >= 700) {
            healthBoostModifier = 4;
        } else if (points >= 600) {
            healthBoostModifier = 3;
        } else if (points >= 500) {
            healthBoostModifier = 2;
        } else if (points >= 400) {
            healthBoostModifier = 1;
        } else if (points >= 300) {
            healthBoostModifier = 0;
        }
        if (healthBoostModifier >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20, healthBoostModifier));
        }
        int potionModifier = points >= 900 ? 1 : 0;
        Map<CardCategory, Integer> byCategories = stats.getCardsByCategory();
        applyBoosts(byCategories, CardCategory.TOOLS, player, serverPlayer -> serverPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20, potionModifier)));
        applyBoosts(byCategories, CardCategory.ARMOR, player, serverPlayer -> serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, potionModifier)));
        applyBoosts(byCategories, CardCategory.MOBS, player, serverPlayer -> serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, potionModifier)));
        applyBoosts(byCategories, CardCategory.NATURE, player, serverPlayer -> serverPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, potionModifier)));
        applyBoosts(byCategories, CardCategory.ITEMS, player, serverPlayer -> serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, potionModifier)));
    }

    private void applyBoosts(Map<CardCategory, Integer> data, CardCategory category, ServerPlayer player, Consumer<ServerPlayer> event) {
        int value = data.getOrDefault(category, 0);
        if (value >= 30) {
            event.accept(player);
        }
    }
}
