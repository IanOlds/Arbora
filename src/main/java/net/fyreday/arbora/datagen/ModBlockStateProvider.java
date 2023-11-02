package net.fyreday.arbora.datagen;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Arbora.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


        logBlock((RotatedPillarBlock)ModBlocks.MAGICAL_LOG_DEAD.get());
        axisBlock(((RotatedPillarBlock) ModBlocks.MAGICAL_WOOD.get()), blockTexture(ModBlocks.MAGICAL_LOG.get()), blockTexture(ModBlocks.MAGICAL_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_MAGICAL_LOG.get()), blockTexture(ModBlocks.STRIPPED_MAGICAL_LOG.get()),
                new ResourceLocation(Arbora.MOD_ID, "block/stripped_magical_log_top"));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_MAGICAL_WOOD.get()), blockTexture(ModBlocks.STRIPPED_MAGICAL_LOG.get()), blockTexture(ModBlocks.STRIPPED_MAGICAL_LOG.get()));

        blockItem(ModBlocks.MAGICAL_LOG);
        blockItem(ModBlocks.MAGICAL_LOG_DEAD);
        blockItem(ModBlocks.MAGICAL_WOOD);
        blockItem(ModBlocks.STRIPPED_MAGICAL_LOG);
        blockItem(ModBlocks.STRIPPED_MAGICAL_WOOD);

        blockWithItem(ModBlocks.MAGICAL_PLANKS);

        leavesBlock(ModBlocks.MAGICAL_LEAVES);

        simpleBlockWithItem(ModBlocks.ESSENCE_BREWING_STATION.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/essence_brewing_station")));
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }
    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(Arbora.MOD_ID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
