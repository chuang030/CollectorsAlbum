package team.tnt.collectoralbum.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.CardDefinition;
import team.tnt.collectoralbum.integration.IntegrationsHelper;

import javax.annotation.Nullable;
import java.util.List;

public class CardItem extends Item implements ICard {

    private static final String TEXT_CARD_NUMBER = "card.tooltip.number";
    private static final String TEXT_CARD_RARITY = "card.tooltip.rarity";
    private static final String TEXT_CARD_CATEGORY = "card.tooltip.category";
    private static final String TEXT_CARD_VALUE = "card.tooltip.value";

    private final CardDefinition card;
    private final CardRarity rarity;
    private final int cardNumber;

    public CardItem(CardRarity rarity, CardDefinition card) {
        super(new Properties().tab(CollectorsAlbum.TAB));
        this.card = card;
        this.rarity = rarity;
        this.cardNumber = CardCategoryIndexPool.getIndexOffset(card.category()) + card.cardNumber();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return IntegrationsHelper.getCardCapabilityProvider(stack, nbt);
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
    public void appendHoverText(ItemStack stack, World level, List<ITextComponent> tooltipComponents, ITooltipFlag flag) {
        CardItem cardItem = (CardItem) stack.getItem();
        CardDefinition definition = cardItem.getCard();
        ITextComponent numberComponent = new StringTextComponent("#" + cardNumber).withStyle(TextFormatting.YELLOW);
        ITextComponent categoryComponent = new StringTextComponent(definition.category().getTranslatedName().getString()).withStyle(definition.category().getTooltipFormat());
        ITextComponent rarityComponent = new StringTextComponent(rarity.name()).withStyle(rarity.getColor());
        ITextComponent valueComponent = new StringTextComponent(rarity.getValue() + " pts").withStyle(TextFormatting.WHITE);
        tooltipComponents.add(new TranslationTextComponent(TEXT_CARD_NUMBER, numberComponent).withStyle(TextFormatting.GRAY));
        tooltipComponents.add(new TranslationTextComponent(TEXT_CARD_CATEGORY, categoryComponent).withStyle(TextFormatting.GRAY));
        tooltipComponents.add(new TranslationTextComponent(TEXT_CARD_RARITY, rarityComponent).withStyle(TextFormatting.GRAY));
        tooltipComponents.add(new TranslationTextComponent(TEXT_CARD_VALUE, valueComponent).withStyle(TextFormatting.GRAY));
    }
}
