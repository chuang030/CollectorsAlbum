package team.tnt.collectoralbum.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MobDropConfig {

    public final ForgeConfigSpec.ConfigValue<Integer> noDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> commonDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> uncommonDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> rareDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> epicDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> legendaryDropWeight;
    public final ForgeConfigSpec.ConfigValue<Integer> mythicalDropWeight;

    public MobDropConfig(ForgeConfigSpec.Builder builder) {
        noDropWeight = builder.define("No Drop Weight", 5700);
        commonDropWeight = builder.define("Common Drop Weight", 480);
        uncommonDropWeight = builder.define("Uncommon Drop Weight", 320);
        rareDropWeight = builder.define("Rare Drop Weight", 230);
        epicDropWeight = builder.define("Epic Drop Weight", 145);
        legendaryDropWeight = builder.define("Legendary Drop Weight", 90);
        mythicalDropWeight = builder.define("Mythical Drop Weight", 70);
    }
}
