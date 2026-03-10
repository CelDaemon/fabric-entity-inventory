package net.voidgroup.proto.entityinventory.client;

import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.TestEntityMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TestEntityScreen extends AbstractContainerScreen<TestEntityMenu> {
    private static final Identifier CONTAINER_LOCATION = EntityInventory.id("textures/gui/container/test_entity.png");
    public TestEntityScreen(TestEntityMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        titleLabelX = (imageWidth - font.width(this.title)) / 2;
        inventoryLabelX = imageWidth - font.width(playerInventoryTitle) - 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int cursorX, int cursorY) {
        int originX = (width - imageWidth) / 2;
        int originY = (height - imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, originX, originY, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int cursorX, int cursorY, float f) {
        super.render(guiGraphics, cursorX, cursorY, f);
        renderTooltip(guiGraphics, cursorX, cursorY);
    }
}
