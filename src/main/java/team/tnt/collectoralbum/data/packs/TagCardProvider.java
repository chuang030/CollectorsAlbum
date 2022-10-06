package team.tnt.collectoralbum.data.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import team.tnt.collectoralbum.util.JsonHelper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TagCardProvider implements ICardDropProvider {

    private final ITag<Item> tag;

    private TagCardProvider(ITag<Item> tag) {
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
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "tag"));
            ITag<Item> tagKey = TagCollectionManager.getInstance().getItems().getTag(id);
            return new TagCardProvider(tagKey);
        }
    }
}
