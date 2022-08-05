package team.tnt.collectoralbum.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.packet.RequestCardPackDropPacket;

import java.util.ArrayList;
import java.util.List;

public class CardOpenScreen extends Screen {

    private static final ResourceLocation CARD_BACK = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/card_back.png");
    private final List<ITickableWidget> tickableWidgets = new ArrayList<>();
    private final List<ItemStack> drops;
    private boolean openedAll;

    public CardOpenScreen(List<ItemStack> drops) {
        super(new TextComponent("Card Open Screen"));
        this.drops = drops;
    }

    @Override
    protected void init() {
        tickableWidgets.clear();
        int total = drops.size();
        int left = (width - total * 70) / 2;
        int top = (height - 64) / 2;
        for (int i = 0; i < total; i++) {
            int desiredX = left + i * 70;
            CardWidget widget = addRenderableWidget(new CardWidget(width - 70 - i * 7, height - 70, 64, 64, desiredX, top));
            widget.setAnimTime(100);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (openedAll) {
            Networking.dispatchServerPacket(new RequestCardPackDropPacket());
        }
    }

    @Override
    public void tick() {
        tickableWidgets.forEach(ITickableWidget::tickWidget);
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

    @Override
    protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget) {
        if (widget instanceof ITickableWidget tickable) {
            tickableWidgets.add(tickable);
        }
        return super.addRenderableWidget(widget);
    }

    private static final class CardWidget extends AbstractWidget implements ITickableWidget {

        private final int targetX;
        private final int targetY;
        private final int startX;
        private final int startY;
        private int animTime = 30;
        private int timer, prevTimer;

        public CardWidget(int x, int y, int width, int height, int targetX, int targetY) {
            super(x, y, width, height, TextComponent.EMPTY);
            this.startX = x;
            this.startY = y;
            this.targetX = targetX;
            this.targetY = targetY;
        }

        public void setAnimTime(int time) {
            this.animTime = time;
        }

        @Override
        public void tickWidget() {
            prevTimer = timer;
            if (timer >= animTime) {
                return;
            }
            ++timer;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            float actual = timer / (float) animTime;
            float old = prevTimer / (float) animTime;
            float rawProgress = Mth.lerp(partialTick, actual, old);
            float progress = 1.0F - (float) Math.pow(1 - rawProgress, 3);
            int diffX = targetX - startX;
            int diffY = targetY - startY;
            float renderX = startX + (diffX * progress);
            float renderY = startY + (diffY * progress);
            this.x = (int) renderX;
            this.y = (int) renderY;
            Lighting.setupForFlatItems();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, CARD_BACK);
            renderTexture(poseStack.last().pose(), renderX, renderY, renderX + width, renderY + height);
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
        }
    }

    public static void renderTexture(Matrix4f pose, float x1, float y1, float x2, float y2) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, x1, y1, 0).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(pose, x1, y2, 0).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, x2, y2, 0).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, x2, y1, 0).uv(1.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
    }
}
