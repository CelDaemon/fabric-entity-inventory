package entityinventory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
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
    public static final int CONTAINER_SIZE = 9;
    private final SimpleContainer container = new SimpleContainer(CONTAINER_SIZE);
    private final TestEntityInventory inventory = new TestEntityInventory(container, equipment, this);
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
        ContainerHelper.saveAllItems(output, container.getItems());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        ContainerHelper.loadAllItems(input, container.getItems());
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
