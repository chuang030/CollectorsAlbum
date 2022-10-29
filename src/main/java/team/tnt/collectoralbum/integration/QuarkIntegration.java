package team.tnt.collectoralbum.integration;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import team.tnt.collectoralbum.common.CardCategoryIndexPool;
import team.tnt.collectoralbum.common.CardDefinition;
import team.tnt.collectoralbum.common.item.CardRarity;
import team.tnt.collectoralbum.common.item.ICard;
import vazkii.quark.api.ICustomSorting;
import vazkii.quark.api.QuarkCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public final class QuarkIntegration {

    public static final ICustomSorting CARD_SORTING = new CardSorter();

    public static ICapabilityProvider getCardsCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt) {
        return CustomSortProvider.INSTANCE;
    }

    private static class CustomSortProvider implements ICapabilityProvider {

        private static final CustomSortProvider INSTANCE = new CustomSortProvider();
        private final LazyOptional<ICustomSorting> capability = LazyOptional.of(() -> CARD_SORTING);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == QuarkCapabilities.SORTING ? capability.cast() : LazyOptional.empty();
        }
    }

    private static class CardSorter implements ICustomSorting {

        private static final String CATEGORY = "collectorsalbum:cards";

        @Override
        public String getSortingCategory() {
            return CATEGORY;
        }

        @Override
        public Comparator<ItemStack> getItemComparator() {
            return (i1, i2) -> {
                if (!(i1.getItem() instanceof ICard) || !(i2.getItem() instanceof ICard)) {
                    return 0;
                }
                ICard card1 = (ICard) i1.getItem();
                ICard card2 = (ICard) i2.getItem();
                CardDefinition def1 = card1.getCard();
                CardDefinition def2 = card2.getCard();
                int a = CardCategoryIndexPool.getIndexOffset(def1.category()) + def1.cardNumber();
                int b = CardCategoryIndexPool.getIndexOffset(def2.category()) + def2.cardNumber();
                if (a == b) {
                    CardRarity rar1 = card1.getCardRarity();
                    CardRarity rar2 = card2.getCardRarity();
                    return rar2.ordinal() - rar1.ordinal();
                }
                return a - b;
            };
        }
    }
}
