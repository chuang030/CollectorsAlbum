package team.tnt.collectoralbum.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.CollectorsAlbum;

public final class SoundRegistry {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CollectorsAlbum.MODID);

    public static final RegistryObject<SoundEvent> FLIP_COMMON = REGISTRY.register("flip_common", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_common")));
    public static final RegistryObject<SoundEvent> FLIP_UNCOMMON = REGISTRY.register("flip_uncommon", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_uncommon")));
    public static final RegistryObject<SoundEvent> FLIP_RARE = REGISTRY.register("flip_rare", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_rare")));
    public static final RegistryObject<SoundEvent> FLIP_EPIC = REGISTRY.register("flip_epic", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_epic")));
    public static final RegistryObject<SoundEvent> FLIP_LEGENDARY = REGISTRY.register("flip_legendary", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_legendary")));
    public static final RegistryObject<SoundEvent> FLIP_MYTHICAL = REGISTRY.register("flip_mythical", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "flip_mythical")));
    public static final RegistryObject<SoundEvent> OPEN = REGISTRY.register("open", () -> new SoundEvent(new ResourceLocation(CollectorsAlbum.MODID, "open")));
}
