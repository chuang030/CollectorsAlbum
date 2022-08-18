package team.tnt.collectoralbum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "CollectorsAlbum")
public class ModConfig implements ConfigData {

    @ConfigEntry.Category("main")
    @ConfigEntry.Gui.CollapsibleObject
    public MobDropConfig mobDrops = new MobDropConfig();

    @ConfigEntry.Category("main")
    public boolean persistAlbumThroughDeath = true;

    @Override
    public void validatePostLoad() throws ValidationException {
        mobDrops.validatePostLoad();
    }
}
