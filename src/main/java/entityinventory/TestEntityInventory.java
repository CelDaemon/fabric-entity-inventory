package entityinventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TestEntityInventory implements Container {
    public static final int INVENTORY_SIZE = 9;
    public static final EquipmentSlot[] EQUIPMENT_MAPPING = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    private final NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
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
    public int getContainerSize() {
        return INVENTORY_SIZE + EQUIPMENT_MAPPING.length;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty() && equipment.isEmpty();
    }

    @Override
    public ItemStack getItem(int slotId) {
        if(slotId < INVENTORY_SIZE)
            return items.get(slotId);
        return equipment.get(mapEquipmentSlot(slotId));
    }

    @Override
    public ItemStack removeItem(int slotId, int count) {
        if(slotId < INVENTORY_SIZE)
            return ContainerHelper.removeItem(items, slotId, count);

        return equipment.get(mapEquipmentSlot(slotId)).split(count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotId) {
        if(slotId < INVENTORY_SIZE)
            return items.set(slotId, ItemStack.EMPTY);
        return equipment.set(mapEquipmentSlot(slotId), ItemStack.EMPTY);
    }

    @Override
    public void setItem(int slotId, ItemStack newItem) {
        if(slotId < INVENTORY_SIZE) {
            items.set(slotId, newItem);
            return;
        }
        equipment.set(mapEquipmentSlot(slotId), newItem);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return entity.isAlive() && player.isWithinEntityInteractionRange(entity.getBoundingBox(), 4.0);
    }

    @Override
    public void clearContent() {
        items.clear();
        equipment.clear();
    }

    public void dropAll() {
        for (int i = 0; i < items.size(); i++) {
            entity.drop(items.set(i, ItemStack.EMPTY), true, false);
        }
        equipment.dropAll(entity);
    }

    public void save(ValueOutput.TypedOutputList<ItemStackWithSlot> output) {
        for (int i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            if(item.isEmpty())
                continue;
            output.add(new ItemStackWithSlot(i, item));
        }
    }

    public void load(ValueInput.TypedInputList<ItemStackWithSlot> input) {
        for (final var item : input) {
            items.set(item.slot(), item.stack());
        }
    }
}
