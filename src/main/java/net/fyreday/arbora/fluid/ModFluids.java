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

    public static final RegistryObject<FlowingFluid> SOURCE_AQUA_ESSENCE = FLUIDS.register("aqua_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.AQUA_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AQUA_ESSENCE = FLUIDS.register("flowing_aqua_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AQUA_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AQUA_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.AQUA_ESSENCE_FLUID_TYPE, SOURCE_AQUA_ESSENCE, FLOWING_AQUA_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.AQUA_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.AQUA_ESSENCE_BUCKET);
    
    public static final RegistryObject<FlowingFluid> SOURCE_AERO_ESSENCE = FLUIDS.register("aero_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.AERO_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AERO_ESSENCE = FLUIDS.register("flowing_aero_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AERO_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AERO_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.AERO_ESSENCE_FLUID_TYPE, SOURCE_AERO_ESSENCE, FLOWING_AERO_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.AERO_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.AERO_ESSENCE_BUCKET);
    
    public static final RegistryObject<FlowingFluid> SOURCE_CRYRO_ESSENCE = FLUIDS.register("cryro_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CRYRO_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CRYRO_ESSENCE = FLUIDS.register("flowing_cryro_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CRYRO_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CRYRO_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.CRYRO_ESSENCE_FLUID_TYPE, SOURCE_CRYRO_ESSENCE, FLOWING_CRYRO_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.CRYRO_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.CRYRO_ESSENCE_BUCKET);
    
    public static final RegistryObject<FlowingFluid> SOURCE_PYRO_ESSENCE = FLUIDS.register("pyro_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PYRO_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PYRO_ESSENCE = FLUIDS.register("flowing_pyro_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PYRO_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PYRO_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PYRO_ESSENCE_FLUID_TYPE, SOURCE_PYRO_ESSENCE, FLOWING_PYRO_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.PYRO_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.PYRO_ESSENCE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_TERRA_ESSENCE = FLUIDS.register("terra_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.TERRA_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TERRA_ESSENCE = FLUIDS.register("flowing_terra_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TERRA_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TERRA_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.TERRA_ESSENCE_FLUID_TYPE, SOURCE_TERRA_ESSENCE, FLOWING_TERRA_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.TERRA_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.TERRA_ESSENCE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_CHAOS_ESSENCE = FLUIDS.register("chaos_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CHAOS_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHAOS_ESSENCE = FLUIDS.register("flowing_chaos_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CHAOS_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CHAOS_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.CHAOS_ESSENCE_FLUID_TYPE, SOURCE_CHAOS_ESSENCE, FLOWING_CHAOS_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.CHAOS_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.CHAOS_ESSENCE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_MIND_ESSENCE = FLUIDS.register("mind_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.MIND_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MIND_ESSENCE = FLUIDS.register("flowing_mind_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.MIND_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties MIND_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.MIND_ESSENCE_FLUID_TYPE, SOURCE_MIND_ESSENCE, FLOWING_MIND_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.MIND_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.MIND_ESSENCE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_ORDER_ESSENCE = FLUIDS.register("order_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ORDER_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ORDER_ESSENCE = FLUIDS.register("flowing_order_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ORDER_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ORDER_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.ORDER_ESSENCE_FLUID_TYPE, SOURCE_ORDER_ESSENCE, FLOWING_ORDER_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.ORDER_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.ORDER_ESSENCE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_SPIRIT_ESSENCE = FLUIDS.register("spirit_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SPIRIT_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SPIRIT_ESSENCE = FLUIDS.register("flowing_spirit_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SPIRIT_ESSENCE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SPIRIT_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.SPIRIT_ESSENCE_FLUID_TYPE, SOURCE_SPIRIT_ESSENCE, FLOWING_SPIRIT_ESSENCE)
            .slopeFindDistance(4).levelDecreasePerBlock(4).block(ModBlocks.SPIRIT_ESSENCE_FLUID_BLOCK)
            .bucket(ModItems.SPIRIT_ESSENCE_BUCKET);
    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
