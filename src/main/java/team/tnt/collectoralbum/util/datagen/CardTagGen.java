package team.tnt.collectoralbum.util.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.common.item.CardCategory;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class CardTagGen {

    private static final File EXPORT_DIR = new File("./exported/datagen/card_tags");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(CardTagGen.class);

    public static void main(String[] args) {
        try {
            new CardTagGen().run(ItemRegistry.class);
        } catch (Exception e) {
            LOGGER.fatal("Error:", e);
        }
    }

    private void run(Class<?> itemRegistry) throws Exception {
        Field[] fields = itemRegistry.getFields();
        LOGGER.info("Mapping cards to their ids from {} class", itemRegistry.getName());
        Map<String, ICard> itemId2CardMap = Arrays.stream(fields)
                .filter(field -> isConstant(field) && Item.class.isAssignableFrom(field.getType()) && ICard.class.isAssignableFrom(field.getType()))
                .collect(Collectors.toMap(Field::getName, field -> {
                    try {
                        return (ICard) field.get(ICard.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));
        LOGGER.info("Mapped cards to id -> card map, total {} entries", itemId2CardMap.size());
        LOGGER.info("Splitting cards by their rarities");
        Map<CardRarity, List<String>> idsByCategory = new EnumMap<>(CardRarity.class);
        for (Map.Entry<String, ICard> entry : itemId2CardMap.entrySet()) {
            String id = CollectorsAlbum.MODID + ":" + entry.getKey().toLowerCase();
            ICard card = entry.getValue();
            CardRarity rarity = card.getCardRarity();
            List<String> list = idsByCategory.computeIfAbsent(rarity, k -> new ArrayList<>());
            list.add(id);
        }
        LOGGER.info("Card rarity map dump:");
        idsByCategory.forEach((key, value) -> LOGGER.info("Rarity: {}, Count: {}", key, value.size()));
        LOGGER.info("Writing output files");
        EXPORT_DIR.mkdirs();
        idsByCategory.forEach((key, value) -> {
            File tagFile = new File(EXPORT_DIR, "cards_" + key.name().toLowerCase() + ".json");
            try {
                tagFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonObject obj = new JsonObject();
            obj.addProperty("replace", false);
            JsonArray array = new JsonArray();
            value.forEach(array::add);
            obj.add("values", array);
            try (FileWriter fw = new FileWriter(tagFile)) {
                String jsonString = GSON.toJson(obj);
                fw.write(jsonString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        LOGGER.info("Tag gen finished");
    }

    private boolean isConstant(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }
}
