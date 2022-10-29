package team.tnt.collectoralbum.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ModConfig {

    public static ModConfig INSTANCE;
    public static ForgeConfigSpec CONFIG_SPEC;
    public final ForgeConfigSpec.ConfigValue<Boolean> persistAlbumThroughDeath;
    public final ForgeConfigSpec.ConfigValue<Boolean> requireTurnAllCards;
    public final MobDropConfig mobDrops;

    public ModConfig(ForgeConfigSpec.Builder builder) {
        persistAlbumThroughDeath = builder.define("Persist Album Through Death", true);
        requireTurnAllCards = builder.comment("This will allow cards to be dropped even when not all cards are turned in package")
                .define("Packages require turning of all cards", false);
        builder.push("Mob Drop Chances");
        this.mobDrops = new MobDropConfig(builder);
        builder.pop();
    }

    static {
        Pair<ModConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ModConfig::new);
        INSTANCE = pair.getKey();
        CONFIG_SPEC = pair.getRight();
    }
}
