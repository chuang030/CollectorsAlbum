package team.tnt.collectoralbum.common.item;

import net.minecraft.world.item.Item;

public class CardItem extends Item {

    private final CardCategory cardCategory;

    public CardItem(Properties properties, CardCategory cardCategory) {
        super(properties);
        this.cardCategory = cardCategory;
    }
}
