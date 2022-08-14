package team.tnt.collectoralbum.data.boosts;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import team.tnt.collectoralbum.util.JsonHelper;

public class GiveEffectsAction implements IAction {

    private final IEffectFactory[] effects;

    private GiveEffectsAction(IEffectFactory[] effects) {
        this.effects = effects;
    }

    @Override
    public void apply(IBoostContext context) {
        Player player = context.get(SimpleBoostContext.PLAYER, Player.class);
        for (IEffectFactory factory : effects) {
            MobEffectInstance instance = factory.makeEffect();
            MobEffectInstance old = player.getEffect(instance.getEffect());
            if (old == null || old.getAmplifier() != instance.getAmplifier()) {
                player.removeEffect(instance.getEffect());
                player.addEffect(instance);
            }
        }
    }

    @FunctionalInterface
    private interface IEffectFactory {
        MobEffectInstance makeEffect();
    }

    public static final class Serializer implements IActionSerializer<GiveEffectsAction> {

        @Override
        public GiveEffectsAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = GsonHelper.getAsJsonArray(data, "effects");
            IEffectFactory[] factories = new IEffectFactory[array.size()];
            int i = 0;
            for (JsonElement element : array) {
                JsonObject effectJson = JsonHelper.asObject(element);
                ResourceLocation effectId = new ResourceLocation(GsonHelper.getAsString(effectJson, "effect"));
                MobEffect effect = Registry.MOB_EFFECT.get(effectId);
                if (effect == null) {
                    throw new JsonSyntaxException("Unknown effect: " + effectId);
                }
                int duration = Math.max(GsonHelper.getAsInt(effectJson, "duration", 20), 0);
                int amplifier = Mth.clamp(GsonHelper.getAsInt(effectJson, "amplifier", 0), 0, 255);
                boolean ambient = GsonHelper.getAsBoolean(effectJson, "ambient", false);
                boolean visible = GsonHelper.getAsBoolean(effectJson, "visible", true);
                boolean showIcon = GsonHelper.getAsBoolean(effectJson, "showIcon", true);
                factories[i++] = () -> new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon);
            }
            return new GiveEffectsAction(factories);
        }
    }
}
