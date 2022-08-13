package team.tnt.collectoralbum.common.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AlbumContainer extends SimpleContainer {
    private final Map<ICardCategory, SimpleContainer> inventoriesByCategory = new HashMap<>();

    public AlbumContainer(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag inventories = tag.getCompound("inventories");
        CardCategoryRegistry.getValues().forEach(category -> {
            SimpleContainer container = new SimpleContainer(30);
            container.addListener(ref -> this.setChanged());
            inventoriesByCategory.put(category, container);
        });
        for (ICardCategory category : CardCategoryRegistry.getValues()) {
            ListTag slots = inventories.getList(category.getId().toString(), Tag.TAG_COMPOUND);
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

    public SimpleContainer forCategory(ICardCategory category) {
        return inventoriesByCategory.get(category);
    }

    public int getCategoryIndexOffset(ICardCategory category) {
        return CardCategoryIndexPool.getIndexOffset(category);
    }

    public AlbumStats getStats() {
        return new AlbumStats(this);
    }

    private record Listener(ItemStack itemRef,
                            Function<ICardCategory, SimpleContainer> containerFetcher) implements ContainerListener {

        @Override
        public void containerChanged(Container invBasic) {
            CompoundTag tag = itemRef.getOrCreateTag();
            CompoundTag inventoryTag = new CompoundTag();
            for (ICardCategory category : CardCategoryRegistry.getValues()) {
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
                inventoryTag.put(category.getId().toString(), listTag);
            }
            tag.put("inventories", inventoryTag);
            itemRef.setTag(tag);
        }
    }
}
