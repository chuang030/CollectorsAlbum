package team.tnt.collectoralbum.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class MobDropConfig {

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int noDropWeight = 3500;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int commonDropWeight = 355;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int uncommonDropWeight = 235;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int rareDropWeight = 170;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int epicDropWeight = 120;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int legendaryDropWeight = 80;

    @ConfigEntry.Category("main")
    @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
    public int mythicalDropWeight = 60;
}
