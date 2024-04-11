package net.fyreday.arbora;

import com.mojang.logging.LogUtils;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffects;
import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.block.entity.MagicalLogBlockEntity;
import net.fyreday.arbora.block.entity.ModBlockEntities;
import net.fyreday.arbora.fluid.ModFluidTypes;
import net.fyreday.arbora.fluid.ModFluids;
import net.fyreday.arbora.item.ModCreativeModeTabs;
import net.fyreday.arbora.item.ModItems;
import net.fyreday.arbora.recipe.ModRecipes;
import net.fyreday.arbora.registries.ModRegistries;
import net.fyreday.arbora.screen.EssenceBrewingScreen;
import net.fyreday.arbora.screen.ModMenuTypes;
import net.fyreday.arbora.util.Util;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import java.awt.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Arbora.MOD_ID)
public class Arbora
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "arbora";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Arbora()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ImbuingEffects.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
//        modEventBus.addListener(this::addArboraRegistries);
        modEventBus.addListener(this::registerBlockColorHandler);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    @SubscribeEvent
    public void registerBlockColorHandler(RegisterColorHandlersEvent.Block event){
        System.out.println("REGISTER COLORS");
        event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof MagicalLogBlockEntity) {
                return Util.getIntFromColor(((MagicalLogBlockEntity) be).getSapType().getColor());
            }
            return Util.getIntFromColor(new Color(255,0,0));
        }, ModBlocks.MAGICAL_LOG.get());
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.AQUA_ICHOR);
        }
    }

    @SubscribeEvent
    public void onNewRegistry(NewRegistryEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.ESSENCE_BREWING_MENU.get(), EssenceBrewingScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MAGICAL_LOG.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TERRA_ESSENCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TERRA_ESSENCE.get(), RenderType.translucent());
        }


    }
}
