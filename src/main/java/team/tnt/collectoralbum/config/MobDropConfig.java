package team.tnt.collectoralbum.config;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import net.minecraft.network.chat.TranslatableComponent;

public class MobDropConfig {

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that no package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int noDropWeight = 5700;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that common package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int commonDropWeight = 480;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that uncommon package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int uncommonDropWeight = 320;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that rare package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int rareDropWeight = 230;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that epic package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int epicDropWeight = 145;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that legendary package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int legendaryDropWeight = 90;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Chance that mythical package is dropped on kill")
    @Configurable.ValueUpdateCallback(method = "validateWeights")
    public int mythicalDropWeight = 70;

    public void validateWeights(int weight, IValidationHandler handler) {
        if (weight == 0) {
            handler.setValidationResult(ValidationResult.warn(new TranslatableComponent("text.config.collectorsalbum.mob_drops.weight_warning")));
        }
    }
}
