package team.tnt.collectoralbum.data.boosts;

import com.google.gson.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.common.init.ActionTypeRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

public class GiveMissingEffectAction implements IAction {

    private final GiveEffectsAction.IEffectFactory[] factories;
    private final ITextComponent[] description;

    public GiveMissingEffectAction(GiveEffectsAction.IEffectFactory[] factories) {
        this.factories = factories;
        this.description = GiveEffectsAction.generateDescriptionForEffects(factories);
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypeRegistry.GIVE_MISSING_EFFECTS;
    }

    @Override
    public void apply(IBoostContext context) {
        PlayerEntity player = context.get(SimpleBoostContext.PLAYER, PlayerEntity.class);
        for (GiveEffectsAction.IEffectFactory factory : factories) {
            EffectInstance instance = factory.makeEffect();
            EffectInstance old = player.getEffect(instance.getEffect());
            if (old == null || old.getAmplifier() != instance.getAmplifier()) {
                player.removeEffect(instance.getEffect());
                player.addEffect(instance);
            }
        }
    }

    @Override
    public ITextComponent[] getDescription() {
        return description;
    }

    public static final class Serializer implements IActionSerializer<GiveMissingEffectAction> {

        @Override
        public GiveMissingEffectAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = JSONUtils.getAsJsonArray(data, "effects");
            GiveEffectsAction.IEffectFactory[] factories = new GiveEffectsAction.IEffectFactory[array.size()];
            int i = 0;
            for (JsonElement element : array) {
                JsonObject effectJson = JsonHelper.asObject(element);
                ResourceLocation effectId = new ResourceLocation(JSONUtils.getAsString(effectJson, "effect"));
                Effect effect = ForgeRegistries.POTIONS.getValue(effectId);
                if (effect == null) {
                    throw new JsonSyntaxException("Unknown effect: " + effectId);
                }
                int duration = Math.max(JSONUtils.getAsInt(effectJson, "duration", 20), 0);
                int amplifier = MathHelper.clamp(JSONUtils.getAsInt(effectJson, "amplifier", 0), 0, 255);
                boolean ambient = JSONUtils.getAsBoolean(effectJson, "ambient", false);
                boolean visible = JSONUtils.getAsBoolean(effectJson, "visible", true);
                boolean showIcon = JSONUtils.getAsBoolean(effectJson, "showIcon", true);
                factories[i++] = () -> new EffectInstance(effect, duration, amplifier, ambient, visible, showIcon);
            }
            return new GiveMissingEffectAction(factories);
        }

        @Override
        public void networkEncode(GiveMissingEffectAction action, PacketBuffer buffer) {
            buffer.writeInt(action.factories.length);
            for (GiveEffectsAction.IEffectFactory factory : action.factories) {
                GiveEffectsAction.encodeEffectFactory(factory, buffer);
            }
        }

        @Override
        public GiveMissingEffectAction networkDecode(ActionType<GiveMissingEffectAction> type, PacketBuffer buffer) {
            GiveEffectsAction.IEffectFactory[] factories = new GiveEffectsAction.IEffectFactory[buffer.readInt()];
            for (int i = 0; i < factories.length; i++) {
                factories[i] = GiveEffectsAction.decodeEffectFactory(buffer);
            }
            return new GiveMissingEffectAction(factories);
        }
    }
}
