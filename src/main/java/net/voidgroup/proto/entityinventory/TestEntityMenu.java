package net.voidgroup.proto.entityinventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TestEntityMenu extends AbstractContainerMenu {
    private final Container container;

    public TestEntityMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(TestEntityInventory.INVENTORY_SIZE + TestEntityInventory.EQUIPMENT_MAPPING.length), null);
    }
    public TestEntityMenu(int containerId, Inventory inventory, Container container, @Nullable TestEntity entity) {
        super(Menus.TEST_MENU, containerId);
        this.container = container;

        for (var i = 0; i < TestEntityInventory.EQUIPMENT_MAPPING.length; i++)
            addSlot(
                    new TestEntityArmorSlot(
                            container,
                            TestEntityInventory.INVENTORY_SIZE + i,
                            8,
                            8 + AbstractContainerMenu.SLOT_SIZE * i,
                            TestEntityInventory.EQUIPMENT_MAPPING[i],
                            entity
                    )
            );

        for (var y = 0; y < 3; y++) {
            for(var x = 0; x < 3; x++)
                addSlot(
                        new Slot(
                                container,
                                x + y * 3,
                                62 + x * AbstractContainerMenu.SLOT_SIZE,
                                17 + y * AbstractContainerMenu.SLOT_SIZE
                        )
                );
        }

        addStandardInventorySlots(inventory, 8, 84);
    }


    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        final var sourceSlot = slots.get(slotId);

        if(!sourceSlot.hasItem())
            return ItemStack.EMPTY;

        final var sourceItem = sourceSlot.getItem();
        final var newItem = sourceItem.copy();

        final var containerSize = container.getContainerSize();
        if (slotId < containerSize) {
            if (!moveItemStackTo(sourceItem, containerSize, containerSize + Inventory.INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(sourceItem, 0, containerSize, false)) {
            return ItemStack.EMPTY;
        }

        if (sourceItem.isEmpty()) {
            sourceSlot.setByPlayer(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceItem.getCount() == newItem.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(player, sourceItem);

        return newItem;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
