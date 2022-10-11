package team.tnt.collectoralbum.data.boosts;

import com.google.gson.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import team.tnt.collectoralbum.util.JsonHelper;
import team.tnt.collectoralbum.util.TextHelper;

public class GiveEffectsAction implements IAction {

    private final IEffectFactory[] effects;
    private final Component[] description;

    private GiveEffectsAction(IEffectFactory[] effects) {
        this.effects = effects;
        this.description = generateDescriptionForEffects(effects);
    }

    public static Component[] generateDescriptionForEffects(IEffectFactory[] factories) {
        Component[] res = new Component[factories.length];
        int index = 0;
        for (IEffectFactory factory : factories) {
            MobEffectInstance instance = factory.makeEffect();
            Component displayText = instance.getEffect().getDisplayName();
            String amplifier = TextHelper.toRomanNumberString(instance.getAmplifier() + 1);
            Component effectValue = new TextComponent(displayText.getString() + " " + amplifier).withStyle(ChatFormatting.GREEN);
            res[index++] = new TranslatableComponent("text.collectorsalbum.album.boost.effect_instance", effectValue).withStyle(ChatFormatting.YELLOW);
        }
        return res;
    }

    @Override
    public void apply(IBoostContext context) {
        Player player = context.get(SimpleBoostContext.PLAYER, Player.class);
        for (IEffectFactory factory : effects) {
            MobEffectInstance instance = factory.makeEffect();
            player.addEffect(instance);
        }
    }

    @Override
    public Component[] getDescription() {
        return description;
    }

    @FunctionalInterface
    interface IEffectFactory {
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
