package entityinventory;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.stream.IntStream;

public class TestEntityInventory implements Container {
    public static final Int2ReferenceMap<EquipmentSlot> EQUIPMENT_MAPPING;
    static {
        final var slots = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        EQUIPMENT_MAPPING = new Int2ReferenceOpenHashMap<>(IntStream.range(0, slots.length).toArray(), slots);
    }
    public static final int EQUIPMENT_SIZE = EQUIPMENT_MAPPING.size();
    private final Container container;
    private final int containerSize;
    private final EntityEquipment equipment;
    private final LivingEntity entity;

    public TestEntityInventory(Container container, EntityEquipment equipment, LivingEntity entity) {
        this.container = container;
        containerSize = container.getContainerSize();
        this.equipment = equipment;
        this.entity = entity;
    }

    private EquipmentSlot mapEquipmentSlot(int slot) {
        return EQUIPMENT_MAPPING.get(slot - containerSize);
    }

    @Override
    public int getContainerSize() {
        return containerSize + EQUIPMENT_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return equipment.isEmpty() && container.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        if(i < containerSize)
            return container.getItem(i);
        return equipment.get(mapEquipmentSlot(i));
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if(i < containerSize)
            return container.removeItem(i, j);
        return equipment.set(mapEquipmentSlot(i), ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if(i < containerSize)
            return container.removeItemNoUpdate(i);
        return equipment.set(mapEquipmentSlot(i), ItemStack.EMPTY);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if(i < containerSize) {
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
