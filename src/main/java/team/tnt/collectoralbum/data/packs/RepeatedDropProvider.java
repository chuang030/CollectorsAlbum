package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.Random;

public class RepeatedDropProvider implements ICardDropProvider {

    private final int minRuns;
    private final int maxRuns;
    private final ICardDropProvider provider;

    private RepeatedDropProvider(int minRuns, int maxRuns, ICardDropProvider provider) {
        this.minRuns = minRuns;
        this.maxRuns = maxRuns;
        this.provider = provider;
    }

    @Override
    public void provideDrops(Player player, Level level) {
        Random random = new Random();
        int count = minRuns + random.nextInt(maxRuns - minRuns + 1);
        for (int i = 0; i < count; i++) {
            provider.provideDrops(player, level);
        }
    }

    public static final class Serializer implements ICardDropSerializer<RepeatedDropProvider> {

        @Override
        public RepeatedDropProvider fromJson(JsonElement data) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(data);
            int min = GsonHelper.getAsInt(object, "min", 1);
            int max = GsonHelper.getAsInt(object, "max", min);
            JsonObject providerJson = GsonHelper.getAsJsonObject(object, "provider");
            ICardDropProvider provider = CardDropProviderType.fromJson(providerJson);
            if (min <= 0) {
                throw new JsonSyntaxException("Min run count cannot be smaller or equal to 0. Got " + min);
            }
            if (max <= 0) {
                throw new JsonSyntaxException("Max run count cannot be smaller or equal to 0. Got " + max);
            }
            if (min > max) {
                throw new JsonSyntaxException("Min count cannot be higher than max count! Got min: " + min + ", max: " + max);
            }
            return new RepeatedDropProvider(min, max, provider);
        }
    }
}
