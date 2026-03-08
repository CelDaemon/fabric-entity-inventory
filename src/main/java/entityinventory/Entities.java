package entityinventory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Entities {
    public static final EntityType<TestEntity> TEST_ENTITY = register("test",
            EntityType.Builder.of(TestEntity::new, MobCategory.CREATURE)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        final var key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EntityInventory.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void init() {
        // DUMMY
    }
}
