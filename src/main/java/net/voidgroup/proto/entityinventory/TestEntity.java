package net.voidgroup.proto.entityinventory;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class TestEntity extends MobEntity implements NamedScreenHandlerFactory {
    public static final String INVENTORY_TAG = "Inventory";
    private final TestEntityInventory inventory = new TestEntityInventory(equipment, this);
    protected TestEntity(EntityType<? extends MobEntity> type, World level) {
        super(type, level);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(0, new LookAtEntityGoal(this, PlayerEntity.class, 6));
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(hand != Hand.MAIN_HAND)
            return ActionResult.PASS;

        if(!getEntityWorld().isClient()) {
            player.openHandledScreen(this);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int containerId, PlayerInventory inventory, PlayerEntity player) {
        return new TestEntityMenu(containerId, player.getInventory(), this.inventory, this);
    }

    @Override
    protected void writeCustomData(WriteView output) {
        super.writeCustomData(output);
        inventory.save(output.getListAppender(INVENTORY_TAG, StackWithSlot.CODEC));
    }

    @Override
    protected void readCustomData(ReadView input) {
        super.readCustomData(input);
        inventory.load(input.getTypedListView(INVENTORY_TAG, StackWithSlot.CODEC));
    }

    @Override
    protected void dropInventory(ServerWorld level) {
        super.dropInventory(level);
        destroyVanishingCursedItems();
        inventory.dropAll();
    }

    private void destroyVanishingCursedItems() {
        for (int i = 0; i < this.inventory.size(); i++) {
            final var stack = this.inventory.getStack(i);
            if (!stack.isEmpty() && EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)) {
                this.inventory.removeStack(i);
            }
        }
    }

    @Override
    public @Nullable StackReference getStackReference(int i) {
        if(i >= PlayerScreenHandler.EQUIPMENT_END && i < PlayerScreenHandler.EQUIPMENT_END + TestEntityInventory.INVENTORY_SIZE)
            return inventory.getStackReference(i - PlayerScreenHandler.EQUIPMENT_END);
        return super.getStackReference(i);
    }
}
