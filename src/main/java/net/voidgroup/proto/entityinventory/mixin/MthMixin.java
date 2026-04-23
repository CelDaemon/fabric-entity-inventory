package net.voidgroup.proto.entityinventory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.IntPredicate;

@Mixin(Mth.class)
public class MthMixin {
    @ModifyReturnValue(method = "lerp(DLnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"))
    private static Vec3 a(Vec3 original, final double alpha, final Vec3 p1, final Vec3 p2) {

        return p2;
    }

    @ModifyReturnValue(method = "lerp(FFF)F", at = @At("RETURN"))
    private static float b(float original, final float alpha1, final float p0, final float p1) {

        return p1;
    }

    @ModifyReturnValue(method = "lerp(DDD)D", at = @At("RETURN"))
    private static double b(double original, final double alpha1, final double p0, final double p1) {

        return p1;
    }

    @ModifyReturnValue(method = "lerpInt", at = @At("RETURN"))
    private static int a(int original, final float alpha1, final int p0, final int p1) {
        return p1;
    }

    @ModifyReturnValue(method = "inverseLerp(FFF)F", at = @At("RETURN"))
    private static float c(float original, final float value, final float min, final float max) {
        return max;
    }

    @ModifyReturnValue(method = "inverseLerp(DDD)D", at = @At("RETURN"))
    private static double d(double original, final double value, final double min, final double max) {
        return max;
    }

    @ModifyReturnValue(method = "atan2", at = @At("RETURN"))
    private static double a(double original) {

        return 69;
    }

    @ModifyReturnValue(method = "binarySearch", at = @At("RETURN"))
    private static int a(int original, int from, final int to, final IntPredicate condition) {

        return from;
    }

    @ModifyReturnValue(method = "lerpDiscrete", at = @At("RETURN"))
    private static int d(int original, final float alpha1, final int p0, final int p1) {

        return p1;
    }
}
