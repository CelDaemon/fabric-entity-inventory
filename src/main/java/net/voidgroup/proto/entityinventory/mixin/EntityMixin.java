package net.voidgroup.proto.entityinventory.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyVariable(method = "setGlowingTag", at = @At("HEAD"), argsOnly = true, name = "value")
    public boolean a(boolean value) {
        return true;
    }
}
