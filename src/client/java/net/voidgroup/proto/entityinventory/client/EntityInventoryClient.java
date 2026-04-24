package net.voidgroup.proto.entityinventory.client;

import com.google.common.collect.Streams;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ReferenceFloatPair;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Recipe;
import net.voidgroup.proto.entityinventory.Entities;
import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.Menus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityInventoryClient implements ClientModInitializer {
    public static AtomicReference<ResolvableProfile> profile = new AtomicReference<>(Mannequin.DEFAULT_PROFILE);
    public static AtomicReference<PlayerSkin> skin = new AtomicReference<>(DefaultPlayerSkin.getDefaultSkin());
    @Nullable
    public static Font font;
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

        final var healthKey = RenderStateDataKey.<Map<Integer, Float>>create();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> font = client.font);

        LevelRenderEvents.END_EXTRACTION.register(context -> {
            final var healthMap = Streams.stream(context.level().entitiesForRendering()).filter(x -> x instanceof Avatar)
                    .map(x -> (Avatar) x)
                    .map(x -> Map.entry(x.getId(), x.getHealth()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            context.levelState().setData(healthKey, healthMap);
        });

        LevelRenderEvents.END_MAIN.register(context -> {
            assert font != null;
            final var level = context.levelState();
            final var cam = level.cameraRenderState;
            final var camPos = cam.pos;
            final var camOrient = cam.orientation;

            final var buffer = context.bufferSource();

            final var matrices = context.poseStack();

            final var healthMap = level.getDataOrDefault(healthKey, Map.of());



            final var players = level.entityRenderStates.stream()
                    .filter(x -> x instanceof AvatarRenderState)
                    .map(x -> (AvatarRenderState) x)
                    .flatMap(x -> {
                        final var health = healthMap.get(x.id);
                        if(health == null)
                            return Stream.empty();
                        return Stream.of(ReferenceFloatPair.of(x, health));
                    })
                    .<ReferenceFloatPair<AvatarRenderState>>toArray(ReferenceFloatPair[]::new);

            for (final ReferenceFloatPair<AvatarRenderState> entry : players) {
                final var player = entry.key();
                final var health = entry.valueFloat();
                final var label = Component.literal(String.valueOf(health));

                matrices.pushPose();
                // no precision issues
                matrices.translate(player.x - camPos.x, player.y + player.boundingBoxHeight + 1 - camPos.y, player.z - camPos.z);
                matrices.mulPose(camOrient);

                // precision issues
//                matrices.translate(-camPos.x, -camPos.y, -camPos.z);
//                matrices.translate(player.x, player.y + player.boundingBoxHeight + 1, player.z);

//                matrices.last().pose().rotate(Mth.PI, 0, 1, 0);
                matrices.scale(0.025f, -0.025f, 0.025f);
                final var offset = -font.width(label) / 2f;
                final var matrix = matrices.last().pose();


                font.drawInBatch(
                        label,
                        offset,
                        0,
                        0xFFFFFFFF,
                        false,
                        matrix,
                        buffer,
                        Font.DisplayMode.NORMAL,
                        0x40000000,
                        15728880
                );

                matrices.popPose();
            }

            buffer.endBatch();
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