package team.tnt.collectoralbum.data.boosts;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import team.tnt.collectoralbum.util.JsonHelper;

public class GiveMissingEffectAction implements IAction {

    private final GiveEffectsAction.IEffectFactory[] factories;
    private final Component[] description;

    public GiveMissingEffectAction(GiveEffectsAction.IEffectFactory[] factories) {
        this.factories = factories;
        this.description = GiveEffectsAction.generateDescriptionForEffects(factories);
    }

    @Override
    public void apply(IBoostContext context) {
        Player player = context.get(SimpleBoostContext.PLAYER, Player.class);
        for (GiveEffectsAction.IEffectFactory factory : factories) {
            MobEffectInstance instance = factory.makeEffect();
            MobEffectInstance old = player.getEffect(instance.getEffect());
            if (old == null || old.getAmplifier() != instance.getAmplifier()) {
                player.removeEffect(instance.getEffect());
                player.addEffect(instance);
            }
        }
    }

    @Override
    public Component[] getDescription() {
        return description;
    }

    public static final class Serializer implements IActionSerializer<GiveMissingEffectAction> {

        @Override
        public GiveMissingEffectAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = GsonHelper.getAsJsonArray(data, "effects");
            GiveEffectsAction.IEffectFactory[] factories = new GiveEffectsAction.IEffectFactory[array.size()];
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
            return new GiveMissingEffectAction(factories);
        }
    }
}
