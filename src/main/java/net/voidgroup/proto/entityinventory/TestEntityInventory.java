package net.voidgroup.proto.entityinventory;

import net.minecraft.entity.EntityEquipment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;

public class TestEntityInventory implements Inventory {
    public static final int INVENTORY_SIZE = 9;
    public static final EquipmentSlot[] EQUIPMENT_MAPPING = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private final EntityEquipment equipment;
    private final LivingEntity entity;

    public TestEntityInventory(EntityEquipment equipment, LivingEntity entity) {
        this.equipment = equipment;
        this.entity = entity;
    }

    private EquipmentSlot mapEquipmentSlot(int slot) {
        return EQUIPMENT_MAPPING[slot - INVENTORY_SIZE];
    }

    @Override
    public int size() {
        return INVENTORY_SIZE + EQUIPMENT_MAPPING.length;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty() && equipment.isEmpty();
    }

    @Override
    public ItemStack getStack(int slotId) {
        if(slotId < INVENTORY_SIZE)
            return items.get(slotId);

        return equipment.get(mapEquipmentSlot(slotId));
    }

    @Override
    public ItemStack removeStack(int slotId, int count) {
        if(slotId < INVENTORY_SIZE)
            return Inventories.splitStack(items, slotId, count);

        return equipment.get(mapEquipmentSlot(slotId)).split(count);
    }

    @Override
    public ItemStack removeStack(int slotId) {
        if(slotId < INVENTORY_SIZE)
            return items.set(slotId, ItemStack.EMPTY);

        return equipment.put(mapEquipmentSlot(slotId), ItemStack.EMPTY);
    }

    @Override
    public void setStack(int slotId, ItemStack newItem) {
        if(slotId < INVENTORY_SIZE) {
            items.set(slotId, newItem);
            return;
        }
        equipment.put(mapEquipmentSlot(slotId), newItem);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return entity.isAlive() && player.canInteractWithEntityIn(entity.getBoundingBox(), 4.0);
    }

    @Override
    public void clear() {
        items.clear();
        equipment.clear();
    }

    public void dropAll() {
        for (int i = 0; i < items.size(); i++) {
            entity.dropItem(items.set(i, ItemStack.EMPTY), true, false);
        }
        equipment.dropAll(entity);
    }

    public void save(WriteView.ListAppender<StackWithSlot> output) {
        for (int i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            if(item.isEmpty())
                continue;
            output.add(new StackWithSlot(i, item));
        }
    }

    public void load(ReadView.TypedListReadView<StackWithSlot> input) {
        for (final var item : input) {
            items.set(item.slot(), item.stack());
        }
    }
}
