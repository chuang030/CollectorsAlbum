package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.util.JsonHelper;

public class PointsCondition implements ICardBoostCondition {

    private final int minPoints;

    private PointsCondition(int minPoints) {
        this.minPoints = minPoints;
    }

    @Override
    public boolean isValid(IBoostContext context) {
        AlbumStats stats = context.get(ActiveBoostContext.STATS, AlbumStats.class);
        int points = stats.getPoints();
        return points >= minPoints;
    }

    public static final class Serializer implements ICardBoostConditionSerializer<PointsCondition> {

        @Override
        public PointsCondition fromJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            int minPoints = JSONUtils.getAsInt(object, "points");
            return new PointsCondition(minPoints);
        }
    }
}
