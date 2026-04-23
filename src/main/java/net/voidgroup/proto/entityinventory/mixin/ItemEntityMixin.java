package net.voidgroup.proto.entityinventory.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.voidgroup.proto.entityinventory.EntityInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private boolean persistent;

    @Unique
    private boolean fakeItem;

    @Inject(method = "setItem", at = @At("HEAD"))
    public void storePersistent(ItemStack itemStack, CallbackInfo ci) {
        persistent = itemStack.has(EntityInventory.PERSISTENT);
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    public boolean a(ItemEntity instance) {
        return fakeItem || !persistent;
    }

    @Inject(method = "makeFakeItem", at = @At("HEAD"))
    public void allowFakeItemDespawn(CallbackInfo ci) {
        fakeItem = true;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void a(ValueOutput output, CallbackInfo ci) {
        output.child(EntityInventory.MOD_ID).putBoolean("fake_item", fakeItem);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void a(ValueInput input, CallbackInfo ci) {
        input.child(EntityInventory.MOD_ID)
                .ifPresent(x ->
                        fakeItem = x.getBooleanOr("fake_item", fakeItem)
                );
    }
}
