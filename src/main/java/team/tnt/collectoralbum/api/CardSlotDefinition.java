package team.tnt.collectoralbum.api;

public class CardSlotDefinition {

    private final int slotIndex;
    private final int slotX;
    private final int slotY;
    private final int cardNumber;

    public CardSlotDefinition(int slotIndex, int slotX, int slotY, int cardNumber) {
        this.slotIndex = slotIndex;
        this.slotX = slotX;
        this.slotY = slotY;
        this.cardNumber = cardNumber;
    }

    public int slotIndex() {
        return slotIndex;
    }

    public int slotX() {
        return slotX;
    }

    public int slotY() {
        return slotY;
    }

    public int cardNumber() {
        return cardNumber;
    }
}
