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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.network.Networking;
import team.tnt.collectoralbum.network.packet.RequestCardPackDropPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CardOpenScreen extends Screen {

    private static final ResourceLocation CARD_BACK = new ResourceLocation(CollectorsAlbum.MODID, "textures/screen/card_back.png");
    private final List<ITickableWidget> tickableWidgets = new ArrayList<>();
    private final List<ItemStack> drops;
    private int flipsRemaining;

    public CardOpenScreen(List<ItemStack> drops) {
        super(new TextComponent("Card Open Screen"));
        this.drops = drops;
        this.flipsRemaining = drops.size();
    }

    @Override
    protected void init() {
        tickableWidgets.clear();
        int total = drops.size();
        int deckWidth = (total - 1) * 65 + 64;
        int left = (width - deckWidth) / 2;
        int top = (height - 64) / 2;
        for (int i = 0; i < total; i++) {
            int desiredX = left + i * 65;
            CardWidget widget = addRenderableWidget(new CardWidget(width - 70 + i * 7, height - 70, 64, 64, desiredX, top, drops.get(i)));
            widget.setAnimTime(40);
            widget.setClickResponder(this::cardFlipped);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (flipsRemaining == 0) {
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

    private void cardFlipped(CardWidget widget) {
        --flipsRemaining;
    }

    private static final class CardWidget extends AbstractWidget implements ITickableWidget {

        private final int targetX;
        private final int targetY;
        private final int startX;
        private final int startY;
        private final ResourceLocation itemTexture;
        private int animTime = 30;
        private int timer, prevTimer;
        private Consumer<CardWidget> widgetConsumer = widget -> {};
        private boolean flipped;

        public CardWidget(int x, int y, int width, int height, int targetX, int targetY, ItemStack stack) {
            super(x, y, width, height, TextComponent.EMPTY);
            this.startX = x;
            this.startY = y;
            this.targetX = targetX;
            this.targetY = targetY;
            ResourceLocation itemId = Registry.ITEM.getKey(stack.getItem());
            this.itemTexture = new ResourceLocation(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
        }

        public void setAnimTime(int time) {
            this.animTime = time;
        }

        public void setClickResponder(Consumer<CardWidget> widgetConsumer) {
            this.widgetConsumer = widgetConsumer;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            widgetConsumer.accept(this);
            flipped = true;
        }

        @Override
        protected boolean clicked(double mouseX, double mouseY) {
            return !flipped && super.clicked(mouseX, mouseY);
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
            int scale = isHovered ? 8 : 0;
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
            RenderSystem.setShaderTexture(0, flipped ? itemTexture : CARD_BACK);
            renderTexture(poseStack.last().pose(), renderX - scale, renderY - scale, renderX + width + scale, renderY + height + scale);
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
