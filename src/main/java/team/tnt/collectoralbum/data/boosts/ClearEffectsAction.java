package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import team.tnt.collectoralbum.util.JsonHelper;

public class ClearEffectsAction implements IAction {

    private final MobEffect[] effects;

    public ClearEffectsAction(MobEffect[] effects) {
        this.effects = effects;
    }

    @Override
    public void apply(IBoostContext context) {
        Player player = context.get(SimpleBoostContext.PLAYER, Player.class);
        for (MobEffect effect : effects) {
            player.removeEffect(effect);
        }
    }

    public static final class Serializer implements IActionSerializer<ClearEffectsAction> {

        @Override
        public ClearEffectsAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = GsonHelper.getAsJsonArray(data, "effects");
            MobEffect[] effects = JsonHelper.resolveRegistryObjectsFromIdArray(array, Registry.MOB_EFFECT, MobEffect[]::new);
            return new ClearEffectsAction(effects);
        }
    }
}
