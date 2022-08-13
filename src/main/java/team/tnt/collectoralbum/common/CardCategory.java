package team.tnt.collectoralbum.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import team.tnt.collectoralbum.api.CardSlotDefinition;
import team.tnt.collectoralbum.api.ICategorySlotDistributor;
import team.tnt.collectoralbum.api.ISlotAppender;

public class CardCategory implements ICardCategory {

    public static final ICategorySlotDistributor DISTRIBUTOR = new SlotDistributor();
    private final int categoryIndex = CardCategoryIndexPool.assignUniqueIndex();
    private final ResourceLocation id;
    private final ChatFormatting formatting;
    private final Component translatedName;

    public CardCategory(ResourceLocation id, ChatFormatting formatting) {
        this.id = id;
        this.formatting = formatting;
        String identifier = id.toString().replaceAll(":", ".");
        this.translatedName = new TranslatableComponent("card.category." + identifier);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public int getIndex() {
        return categoryIndex;
    }

    @Override
    public int getCapacity() {
        return 30;
    }

    @Override
    public ICategorySlotDistributor getMenuSlotDistributor() {
        return DISTRIBUTOR;
    }

    @Override
    public ChatFormatting getTooltipFormat() {
        return formatting;
    }

    @Override
    public Component getTranslatedName() {
        return translatedName;
    }

    private static final class SlotDistributor implements ICategorySlotDistributor {

        @Override
        public void distributeSlot(ISlotAppender<CardSlotDefinition> appender, int index, int cardIndexOffset) {
            int cardNumber = cardIndexOffset + index;
            int slotX = index < 15 ? 31 + (index % 3) * 41 : 170 + ((index - 15) % 3) * 41;
            int slotY = 21 + ((index % 15) / 3) * 29;
            appender.appendSlot(new CardSlotDefinition(index, slotX, slotY, cardNumber));
        }

        @Override
        public void addPlayerSlots(ISlotAppender<Slot> appender, Inventory inventory) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    int slotId = x + (y * 9) + 9;
                    appender.appendSlot(new Slot(inventory, slotId, 73 + x * 18, 176 + y * 18));
                }
            }
            for (int x = 0; x < 9; x++) {
                appender.appendSlot(new Slot(inventory, x, 73 + x * 18, 234));
            }
        }
    }
}
