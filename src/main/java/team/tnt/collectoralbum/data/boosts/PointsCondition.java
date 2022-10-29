package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.init.CardBoostConditionRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

public class PointsCondition implements ICardBoostCondition {

    private final int minPoints;
    private final Component[] description;

    private PointsCondition(int minPoints) {
        this.minPoints = minPoints;
        this.description = new Component[] { this.getDescriptionText() };
    }

    @Override
    public CardBoostConditionType<?> getType() {
        return CardBoostConditionRegistry.POINTS;
    }

    @Override
    public boolean isValid(IBoostContext context) {
        AlbumStats stats = context.get(ActiveBoostContext.STATS, AlbumStats.class);
        int points = stats.getPoints();
        return points >= minPoints;
    }

    @Override
    public Component[] getDescription() {
        return description;
    }

    @Override
    public int compareTo(@NotNull IDescriptionProvider o) {
        if (o instanceof CardsCondition) {
            return -1;
        }
        if (o instanceof PointsCondition) {
            PointsCondition c = (PointsCondition) o;
            return minPoints - c.minPoints;
        }
        return 0;
    }

    private Component getDescriptionText() {
        Component points = new TextComponent(String.valueOf(minPoints)).withStyle(ChatFormatting.AQUA);
        return new TranslatableComponent("text.collectorsalbum.album.boost.condition.points", points).withStyle(ChatFormatting.GRAY);
    }

    public static final class Serializer implements ICardBoostConditionSerializer<PointsCondition> {

        @Override
        public PointsCondition fromJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            int minPoints = GsonHelper.getAsInt(object, "points");
            return new PointsCondition(minPoints);
        }

        @Override
        public void networkEncode(PointsCondition condition, FriendlyByteBuf buffer) {
            buffer.writeInt(condition.minPoints);
        }

        @Override
        public PointsCondition networkDecode(CardBoostConditionType<PointsCondition> type, FriendlyByteBuf buffer) {
            return new PointsCondition(buffer.readInt());
        }
    }
}
