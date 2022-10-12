package team.tnt.collectoralbum.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.config.ModConfig;
import team.tnt.collectoralbum.util.math.WeightedRandom;

import java.util.function.Supplier;

public final class MobDrops implements Supplier<Item> {

    private static final MobDrops SINGLETON = new MobDrops();

    private static final WeightedRandom<DropEntry> ENTRIES = WeightedRandom.Builder.<DropEntry>create()
            .provider(DropEntry::weight)
            .append(new DropEntry(() -> Items.AIR, ModConfig.INSTANCE.mobDrops.noDropWeight.get()))
            .append(new DropEntry(ItemRegistry.COMMON_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.commonDropWeight.get()))
            .append(new DropEntry(ItemRegistry.UNCOMMON_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.uncommonDropWeight.get()))
            .append(new DropEntry(ItemRegistry.RARE_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.rareDropWeight.get()))
            .append(new DropEntry(ItemRegistry.EPIC_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.epicDropWeight.get()))
            .append(new DropEntry(ItemRegistry.LEGENDARY_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.legendaryDropWeight.get()))
            .append(new DropEntry(ItemRegistry.MYTHICAL_CARD_PACKAGE, ModConfig.INSTANCE.mobDrops.mythicalDropWeight.get()))
            .build(DropEntry[]::new);

    public static MobDrops instance() {
        return SINGLETON;
    }

    @Override
    public Item get() {
        DropEntry randomEntry = ENTRIES.get();
        Supplier<? extends Item> itemReferenceHolder = randomEntry.itemRef();
        return itemReferenceHolder.get();
    }

    private record DropEntry(Supplier<? extends Item> itemRef, int weight) {

    }
}
