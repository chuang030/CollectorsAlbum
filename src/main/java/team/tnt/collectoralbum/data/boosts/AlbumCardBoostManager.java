package team.tnt.collectoralbum.data.boosts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.*;

public class AlbumCardBoostManager extends JsonReloadListener {

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
    protected void apply(Map<ResourceLocation, JsonElement> object, IResourceManager resourceManager, IProfiler profiler) {
        LOGGER.info("Loading album boosts");
        Map<OpType, List<IAction>> loaded = new EnumMap<>(OpType.class);
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation filePath = entry.getKey();
            JsonElement fileData = entry.getValue();
            try {
                JsonObject data = JsonHelper.asObject(fileData);
                String opTypeId = JSONUtils.getAsString(data, "op");
                OpType type = OpType.valueOf(opTypeId);
                List<IAction> values = loaded.computeIfAbsent(type, k -> new ArrayList<>());
                IAction action = ActionType.fromJson(type, JSONUtils.getAsJsonObject(data, "action"));
                values.add(action);
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Error loading {} file: {}", filePath, e);
            }
        }
        this.collection = new AlbumCardBoostCollection(loaded.get(OpType.CLEANUP).toArray(new IAction[0]), loaded.get(OpType.ACTIVE).toArray(new IAction[0]));
        LOGGER.info("Album boosts loaded");
    }
}
