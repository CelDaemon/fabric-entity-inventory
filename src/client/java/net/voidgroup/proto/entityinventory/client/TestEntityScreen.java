package net.voidgroup.proto.entityinventory.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.TestEntityMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TestEntityScreen extends AbstractContainerScreen<TestEntityMenu> {
    private static final Identifier CONTAINER_LOCATION = EntityInventory.id("textures/gui/container/test_entity.png");
    public TestEntityScreen(TestEntityMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(this.title)) / 2;
        inventoryLabelX = imageWidth - font.width(playerInventoryTitle) - 8;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        final var originX = (width - imageWidth) / 2;
        final var originY = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, originX, originY, 0.0F, 0.0F, imageWidth, imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
