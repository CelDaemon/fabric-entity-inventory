package entityinventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TestEntityInventory implements Container {
    public static final EquipmentSlot[] EQUIPMENT_MAPPING = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    private final Container container;
    private final EntityEquipment equipment;
    private final LivingEntity entity;

    public TestEntityInventory(Container container, EntityEquipment equipment, LivingEntity entity) {
        this.container = container;
        this.equipment = equipment;
        this.entity = entity;
    }

    private EquipmentSlot mapEquipmentSlot(int slot) {
        return EQUIPMENT_MAPPING[slot - container.getContainerSize()];
    }

    @Override
    public int getContainerSize() {
        return container.getContainerSize() + EQUIPMENT_MAPPING.length;
    }

    @Override
    public boolean isEmpty() {
        return equipment.isEmpty() && container.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        if(i < container.getContainerSize())
            return container.getItem(i);
        return equipment.get(mapEquipmentSlot(i));
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if(i < container.getContainerSize())
            return container.removeItem(i, j);
        return equipment.set(mapEquipmentSlot(i), ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if(i < container.getContainerSize())
            return container.removeItemNoUpdate(i);
        return equipment.set(mapEquipmentSlot(i), ItemStack.EMPTY);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if(i < container.getContainerSize()) {
            container.setItem(i, itemStack);
            return;
        }
        equipment.set(mapEquipmentSlot(i), itemStack);
    }

    @Override
    public void setChanged() {
        container.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return entity.isAlive() && player.isWithinEntityInteractionRange(entity.getBoundingBox(), 4.0) && container.stillValid(player);
    }

    @Override
    public void clearContent() {
        container.clearContent();
        equipment.clear();
    }
}
