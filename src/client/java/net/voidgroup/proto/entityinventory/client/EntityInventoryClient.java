package net.voidgroup.proto.entityinventory.client;

import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import net.voidgroup.proto.entityinventory.Entities;
import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.Menus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EntityInventoryClient implements ClientModInitializer {
    public static AtomicReference<ResolvableProfile> profile = new AtomicReference<>(Mannequin.DEFAULT_PROFILE);
    public static AtomicReference<PlayerSkin> skin = new AtomicReference<>(DefaultPlayerSkin.getDefaultSkin());

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(
                Entities.TEST_ENTITY, TestEntityRenderer::new
        );

        MenuScreens.register(
                Menus.TEST_MENU,
                TestEntityScreen::new
        );

        ClientLifecycleEvents.CLIENT_STARTED.register(
                client -> {
                    final var profile = client.getGameProfile();
                    EntityInventoryClient.profile.set(ResolvableProfile.createResolved(profile));
                    client.getSkinManager().get(profile)
                            .thenApply(Optional::orElseThrow)
                    .thenAccept(skin::set).exceptionally(x -> {
                        EntityInventory.LOGGER.error("Failed to load skin", x);
                        throw new RuntimeException(x);
                    });
                }
        );
    }
}