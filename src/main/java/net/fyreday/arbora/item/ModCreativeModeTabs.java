package net.fyreday.arbora.item;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Arbora.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARBORA_TAB = CREATIVE_MODE_TABS.register("arbor_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AQUA_ICHOR.get()))
                    .title(Component.translatable("creativetab.arbora_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.AQUA_ICHOR.get());
                        pOutput.accept(ModItems.AERO_ICHOR.get());
                        pOutput.accept(ModItems.CRYRO_ICHOR.get());
                        pOutput.accept(ModItems.PYRO_ICHOR.get());
                        pOutput.accept(ModItems.TERRA_ICHOR.get());
                        pOutput.accept(ModItems.CHAOS_ICHOR.get());
                        pOutput.accept(ModItems.MIND_ICHOR.get());
                        pOutput.accept(ModItems.ORDER_ICHOR.get());
                        pOutput.accept(ModItems.SPIRIT_ICHOR.get());

                        pOutput.accept(ModBlocks.MAGICAL_LOG.get());
                        pOutput.accept(ModBlocks.MAGICAL_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_MAGICAL_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_MAGICAL_WOOD.get());
                        pOutput.accept(ModBlocks.MAGICAL_PLANKS.get());
                        pOutput.accept(ModBlocks.MAGICAL_LEAVES.get());
                        pOutput.accept(ModBlocks.MAGICAL_SAPLING.get());

                        pOutput.accept(ModBlocks.ESSENCE_BREWING_STATION.get());
                        pOutput.accept(ModItems.METAL_DETECTOR.get());
                    })
                    .build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
