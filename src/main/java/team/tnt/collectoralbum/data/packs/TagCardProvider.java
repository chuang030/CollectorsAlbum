package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.tnt.collectoralbum.util.JsonHelper;
import team.tnt.collectoralbum.util.PlayerHelper;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TagCardProvider implements ICardDropProvider {

    private final TagKey<Item> tag;

    private TagCardProvider(TagKey<Item> tag) {
        this.tag = tag;
    }

    @Override
    public void provideDrops(Player player, Level level) {
        Random random = player.getRandom();
        Optional<HolderSet.Named<Item>> items = Registry.ITEM.getTag(tag);
        items.ifPresent(namedHolder -> {
            Either<TagKey<Item>, List<Holder<Item>>> either = namedHolder.unwrap();
            either.right().ifPresent(list -> {
                Holder<Item> itemHolder = list.get(random.nextInt(list.size()));
                ItemStack itemStack = new ItemStack(itemHolder);
                PlayerHelper.giveItem(player, itemStack);
            });
        });
    }

    public static class Serializer implements ICardDropSerializer<TagCardProvider> {

        @Override
        public TagCardProvider fromJson(JsonElement data) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(data);
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "tag"));
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, id);
            Optional<HolderSet.Named<Item>> optional = Registry.ITEM.getTag(tagKey);
            if (optional.isEmpty()) {
                throw new JsonSyntaxException("Unknown tag: " + id);
            }
            return new TagCardProvider(tagKey);
        }
    }
}
