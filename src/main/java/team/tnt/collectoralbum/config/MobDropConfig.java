package team.tnt.collectoralbum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import team.tnt.collectoralbum.CollectorsAlbum;

public class MobDropConfig {

    @ConfigEntry.Category("main")
    public int noDropWeight = 5700;

    @ConfigEntry.Category("main")
    public int commonDropWeight = 460;

    @ConfigEntry.Category("main")
    public int uncommonDropWeight = 320;

    @ConfigEntry.Category("main")
    public int rareDropWeight = 230;

    @ConfigEntry.Category("main")
    public int epicDropWeight = 145;

    @ConfigEntry.Category("main")
    public int legendaryDropWeight = 90;

    @ConfigEntry.Category("main")
    public int mythicalDropWeight = 70;

    void validatePostLoad() throws ConfigData.ValidationException {
        if (noDropWeight <= 0) {
            noDropWeight = 1;
            logCorrectionMessage("noDropWeight");
        }
        if (commonDropWeight <= 0) {
            commonDropWeight = 1;
            logCorrectionMessage("commonDropWeight");
        }
        if (uncommonDropWeight <= 0) {
            uncommonDropWeight = 1;
            logCorrectionMessage("uncommonDropWeight");
        }
        if (rareDropWeight <= 0) {
            rareDropWeight = 1;
            logCorrectionMessage("rareDropWeight");
        }
        if (epicDropWeight <= 0) {
            epicDropWeight = 1;
            logCorrectionMessage("epicDropWeight");
        }
        if (legendaryDropWeight <= 0) {
            legendaryDropWeight = 1;
            logCorrectionMessage("legendaryDropWeight");
        }
        if (mythicalDropWeight <= 0) {
            mythicalDropWeight = 1;
            logCorrectionMessage("mythicalDropWeight");
        }
    }

    private void logCorrectionMessage(String field) {
        CollectorsAlbum.LOGGER.warn("Corrected \"{}\" value to 1", field);
    }
}
