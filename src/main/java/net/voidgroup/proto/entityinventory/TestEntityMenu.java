package net.voidgroup.proto.entityinventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jspecify.annotations.Nullable;

public class TestEntityMenu extends ScreenHandler {
    private final Inventory container;

    public TestEntityMenu(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, new SimpleInventory(TestEntityInventory.INVENTORY_SIZE + TestEntityInventory.EQUIPMENT_MAPPING.length), null);
    }
    public TestEntityMenu(int containerId, PlayerInventory inventory, Inventory container, @Nullable TestEntity entity) {
        super(Menus.TEST_MENU, containerId);
        this.container = container;

        for (var i = 0; i < TestEntityInventory.EQUIPMENT_MAPPING.length; i++)
            addSlot(
                    new TestEntityArmorSlot(
                            container,
                            TestEntityInventory.INVENTORY_SIZE + i,
                            8,
                            8 + ScreenHandler.field_52558 * i,
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
                                62 + x * ScreenHandler.field_52558,
                                17 + y * ScreenHandler.field_52558
                        )
                );
        }

        addPlayerSlots(inventory, 8, 84);
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        final var sourceSlot = slots.get(slotId);

        if(!sourceSlot.hasStack())
            return ItemStack.EMPTY;

        final var sourceItem = sourceSlot.getStack();
        final var newItem = sourceItem.copy();

        final var containerSize = container.size();
        if (slotId < containerSize) {
            if (!insertItem(sourceItem, containerSize, containerSize + PlayerInventory.MAIN_SIZE, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!insertItem(sourceItem, 0, containerSize, false)) {
            return ItemStack.EMPTY;
        }

        if (sourceItem.isEmpty()) {
            sourceSlot.setStack(ItemStack.EMPTY);
        } else {
            sourceSlot.markDirty();
        }

        if (sourceItem.getCount() == newItem.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTakeItem(player, sourceItem);

        return newItem;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return container.canPlayerUse(player);
    }
}
