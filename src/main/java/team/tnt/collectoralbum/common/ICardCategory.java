package team.tnt.collectoralbum.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
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

    ITextComponent getTranslatedName();

    @OnlyIn(Dist.CLIENT)
    default IAlbumScreenFactory getAlbumScreenFactory() {
        return IAlbumScreenFactory.DEFAULT;
    }

    default TextFormatting getTooltipFormat() {
        return TextFormatting.RESET;
    }
}
