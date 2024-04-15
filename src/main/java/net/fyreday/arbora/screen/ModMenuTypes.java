package net.fyreday.arbora.screen;

import net.fyreday.arbora.Arbora;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Arbora.MOD_ID);

    public static final RegistryObject<MenuType<EssenceBrewingMenu>> ESSENCE_BREWING_MENU =
            registerMenuType("essence_brewing_menu", EssenceBrewingMenu::new);

    public static final RegistryObject<MenuType<DistillMenu>> DISTILLING_MENU =
            registerMenuType("distilling_menu", DistillMenu::new);
    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
