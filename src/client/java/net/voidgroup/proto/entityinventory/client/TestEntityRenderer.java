package net.voidgroup.proto.entityinventory.client;

import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.text.Text;
import net.voidgroup.proto.entityinventory.TestEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;

public class TestEntityRenderer extends LivingEntityRenderer<TestEntity, PlayerEntityRenderState, PlayerEntityModel> {
    public TestEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel(context.getPart(EntityModelLayers.PLAYER), isSlim()), 0.7f);
        addFeature(new ArmorFeatureRenderer<>(this, EquipmentModelData.mapToEntityModel(EntityModelLayers.PLAYER_EQUIPMENT, context.getEntityModels(), x -> new PlayerEntityModel(x, isSlim())),
                context.getEquipmentRenderer()));
    }

    @Override
    public PlayerEntityRenderState createRenderState() {
        return new PlayerEntityRenderState();
    }

    @Override
    public void updateRenderState(TestEntity entity, PlayerEntityRenderState state, float f) {
        BipedEntityRenderer.updateBipedRenderState(entity, state, f, itemModelResolver);
        state.skinTextures = EntityInventoryClient.skin.get();
        super.updateRenderState(entity, state, f);
        state.displayName = Text.literal(EntityInventoryClient.profile.get().getName().orElseThrow());
    }

    @Override
    public Identifier getTexture(PlayerEntityRenderState state) {
        return state.skinTextures.body().texturePath();
    }

    private static boolean isSlim() {
        return EntityInventoryClient.skin.get().model() == PlayerSkinType.SLIM;
    }
}
