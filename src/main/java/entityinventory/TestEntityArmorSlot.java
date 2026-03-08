package entityinventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public class TestEntityArmorSlot extends Slot {
    private final EquipmentSlot slotType;
    @Nullable
    private final LivingEntity owner;
    public TestEntityArmorSlot(Container container, int slotId, int x, int y, EquipmentSlot slotType, @Nullable LivingEntity owner) {
        super(container, slotId, x, y);
        this.slotType = slotType;
        this.owner = owner;
    }

    @Override
    public boolean mayPlace(ItemStack newStack) {
        final var equippable = newStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot() == slotType && super.mayPlace(newStack);
    }

    @Override
    public boolean mayPickup(Player player) {
        return (player.isCreative() || !EnchantmentHelper.has(getItem(), EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))
                && super.mayPickup(player);
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return InventoryMenu.TEXTURE_EMPTY_SLOTS.get(slotType);
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        if(owner != null)
            owner.onEquipItem(slotType, oldStack, newStack);

        super.setByPlayer(newStack, oldStack);
    }
}
