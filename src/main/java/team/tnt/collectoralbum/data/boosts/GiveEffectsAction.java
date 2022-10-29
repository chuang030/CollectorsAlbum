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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.common.init.ActionTypeRegistry;
import team.tnt.collectoralbum.util.JsonHelper;
import team.tnt.collectoralbum.util.TextHelper;

public class GiveEffectsAction implements IAction {

    private final IEffectFactory[] effects;
    private final ITextComponent[] description;

    private GiveEffectsAction(IEffectFactory[] effects) {
        this.effects = effects;
        this.description = generateDescriptionForEffects(effects);
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypeRegistry.GIVE_EFFECTS;
    }

    public static ITextComponent[] generateDescriptionForEffects(IEffectFactory[] factories) {
        ITextComponent[] res = new ITextComponent[factories.length];
        int index = 0;
        for (IEffectFactory factory : factories) {
            EffectInstance instance = factory.makeEffect();
            ITextComponent displayText = instance.getEffect().getDisplayName();
            String amplifier = TextHelper.toRomanNumberString(instance.getAmplifier() + 1);
            ITextComponent effectValue = new StringTextComponent(displayText.getString() + " " + amplifier).withStyle(TextFormatting.GREEN);
            res[index++] = new TranslationTextComponent("text.collectorsalbum.album.boost.effect_instance", effectValue).withStyle(TextFormatting.YELLOW);
        }
        return res;
    }

    @Override
    public void apply(IBoostContext context) {
        PlayerEntity player = context.get(SimpleBoostContext.PLAYER, PlayerEntity.class);
        for (IEffectFactory factory : effects) {
            EffectInstance instance = factory.makeEffect();
            player.addEffect(instance);
        }
    }

    @Override
    public ITextComponent[] getDescription() {
        return description;
    }

    public static void encodeEffectFactory(IEffectFactory factory, PacketBuffer buffer) {
        EffectInstance instance = factory.makeEffect();
        Effect type = instance.getEffect();
        ResourceLocation effectId = type.getRegistryName();

        buffer.writeResourceLocation(effectId);
        buffer.writeInt(instance.getDuration());
        buffer.writeInt(instance.getAmplifier());
        buffer.writeBoolean(instance.isAmbient());
        buffer.writeBoolean(instance.isVisible());
        buffer.writeBoolean(instance.showIcon());
    }

    public static IEffectFactory decodeEffectFactory(PacketBuffer buffer) {
        ResourceLocation effectId = buffer.readResourceLocation();
        Effect type = ForgeRegistries.POTIONS.getValue(effectId);
        int duration = buffer.readInt();
        int amplifier = buffer.readInt();
        boolean ambient = buffer.readBoolean();
        boolean visible = buffer.readBoolean();
        boolean icon = buffer.readBoolean();
        return () -> new EffectInstance(type, duration, amplifier, ambient, visible, icon);
    }

    @FunctionalInterface
    interface IEffectFactory {
        EffectInstance makeEffect();
    }

    public static final class Serializer implements IActionSerializer<GiveEffectsAction> {

        @Override
        public GiveEffectsAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = JSONUtils.getAsJsonArray(data, "effects");
            IEffectFactory[] factories = new IEffectFactory[array.size()];
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
            return new GiveEffectsAction(factories);
        }

        @Override
        public void networkEncode(GiveEffectsAction action, PacketBuffer buffer) {
            buffer.writeInt(action.effects.length);
            for (IEffectFactory factory : action.effects) {
                encodeEffectFactory(factory, buffer);
            }
        }

        @Override
        public GiveEffectsAction networkDecode(ActionType<GiveEffectsAction> type, PacketBuffer buffer) {
            IEffectFactory[] factories = new IEffectFactory[buffer.readInt()];
            for (int i = 0; i < factories.length; i++) {
                factories[i] = decodeEffectFactory(buffer);
            }
            return new GiveEffectsAction(factories);
        }
    }
}
