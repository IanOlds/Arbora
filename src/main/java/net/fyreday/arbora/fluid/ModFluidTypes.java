package net.fyreday.arbora.fluid;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.util.ArboraEnums;
import net.fyreday.arbora.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

import java.awt.*;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation ESSENCE_OVERLAY_RL = new ResourceLocation(Arbora.MOD_ID, "misc/in_essence_fluid");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Arbora.MOD_ID);

    public static final RegistryObject<FluidType> TERRA_ESSENCE_FLUID_TYPE = registerEssenceFluid("terra_essence_fluid",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK), ArboraEnums.SapType.TERRA.getColor());
    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties, int tintColor, Vector3f fogColor) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, ESSENCE_OVERLAY_RL,
                tintColor, fogColor, properties));
    }
    private static RegistryObject<FluidType> registerEssenceFluid(String name, FluidType.Properties properties, Color color){
        return register(name, properties, Util.getIntFromColor(color), Util.getVectorFromColor(color));
    }
    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
