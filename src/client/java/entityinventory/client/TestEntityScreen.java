package entityinventory.client;

import entityinventory.EntityInventory;
import entityinventory.TestEntityMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TestEntityScreen extends AbstractContainerScreen<TestEntityMenu> {
    private static final Identifier CONTAINER_LOCATION = Identifier.fromNamespaceAndPath(EntityInventory.MOD_ID,"textures/gui/container/test_entity.png");
    public TestEntityScreen(TestEntityMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        titleLabelX = (imageWidth - font.width(title)) / 2;
        inventoryLabelX = imageWidth - font.width(playerInventoryTitle) - 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int centerX = (this.width - this.imageWidth) / 2;
        int centerY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, centerX, centerY, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        renderTooltip(guiGraphics, i, j);
    }
}
