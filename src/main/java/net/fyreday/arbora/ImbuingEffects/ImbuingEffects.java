package net.fyreday.arbora.ImbuingEffects;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.registries.ModRegistries;
import net.fyreday.arbora.util.ArboraEnums;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ImbuingEffects {
    public static final DeferredRegister<ImbuingEffect> IMBUINGEFFECTS =
            DeferredRegister.create(ModRegistries.IMBUINGEFFECTS, Arbora.MOD_ID);
    public static final RegistryObject<ImbuingEffect> SPEED = IMBUINGEFFECTS.register("imbuing_speed",
            () -> create("imbuing_speed", MobEffects.MOVEMENT_SPEED, Map.of(),
                    Map.of(ArboraEnums.ValidImbuingArmor.LEGS, true)));

    private static ImbuingEffect create(String name, MobEffect mobEffect, Map<ArboraEnums.ValidImbuingToolType, Boolean> validTools, Map<ArboraEnums.ValidImbuingArmor, Boolean> validArmor){
        return ImbuingEffect.create(name, mobEffect, Component.translatable(mobEffect.getDescriptionId()).withStyle(Style.EMPTY.withColor(9901575)),
                validTools, validArmor);
    }
    public static void register(IEventBus eventBus){IMBUINGEFFECTS.register(eventBus);}
}
