package team.tnt.collectoralbum.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;

import java.util.List;

public class CardOpenScreen extends Screen {

    private static final ResourceLocation CARD_BACK = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/card_back.png");
    private final List<ItemStack> drops;

    public CardOpenScreen(List<ItemStack> drops) {
        super(new TextComponent("Card Open Screen"));
        this.drops = drops;
    }

    @Override
    protected void init() {
        // TODO add components
    }

    @Override
    public void onClose() {
        super.onClose();
        // TODO give drops to player
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
