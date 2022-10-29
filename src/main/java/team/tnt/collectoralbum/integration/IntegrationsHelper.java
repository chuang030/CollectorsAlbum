package team.tnt.collectoralbum.integration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public final class IntegrationsHelper {

    public static final String MOD_QUARK = "quark";

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static ICapabilityProvider getCardCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt) {
        return isModLoaded(MOD_QUARK) ? QuarkIntegration.getCardsCapabilityProvider(stack, nbt) : null;
    }
}
