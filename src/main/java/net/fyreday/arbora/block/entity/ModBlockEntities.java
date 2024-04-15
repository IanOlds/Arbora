package net.fyreday.arbora.block.entity;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Arbora.MOD_ID);

    public static final RegistryObject<BlockEntityType<EssenceBrewingStationBlockEntity>> ESSENCE_BREWING_BE =
            BLOCK_ENTITIES.register("essence_brewing_be", () ->
                    BlockEntityType.Builder.of(EssenceBrewingStationBlockEntity::new,
                            ModBlocks.ESSENCE_BREWING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<ArboricDistillerBlockEntity>> ARBORIC_DISTILLER =
            BLOCK_ENTITIES.register("arboric_distiller_be", () ->
                    BlockEntityType.Builder.of(ArboricDistillerBlockEntity::new,
                            ModBlocks.ARBORIC_DISTILLER.get()).build(null));
    public static final RegistryObject<BlockEntityType<MagicalLogBlockEntity>> MAGICAL_LOG_BE =
            BLOCK_ENTITIES.register("magical_log_be", () ->
                    BlockEntityType.Builder.of(MagicalLogBlockEntity::new,
                            ModBlocks.MAGICAL_LOG.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
