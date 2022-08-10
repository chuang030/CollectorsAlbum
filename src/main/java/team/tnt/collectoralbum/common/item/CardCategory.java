package team.tnt.collectoralbum.common.item;

import net.minecraft.ChatFormatting;

public enum CardCategory {

    TOOLS(ChatFormatting.YELLOW),
    ARMOR(ChatFormatting.BLUE),
    MOBS(ChatFormatting.RED),
    NATURE(ChatFormatting.GREEN),
    ITEMS(ChatFormatting.WHITE);

    private final ChatFormatting color;

    CardCategory(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }
}
