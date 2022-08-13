package team.tnt.collectoralbum.data.boosts;

import com.google.gson.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.*;

public class AlbumCardBoostManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    private static final Logger LOGGER = LogManager.getLogger(AlbumCardBoostManager.class);
    private static final ResourceLocation FABRIC_ID = new ResourceLocation(CollectorsAlbum.MODID, "album_card_boost_manager");
    private static final Gson GSON = new Gson();

    private AlbumCardBoostCollection collection;

    public AlbumCardBoostManager() {
        super(GSON, "card_boosts");
    }

    public Optional<AlbumCardBoostCollection> getBoosts() {
        return Optional.ofNullable(collection);
    }

    @Override
    public ResourceLocation getFabricId() {
        return FABRIC_ID;
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
        this.collection = new AlbumCardBoostCollection(loaded.get(OpType.CLEANUP).toArray(IAction[]::new), loaded.get(OpType.ACTIVE).toArray(IAction[]::new));
        LOGGER.info("Album boosts loaded");
    }
}
