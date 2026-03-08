package net.voidgroup.proto.entityinventory.client;

import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.TestEntityMenu;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TestEntityScreen extends HandledScreen<TestEntityMenu> {
    private static final Identifier CONTAINER_LOCATION = EntityInventory.id("textures/gui/container/test_entity.png");
    public TestEntityScreen(TestEntityMenu menu, PlayerInventory inventory, Text title) {
        super(menu, inventory, title);
        titleX = (backgroundWidth - textRenderer.getWidth(this.title)) / 2;
        playerInventoryTitleX = backgroundWidth - textRenderer.getWidth(playerInventoryTitle) - 8;
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float f, int cursorX, int cursorY) {
        int centerX = (width - backgroundWidth) / 2;
        int centerY = (height - backgroundHeight) / 2;
        guiGraphics.drawTexture(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, centerX, centerY, 0.0F, 0.0F, backgroundWidth, backgroundHeight, field_52802, field_52803);
    }

    @Override
    public void render(DrawContext guiGraphics, int cursorX, int cursorY, float f) {
        super.render(guiGraphics, cursorX, cursorY, f);
        drawMouseoverTooltip(guiGraphics, cursorX, cursorY);
    }
}
