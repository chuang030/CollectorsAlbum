package team.tnt.collectoralbum.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class MobDropConfig {

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int noDropWeight = 5500;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int commonDropWeight = 600;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int uncommonDropWeight = 285;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int rareDropWeight = 200;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int epicDropWeight = 135;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int legendaryDropWeight = 90;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int mythicalDropWeight = 70;
}
