package entityinventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TestEntityArmorSlot extends Slot {
    private final EquipmentSlot equipmentSlot;
    @Nullable
    private final LivingEntity entity;
    public TestEntityArmorSlot(Container container, int i, int j, int k, EquipmentSlot equipmentSlot, @Nullable LivingEntity entity) {
        super(container, i, j, k);
        this.equipmentSlot = equipmentSlot;
        this.entity = entity;
    }

    @Override
    public boolean mayPlace(ItemStack newStack) {
        final var equippable = newStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot() == equipmentSlot && super.mayPlace(newStack);
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return InventoryMenu.TEXTURE_EMPTY_SLOTS.get(equipmentSlot);
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        if(entity != null)
            entity.onEquipItem(equipmentSlot, oldStack, newStack);

        super.setByPlayer(newStack, oldStack);
    }
}
