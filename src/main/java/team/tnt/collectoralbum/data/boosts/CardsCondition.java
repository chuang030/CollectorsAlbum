package team.tnt.collectoralbum.data.boosts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.*;
import java.util.stream.Collectors;

public class CardsCondition implements ICardBoostCondition {

    private final ICardCategory category;
    private final CardRarity rarity;
    private final int count;

    private CardsCondition(ICardCategory category, CardRarity rarity, int count) {
        this.category = category;
        this.rarity = rarity;
        this.count = count;
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
