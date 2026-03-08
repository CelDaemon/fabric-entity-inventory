package entityinventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TestEntityInventory implements Container {
    public static final EquipmentSlot[] EQUIPMENT_MAPPING = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    private final SimpleContainer container;
    private final EntityEquipment equipment;
    private final LivingEntity entity;

    public TestEntityInventory(SimpleContainer container, EntityEquipment equipment, LivingEntity entity) {
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
    public ItemStack getItem(int slotId) {
        if(slotId < container.getContainerSize())
            return container.getItem(slotId);
        return equipment.get(mapEquipmentSlot(slotId));
    }

    @Override
    public ItemStack removeItem(int slotId, int count) {
        if(slotId < container.getContainerSize())
            return container.removeItem(slotId, count);
        return equipment.set(mapEquipmentSlot(slotId), ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotId) {
        if(slotId < container.getContainerSize())
            return container.removeItemNoUpdate(slotId);
        return equipment.set(mapEquipmentSlot(slotId), ItemStack.EMPTY);
    }

    @Override
    public void setItem(int slotId, ItemStack newItem) {
        if(slotId < container.getContainerSize()) {
            container.setItem(slotId, newItem);
            return;
        }
        equipment.set(mapEquipmentSlot(slotId), newItem);
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

    public void dropAll() {
        equipment.dropAll(entity);
        container.removeAllItems().forEach(x -> entity.drop(x, true, false));
    }
}
