package net.voidgroup.proto.entityinventory.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(InventoryMenu.class)
public interface InventoryMenuAccessor {
    @Accessor("TEXTURE_EMPTY_SLOTS")
    static Map<EquipmentSlot, Identifier> getTextureEmptySlots() {
        return Map.of();
    }
}
