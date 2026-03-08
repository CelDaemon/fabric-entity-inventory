package net.voidgroup.proto.entityinventory;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class Menus {

    public static final ScreenHandlerType<TestEntityMenu> TEST_MENU = register("test", TestEntityMenu::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> supplier) {
        return Registry.register(Registries.SCREEN_HANDLER, EntityInventory.id(name), new ScreenHandlerType<>(supplier, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    }

    public static void init() {
        // DUMMY
    }
}
