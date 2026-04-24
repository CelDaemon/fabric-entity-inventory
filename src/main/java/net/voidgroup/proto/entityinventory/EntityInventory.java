package net.voidgroup.proto.entityinventory;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityInventory implements ModInitializer {
	public static final String MOD_ID = "entity-inventory";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final DataComponentType<Unit> PERSISTENT = DataComponentType.<Unit>builder().persistent(Unit.CODEC).build();

	@Override
	public void onInitialize() {
		Entities.init();
		Menus.init();
		FabricDefaultAttributeRegistry.register(
				Entities.TEST_ENTITY,
				TestEntity.createMobAttributes()
		);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, "meow", PERSISTENT);

        try {
            EntityInventory.class.getClassLoader().loadClass("net.voidgroup.proto.entityinventory.Test");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}