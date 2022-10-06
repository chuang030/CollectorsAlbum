package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.util.JsonHelper;

public class ClearEffectsAction implements IAction {

    private final Effect[] effects;

    public ClearEffectsAction(Effect[] effects) {
        this.effects = effects;
    }

    @Override
    public void apply(IBoostContext context) {
        PlayerEntity player = context.get(SimpleBoostContext.PLAYER, PlayerEntity.class);
        for (Effect effect : effects) {
            player.removeEffect(effect);
        }
    }

    public static final class Serializer implements IActionSerializer<ClearEffectsAction> {

        @Override
        public ClearEffectsAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = JSONUtils.getAsJsonArray(data, "effects");
            Effect[] effects = JsonHelper.resolveRegistryObjectsFromIdArray(array, ForgeRegistries.POTIONS, Effect[]::new);
            return new ClearEffectsAction(effects);
        }
    }
}
