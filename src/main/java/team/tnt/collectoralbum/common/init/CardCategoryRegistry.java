package team.tnt.collectoralbum.common.init;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.CardCategory;
import team.tnt.collectoralbum.common.ICardCategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CardCategoryRegistry {

    private static final Map<ResourceLocation, ICardCategory> REGISTRY = new HashMap<>();

    public static final ICardCategory TOOLS = internalRegister("tools", ChatFormatting.YELLOW);
    public static final ICardCategory ARMOR = internalRegister("armor", ChatFormatting.BLUE);
    public static final ICardCategory MOBS = internalRegister("mobs", ChatFormatting.RED);
    public static final ICardCategory NATURE = internalRegister("nature", ChatFormatting.GREEN);
    public static final ICardCategory ITEMS = internalRegister("items", ChatFormatting.WHITE);

    public static void register(ICardCategory category) {
        if (REGISTRY.put(category.getId(), category) != null) {
            throw new IllegalStateException("Duplicate card category: " + category.getId());
        }
    }

    public static ICardCategory getByKey(ResourceLocation key) {
        return REGISTRY.get(key);
    }

    public static Collection<ICardCategory> getValues() {
        return REGISTRY.values();
    }

    public static int getCount() {
        return REGISTRY.size();
    }

    public static ICardCategory byIndex(int index) {
        return REGISTRY.values().stream().filter(cat -> cat.getIndex() == index).findFirst().orElse(null);
    }

    private static ICardCategory internalRegister(String id, ChatFormatting formatting) {
        ICardCategory category = new CardCategory(new ResourceLocation(CollectorsAlbum.MODID, id), formatting);
        register(category);
        return category;
    }
}
