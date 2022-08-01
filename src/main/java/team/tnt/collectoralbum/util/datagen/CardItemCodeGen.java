package team.tnt.collectoralbum.util.datagen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.tnt.collectoralbum.common.init.CardRegistry;
import team.tnt.collectoralbum.common.item.CardRarity;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardItemCodeGen {

    private static final File CARD_ITEM_DIR = new File("./run/exported/cards");
    private static final Logger LOGGER = LogManager.getLogger(CardItemCodeGen.class);

    public static void main(String[] args) {
        try {
            new CardItemCodeGen().run(CardRegistry.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void run(Class<?> cardRegistry) throws Exception {
        LOGGER.info("Running CardItemCode generator");
        LOGGER.info("Retrieving all cards from registry");
        Field[] cardFields = cardRegistry.getFields();
        LOGGER.info("Retrieving field names");
        String[] cardIds = Arrays.stream(cardFields)
                .filter(field -> {
                    int modifiers = field.getModifiers();
                    return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
                })
                .map(Field::getName)
                .toArray(String[]::new);
        List<String> variables = new ArrayList<>();
        List<String> registration = new ArrayList<>();
        LOGGER.info("Generating...");
        for (String card : cardIds) {
            LOGGER.info("Running generator for {} card", card);
            for (CardRarity rarity : CardRarity.values()) {
                String rarityName = rarity.name().toLowerCase();
                String fieldName = card.toLowerCase();
                String itemId = String.format("%s_%s", rarityName, fieldName);
                String variableLine = String.format("public static final CardItem %s = new CardItem(CardRarity.%s, CardRegistry.%s);", itemId.toUpperCase(), rarity.name(), card);
                LOGGER.info("Generated constant code line: {}", variableLine);
                String registrationLine = String.format("registerItem(\"%s\", %s);", itemId, itemId.toUpperCase());
                LOGGER.info("Generated registration code line: {}", registrationLine);
                variables.add(variableLine);
                registration.add(registrationLine);
            }
            LOGGER.info("Generator finished for {} card", card);
        }
        LOGGER.info("Writing output files...");
        File variableOut = new File(CARD_ITEM_DIR, "variables.txt");
        File registrationOut = new File(CARD_ITEM_DIR, "registration.txt");
        CARD_ITEM_DIR.mkdirs();
        variableOut.createNewFile();
        registrationOut.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(variableOut))) {
            variables.forEach(writer::println);
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(registrationOut))) {
            registration.forEach(writer::println);
        }
        LOGGER.info("Output files generated in {}", CARD_ITEM_DIR.getAbsolutePath());
    }
}
