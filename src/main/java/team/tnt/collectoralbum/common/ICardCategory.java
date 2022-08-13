package team.tnt.collectoralbum.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.api.IAlbumScreenFactory;
import team.tnt.collectoralbum.api.ICategorySlotDistributor;

public interface ICardCategory {

    ResourceLocation getId();

    int getCapacity();

    int getIndex();

    ICategorySlotDistributor getMenuSlotDistributor();

    // DISPLAY STUFF

    Component getTranslatedName();

    @Environment(EnvType.CLIENT)
    default IAlbumScreenFactory getAlbumScreenFactory() {
        return IAlbumScreenFactory.DEFAULT;
    }

    default ChatFormatting getTooltipFormat() {
        return ChatFormatting.RESET;
    }
}
