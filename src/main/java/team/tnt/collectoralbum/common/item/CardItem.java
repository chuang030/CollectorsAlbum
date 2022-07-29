package team.tnt.collectoralbum.common.item;

import net.minecraft.world.item.Item;
import team.tnt.collectoralbum.common.CardDefinition;

public class CardItem extends Item implements ICard {

    private final CardDefinition card;
    private final CardRarity rarity;

    public CardItem(CardRarity rarity, CardDefinition card) {
        super(new Properties().stacksTo(1));
        this.card = card;
        this.rarity = rarity;
    }

    @Override
    public CardDefinition getCard() {
        return card;
    }

    @Override
    public CardRarity getCardRarity() {
        return rarity;
    }
}
