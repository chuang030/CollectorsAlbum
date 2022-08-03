package team.tnt.collectoralbum.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.init.ItemRegistry;
import team.tnt.collectoralbum.util.math.WeightedRandom;

import java.util.function.Supplier;

public final class MobDrops implements Supplier<Item> {

    private static final MobDrops SINGLETON = new MobDrops();

    private static final WeightedRandom<DropEntry> ENTRIES = WeightedRandom.Builder.<DropEntry>create()
            .provider(DropEntry::weight)
            .append(new DropEntry(() -> Items.AIR, CollectorsAlbum.config.mobDrops.noDropWeight))
            .append(new DropEntry(() -> ItemRegistry.COMMON_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.commonDropWeight))
            .append(new DropEntry(() -> ItemRegistry.UNCOMMON_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.uncommonDropWeight))
            .append(new DropEntry(() -> ItemRegistry.RARE_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.rareDropWeight))
            .append(new DropEntry(() -> ItemRegistry.EPIC_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.epicDropWeight))
            .append(new DropEntry(() -> ItemRegistry.LEGENDARY_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.legendaryDropWeight))
            .append(new DropEntry(() -> ItemRegistry.MYTHICAL_CARD_PACKAGE, CollectorsAlbum.config.mobDrops.mythicalDropWeight))
            .build(DropEntry[]::new);

    public static MobDrops instance() {
        return SINGLETON;
    }

    @Override
    public Item get() {
        DropEntry randomEntry = ENTRIES.get();
        Supplier<Item> itemReferenceHolder = randomEntry.itemRef();
        return itemReferenceHolder.get();
    }

    private record DropEntry(Supplier<Item> itemRef, int weight) {
    }
}
