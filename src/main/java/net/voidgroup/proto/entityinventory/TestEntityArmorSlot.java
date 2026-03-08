package net.voidgroup.proto.entityinventory;

import net.voidgroup.proto.entityinventory.mixin.InventoryMenuAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class TestEntityArmorSlot extends Slot {
    private final EquipmentSlot slotType;
    @Nullable
    private final LivingEntity owner;
    public TestEntityArmorSlot(Inventory container, int slotId, int x, int y, EquipmentSlot slotType, @Nullable LivingEntity owner) {
        super(container, slotId, x, y);
        this.slotType = slotType;
        this.owner = owner;
    }

    @Override
    public boolean canInsert(ItemStack newStack) {
        final var equippable = newStack.get(DataComponentTypes.EQUIPPABLE);
        return equippable != null && equippable.slot() == slotType && super.canInsert(newStack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        return (player.isCreative() || !EnchantmentHelper.hasAnyEnchantmentsWith(getStack(), EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE))
                && super.canTakeItems(player);
    }

    @Override
    public @Nullable Identifier getBackgroundSprite() {
        return InventoryMenuAccessor.getTextureEmptySlots().get(slotType);
    }

    @Override
    public void setStack(ItemStack newStack, ItemStack oldStack) {
        if(owner != null)
            owner.onEquipStack(slotType, oldStack, newStack);

        super.setStack(newStack, oldStack);
    }
}
