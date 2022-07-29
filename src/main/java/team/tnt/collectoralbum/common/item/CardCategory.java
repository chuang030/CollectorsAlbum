package team.tnt.collectoralbum.common.item;

import net.minecraft.ChatFormatting;

public enum CardCategory {
    ARMOR(ChatFormatting.BLUE),
    TOOLS(ChatFormatting.YELLOW),
    NATURE(ChatFormatting.GREEN),
    MOBS(ChatFormatting.RED),
    ITEMS(ChatFormatting.WHITE);

    private final ChatFormatting color;

    CardCategory(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }
}
