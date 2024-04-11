package net.fyreday.arbora.fluid;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Arbora.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_TERRA_ESSENCE = FLUIDS.register("terra_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.TERRA_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TERRA_ESSENCE = FLUIDS.register("flowing_terra_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TERRA_ESSENCE_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties TERRA_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.TERRA_ESSENCE_FLUID_TYPE, SOURCE_TERRA_ESSENCE, FLOWING_TERRA_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.TERRA_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.TERRA_ESSENCE_BUCKET);
    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
