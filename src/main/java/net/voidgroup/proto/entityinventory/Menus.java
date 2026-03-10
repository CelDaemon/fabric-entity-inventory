package net.voidgroup.proto.entityinventory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class Menus {

    public static final MenuType<TestEntityMenu> TEST_MENU = register("test", TestEntityMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> supplier) {
        return Registry.register(BuiltInRegistries.MENU, EntityInventory.id(name), new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS));
    }

    public static void init() {
        // DUMMY
    }
}
