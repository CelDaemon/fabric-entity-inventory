package entityinventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class EntityInventoryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		EntityRenderers.register(
				Entities.TEST_ENTITY, TestEntityRenderer::new
		);
		MenuScreens.register(
				Menus.TEST_MENU,
				TestEntityScreen::new
		);
	}
}