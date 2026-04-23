package net.voidgroup.proto.entityinventory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Vec3.class)
public class Vec3Mixin {
    @ModifyReturnValue(method = "lerp", at = @At("RETURN"))
    public Vec3 a(final Vec3 original, final Vec3 vec, final double a) {
        return vec;
    }
}
