package net.voidgroup.proto.entityinventory.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(PlayerScreenHandler.class)
public interface InventoryMenuAccessor {
    @Accessor("EMPTY_ARMOR_SLOT_TEXTURES")
    static Map<EquipmentSlot, Identifier> getTextureEmptySlots() {
        return Map.of();
    }
}
