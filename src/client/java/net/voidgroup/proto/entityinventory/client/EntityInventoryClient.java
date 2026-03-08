package net.voidgroup.proto.entityinventory.client;

import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.util.Util;
import net.voidgroup.proto.entityinventory.Entities;
import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.Menus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.player.SkinTextures;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class EntityInventoryClient implements ClientModInitializer {
	public static AtomicReference<ProfileComponent> profile = new AtomicReference<>(MannequinEntity.DEFAULT_INFO);
    public static AtomicReference<SkinTextures> skin = new AtomicReference<>(DefaultSkinHelper.getSteve());

	@Override
	public void onInitializeClient() {
        EntityRendererFactories.register(
				Entities.TEST_ENTITY, TestEntityRenderer::new
		);

        HandledScreens.register(
				Menus.TEST_MENU,
				TestEntityScreen::new
		);

        ClientLifecycleEvents.CLIENT_STARTED.register(
                client -> {
                    final var profileFuture = CompletableFuture.supplyAsync(client::getGameProfile, Util.getIoWorkerExecutor());
                    profileFuture.thenAccept(profile -> EntityInventoryClient.profile.set(ProfileComponent.ofStatic(profile)));
                    profileFuture.thenCompose(client.getSkinProvider()::fetchSkinTextures)
                            .thenApply(Optional::orElseThrow)
                            .thenAccept(skin::set).exceptionally(x -> {
                                EntityInventory.LOGGER.error("Failed to retrieve entity looks", x);
                                throw new RuntimeException(x);
                            });
                }
        );
    }
}