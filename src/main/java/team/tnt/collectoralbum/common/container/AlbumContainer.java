package team.tnt.collectoralbum.common.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import team.tnt.collectoralbum.common.AlbumStats;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.ICardCategory;
import team.tnt.collectoralbum.common.init.CardCategoryRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AlbumContainer extends Inventory {
    private final Map<ICardCategory, Inventory> inventoriesByCategory = new HashMap<>();

    public AlbumContainer(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT inventories = tag.getCompound("inventories");
        CardCategoryRegistry.getValues().forEach(category -> {
            Inventory container = new Inventory(category.getCapacity());
            container.addListener(ref -> this.setChanged());
            inventoriesByCategory.put(category, container);
        });
        for (ICardCategory category : CardCategoryRegistry.getValues()) {
            ListNBT slots = inventories.getList(category.getId().toString(), Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < slots.size(); i++) {
                CompoundNBT slotDef = slots.getCompound(i);
                int slotIndex = slotDef.getInt("slotIndex");
                CompoundNBT itemTag = slotDef.getCompound("itemStack");
                ItemStack item = ItemStack.of(itemTag);
                inventoriesByCategory.get(category).setItem(slotIndex, item);
            }
        }
        this.addListener(new Listener(stack, inventoriesByCategory::get));
    }

    public Inventory forCategory(ICardCategory category) {
        return inventoriesByCategory.get(category);
    }

    public int getCategoryIndexOffset(ICardCategory category) {
        return CardCategoryIndexPool.getIndexOffset(category);
    }

    public AlbumStats getStats() {
        return new AlbumStats(this);
    }

    private static class Listener implements IInventoryChangedListener {

        private final ItemStack itemRef;
        private final Function<ICardCategory, Inventory> containerFetcher;

        public Listener(ItemStack itemRef, Function<ICardCategory, Inventory> containerFetcher) {
            this.itemRef = itemRef;
            this.containerFetcher = containerFetcher;
        }

        @Override
        public void containerChanged(IInventory invBasic) {
            CompoundNBT tag = itemRef.getOrCreateTag();
            CompoundNBT inventoryTag = new CompoundNBT();
            for (ICardCategory category : CardCategoryRegistry.getValues()) {
                ListNBT listTag = new ListNBT();
                Inventory container = containerFetcher.apply(category);
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack stack = container.getItem(i);
                    if (stack.isEmpty()) continue;
                    CompoundNBT slotDef = new CompoundNBT();
                    slotDef.putInt("slotIndex", i);
                    slotDef.put("itemStack", stack.save(new CompoundNBT()));
                    listTag.add(slotDef);
                }
                inventoryTag.put(category.getId().toString(), listTag);
            }
            tag.put("inventories", inventoryTag);
            itemRef.setTag(tag);
        }
    }
}
