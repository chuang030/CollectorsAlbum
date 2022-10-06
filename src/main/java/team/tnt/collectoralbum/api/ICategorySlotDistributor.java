package team.tnt.collectoralbum.api;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;

public interface ICategorySlotDistributor {

    void distributeSlot(ISlotAppender<CardSlotDefinition> appender, int index, int cardIndexOffset);

    void addPlayerSlots(ISlotAppender<Slot> appender, PlayerInventory inventory);
}
