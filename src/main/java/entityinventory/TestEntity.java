package entityinventory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class TestEntity extends LivingEntity implements MenuProvider {
    public static final String INVENTORY_TAG = "Inventory";
    private final TestEntityInventory inventory = new TestEntityInventory(equipment, this);
    protected TestEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        if(!level().isClientSide()) {
            player.openMenu(this);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new TestEntityMenu(containerId, player.getInventory(), this.inventory, this);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        inventory.save(output.list(INVENTORY_TAG, ItemStackWithSlot.CODEC));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        inventory.load(input.listOrEmpty(INVENTORY_TAG, ItemStackWithSlot.CODEC));
    }

    @Override
    protected void dropEquipment(ServerLevel level) {
        super.dropEquipment(level);
        destroyVanishingCursedItems();
        inventory.dropAll();
    }

    private void destroyVanishingCursedItems() {
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            final var stack = this.inventory.getItem(i);
            if (!stack.isEmpty() && EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                this.inventory.removeItemNoUpdate(i);
            }
        }
    }
}
