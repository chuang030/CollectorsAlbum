package team.tnt.collectoralbum.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.tnt.collectoralbum.api.IAlbumScreenFactory;
import team.tnt.collectoralbum.api.ICategorySlotDistributor;

public interface ICardCategory {

    ResourceLocation getId();

    int getCapacity();

    int getIndex();

    ICategorySlotDistributor getMenuSlotDistributor();

    // DISPLAY STUFF

    Component getTranslatedName();

    @OnlyIn(Dist.CLIENT)
    default IAlbumScreenFactory getAlbumScreenFactory() {
        return IAlbumScreenFactory.DEFAULT;
    }

    default ChatFormatting getTooltipFormat() {
        return ChatFormatting.RESET;
    }
}
