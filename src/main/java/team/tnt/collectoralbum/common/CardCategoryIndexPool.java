package team.tnt.collectoralbum.common;

import team.tnt.collectoralbum.common.init.CardCategoryRegistry;

public class CardCategoryIndexPool {

    private static int index;

    public static int assignUniqueIndex() {
        return index++;
    }

    public static int getIndexOffset(ICardCategory category) {
        return CardCategoryRegistry.getValues()
                .stream()
                .filter(c -> c.getIndex() < category.getIndex())
                .mapToInt(ICardCategory::getCapacity)
                .sum();
    }
}
