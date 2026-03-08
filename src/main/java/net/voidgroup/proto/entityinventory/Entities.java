package net.voidgroup.proto.entityinventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class Entities {
    public static final EntityType<TestEntity> TEST_ENTITY = register("test",
            EntityType.Builder.create(TestEntity::new, SpawnGroup.CREATURE)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        final var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, EntityInventory.id(name));
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void init() {
        // DUMMY
    }
}
