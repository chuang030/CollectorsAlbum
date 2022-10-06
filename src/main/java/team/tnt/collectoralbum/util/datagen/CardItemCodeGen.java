package team.tnt.collectoralbum.util.datagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardItemCodeGen {

    private static final File CARD_ITEM_DIR = new File("./run/exported/cards");

    public static void main(String[] args) {
        try {
            new CardItemCodeGen().run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void run() throws Exception {
        File file = new File("./src/main/java/team/tnt/collectoralbum/common/init/ItemRegistry.java");
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        String fileContent = builder.toString();
        String replaced = fileContent.replaceAll("public static final (\\w+) (\\w+) = (new \\w+\\(.+\\));", "public static final RegistryObject<$1> $2 = REGISTRY.register(\"$2\", () -> $3);");
        Pattern pattern = Pattern.compile("(\"\\w+\")");
        Matcher matcher = pattern.matcher(replaced);
        while (matcher.find()) {
            String id = matcher.group();
            String lowercase = id.toLowerCase();
            replaced = replaced.replaceAll(id, lowercase);
        }
        try (FileWriter writer = new FileWriter(new File(CARD_ITEM_DIR, "item_registry.txt"))) {
            writer.write(replaced);
        }
    }
}
