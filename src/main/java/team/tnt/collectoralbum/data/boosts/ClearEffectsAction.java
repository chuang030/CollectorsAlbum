package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import team.tnt.collectoralbum.common.init.ActionTypeRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

public class ClearEffectsAction implements IAction {

    private final Effect[] effects;

    public ClearEffectsAction(Effect[] effects) {
        this.effects = effects;
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypeRegistry.CLEAR_EFFECTS;
    }

    @Override
    public void apply(IBoostContext context) {
        PlayerEntity player = context.get(SimpleBoostContext.PLAYER, PlayerEntity.class);
        for (Effect effect : effects) {
            player.removeEffect(effect);
        }
    }

    @Override
    public ITextComponent[] getDescription() {
        return new ITextComponent[0];
    }

    public static final class Serializer implements IActionSerializer<ClearEffectsAction> {

        @Override
        public ClearEffectsAction fromJson(JsonObject data, OpType opType) throws JsonParseException {
            JsonArray array = JSONUtils.getAsJsonArray(data, "effects");
            Effect[] effects = JsonHelper.resolveRegistryObjectsFromIdArray(array, ForgeRegistries.POTIONS, Effect[]::new);
            return new ClearEffectsAction(effects);
        }

        @Override
        public void networkEncode(ClearEffectsAction action, PacketBuffer buffer) {
            buffer.writeInt(action.effects.length);
            for (Effect effect : action.effects) {
                buffer.writeResourceLocation(effect.getRegistryName());
            }
        }

        @Override
        public ClearEffectsAction networkDecode(ActionType<ClearEffectsAction> type, PacketBuffer buffer) {
            int count = buffer.readInt();
            Effect[] effects = new Effect[count];
            for (int i = 0; i < count; i++) {
                effects[i] = ForgeRegistries.POTIONS.getValue(buffer.readResourceLocation());
            }
            return new ClearEffectsAction(effects);
        }
    }
}
