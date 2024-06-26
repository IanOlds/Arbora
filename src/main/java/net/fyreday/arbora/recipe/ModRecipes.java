package net.fyreday.arbora.recipe;

import net.fyreday.arbora.Arbora;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Arbora.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EssenceInfusionRecipe>> ESSENCE_BREWING_SERIALIZER =
            SERIALIZERS.register("essence_brewing", () -> EssenceInfusionRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<EssenceImbuingRecipe>> ESSENCE_IMBUING_SERIALIZER =
            SERIALIZERS.register("essence_imbuing", () -> EssenceImbuingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<BrewableRecipe>> ESSENCE_BREWABLE_SERIALIZER =
            SERIALIZERS.register("essence_brewable", () -> BrewableRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<DistillRecipe>> DISTILLING_SERIALIZER =
            SERIALIZERS.register("distilling", () -> DistillRecipe.Serializer.INSTANCE);
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
