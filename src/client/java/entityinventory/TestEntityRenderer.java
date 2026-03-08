package entityinventory;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

public class TestEntityRenderer extends LivingEntityRenderer<TestEntity, AvatarRenderState, PlayerModel> {
    public TestEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.7f);
        addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), x -> new PlayerModel(x, false)),
                context.getEquipmentRenderer()));
    }

    @Override
    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    @Override
    public void extractRenderState(TestEntity livingEntity, AvatarRenderState livingEntityRenderState, float f) {
        HumanoidMobRenderer.extractHumanoidRenderState(livingEntity, livingEntityRenderState, f, itemModelResolver);
        super.extractRenderState(livingEntity, livingEntityRenderState, f);
    }

    @Override
    public Identifier getTextureLocation(AvatarRenderState livingEntityRenderState) {
        return MissingTextureAtlasSprite.getLocation();
    }
}
