package team.tnt.collectoralbum.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.menu.AlbumMenu;

public class AlbumScreen extends AbstractContainerScreen<AlbumMenu> {

    private static final ResourceLocation TITLE = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/album_title.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/album.png");
    private static final ResourceLocation ARROW_LEFT = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/album_previous.png");
    private static final ResourceLocation ARROW_RIGHT = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/album_next.png");
    private int pageIndex;

    public AlbumScreen(AlbumMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 306;
        this.imageHeight = 257;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ArrowWidget(leftPos + 18, topPos + 5, 16, 16, ARROW_LEFT));
        addRenderableWidget(new ArrowWidget(leftPos + 265, topPos + 4, 16, 16, ARROW_RIGHT));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, this.menu.isTitle() ? TITLE : BACKGROUND);
        Matrix4f pose = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, leftPos, topPos, 0).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos, topPos + imageHeight, 0).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos + imageWidth, topPos + imageHeight, 0).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos + imageWidth, topPos, 0).uv(1.0F, 0.0F).endVertex();
        tesselator.end();
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        int page = (pageIndex * 2) + 1;
        String page1 = String.valueOf(page);
        String page2 = String.valueOf(page + 1);
        font.draw(poseStack, page1, 26 - font.width(page1) / 2.0f, 157, 0);
        font.draw(poseStack, page2, 277 - font.width(page2) / 2.0f, 157, 0);
        if (this.menu.isTitle()) return;
        for (Slot slot : this.menu.slots) {
            if (slot instanceof AlbumMenu.CardSlot cardSlot) {
                int cardNumber = cardSlot.getCardNumber();
                String text = "#" + cardNumber;
                font.draw(poseStack, text, slot.x + (18 - font.width(text)) / 2.0F - 1, slot.y + 18, 0x7C5D4D);
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    private static final class ArrowWidget extends AbstractWidget {

        private final ResourceLocation location;

        public ArrowWidget(int x, int y, int width, int height, ResourceLocation location) {
            super(x, y, width, height, TextComponent.EMPTY);
            this.location = location;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, location);
            Matrix4f pose = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(pose, x, y, 0).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.vertex(pose, x, y + width, 0).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.vertex(pose, x + height, y + width, 0).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.vertex(pose, x + height, y, 0).uv(1.0F, 0.0F).endVertex();
            tesselator.end();
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}
