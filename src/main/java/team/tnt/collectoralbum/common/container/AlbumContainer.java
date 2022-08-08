package team.tnt.collectoralbum.common.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.item.CardCategory;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class AlbumContainer extends SimpleContainer {
    private final Map<CardCategory, SimpleContainer> inventoriesByCategory = new EnumMap<>(CardCategory.class);

    public AlbumContainer(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag inventories = tag.getCompound("inventories");
        Arrays.stream(CardCategory.values()).forEach(category -> {
            SimpleContainer container = new SimpleContainer(30);
            container.addListener(ref -> this.setChanged());
            inventoriesByCategory.put(category, container);
        });
        for (CardCategory category : CardCategory.values()) {
            ListTag slots = inventories.getList(category.name(), Tag.TAG_COMPOUND);
            for (int i = 0; i < slots.size(); i++) {
                CompoundTag slotDef = slots.getCompound(i);
                int slotIndex = slotDef.getInt("slotIndex");
                CompoundTag itemTag = slotDef.getCompound("itemStack");
                ItemStack item = ItemStack.of(itemTag);
                inventoriesByCategory.get(category).setItem(slotIndex, item);
            }
        }
        this.addListener(new Listener(stack, inventoriesByCategory::get));
    }

    public SimpleContainer forCategory(CardCategory category) {
        return inventoriesByCategory.get(category);
    }

    public int getCategoryIndexOffset(CardCategory category) {
        return category.ordinal() * 30;
    }

    public AlbumStats getStats() {
        return new AlbumStats(this);
    }

    private record Listener(ItemStack itemRef, Function<CardCategory, SimpleContainer> containerFetcher) implements ContainerListener {

        @Override
        public void containerChanged(Container invBasic) {
            CompoundTag tag = itemRef.getOrCreateTag();
            CompoundTag inventoryTag = new CompoundTag();
            for (CardCategory category : CardCategory.values()) {
                ListTag listTag = new ListTag();
                SimpleContainer container = containerFetcher.apply(category);
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack stack = container.getItem(i);
                    if (stack.isEmpty()) continue;
                    CompoundTag slotDef = new CompoundTag();
                    slotDef.putInt("slotIndex", i);
                    slotDef.put("itemStack", stack.save(new CompoundTag()));
                    listTag.add(slotDef);
                }
                inventoryTag.put(category.name(), listTag);
            }
            tag.put("inventories", inventoryTag);
            itemRef.setTag(tag);
        }
    }
}
