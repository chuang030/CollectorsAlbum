package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.init.CardBoostConditionRegistry;
import team.tnt.collectoralbum.util.JsonHelper;

public class PointsCondition implements ICardBoostCondition {

    private final int minPoints;
    private final ITextComponent[] description;

    private PointsCondition(int minPoints) {
        this.minPoints = minPoints;
        this.description = new ITextComponent[] { this.getDescriptionText() };
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
    public ITextComponent[] getDescription() {
        return description;
    }

    @Override
    public int compareTo(IDescriptionProvider o) {
        if (o instanceof CardsCondition) {
            return -1;
        }
        if (o instanceof PointsCondition) {
            PointsCondition c = (PointsCondition) o;
            return minPoints - c.minPoints;
        }
        return 0;
    }

    private ITextComponent getDescriptionText() {
        ITextComponent points = new StringTextComponent(String.valueOf(minPoints)).withStyle(TextFormatting.AQUA);
        return new TranslationTextComponent("text.collectorsalbum.album.boost.condition.points", points).withStyle(TextFormatting.GRAY);
    }

    public static final class Serializer implements ICardBoostConditionSerializer<PointsCondition> {

        @Override
        public PointsCondition fromJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            int minPoints = JSONUtils.getAsInt(object, "points");
            return new PointsCondition(minPoints);
        }

        @Override
        public void networkEncode(PointsCondition condition, PacketBuffer buffer) {
            buffer.writeInt(condition.minPoints);
        }

        @Override
        public PointsCondition networkDecode(CardBoostConditionType<PointsCondition> type, PacketBuffer buffer) {
            return new PointsCondition(buffer.readInt());
        }
    }
}
