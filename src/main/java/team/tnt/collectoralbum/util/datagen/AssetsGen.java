package team.tnt.collectoralbum.util.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.ItemRegistry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AssetsGen {

    private static final String NAMESPACE = CollectorsAlbum.MODID;
    private static final File DIR_ASSETS = new File("../src/main/resources/assets/" + NAMESPACE);
    private static final File DIR_MODELS = new File(DIR_ASSETS, "models");
    private static final File DIR_BLOCKSTATES = new File(DIR_ASSETS, "blockstates");
    private static final File DIR_MODELS_BLOCK = new File(DIR_MODELS, "block");
    private static final File DIR_MODELS_ITEM = new File(DIR_MODELS, "item");
    private static final File DIR_EXPORTED_LOCALIZATION = new File("./exported/datagen");

    private static final Logger LOG = LogManager.getLogger("AssetsGenerator");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<Entry> datagenEntries = new ArrayList<>();
    private final List<Pair<String, String>> localizationBuffer = new ArrayList<>();
    private final List<Runnable> finalActionsBuffer = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            new AssetsGen().run();
        } catch (Throwable t) {
            LOG.fatal(t);
        }
        long totalExecutionTime = System.currentTimeMillis() - startTime;
        LOG.info("Data generation finished after {}ms", totalExecutionTime);
    }

    private void run() throws Exception {
        LOG.info("Running data generator");
        LOG.info("Registering data generator entries");
        registerEntry(ItemRegistry.class, this::runItemGen);
        LOG.info("Processing datagen entries");
        datagenEntries.parallelStream().forEach(entry -> {
            Class<?> type = entry.type();
            Field[] fields = type.getFields();
            String[] expectedIds = Arrays.stream(fields)
                    .parallel()
                    .filter(field -> {
                        int modifiers = field.getModifiers();
                        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
                    })
                    .map(field -> field.getName().toLowerCase(Locale.ROOT))
                    .toArray(String[]::new);
            IDataGenerator generator = entry.generator();
            try {
                generator.generate(expectedIds);
            } catch (Exception e) {
                LOG.fatal(e);
            }
        });
        // Localization export
        LOG.info("Exporting prepared localizations");
        if (!localizationBuffer.isEmpty()) {
            LOG.info("Sorting localizations");
            localizationBuffer.sort(Comparator.comparing(Pair::getFirst));
            LOG.info("Prepared localizations dump");
            localizationBuffer.forEach(pair -> LOG.info("Key: {}, Value: {}", pair.getFirst(), pair.getSecond()));
            ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
            String fileSuffix = "_" + zdt.format(DateTimeFormatter.BASIC_ISO_DATE);
            File file = new File(DIR_EXPORTED_LOCALIZATION, "en_us_exported" + fileSuffix + ".json");
            if (DIR_EXPORTED_LOCALIZATION.mkdirs()) {
                LOG.info("Created directory structure for localization exports");
            }
            if (file.createNewFile()) {
                LOG.info("Created new empty localizations file with name {}", file.getName());
            }
            LOG.info("Converting localizations to JSON format");
            JsonObject root = new JsonObject();
            for (Pair<String, String> pair : localizationBuffer) {
                root.addProperty(pair.getFirst(), pair.getSecond());
            }
            writeJsonContentToFile(root, file);
            LOG.info("Localizations exported");
        } else {
            LOG.warn("No localizations to export, aborting");
        }
        LOG.info("Running post run actions and logging");
        finalActionsBuffer.forEach(Runnable::run);
        LOG.info("Datagen run has finished");
    }

    private void registerEntry(Class<?> type, IDataGenerator generator) {
        datagenEntries.add(new Entry(type, generator));
        LOG.info("Registered generator for {} class", type.getSimpleName());
    }

    private void runBlockGen(String[] objectIds) throws Exception {
        String localizationPrefix = String.format("block.%s.", NAMESPACE);
        int blockGenerated = 0;
        for (String id : objectIds) {
            LOG.info("Running datagen for block {}", id);
            File blockstateFile = new File(DIR_BLOCKSTATES, id + ".json");
            if (blockstateFile.exists()) {
                LOG.warn("Skipping datagen for block {}, file already exists", id);
                continue;
            }
            File blockModelFile = new File(DIR_MODELS_BLOCK, id + ".json");
            File itemModelFile = new File(DIR_MODELS_ITEM, id + ".json");
            if (blockstateFile.createNewFile()) {
                LOG.info("Created new empty blockstate file for block {}", id);
            }
            if (blockModelFile.createNewFile()) {
                LOG.info("Created new empty block model file for {}", id);
            }
            if (itemModelFile.createNewFile()) {
                LOG.info("Created new empty block item model file for {}", id);
            }
            LOG.info("Generating JSON contents for {} block", id);
            // blockstate
            JsonObject blockstateJsonRoot = new JsonObject();
            JsonObject variantsJson = new JsonObject();
            JsonObject variantModelMappingJson = new JsonObject();
            variantModelMappingJson.addProperty("model", String.format("%s:block/%s", NAMESPACE, id));
            variantsJson.add("", variantModelMappingJson);
            blockstateJsonRoot.add("variants", variantsJson);
            // models/block
            JsonObject blockModelRoot = new JsonObject();
            blockModelRoot.addProperty("parent", "block/cube_all");
            JsonObject blockModelTextures = new JsonObject();
            blockModelTextures.addProperty("all", String.format("%s:block/%s", NAMESPACE, id));
            blockModelRoot.add("textures", blockModelTextures);
            // models/item
            JsonObject itemModelRoot = new JsonObject();
            itemModelRoot.addProperty("parent", String.format("%s:block/%s", NAMESPACE, id));
            LOG.info("Writing blockstate file content for {}", id);
            writeJsonContentToFile(blockstateJsonRoot, blockstateFile);
            LOG.info("Writing block model file content for {}", id);
            writeJsonContentToFile(blockModelRoot, blockModelFile);
            LOG.info("Writing item block model file content for {}", id);
            writeJsonContentToFile(itemModelRoot, itemModelFile);
            LOG.info("Model files successfully created for {} block", id);
            prepareLocalizationPair(localizationPrefix, id);
            ++blockGenerated;
        }
        String text = "Generated " + blockGenerated + " block model files and localizations";
        finalActionsBuffer.add(() -> LOG.info(text));
    }

    private void runItemGen(String[] objectIds) throws Exception {
        int itemsGenerated = 0;
        String localizationPrefix = String.format("item.%s.", NAMESPACE);
        for (String objectId : objectIds) {
            LOG.info("Running datagen for item {}", objectId);
            File modelFile = new File(DIR_MODELS_ITEM, objectId + ".json");
            if (modelFile.exists()) {
                LOG.warn("Skipping datagen for item {}, file already exists", objectId);
                continue;
            }
            if (modelFile.createNewFile()) {
                LOG.info("Created new empty item model file for item {}", objectId);
            }
            LOG.info("Generating JSON contents for {} item", objectId);
            JsonObject rootJson = new JsonObject();
            rootJson.addProperty("parent", "item/generated");
            JsonObject texturesJson = new JsonObject();
            texturesJson.addProperty("layer0", String.format("%s:item/%s", NAMESPACE, objectId));
            rootJson.add("textures", texturesJson);
            LOG.info("Populating model file of {} item", objectId);
            writeJsonContentToFile(rootJson, modelFile);
            LOG.info("Model file successfully created for {} item", objectId);
            prepareLocalizationPair(localizationPrefix, objectId);
            ++itemsGenerated;
        }
        String text = "Generated " + itemsGenerated + " item files and localizations";
        finalActionsBuffer.add(() -> LOG.info(text));
    }

    private void prepareLocalizationPair(String prefix, String objectId) {
        Pair<String, String> localizationPair = new Pair<>(prefix + objectId, this.guessLocalization(objectId));
        localizationBuffer.add(localizationPair);
        LOG.info("Prepared localization pair: " + localizationPair);
    }

    private String guessLocalization(String objectId) {
        return String.join(" ", Arrays.stream(objectId.split("_+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .toArray(String[]::new));
    }

    private void writeJsonContentToFile(JsonElement content, File dest) throws IOException {
        try (FileWriter fw = new FileWriter(dest)) {
            fw.write(GSON.toJson(content));
        }
    }

    @FunctionalInterface
    private interface IDataGenerator {
        void generate(String[] objectIds) throws Exception;
    }

    private record Entry(Class<?> type, IDataGenerator generator) {
    }
}
