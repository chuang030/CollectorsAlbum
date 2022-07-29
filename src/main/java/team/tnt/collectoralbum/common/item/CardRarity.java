package team.tnt.collectoralbum.common.item;

import net.minecraft.ChatFormatting;

public enum CardRarity {
    COMMON(ChatFormatting.WHITE),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.BLUE),
    EPIC(ChatFormatting.DARK_PURPLE),
    LEGENDARY(ChatFormatting.GOLD),
    MYTHICAL(ChatFormatting.RED);

    final int value;
    final ChatFormatting color;

    CardRarity(ChatFormatting color) {
        this.value = this.ordinal() + 1;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public ChatFormatting getColor() {
        return color;
    }
}
