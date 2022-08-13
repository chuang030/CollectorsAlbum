package team.tnt.collectoralbum.api;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public interface ICategorySlotDistributor {

    void distributeSlot(ISlotAppender<CardSlotDefinition> appender, int index, int cardIndexOffset);

    void addPlayerSlots(ISlotAppender<Slot> appender, Inventory inventory);
}
