package net.voidgroup.proto.entityinventory;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityInventory implements ModInitializer {
	public static final String MOD_ID = "entity-inventory";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Entities.init();
		Menus.init();
		FabricDefaultAttributeRegistry.register(
				Entities.TEST_ENTITY,
				TestEntity.createMobAttributes()
		);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}