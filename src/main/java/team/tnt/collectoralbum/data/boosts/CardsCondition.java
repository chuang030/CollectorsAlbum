package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.CardBoostConditionRegistry;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;
import team.tnt.collectoralbum.util.JsonHelper;
import team.tnt.collectoralbum.util.TextHelper;

import java.util.*;
import java.util.stream.Collectors;

public class CardsCondition implements ICardBoostCondition {

    private final ICardCategory category;
    private final CardRarity rarity;
    private final int count;
    private final ITextComponent[] description;

    private CardsCondition(ICardCategory category, CardRarity rarity, int count) {
        this.category = category;
        this.rarity = rarity;
        this.count = count;
        this.description = new ITextComponent[] { this.createFullDescription() };
    }

    @Override
    public CardBoostConditionType<?> getType() {
        return CardBoostConditionRegistry.CARDS;
    }

    @Override
    public boolean isValid(IBoostContext context) {
        AlbumStats stats = context.get(ActiveBoostContext.STATS, AlbumStats.class);
        Map<ICardCategory, List<ICard>> map = stats.getCardsByCategory();
        List<ICard> cardList;
        if (category == null) {
            cardList = new ArrayList<>();
            for (ICardCategory cardCategory : CardCategoryRegistry.getValues()) {
                List<ICard> list = map.get(cardCategory);
                if (list != null) {
                    cardList.addAll(list);
                }
            }
        } else {
            cardList = Optional.ofNullable(map.get(category)).orElse(Collections.emptyList());
        }
        if (rarity != null) {
            cardList = cardList.stream().filter(card -> card.getCardRarity() == rarity).collect(Collectors.toList());
        }
        return cardList.size() >= count;
    }

    @Override
    public ITextComponent[] getDescription() {
        return description;
    }

    private ITextComponent createFullDescription() {
        ITextComponent wildcard = new TranslationTextComponent("text.collectorsalbum.album.boost.condition.cards.wildcard");
        ITextComponent count = new StringTextComponent(String.valueOf(this.count)).withStyle(TextFormatting.AQUA);
        ITextComponent categoryText = category != null ?
                new StringTextComponent(category.getTranslatedName().getString()).withStyle(category.getTooltipFormat()) :
                wildcard;
        ITextComponent rarityText = rarity != null ?
                new StringTextComponent(TextHelper.splitAndCapitalizeFirstWords(rarity.name(), "_+")).withStyle(rarity.getColor()):
                wildcard;
        return new TranslationTextComponent("text.collectorsalbum.album.boost.condition.cards", count, rarityText, categoryText).withStyle(TextFormatting.GRAY);
    }

    @Override
    public int compareTo(IDescriptionProvider o) {
        if (o instanceof PointsCondition) {
            return 1;
        }
        if (o instanceof CardsCondition) {
            CardsCondition c = (CardsCondition) o;
            CardRarity oRarity = c.rarity;
            int oCount = c.count;
            if (oCount == count) {
                if (Objects.equals(rarity, oRarity)) {
                    if (category == null && Objects.equals(category, c.category)) {
                        return 0;
                    }
                    return category.getIndex() - c.category.getIndex();
                }
                return rarity == null || oRarity == null ? TextHelper.nullSortFirst(rarity, oRarity) : rarity.ordinal() - oRarity.ordinal();
            }
            return count - oCount;
        }
        return 0;
    }

    public static final class Serializer implements ICardBoostConditionSerializer<CardsCondition> {

        @Override
        public CardsCondition fromJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(element);
            String categoryId = JsonHelper.resolveNullable(object, "category", JsonElement::getAsString);
            String rarityId = JsonHelper.resolveNullable(object, "rarity", JsonElement::getAsString);
            CardRarity rarity = null;
            if (rarityId != null) {
                try {
                    rarity = CardRarity.valueOf(rarityId);
                } catch (IllegalArgumentException e) {
                    throw new JsonSyntaxException("Unknown card rarity: " + rarityId);
                }
            }
            ICardCategory category = null;
            if (categoryId != null) {
                category = CardCategoryRegistry.getByKey(new ResourceLocation(categoryId));
                if (category == null) {
                    throw new JsonSyntaxException("Unknown card category: " + categoryId);
                }
            }
            int requiredCount = JSONUtils.getAsInt(object, "count");
            return new CardsCondition(category, rarity, requiredCount);
        }

        @Override
        public void networkEncode(CardsCondition condition, PacketBuffer buffer) {
            ICardCategory cardCategory = condition.category;
            CardRarity rarity = condition.rarity;
            buffer.writeBoolean(cardCategory != null);
            if (cardCategory != null) {
                buffer.writeResourceLocation(cardCategory.getId());
            }
            buffer.writeBoolean(rarity != null);
            if (rarity != null) {
                buffer.writeEnum(rarity);
            }
            buffer.writeInt(condition.count);
        }

        @Override
        public CardsCondition networkDecode(CardBoostConditionType<CardsCondition> type, PacketBuffer buffer) {
            boolean hasCategory = buffer.readBoolean();
            ICardCategory cardCategory = hasCategory ? CardCategoryRegistry.getByKey(buffer.readResourceLocation()) : null;
            boolean hasRarity = buffer.readBoolean();
            CardRarity rarity = hasRarity ? buffer.readEnum(CardRarity.class) : null;
            int count = buffer.readInt();
            return new CardsCondition(cardCategory, rarity, count);
        }
    }
}
