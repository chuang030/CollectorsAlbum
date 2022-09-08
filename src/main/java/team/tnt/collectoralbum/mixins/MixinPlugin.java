package team.tnt.collectoralbum.mixins;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class MixinPlugin implements IMixinConfigPlugin {

    private static final BooleanSupplier IS_YIGD_MOD_LOADED = () -> FabricLoader.getInstance().isModLoaded("yigd");
    private static final String SERVER_PLAYER_MIXIN = "team.tnt.collectoralbum.mixins.ServerPlayerMixin";
    private static final String PLAYER_MIXIN = "team.tnt.collectoralbum.mixins.PlayerMixin";

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals(SERVER_PLAYER_MIXIN) || mixinClassName.equals(PLAYER_MIXIN)) {
            return !IS_YIGD_MOD_LOADED.getAsBoolean();
        }
        return true;
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
