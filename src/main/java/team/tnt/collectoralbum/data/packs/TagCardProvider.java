package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TagCardProvider implements ICardDropProvider {

    private final Tag<Item> tag;

    private TagCardProvider(Tag<Item> tag) {
        this.tag = tag;
    }

    @Override
    public List<ItemStack> provideDrops() {
        Random random = new Random();
        List<Item> items = tag.getValues();
        Item item = items.get(random.nextInt(items.size()));
        ItemStack itemStack = new ItemStack(item);
        return Collections.singletonList(itemStack);
    }

    public static class Serializer implements ICardDropSerializer<TagCardProvider> {

        @Override
        public TagCardProvider fromJson(JsonElement data) throws JsonParseException {
            JsonObject object = JsonHelper.asObject(data);
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "tag"));
            Tag<Item> tagKey = SerializationTags.getInstance().getItems().getTag(id);
            return new TagCardProvider(tagKey);
        }
    }
}
