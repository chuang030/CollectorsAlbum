package team.tnt.collectoralbum.common.item;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import team.tnt.collectoralbum.common.init.SoundRegistry;

import java.util.function.Supplier;

public enum CardRarity {
    COMMON(TextFormatting.WHITE, SoundRegistry.FLIP_COMMON),
    UNCOMMON(TextFormatting.GREEN, SoundRegistry.FLIP_UNCOMMON),
    RARE(TextFormatting.BLUE, SoundRegistry.FLIP_RARE),
    EPIC(TextFormatting.DARK_PURPLE, SoundRegistry.FLIP_EPIC),
    LEGENDARY(TextFormatting.GOLD, SoundRegistry.FLIP_LEGENDARY),
    MYTHICAL(TextFormatting.RED, SoundRegistry.FLIP_MYTHICAL);

    final int value;
    final TextFormatting color;
    final Supplier<SoundEvent> discoverySound;

    CardRarity(TextFormatting color, Supplier<SoundEvent> discoverySound) {
        this.value = this.ordinal() + 1;
        this.color = color;
        this.discoverySound = discoverySound;
    }

    public SoundEvent getDiscoverySound() {
        return discoverySound.get();
    }

    public int getValue() {
        return value;
    }

    public TextFormatting getColor() {
        return color;
    }
}
