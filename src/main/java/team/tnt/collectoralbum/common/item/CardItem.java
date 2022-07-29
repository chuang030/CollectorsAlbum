package team.tnt.collectoralbum.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.CardDefinition;

import java.util.List;

public class CardItem extends Item implements ICard {

    private static final String TEXT_CARD_NUMBER = "card.tooltip.number";
    private static final String TEXT_CARD_RARITY = "card.tooltip.rarity";
    private static final String TEXT_CARD_CATEGORY = "card.tooltip.category";
    private static final String TEXT_CARD_VALUE = "card.tooltip.value";

    private final CardDefinition card;
    private final CardRarity rarity;

    public CardItem(CardRarity rarity, CardDefinition card) {
        super(new Properties().stacksTo(1).tab(CollectorsAlbum.TAB));
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        CardItem cardItem = (CardItem) stack.getItem();
        CardDefinition definition = cardItem.getCard();
        Component numberComponent = new TextComponent("#" + definition.cardNumber()).withStyle(ChatFormatting.YELLOW);
        Component categoryComponent = new TextComponent(definition.category().name()).withStyle(definition.category().getColor());
        Component rarityComponent = new TextComponent(rarity.name()).withStyle(rarity.getColor());
        Component valueComponent = new TextComponent(rarity.getValue() + " pts").withStyle(ChatFormatting.WHITE);
        tooltipComponents.add(new TranslatableComponent(TEXT_CARD_NUMBER, numberComponent).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(new TranslatableComponent(TEXT_CARD_CATEGORY, categoryComponent).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(new TranslatableComponent(TEXT_CARD_RARITY, rarityComponent).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(new TranslatableComponent(TEXT_CARD_VALUE, valueComponent).withStyle(ChatFormatting.GRAY));
    }
}
