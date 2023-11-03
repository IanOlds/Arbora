package net.fyreday.arbora.datagen;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.block.custom.MagicalLog;
import net.fyreday.arbora.util.ArboraBlockProperties;
import net.fyreday.arbora.util.ArboraEnums;
import net.fyreday.arbora.util.Util;
import net.minecraft.core.Direction;
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

        newMagicalLogBLock((MagicalLog) ModBlocks.MAGICAL_LOG.get());
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

    private void newMagicalLogBLock(MagicalLog block) {
        myAxisBlock(block, blockTexture(block), extend(blockTexture(block), "_top"));
    }
    public void myAxisBlock(MagicalLog block, ResourceLocation side, ResourceLocation end) {
        myAxisBlock(block,
                models().cubeColumn(name(block), side, end),
                models().cubeColumnHorizontal(name(block) + "_horizontal", side, end));
    }
    public void myAxisBlock(MagicalLog block, ModelFile vertical, ModelFile horizontal) {
        getVariantBuilder(block)
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 0)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 1)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 2)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 3)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 4)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Y).with(MagicalLog.SAP_LEVEL, 5)
                .modelForState().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/magical_log_sap"))).addModel()

                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 0)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 1)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 2)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 3)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 4)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.Z).with(MagicalLog.SAP_LEVEL, 5)
                .modelForState().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/magical_log_horizontal_sap"))).rotationX(90).addModel()
                
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 0)
                .modelForState().modelFile(horizontal).rotationY(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 1)
                .modelForState().modelFile(horizontal).rotationY(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 2)
                .modelForState().modelFile(horizontal).rotationY(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 3)
                .modelForState().modelFile(horizontal).rotationY(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 4)
                .modelForState().modelFile(horizontal).rotationY(90).addModel()
                .partialState().with(MagicalLog.AXIS, Direction.Axis.X).with(MagicalLog.SAP_LEVEL, 5)
                .modelForState().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/magical_log_horizontal_sap"))).rotationY(90).addModel();
    }
    private String name(Block block) {
        return key(block).getPath();
    }
    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}
