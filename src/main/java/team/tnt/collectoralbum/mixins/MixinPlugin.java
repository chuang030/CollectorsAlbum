package team.tnt.collectoralbum.mixins;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    private static final String SERVER_PLAYER_MIXIN = "team.tnt.collectoralbum.mixins.ServerPlayerMixin";
    private static final String PLAYER_MIXIN = "team.tnt.collectoralbum.mixins.PlayerMixin";
    private static final Set<String> INCOMPATIBLE_MODS = new ImmutableSet.Builder<String>()
            .add("forgottengraves")
            .build();

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals(SERVER_PLAYER_MIXIN) || mixinClassName.equals(PLAYER_MIXIN)) {
            boolean isValid = true;
            for (String modId : INCOMPATIBLE_MODS) {
                if (FabricLoader.getInstance().isModLoaded(modId)) {
                    isValid = false;
                    break;
                }
            }
            return isValid;
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
