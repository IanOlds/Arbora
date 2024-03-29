package net.fyreday.arbora.ImbuingEffects;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.Modifiers.ModModifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class ImbuingEffects {
    private static boolean isInitialized = false;
    private static Supplier<IForgeRegistry<ImbuingEffect>> REGISTRY;
    public static final DeferredRegister<ImbuingEffect> IMBUINGEFFECTS =
            DeferredRegister.create(new ResourceLocation(Arbora.MOD_ID, "imbuing_effects"), Arbora.MOD_ID);

    public static final RegistryObject<ImbuingEffect> SPEED = IMBUINGEFFECTS.register("imbuing_speed", () -> new ImbuingEffect(Attributes.MOVEMENT_SPEED.getDescriptionId(),ModModifiers.imbuedSpeed.getId()));
    public static final RegistryObject<ImbuingEffect> JUMP = IMBUINGEFFECTS.register("imbuing_health", () -> new ImbuingEffect(Attributes.MAX_HEALTH.getDescriptionId(),ModModifiers.imbuedSpeed.getId()));

    public static void register(IEventBus eventBus){
        if (isInitialized) {
            throw new IllegalStateException("Imbuing effects already initialized");
        }
        REGISTRY = IMBUINGEFFECTS.makeRegistry(RegistryBuilder::new);
        IMBUINGEFFECTS.register(eventBus);
        isInitialized = true;
        //LogManager.getLogManager().getLogger(Arbora.MOD_ID).info("Registered Imbuing Effects");
        IMBUINGEFFECTS.register(eventBus);
    }

    public static Supplier<IForgeRegistry<ImbuingEffect>> getREGISTRY() {
        if(!isInitialized){
            throw new IllegalStateException("Imbuing effects are not initialized");
        }
        return REGISTRY;
    }
}
