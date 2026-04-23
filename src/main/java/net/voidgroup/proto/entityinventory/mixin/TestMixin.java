package net.voidgroup.proto.entityinventory.mixin;

import net.voidgroup.proto.entityinventory.EntityInventory;
import net.voidgroup.proto.entityinventory.Test;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Debug(export = true)
@Mixin(Test.class)
public abstract class TestMixin {
    private static final ThreadLocal<?> KEY_DEP = new ThreadLocal();

    private final String o1 = EntityInventory.id(EntityInventory.MOD_ID).toString();

    private final String o2 = EntityInventory.id("test").toString();
}
