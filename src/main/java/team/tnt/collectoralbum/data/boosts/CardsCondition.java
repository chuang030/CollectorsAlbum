package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.ICardCategory;
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
    private final Component[] description;

    private CardsCondition(ICardCategory category, CardRarity rarity, int count) {
        this.category = category;
        this.rarity = rarity;
        this.count = count;
        this.description = new Component[] { this.createFullDescription() };
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
    public Component[] getDescription() {
        return description;
    }

    private Component createFullDescription() {
        Component wildcard = new TranslatableComponent("text.collectorsalbum.album.boost.condition.cards.wildcard");
        Component count = new TextComponent(String.valueOf(this.count)).withStyle(ChatFormatting.AQUA);
        Component categoryText = category != null ?
                new TextComponent(category.getTranslatedName().getString()).withStyle(category.getTooltipFormat()) :
                wildcard;
        Component rarityText = rarity != null ?
                new TextComponent(TextHelper.splitAndCapitalizeFirstWords(rarity.name(), "_+")).withStyle(rarity.getColor()):
                wildcard;
        return new TranslatableComponent("text.collectorsalbum.album.boost.condition.cards", count, rarityText, categoryText).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public int compareTo(@NotNull IDescriptionProvider o) {
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
            int requiredCount = GsonHelper.getAsInt(object, "count");
            return new CardsCondition(category, rarity, requiredCount);
        }
    }
}
