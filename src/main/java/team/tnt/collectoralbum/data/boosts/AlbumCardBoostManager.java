package team.tnt.collectoralbum.data.boosts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.*;

public class AlbumCardBoostManager extends SimpleJsonResourceReloadListener {

    private static final Logger LOGGER = LogManager.getLogger(AlbumCardBoostManager.class);
    private static final Gson GSON = new Gson();

    private AlbumCardBoostCollection collection;

    public AlbumCardBoostManager() {
        super(GSON, "card_boosts");
    }

    public Optional<AlbumCardBoostCollection> getBoosts() {
        return Optional.ofNullable(collection);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        LOGGER.info("Loading album boosts");
        Map<OpType, List<IAction>> loaded = new EnumMap<>(OpType.class);
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation filePath = entry.getKey();
            JsonElement fileData = entry.getValue();
            try {
                JsonObject data = JsonHelper.asObject(fileData);
                String opTypeId = GsonHelper.getAsString(data, "op");
                OpType type = OpType.valueOf(opTypeId);
                List<IAction> values = loaded.computeIfAbsent(type, k -> new ArrayList<>());
                IAction action = ActionType.fromJson(type, GsonHelper.getAsJsonObject(data, "action"));
                values.add(action);
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Error loading {} file: {}", filePath, e);
            }
        }
        this.collection = new AlbumCardBoostCollection(loaded.get(OpType.CLEANUP).toArray(new IAction[0]), loaded.get(OpType.ACTIVE).toArray(new IAction[0]));
        LOGGER.info("Album boosts loaded");
    }
}
