package net.fyreday.arbora.datagen;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Arbora.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.AQUA_ICHOR);
        simpleItem(ModItems.AERO_ICHOR);
        simpleItem(ModItems.CHAOS_ICHOR);
        simpleItem(ModItems.CRYRO_ICHOR);
        simpleItem(ModItems.METAL_DETECTOR);
        simpleItem(ModItems.MIND_ICHOR);
        simpleItem(ModItems.ORDER_ICHOR);
        simpleItem(ModItems.PYRO_ICHOR);
        simpleItem(ModItems.SPIRIT_ICHOR);
        simpleItem(ModItems.TERRA_ICHOR);

    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Arbora.MOD_ID,"item/" + item.getId().getPath()));
    }
}
