package net.voidgroup.proto.entityinventory.client;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.Vec3;
import net.voidgroup.proto.entityinventory.Entities;
import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.Menus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
                    final var profileFuture = CompletableFuture.supplyAsync(client::getGameProfile, Util.ioPool());
                    profileFuture.thenAccept(profile -> EntityInventoryClient.profile.set(ResolvableProfile.createResolved(profile)));
                    profileFuture.thenCompose(client.getSkinManager()::get)
                            .thenApply(Optional::orElseThrow)
                            .thenAccept(skin::set).exceptionally(x -> {
                                EntityInventory.LOGGER.error("Failed to retrieve entity looks", x);
                                throw new RuntimeException(x);
                            });
                }
        );

        LevelRenderEvents.END_MAIN.register(context -> {
            final var client = Minecraft.getInstance();
            if (client.level == null || client.player == null) return;

            Vec3 camPos = context.levelState().cameraRenderState.pos;
            Matrix4f camRot = context.levelState().cameraRenderState.viewRotationMatrix;
            Quaternionf camOrient = context.levelState().cameraRenderState.orientation;
            final var font = client.font;

            final var immediate = context.bufferSource();

            float tickDelta = client.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            final var matrices = context.poseStack();

            for (final var player : client.level.players()) {
                if (player == client.player && client.options.getCameraType().isFirstPerson()) continue;


                final var label = Component.literal("Health: ").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(String.valueOf(player.getHealth())).withStyle(ChatFormatting.GOLD));

                final var pos = player.oldPosition().lerp(player.position(), tickDelta).subtract(camPos);

                matrices.pushPose();
                matrices.translate(pos.x, pos.y + player.getBbHeight() + 1, pos.z);
                matrices.mulPose(camOrient);

                matrices.scale(0.025f, -0.025f, 0.025f);


                float labelWidth = -font.width(label) / 2f;
                var matrix = matrices.last().pose();

                font.drawInBatch(
                        label,
                        labelWidth,
                        0,
                        0xFFFFFFFF,
                        false,
                        matrix,
                        immediate,
                        Font.DisplayMode.SEE_THROUGH,
                        0x40000000,
                        15728880
                );
                matrices.popPose();
            }

            immediate.endBatch();
        });

        CommonLifecycleEvents.TAGS_LOADED.register((a, b) -> {
            if(!b)
                return;
            final var object = JsonParser.parseString("""
                    {
                      "type": "minecraft:crafting_shaped",
                      "category": "misc",
                      "group": "stained_glass_pane",
                      "key": {
                        "#": "minecraft:yellow_stained_glass"
                      },
                      "pattern": [
                        "###",
                        "###"
                      ],
                      "result": {
                        "count": 16,
                        "id": "minecraft:yellow_stained_glass_pane"
                      }
                    }""").getAsJsonObject();
            final var recipe = Recipe.CODEC.parse(a.createSerializationContext(JsonOps.INSTANCE), object).getOrThrow(JsonParseException::new);
            EntityInventory.LOGGER.info("recipe: {}", recipe);
        });
    }
}