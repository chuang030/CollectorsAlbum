package team.tnt.collectoralbum.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import team.tnt.collectoralbum.CollectorsAlbum;

public final class SoundRegistry {

    public static final SoundEvent FLIP_COMMON = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_common"));
    public static final SoundEvent FLIP_UNCOMMON = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_uncommon"));
    public static final SoundEvent FLIP_RARE = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_rare"));
    public static final SoundEvent FLIP_EPIC = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_epic"));
    public static final SoundEvent FLIP_LEGENDARY = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_legendary"));
    public static final SoundEvent FLIP_MYTHICAL = new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_mythical"));

    public static void registerSounds() {
        registerSound(FLIP_COMMON);
        registerSound(FLIP_UNCOMMON);
        registerSound(FLIP_RARE);
        registerSound(FLIP_EPIC);
        registerSound(FLIP_LEGENDARY);
        registerSound(FLIP_MYTHICAL);
    }

    private static void registerSound(SoundEvent event) {
        registerSound(event.getLocation(), event);
    }

    private static void registerSound(ResourceLocation id, SoundEvent event) {
        Registry.register(Registry.SOUND_EVENT, id, event);
    }
}
