package team.tnt.collectoralbum.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import team.tnt.collectoralbum.CollectorsAlbum;

public final class SoundRegistry {

    public static void registerSounds() {}

    private static void registerSound(String id, SoundEvent event) {
        registerSound(new ResourceLocation(CollectorsAlbum.MODID, id), event);
    }

    private static void registerSound(ResourceLocation id, SoundEvent event) {
        Registry.register(Registry.SOUND_EVENT, id, event);
    }
}
